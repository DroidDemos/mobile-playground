package cn.beriru.su;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import cn.beriru.annotation.InjectView;
import cn.beriru.annotation.Inspector;
import cn.beriru.app.App;
import cn.beriru.playground.R;
import cn.beriru.su.Shell.OnShellExecuted;

public class SuEntryActivity extends Activity {

	
	
	@InjectView(R.id.resp) public TextView mResult;
	@InjectView(R.id.input) EditText mInput;
	@InjectView(R.id.ok) Button mOk;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shell_code);
		mResult = (TextView) findViewById(R.id.resp);
		mInput = (EditText) findViewById(R.id.input);
		mOk = (Button) findViewById(R.id.ok);
		mOk.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(final View v) {
				new Thread(){
					@Override
					public void run() {
						String cmd = mInput.getText().toString();
						Shell.run(cmd.trim(), new OnShellExecuted(){
							@Override
							public void onShellExec(List<String> rets) {
								mInput.setText("");
								if(rets != null){
									for(String s : rets){
										mResult.append(s + "\n");
									}
								}
							}

							@Override
							public void handleException(Throwable e) {
								App.toast(e.getMessage());
							}
						});
					}
				}.start();
			}
		});
		
		Inspector.inspect(this);
	}
	
	
	
	
	

}
