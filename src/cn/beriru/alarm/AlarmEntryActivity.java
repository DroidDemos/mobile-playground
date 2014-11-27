package cn.beriru.alarm;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import cn.beriru.annotation.Click;
import cn.beriru.annotation.InjectView;
import cn.beriru.annotation.Inspector;
import cn.beriru.playground.R;


/**
 * test alarm manager for activity recv
 * @author algerwang
 *
 */
public class AlarmEntryActivity extends Activity {
	
	private static final int REQ_CODE = 1000;
	private AlarmManager alarmManager;
	
	
	@InjectView(R.id.main) Button mTrigger;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.plain);
		Inspector.inspect(this);
	}

	
	@Click(R.id.main)
	public void scheduleAlarm(){
		/*
		getPackageManager().setComponentEnabledSetting(
				new ComponentName(this, AlarmReceiver.class),
				PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
				PackageManager.DONT_KILL_APP);
				*/

		
		alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		
		// pending intent
		Intent i = new Intent(this,AlarmReceiver.class);
		i.setAction("Tick!~");
		
		PendingIntent pi = PendingIntent.getBroadcast(this, REQ_CODE, i, PendingIntent.FLAG_UPDATE_CURRENT);
		
		
		// every 5mins
		int interval = 1000 * 60 * 5;
		long wakeupTime = System.currentTimeMillis() + interval;
		alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, wakeupTime, interval , pi);
		// alarmManager.set(AlarmManager.RTC_WAKEUP, wakeupTime, pi);
		mTrigger.setText("Clicked");
		mTrigger.setEnabled(false);
		
	}

}
