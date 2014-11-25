package cn.beriru.dynamic;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View.OnClickListener;
import android.widget.Button;
import cn.beriru.playground.R;

public class DynamicJavaActivity extends Activity {
	
	protected static final String TAG = DynamicJavaActivity.class.getCanonicalName();


	InvocationHandler mHandler = new InvocationHandler() {
		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			// warning !!! do not touch proxy anyway because it may cause a recursive call to invoke!!
			Log.d(TAG,"" + method + args + "");
			mBtn.append("Clicked! Processed By dynamic Invocation\n");
			return null;
		}
	};
	
	
	OnClickListener mBtnHandler = (OnClickListener) Proxy.newProxyInstance(
			Thread.currentThread().getContextClassLoader(),
			new Class[]{android.view.View.OnClickListener.class},
			mHandler);
			

	Button mBtn = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shell_code);
		mBtn = (Button) findViewById(R.id.ok);
		mBtn.setOnClickListener(mBtnHandler);
	}
	

}
