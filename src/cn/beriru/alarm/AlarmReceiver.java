package cn.beriru.alarm;

import java.util.HashMap;
import java.util.Map;

import cn.beriru.app.App;

import android.R;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class AlarmReceiver extends BroadcastReceiver {
	
	public static Map<String,Integer> alarmRecord = new HashMap<String,Integer>();
	
	public static int NOTIFICATION_ID = 1;
	
	@SuppressLint("NewApi") 
	@Override
	public void onReceive(Context context, Intent intent) {
		String actionName = intent.getAction();
		Integer count = alarmRecord.get(actionName);
		int cnt = 0;
		if(count != null){
			cnt = count.intValue();
		}
		cnt++;
		alarmRecord.put(actionName,Integer.valueOf(cnt));
		String s = String.format(" %s cnt:  %s", actionName,cnt);
		
		Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.zoom_plate);
		
		Notification.Builder builder = new Notification.Builder(context);
		builder.setSmallIcon(R.drawable.zoom_plate);
		builder.setAutoCancel(true);
		builder.setContentText(s);
		builder.setContentTitle("Minami");
		builder.setTicker("miaonami going on");
		builder.setLargeIcon(bitmap);
		Notification no = builder.build();
		
		NotificationManager notifyManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		notifyManager.notify(actionName.hashCode(),no);
	}

}
