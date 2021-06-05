package com.pomzwj;

import org.junit.Test;

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

}
