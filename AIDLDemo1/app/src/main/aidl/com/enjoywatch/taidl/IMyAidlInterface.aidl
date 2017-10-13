package com.enjoywatch.taidl;
import com.enjoywatch.taidl.Person;
interface IMyAidlInterface {

    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);
    /**
    * 两个数相加
    */
    int add(int num1,int num2);

    /**
    * 传递Parcelable对象，需要使用in out标记出入
    */
    List<Person> addPerson(in Person person);
}
