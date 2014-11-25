package cn.beriru.su;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import android.util.Log;

public class StreamingProcess extends Thread {

	private interface OnLineListener {
		public void onLine(String line);
	}


	private static final String TAG = StreamingProcess.class.getCanonicalName();
	
	private String shell = "";
	private BufferedReader reader = null;
	private List<String> writer = null;
	private OnLineListener listener = null;
	
	
	public StreamingProcess(String shell,InputStream ins,List<String> ots){
		this.shell = shell;
		reader = new BufferedReader(new InputStreamReader(ins));
		writer = ots;
	}
	
	
	@Override
	public void run(){
		try{
			String line = null;
			while((line = reader.readLine()) != null){
				Log.d(TAG,String.format("[%s] %s", shell,line));
				if(writer != null){
					writer.add(line);
					if(listener != null){
						listener.onLine(line);
					}
				}
			}
		}catch(IOException e){
			Log.e(TAG,e.getMessage());
		}finally{
			try {
				reader.close();
			} catch (IOException e) {
				Log.e(TAG,e.getMessage());
			}
		}
		
		
		
	}
	
}
