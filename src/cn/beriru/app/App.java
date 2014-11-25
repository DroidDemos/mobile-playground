package cn.beriru.app;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

public class App extends Application {

	public static App instance = null;
	
	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
	}

	public static void toast(String err) {
		Toast.makeText(instance, err, Toast.LENGTH_SHORT).show();
	}
	
	
	public static void runOnMainThead(Runnable continuation){
		runOnMainThreadDelayed(continuation, 0);
	}
	
	
	public static void runOnMainThreadDelayed(Runnable continuation,int delayMill){
		if(isOnMainThread() && delayMill == 0){
			continuation.run();
		}else{
			sHandler.postDelayed(continuation,delayMill);
		}
	}
	
	
	public static Looper sLooper = Looper.getMainLooper();
	
	public static Handler sHandler = new Handler(sLooper);
	
	public static boolean isOnMainThread(){
		return Thread.currentThread() == sLooper.getThread();
	}
	
	
}
