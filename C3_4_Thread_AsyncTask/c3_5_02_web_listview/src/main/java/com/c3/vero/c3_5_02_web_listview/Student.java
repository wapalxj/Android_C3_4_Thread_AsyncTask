package com.c3.vero.c3_5_02_web_listview;

/**
 * Created by vero on 2015/12/10.
 */
public class Student {
    private String sid;
    private String name;
    private int age;

    public Student() {

    }
    public Student(String sid, String name, int age) {
        this.sid = sid;
        this.age = age;
        this.name = name;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
