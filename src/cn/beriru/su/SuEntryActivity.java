package cn.beriru.su;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import cn.beriru.playground.R;

public class SuEntryActivity extends Activity {

	
	
	public TextView mResult;
	public EditText mInput;
	public Button mOk;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shell_code);
		mResult = (TextView) findViewById(R.id.resp);
		mInput = (EditText) findViewById(R.id.input);
		mOk = (Button) findViewById(R.id.ok);
		final Shell shell = new Shell();
		
				
		mOk.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String cmd = mInput.getText().toString();
				shell.run("su", new String[]{cmd}, null, true);
				mInput.setText("");
				
			}
		});
		
		// currently no ButterKnife available
		// Views.inspect();
	}
	
	
	
	
	

}
