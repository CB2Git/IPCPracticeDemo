package com.enjoywatch.aidlcilent;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.enjoywatch.taidl.IMyAidlInterface;
import com.enjoywatch.taidl.Person;

import java.util.List;


public class MainActivity extends Activity implements View.OnClickListener {

    private Button mAddBtn;
    private Button mAddPerson;
    private IMyAidlInterface mMyAidlInterface;
    private ServiceConnection mServiceConn = new ServiceConnection() {
        /**
         * 当绑定成功以后调用
         */
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mMyAidlInterface = IMyAidlInterface.Stub.asInterface(service);
        }

        /**
         * 断开连接的时候调用
         */
        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        bindRemoteService();
    }

    /**
     * 绑定远程Service，其中Service定义Action为remote_service
     */
    private void bindRemoteService() {
        Intent intent = new Intent();
        intent.setAction("remote_service");
        bindService(intent, mServiceConn, Context.BIND_AUTO_CREATE);
    }

    private void initView() {
        mAddBtn = (Button) findViewById(R.id.add);
        mAddPerson = (Button) findViewById(R.id.addPerson);
        mAddBtn.setOnClickListener(this);
        mAddPerson.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add:
                int result = 0;
                try {
                    result = mMyAidlInterface.add(1, 1);
                    Toast.makeText(MainActivity.this, "AIDL返回结果为:1+1=" + result, Toast.LENGTH_SHORT).show();
                } catch (RemoteException e) {
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.addPerson:
                try {
                    List<Person> persons = mMyAidlInterface.addPerson(new Person("1", 1));
                    Toast.makeText(MainActivity.this, persons.toString(), Toast.LENGTH_SHORT).show();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    //当Activity销毁的时候解除绑定
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mServiceConn);
    }
}
