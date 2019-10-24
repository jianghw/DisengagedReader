package com.jianghw.lib.xjava;

import java.util.ArrayList;
import java.util.List;

public class MyClass {
    public static void main(String[] args) {

    }

    private static void abc(int a) {
        if (a > 10) {
            return;
        }
        System.out.println("----->");
    }

    private static void fsdfsd(int a) {
        if (a > 10) {
            return;
        }
        System.out.println("-----dfdfddfd>");
    }

    public static List<Demo> for2() {
        List<Demo> demos = new ArrayList<Demo>();
        Demo demo = null;
        for (int i = 0; i < 10; i++) {
            if (demo != null) {
                demo = null;
            }
            demo = new Demo();
            demo.setId(i);
            demos.add(demo);
        }
        return demos;
    }

    static class Demo {
        int id;

        public Demo() {
        }

        void setId(int id) {
            this.id = id;
        }

        int getId() {
            return id;
        }
    }
}
