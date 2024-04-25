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
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ClassUtils {
    static final Logger log = LoggerFactory.getLogger(ClassUtils.class);
    public static List<Field>sortColumnField(Class<? extends DbColumnInfo> columnInfoClazz){
        List<Field> orderedFields = Lists.newArrayList(columnInfoClazz.getFields());
        orderedFields.sort(Comparator.comparingInt(field -> {
            return field.getAnnotation(DataColumnName.class).order();
        }));
        return orderedFields;
    }

    public static List<Field>sortIndexField(Class<? extends DbIndexInfo> indexInfoClazz){
        List<Field> orderedFields = Lists.newArrayList(indexInfoClazz.getFields());
        orderedFields.sort(Comparator.comparingInt(field -> {
            return field.getAnnotation(DbIndexName.class).order();
        }));
        return orderedFields;
    }

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

    public static DynamicType.Unloaded<DbColumnInfo> createDbColumBean(Class<? extends DbColumnInfo>clazz, List<String>showColumnName){
        List<String> baseColumnList = ClassUtils.sortColumnField(DbColumnInfo.class).stream().map(Field::getName).collect(Collectors.toList());
        List<Field> fields = ClassUtils.sortColumnField(clazz);
        ClassUtils.excludeBaseColumnField(showColumnName);
        DynamicType.Builder<DbColumnInfo> subclass = new ByteBuddy().subclass(DbColumnInfo.class);
        for(Field field : fields){
            if(CollectionUtils.isEmpty(showColumnName)){
                break;
            }
            if(CollectionUtils.isNotEmpty(showColumnName) && !showColumnName.contains(field.getName())){
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
        try {
            //subclass.make().saveIn(new File("D://"));
            return subclass.make();
        } catch (Exception e) {
            log.error("createDbColumBean error",e);
        }
        return null;
    }

    public static DynamicType.Unloaded<DbIndexInfo> createDbIndexBean(Class<? extends DbIndexInfo>clazz, List<String>showIndexName){
        List<String> baseIndexList = ClassUtils.sortIndexField(DbIndexInfo.class).stream().map(Field::getName).collect(Collectors.toList());
        List<Field> fields = ClassUtils.sortIndexField(clazz);
        ClassUtils.excludeBaseIndexField(showIndexName);
        DynamicType.Builder<DbIndexInfo> subclass = new ByteBuddy().subclass(DbIndexInfo.class);
        for(Field field : fields){
            if(CollectionUtils.isEmpty(showIndexName)){
                break;
            }
            if(CollectionUtils.isNotEmpty(showIndexName) && !showIndexName.contains(field.getName())){
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
        try {
            //subclass.make().saveIn(new File("D://"));
            return subclass.make();
        } catch (Exception e) {
            log.error("createDbColumBean error",e);
        }
        return null;
    }
}
