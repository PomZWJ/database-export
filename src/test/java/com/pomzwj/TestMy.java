package com.pomzwj;

import com.alibaba.fastjson.JSON;
import com.pomzwj.domain.DbTable;
import com.pomzwj.utils.DbConnnecttion;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class TestMy {
    @Test
    public void fun1(){
        int i=0;
        System.out.println(++i);
    }
    @Test
    public void fun2(){
        int k=1;
        do{
            System.out.println(k--);
        }while (k>1);
    }
    @Test
    public void fun3(){
        List<DbTable> dataList = new ArrayList<>();
        for(int i=0;i<54;i++){
            DbTable dbTable = new DbTable();
            dbTable.setTableName(i+"");
            dbTable.setTableComments(i+"");
            dataList.add(dbTable);
        }
        int pageNum = dataList.size();
        int pageSize = 10;
        int totalPage = (pageNum + pageSize - 1)/pageSize;
        boolean finishFlag = false;
        List[] listArray = new ArrayList[totalPage];
        for(int i=0;i<totalPage;i++){
            List<DbTable>temp = new ArrayList<>();
            for(int j=0;j<pageSize;j++){
                int index = i*pageSize+j;
                if(index>=pageNum){
                    finishFlag = true;
                    break;
                }
                temp.add(dataList.get(index));
            }
            listArray[i] = temp;
            if(finishFlag){
                break;
            }
        }
        //多线程分配
        t(listArray);
        /*for(List list :listArray){
            System.out.println(JSON.toJSONString(list));
        }*/
        for(DbTable d:dataList){
            System.out.println(JSON.toJSONString(d));
        }
    }
    public void t(List[] listArray){
        int num = listArray.length;
        ExecutorService es = Executors.newFixedThreadPool(4);
        try{
            List<Future>resultFuture = new ArrayList<>();
            for(int i=0;i<num;i++){
                int finalI = i;
                Future<Boolean> submit = es.submit(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        try {
                            List<DbTable> list = (List<DbTable>)listArray[finalI];
                            for(int j=0;j<list.size();j++){
                                DbTable dbTable = list.get(j);
                                dbTable.setTableComments(UUID.randomUUID().toString());
                            }
                            return true;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return false;
                    }
                });
                resultFuture.add(submit);
            }
            for(int i=0;i<resultFuture.size();i++){
                try{
                    Boolean o = (Boolean)resultFuture.get(i).get();
                }catch (Exception e){
                  e.printStackTrace();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Test
    public void fun5()throws Exception{
        Connection connection = DbConnnecttion.getConn("jdbc:mysql://localhost:3306/shujuku?serverTimezone=UTC", "root", "safedog@yz2020", "com.mysql.cj.jdbc.Driver");
        Statement statement = null;
        try {
            StringBuffer sb = new StringBuffer();
            sb.append("CREATE TABLE `%s`  (");
            sb.append("`user_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户ID',");
            sb.append("`dept_id` int(11) NULL DEFAULT NULL COMMENT '部门ID',");
            sb.append("`login_name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '登录账号',");
            sb.append("PRIMARY KEY (`user_id`) USING BTREE,INDEX `sys_user_login_name_index`(`login_name`) USING BTREE)");
            sb.append(" ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户信息表' ROW_FORMAT = Dynamic;");
            String sql = sb.toString();
            for(int i=0;i<10*100;i++){
                statement = connection.createStatement();
                boolean execute = statement.execute(String.format(sql, "sys_user_" + i));
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            DbConnnecttion.closeStat(statement);
            System.out.println("运行结束");
        }


    }
}
