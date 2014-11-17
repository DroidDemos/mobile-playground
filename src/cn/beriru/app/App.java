package cn.beriru.app;

import android.app.Application;
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
	
}
