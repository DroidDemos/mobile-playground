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

public class Shell {
	
	
	public static List<String> run(String shell,String[] commands,String[] env,boolean wantSTDERR){
		String shellUpper = shell.toUpperCase(Locale.ENGLISH);
		
		if(Debug.onMainThread()){
			Debug.log(ShellOnMainThreadException.EXCEPTION_COMMAND);
			throw new ShellOnMainThreadException(ShellOnMainThreadException.EXCEPTION_COMMAND);
		}
		
		Debug.log(shellUpper);
		
		try{
			if(env != null){
				Map<String,String> newEnv  = new HashMap<String, String>();
				newEnv.putAll(System.getenv());
				for(String entry : env){
					String[] pair = entry.split("\\s*=\\s*");
					if(pair.length == 2){
						newEnv.put(pair[0].trim(), pair[1].trim());
					}
				}
				
				List<String> newPair = Collections.emptyList();
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
			
			try{
				STDIN.close();
			}catch(IOException e){
			}
			
			STDOUT.join();
			STDERR.join();
			process.destroy();
			
			if(Su.isSu(shell) && (process.exitValue() == 255)){
				res = null;
			}
			return res;
			
		}catch(Exception e){
			Debug.log(e.getMessage());
		}
		return null;
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
