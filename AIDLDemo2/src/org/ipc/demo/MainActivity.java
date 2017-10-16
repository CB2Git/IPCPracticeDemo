
package org.ipc.demo;

import org.ipc.demo.remote.IClientCallBack;
import org.ipc.demo.remote.IRemote;
import org.ipc.demo.remote.RemoteObject;
import org.ipc.demo.services.RemoteService;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.telephony.IccOpenLogicalChannelResponse;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {

    // 远程连接对象
    private IRemote remoteInterface = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_0) {
            unbindService(conn);
        }
        return super.onKeyDown(keyCode, event);
    }

    private ServiceConnection conn = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            remoteInterface = null;
            Log.i("IPC", "onServiceDisconnected");
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i("IPC", service.toString());
            remoteInterface = IRemote.Stub.asInterface(service);
            if (remoteInterface == null) {
                Log.e("IPC", "bind remote service fail");
            }
        }
    };

    /**
     * 绑定远程服务
     */
    public void bindRemoteService(View v) {
        Intent intent = new Intent(this, RemoteService.class);
        bindService(intent, conn, Context.BIND_AUTO_CREATE);
    }

    /**
     * 测试in修饰符
     */
    public void testRemoteIn(View v) {
        if (remoteInterface == null) {
            return;
        }
        RemoteObject obj = new RemoteObject();
        Log.i("IPC", "local obj = " + obj);
        try {
            remoteInterface.testIn(obj);
            Log.i("IPC", "local obj = " + obj);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 测试out修饰符
     */
    public void testRemoteOut(View v) {
        if (remoteInterface == null) {
            return;
        }
        RemoteObject obj = new RemoteObject();
        obj.name = "123";
        Log.i("IPC", "local obj = " + obj);
        try {
            remoteInterface.testOut(obj);
            Log.i("IPC", "local obj = " + obj);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 测试in out修饰符
     */
    public void testRemoteInOut(View v) {
        if (remoteInterface == null) {
            return;
        }
        RemoteObject obj = new RemoteObject();
        Log.i("IPC", "local obj = " + obj);
        try {
            remoteInterface.testInOut(obj);
            Log.i("IPC", "local obj = " + obj);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /***
     * 测试OneWay修饰符
     */
    public void testOneWay(View v) {
        if (remoteInterface == null) {
            return;
        }
        try {
            remoteInterface.testOneWay();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private IBinder mToken = new Binder();

    public void testError(View v) {
        if (remoteInterface == null) {
            return;
        }
        try {
            remoteInterface.testClientError(mToken);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        int i = 0 / 0;
    }

    public void testRegister(View v) {
        if (remoteInterface == null) {
            return;
        }
        try {
            remoteInterface.register(mCallBack);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void testUnregister(View v) {
        if (remoteInterface == null) {
            return;
        }
        try {
            remoteInterface.unregister(mCallBack);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void testLongInvoke(View v) {
        if (remoteInterface == null) {
            return;
        }
        try {
            remoteInterface.testLong(6000);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private IClientCallBack mCallBack = new IClientCallBack.Stub() {

        @Override
        public void showToastInClient(String msg) throws RemoteException {
            Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();

        }
    };

}
