package cn.beriru.twowayipc;

import static cn.beriru.twowayipc.ProcessService.PING;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import static cn.beriru.twowayipc.ProcessService.*;


/**
 * simple two way IPC demo using messenger
 */
public class MainActivity extends Activity {

	public final static String TAG = MainActivity.class.getCanonicalName();
	
	TextView mContent = null;
	
	
	
	Messenger mLocalMessenger = new Messenger(new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case PONG:
				processPong(msg.arg1);
				break;

			default:
				break;
			}
		}
		
	});
	
	Messenger mRemoteMessenger;
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        mContent = (TextView) findViewById(R.id.content);
        mContent.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mRemoteMessenger != null){
					try {
						mRemoteMessenger.send(Message.obtain(null, PING, android.os.Process.myPid(), 0));
					} catch (RemoteException e) {
						Log.d(TAG,e.getMessage());
					}
				}
			}
		});
        
        bindRemoteService();
    }


    protected void processPong(int fromProcess) {
    	String s = "\nPONG from " + fromProcess + " process in " + android.os.Process.myPid();
    	mContent.append(s);
	}


	private void bindRemoteService() {
    	Intent i = new Intent(this,ProcessService.class);
    	bindService(i, new ServiceConnection() {
			
			@Override
			public void onServiceDisconnected(ComponentName name) {
				mRemoteMessenger = null;
				
			}
			
			@Override
			public void onServiceConnected(ComponentName name, IBinder service) {
				mRemoteMessenger = new Messenger(service);
				registerBack();
			}
		}, BIND_AUTO_CREATE);
	}


	protected void registerBack() {
		Message msg = Message.obtain(null, REGISTER);
		msg.replyTo = mLocalMessenger;
		try {
			mRemoteMessenger.send(msg);
		} catch (RemoteException e) {
			Log.d(TAG,e.getMessage());
		}
	}


	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
