package cn.beriru.jni;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import cn.beriru.playground.R;

public class CallJniActivity extends Activity {
	
	private static final String LIB_NAME = "jniCall";
	
	// jni declaration
	private native String concat(String in);
	
	private native int initWatchDog();
	
	static{
		System.loadLibrary(LIB_NAME);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.jni);
		TextView tv = (TextView) findViewById(R.id.jni_str);
		tv.setText(concat(" ,String from java"));
		initWatchDog();
	}

}
