package cn.beriru.su;

import android.os.Looper;
import android.util.Log;

public class Debug {

	private static final String TAG = Debug.class.getCanonicalName();

	public static boolean onMainThread() {
		return Thread.currentThread() == Looper.getMainLooper().getThread();
	}

	public static void log(String exceptionCommand) {
		Log.e(TAG,exceptionCommand);
	}

	
}
