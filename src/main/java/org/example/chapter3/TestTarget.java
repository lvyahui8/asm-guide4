package org.example.chapter3;

public class TestTarget {
    int a ;

    public void setA(int a) {
        if (a > 0) {
            return;
        }
        this.a = a;
    }

    public static void printf(int a) {
        System.out.println(a);
    }
}
