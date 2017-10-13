package com.enjoywatch.taidl;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import java.util.ArrayList;
import java.util.List;

public class MyService extends Service {

    private List<Person> persons = new ArrayList<>();

    public MyService() {
    }

    /**
     * 当使用bindService绑定服务的时候，返回实现了AIDL接口的Binder对象
     */
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    /**
     * 实现AIDL接口，IMyAidlInterface.Stub已经实现了android.os.Binder接口
     * AIDL其实是基于Binder的
     */
    private IMyAidlInterface.Stub mBinder = new IMyAidlInterface.Stub() {
        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }

        @Override
        public int add(int num1, int num2) throws RemoteException {
            return num1 + num2;
        }

        @Override
        public List<Person> addPerson(Person person) throws RemoteException {
            persons.add(person);
            return persons;
        }
    };
}
