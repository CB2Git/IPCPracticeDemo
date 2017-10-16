
package org.ipc.messengerdemo.remote;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

public class RemoteService extends Service {

    private static final class RemoteHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            Log.i("IPC", "msg.what = " + msg.what);
            if (msg.what == RemoteConstants.MESSAGE_FROM_CLIENT) {
                Bundle data = msg.getData();
                String string = data.getString("msg");
                Log.i("IPC", string);
                // 开始回复客户端
                Messenger replyTo = msg.replyTo;
                if (replyTo != null) {
                    Message obtain = Message.obtain();
                    obtain.what = RemoteConstants.MESSAGE_FROM_REMOTE;
                    Bundle bundle = new Bundle();
                    bundle.putString("msg", "msg from Service");
                    obtain.setData(bundle);
                    try {
                        replyTo.send(obtain);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    // 处理客户端消息的Handler
    private RemoteHandler mHandler = new RemoteHandler();

    // 传递消息的Messenger
    private Messenger mMessenger = new Messenger(mHandler);

    @Override
    public IBinder onBind(Intent intent) {
        int check = checkCallingOrSelfPermission("org.remote.IPC");
        if (check == PackageManager.PERMISSION_DENIED) {
            Log.i("IPC", "bind error");
            return null;
        }
        return mMessenger.getBinder();
    }

}
