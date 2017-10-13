
package org.ipc.demo.remote;

import org.ipc.demo.remote.RemoteObject;
import org.ipc.demo.remote.IClientCallBack;
import android.os.IBinder;

 interface  IRemote{

	void testIn(in RemoteObject obj);

	void testOut(out RemoteObject obj);
	
	void testInOut(inout RemoteObject obj);
	
	 void testLong(long time);
	 
	 oneway void testOneWay();
	 
	 void testClientError(IBinder binder);
	 
	 void register(IClientCallBack callback);
	 
	 void unregister(IClientCallBack callback);
}