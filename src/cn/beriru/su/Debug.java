package cn.beriru.su;

import android.util.Log;

public class Debug {

	private static final String TAG = Debug.class.getCanonicalName();

	public static void log(String exceptionCommand) {
		Log.e(TAG,exceptionCommand);
	}

	
}
