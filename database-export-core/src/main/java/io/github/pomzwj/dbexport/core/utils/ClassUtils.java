package io.github.pomzwj.dbexport.core.utils;

import com.google.common.collect.Lists;
import io.github.pomzwj.dbexport.core.anno.DataColumnName;
import io.github.pomzwj.dbexport.core.anno.DbIndexName;
import io.github.pomzwj.dbexport.core.domain.DbColumnInfo;
import io.github.pomzwj.dbexport.core.domain.DbIndexInfo;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.description.modifier.Visibility;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.FieldAccessor;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 类操作工具类，提供字段排序、校验、对象复制及动态Bean创建等功能
 *
 * @author PomZWJ
 */
public class ClassUtils {
    static final Logger log = LoggerFactory.getLogger(ClassUtils.class);
    /** 字段排序结果缓存，避免大表导出时每行都重复反射+排序 */
    private static final ConcurrentHashMap<Class<?>, List<Field>> COLUMN_FIELD_CACHE = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<Class<?>, List<Field>> INDEX_FIELD_CACHE = new ConcurrentHashMap<>();

    /**
     * 对列字段进行排序并缓存结果
     *
     * @param columnInfoClazz 列信息类，需为 {@code DbColumnInfo} 的子类
     * @return 按 {@link DataColumnName#order()} 排序后的字段列表
     */
    public static List<Field>sortColumnField(Class<? extends DbColumnInfo> columnInfoClazz){
        return COLUMN_FIELD_CACHE.computeIfAbsent(columnInfoClazz, clazz -> {
            List<Field> orderedFields = new ArrayList<>(Lists.newArrayList(clazz.getFields()));
            orderedFields.sort(Comparator.comparingInt(field -> field.getAnnotation(DataColumnName.class).order()));
            return orderedFields;
        });
    }

    /**
     * 对索引字段进行排序并缓存结果
     *
     * @param indexInfoClazz 索引信息类，需为 {@code DbIndexInfo} 的子类
     * @return 按 {@link DbIndexName#order()} 排序后的字段列表
     */
    public static List<Field>sortIndexField(Class<? extends DbIndexInfo> indexInfoClazz){
        return INDEX_FIELD_CACHE.computeIfAbsent(indexInfoClazz, clazz -> {
            List<Field> orderedFields = new ArrayList<>(Lists.newArrayList(clazz.getFields()));
            orderedFields.sort(Comparator.comparingInt(field -> field.getAnnotation(DbIndexName.class).order()));
            return orderedFields;
        });
    }

    /**
     * 校验指定的列名是否全部存在于列字段中
     *
     * @param clazz          列信息类，需为 {@code DbColumnInfo} 的子类
     * @param showColumnName 待校验的列名列表
     * @return 全部存在返回 true，否则返回 false；列表为空返回 true
     */
    public static boolean checkColumnField(Class<? extends DbColumnInfo>clazz , List<String>showColumnName){
        if(CollectionUtils.isEmpty(showColumnName)){
            return true;
        }
        List<Field> baseField = ClassUtils.sortColumnField(clazz);
        List<String> fieldNames = baseField.stream().map(Field::getName).collect(Collectors.toList());
        for(String colName : showColumnName){
            if(!fieldNames.contains(colName)){
                return false;
            }
        }
        return true;
    }

    /**
     * 校验指定的索引名是否全部存在于索引字段中
     *
     * @param clazz          索引信息类，需为 {@code DbIndexInfo} 的子类
     * @param showColumnName 待校验的索引名列表
     * @return 全部存在返回 true，否则返回 false；列表为空返回 true
     */
    public static boolean checkIndexField(Class<? extends DbIndexInfo>clazz , List<String>showColumnName){
        if(CollectionUtils.isEmpty(showColumnName)){
            return true;
        }
        List<Field> baseField = ClassUtils.sortIndexField(clazz);
        List<String> fieldNames = baseField.stream().map(Field::getName).collect(Collectors.toList());
        for(String colName : showColumnName){
            if(!fieldNames.contains(colName)){
                return false;
            }
        }
        return true;
    }


    /**
     * 从列名列表中移除基础列字段对应的名称
     *
     * @param showColumnName 待处理的列名列表，会被直接修改
     */
    public static void excludeBaseColumnField(List<String>showColumnName){
        if(CollectionUtils.isEmpty(showColumnName)){
            return;
        }
        List<Field> baseField = ClassUtils.sortColumnField(DbColumnInfo.class);
        for(Field item : baseField){
            if(showColumnName.contains(item.getName())){
                showColumnName.remove(item.getName());
            }
        }
    }

    /**
     * 从索引名列表中移除基础索引字段对应的名称
     *
     * @param showIndexName 待处理的索引名列表，会被直接修改
     */
    public static void excludeBaseIndexField(List<String>showIndexName){
        if(CollectionUtils.isEmpty(showIndexName)){
            return;
        }
        List<Field> baseField = ClassUtils.sortIndexField(DbIndexInfo.class);
        for(Field item : baseField){
            if(showIndexName.contains(item.getName())){
                showIndexName.remove(item.getName());
            }
        }
    }

    /**
     * 复制列信息对象到指定子类实例
     *
     * @param dbColumBean   目标列信息类，需为 {@code DbColumnInfo} 的子类
     * @param dbColumnInfo  源列信息对象
     * @return 复制后的目标对象，失败返回 null
     */
    public static DbColumnInfo copyDbColumnTarget(Class<? extends DbColumnInfo> dbColumBean,DbColumnInfo dbColumnInfo){
        DbColumnInfo target = null;
        try {
            target = dbColumBean.newInstance();
            BeanUtils.copyProperties(target,dbColumnInfo);
            return target;
        }catch (Exception e) {
            log.error("copyDbColumnTarget error",e);
        }
        return null;
    }

    /**
     * 复制索引信息对象到指定子类实例
     *
     * @param dbIndexBean  目标索引信息类，需为 {@code DbIndexInfo} 的子类
     * @param dbIndexInfo  源索引信息对象
     * @return 复制后的目标对象，失败返回 null
     */
    public static DbIndexInfo copyDbIndexTarget(Class<? extends DbIndexInfo> dbIndexBean,DbIndexInfo dbIndexInfo){
        DbIndexInfo target = null;
        try {
            target = dbIndexBean.newInstance();
            BeanUtils.copyProperties(target,dbIndexInfo);
            return target;
        }catch (Exception e) {
            log.error("copyDbColumnTarget error",e);
        }
        return null;
    }

    /**
     * 动态创建列信息Bean，仅包含指定列字段
     *
     * @param clazz          列信息类，需为 {@code DbColumnInfo} 的子类
     * @param showColumnName 需要保留的列名列表，为空或null表示保留全部
     * @return 动态生成的未加载Bean类型
     */
    public static DynamicType.Unloaded<DbColumnInfo> createDbColumBean(Class<? extends DbColumnInfo>clazz, List<String>showColumnName){
        boolean showColumnFlag = CollectionUtils.isNotEmpty(showColumnName);
        List<String> baseColumnList = ClassUtils.sortColumnField(DbColumnInfo.class).stream().map(Field::getName).collect(Collectors.toList());
        List<Field> fields = ClassUtils.sortColumnField(clazz);
        ClassUtils.excludeBaseColumnField(showColumnName);
        DynamicType.Builder<DbColumnInfo> subclass = new ByteBuddy().subclass(DbColumnInfo.class);
        for(Field field : fields){
            if(showColumnFlag && !showColumnName.contains(field.getName())){
                continue;
            }
            if(baseColumnList.contains(field.getName())){
                continue;
            }
            String fieldName = StringUtils.capitalize(field.getName());
            DataColumnName annotation = field.getAnnotation(DataColumnName.class);
            subclass = subclass.defineField(field.getName(), field.getType(), Visibility.PUBLIC).annotateField(AnnotationDescription.Builder.ofType(DataColumnName.class)
                            // 并且给注解的value赋值，由于我的value是定义为：String name() default "";
                            .define("name", annotation.name())
                            .define("order", annotation.order())
                            .define("show", annotation.show())
                            .build())
                    // 添加get方法，返回String，公有public方法
                    .defineMethod("get" + fieldName, field.getType(), Visibility.PUBLIC)
                    // 按照bean方法实现
                    .intercept(FieldAccessor.ofBeanProperty())
                    // 添加set方法，返回void，公有public方法
                    .defineMethod("set" + fieldName, void.class, Visibility.PUBLIC)
                    // 接收的参数类型
                    .withParameter(field.getType())
                    // 按照bean方法实现
                    .intercept(FieldAccessor.ofBeanProperty());
        }
        return subclass.make();
    }

    /**
     * 动态创建索引信息Bean，仅包含指定索引字段
     *
     * @param clazz         索引信息类，需为 {@code DbIndexInfo} 的子类
     * @param showIndexName 需要保留的索引名列表，为空或null表示保留全部
     * @return 动态生成的未加载Bean类型
     */
    public static DynamicType.Unloaded<DbIndexInfo> createDbIndexBean(Class<? extends DbIndexInfo>clazz, List<String>showIndexName){
        boolean showIndexFlag = CollectionUtils.isNotEmpty(showIndexName);
        List<String> baseIndexList = ClassUtils.sortIndexField(DbIndexInfo.class).stream().map(Field::getName).collect(Collectors.toList());
        List<Field> fields = ClassUtils.sortIndexField(clazz);
        ClassUtils.excludeBaseIndexField(showIndexName);
        DynamicType.Builder<DbIndexInfo> subclass = new ByteBuddy().subclass(DbIndexInfo.class);
        for(Field field : fields){
            if(showIndexFlag && !showIndexName.contains(field.getName())){
                continue;
            }
            if(baseIndexList.contains(field.getName())){
                continue;
            }
            String fieldName = StringUtils.capitalize(field.getName());
            DbIndexName annotation = field.getAnnotation(DbIndexName.class);
            subclass = subclass.defineField(field.getName(), field.getType(), Visibility.PUBLIC).annotateField(AnnotationDescription.Builder.ofType(DbIndexName.class)
                            // 并且给注解的value赋值，由于我的value是定义为：String name() default "";
                            .define("name", annotation.name())
                            .define("order", annotation.order())
                            .define("show", annotation.show())
                            .build())
                    // 添加get方法，返回String，公有public方法
                    .defineMethod("get" + fieldName, field.getType(), Visibility.PUBLIC)
                    // 按照bean方法实现
                    .intercept(FieldAccessor.ofBeanProperty())
                    // 添加set方法，返回void，公有public方法
                    .defineMethod("set" + fieldName, void.class, Visibility.PUBLIC)
                    // 接收的参数类型
                    .withParameter(field.getType())
                    // 按照bean方法实现
                    .intercept(FieldAccessor.ofBeanProperty());
        }
        return subclass.make();
    }
}
