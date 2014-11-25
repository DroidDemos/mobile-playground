package cn.beriru.su;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import android.util.Log;

import cn.beriru.app.App;

public class Shell {
	
	
	public interface OnShellExecuted {
		public void onShellExec(List<String> rets);
		public void handleException(Throwable e);
	}
	
	
	public static void run(final String cmd,final OnShellExecuted callback){
		new Thread(){
			@Override
			public void run() {
				try{
					final List<String> rets = Shell.run(cmd,new String[]{},new String[]{},true);
					if(callback != null){
						App.runOnMainThead(new Runnable() {
							@Override
							public void run() {
								callback.onShellExec(rets);
							}
						});
					}
				}catch(final Throwable e){
					if(callback != null){
						App.runOnMainThead(new Runnable() {
							@Override
							public void run() {
								callback.handleException(e);
							}
						});
					}
				}
			}
		}.run();
	}
	
	
	public static List<String> run(String shell,String[] commands,String[] env,boolean wantSTDERR) throws Throwable{
		String shellUpper = shell.toUpperCase(Locale.ENGLISH);

		if(Debug.onMainThread()){
			Debug.log(ShellOnMainThreadException.EXCEPTION_COMMAND);
			throw new ShellOnMainThreadException(ShellOnMainThreadException.EXCEPTION_COMMAND);
		}

		Debug.log(shellUpper);

		if(env != null){
			Map<String,String> newEnv  = new HashMap<String, String>();
			newEnv.putAll(System.getenv());
			for(String entry : env){
				String[] pair = entry.split("\\s*=\\s*");
				if(pair.length == 2){
					newEnv.put(pair[0].trim(), pair[1].trim());
				}
			}

			List<String> newPair = new ArrayList<String>();
			for(Entry<String, String> p : newEnv.entrySet()){
				newPair.add(p.getKey() + "=" + p.getValue());
			}
			env = newPair.toArray(new String[0]);
		}


		// setup new environment
		List<String> res = Collections.synchronizedList(new ArrayList<String>());
		Process process = Runtime.getRuntime().exec(shell, env);
		DataOutputStream STDIN = new DataOutputStream(process.getOutputStream());
		StreamingProcess STDOUT = new StreamingProcess(shellUpper + "-", process.getInputStream()  , res);
		StreamingProcess STDERR = new StreamingProcess(shellUpper + "*",process.getErrorStream() , wantSTDERR ? res : null);
		STDOUT.start();
		STDERR.start();

		for(String write : commands){
			Debug.log(String.format("[%s+] %s", shellUpper,write));
			STDIN.write((write + "\n").getBytes("UTF-8"));
			STDIN.flush();
		}

		try{
			STDIN.write("exit\n".getBytes("UTF-8"));
			STDIN.flush();
		}catch(IOException e){

		}

		process.waitFor();

		STDIN.close();
		STDOUT.join();
		STDERR.join();
		process.destroy();

		if(Su.isSu(shell) && (process.exitValue() == 255)){
			res = null;
		}
		return res;
	}
	
	
	
	protected static String[] mockCommands = new String[]{
		"echo -BOC-",
		"id"
	};
	
	
	
	protected static boolean parseAvailableResult(List<String> ret, boolean  checkForRoot){
		if(ret == null){
			return false;
		}
		
		boolean echoSeen = false;
		for(String line : ret){
			if(line.contains("uid=")){
				return !checkForRoot || line.contains("uid=0");
			}else if(line.contains("-BOC-")){
				echoSeen = true;
			}
			
		}
		return echoSeen;
	}
	
	
}
