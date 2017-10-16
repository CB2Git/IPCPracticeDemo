
package org.ipc.demo.services;

import org.ipc.demo.remote.IClientCallBack;
import org.ipc.demo.remote.IRemote;
import org.ipc.demo.remote.IRemote.Stub;
import org.ipc.demo.remote.RemoteObject;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.os.SystemClock;
import android.util.Log;

public class RemoteService extends Service {

    private RemoteCallbackList<IClientCallBack> mList = new RemoteCallbackList<IClientCallBack>();

    @Override
    public void onDestroy() {
        mList.kill();
        super.onDestroy();
    }

    /**
     * 这里也可以进行权限验证，null不允许绑定，非null允许绑定
     */
    @Override
    public IBinder onBind(Intent arg0) {
        // 如果声明了com.remote.service.PRI权限，才允许绑定
        int check = checkCallingOrSelfPermission("com.remote.service.PRI");
        Log.d("IPC", "onbind check=" + check);
        if (check == PackageManager.PERMISSION_DENIED) {
            return null;
        }
        return mStud;
    }

    private IRemote.Stub mStud = new Stub() {

        /**
         * 这里可以进行权限验证，true，允许绑定，false，不允许绑定
         */
        public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags)
                throws RemoteException {
            int check = checkCallingOrSelfPermission("com.ryg.chapter_2.permission.ACCESS_BOOK_SERVICE");
            Log.d("IPC", "onbind check=" + check);
            // getCallingPid();
            // getCallingUid();
            // 只允许包名以org.ipc.demo开头的app 调用本服务提供的方法
            // 此方法并不能阻止绑定，但是能让非法调用者无法使用本服务提供的方法
            String[] packagesForUid = getPackageManager().getPackagesForUid(getCallingUid());
            if (packagesForUid != null && packagesForUid.length > 0) {
                if (packagesForUid[0].startsWith("org.ipc.demo")) {
                    return super.onTransact(code, data, reply, flags);
                }
            }

            return false;
        };

        @Override
        public void testIn(RemoteObject obj) throws RemoteException {
            Log.i("IPC", "testIn");
            dealLoacl(obj);
        }

        @Override
        public void testOut(RemoteObject obj) throws RemoteException {
            dealLoacl(obj);

        }

        @Override
        public void testInOut(RemoteObject obj) throws RemoteException {
            dealLoacl(obj);
        }

        private void dealLoacl(RemoteObject obj) {
            Log.i("IPC", "remote obj = " + obj);
            obj.id = 100;
            Log.i("IPC", "modify obj id = " + obj);
        }

        @Override
        public void testLong(long time) throws RemoteException {
            if (Looper.getMainLooper().equals(Looper.myLooper())) {
                Log.i("IPC", "main thread");
            } else {
                Log.i("IPC", "no main thread");
            }
            SystemClock.sleep(time);
        }

        @Override
        public void testOneWay() throws RemoteException {
            // 阻塞自己，以后的调用排队
            Log.i("IPC", Thread.currentThread().getName());
            SystemClock.sleep(10000);
        }

        @Override
        public void testClientError(IBinder binder) throws RemoteException {
            binder.linkToDeath(new DeathRecipient() {

                @Override
                public void binderDied() {
                    Log.i("IPC", "client died");
                }
            }, 0);
        }

        @Override
        public void register(IClientCallBack callback) throws RemoteException {
            mList.register(callback);
            callback.asBinder().linkToDeath(new DeathRecipient() {

                @Override
                public void binderDied() {
                    // TODO Auto-generated method stub

                }
            }, 0);
            int i = mList.beginBroadcast();
            Log.i("IPC", "size = " + (i - 1));
            while (i > 0) {
                i--;
                try {
                    mList.getBroadcastItem(i).showToastInClient("i am from Server");
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
            mList.finishBroadcast();
        }

        @Override
        public void unregister(IClientCallBack callback) throws RemoteException {
            mList.unregister(callback);
        }
    };

}
