package com.pomzwj;

import com.pomzwj.constant.DataBaseType;

public class Test {
    @org.junit.Test
    public void fun1(){
        String path = this.getClass().getResource("/").getPath();
        System.out.println(path);
    }

}
