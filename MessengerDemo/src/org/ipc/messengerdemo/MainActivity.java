
package org.ipc.messengerdemo;

import org.ipc.messengerdemo.remote.RemoteConstants;
import org.ipc.messengerdemo.remote.RemoteService;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private static final class ClientHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == RemoteConstants.MESSAGE_FROM_REMOTE) {
                Bundle data = msg.getData();
                String string = data.getString("msg");
                Log.i("IPC", string);
            }
        }
    }

    // 处理服务端返回的消息
    private ClientHandler mClientHandler = new ClientHandler();

    // 获取远程服务的消息的Messenger
    private Messenger mClientMessenger = new Messenger(mClientHandler);

    // 创建Messenger对象
    private Messenger mMessenger;

    private ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i("IPC", "remote service died");
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i("IPC", "onServiceConnected");
            // 使用IBinder对象初始化Messenger对象
            mMessenger = new Messenger(service);
            // 获取一个消息对象
            Message message = Message.obtain();
            message.what = RemoteConstants.MESSAGE_FROM_CLIENT;
            // 设置需要传递的数据
            Bundle data = new Bundle();
            data.putString("msg", "msg from client");
            message.setData(data);
            message.replyTo = mClientMessenger;
            try {
                mMessenger.send(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    };

    public void onBind(View v) {
        Intent intent = new Intent(this, RemoteService.class);
        bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
        Log.i("IPC", "bind Remote Service");
    }

    @Override
    protected void onDestroy() {
        unbindService(mServiceConnection);
        Log.i("IPC", "unbindService");
        super.onDestroy();
    }

}
