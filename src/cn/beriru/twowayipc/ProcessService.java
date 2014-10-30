package cn.beriru.twowayipc;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

public class ProcessService extends Service {

	public static final String TAG = ProcessService.class.getCanonicalName();
	
	public static final int REGISTER = 1;
	public static final int UNREGISTER = 2;
	public static final int PING = 3;
	public static final int PONG = 4;
	
	protected static final long PONGBACK_INTERVAL = 1000;

	Messenger localMessenger = new Messenger(new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case REGISTER:
				remoteMessenger = msg.replyTo;
				break;
				
			case UNREGISTER:
				remoteMessenger = null;
				break;
			
			case PING:
				showPingToast(msg.arg1);
				postDelayed(new Runnable() {
					@Override
					public void run() {
						pongBack(android.os.Process.myPid());
					}
				}, PONGBACK_INTERVAL);
				break;

			default:
				super.handleMessage(msg);
			}
		}
	});
	
	Messenger remoteMessenger = null;
	
	private void pongBack(int myPid) {
		if(remoteMessenger != null){
			try {
				remoteMessenger.send(Message.obtain(null, PONG, myPid, 0));
			} catch (RemoteException e) {
				Log.d(TAG,e.getMessage());
			}
		}
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return localMessenger.getBinder();
	}

	protected void showPingToast(int fromProcess) {
		Toast.makeText(this, "ping from " + fromProcess + " and perform at " + android.os.Process.myPid(), Toast.LENGTH_SHORT).show();
	}
	
}
