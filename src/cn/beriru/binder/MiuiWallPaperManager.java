package cn.beriru.binder;

import java.io.Serializable;

import cn.beriru.app.App;
import miui.content.res.IThemeService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

public class MiuiWallPaperManager {
	
	private static MiuiWallPaperManager instance = null;
	
	public static MiuiWallPaperManager getInstance(){
		if(instance == null){
			instance = new MiuiWallPaperManager();
		}
		return instance;
	}
	
	
	private  MiuiWallPaperManager(){
		ctx = App.instance;
		bind();
	}
	
	
	private Context ctx;
	
	private IThemeService service  = null;
	
	public static final String  DIST_PACKAGE_NAME = "com.miui.service.THEME";

	private static final String TAG = MiuiWallPaperManager.class.getCanonicalName();
	
	private ServiceConnection conn = new ServiceConnection() {
		
		@Override
		public void onServiceDisconnected(ComponentName name) {
			service = null;
		}
		
		@Override
		public void onServiceConnected(ComponentName name, IBinder binder) {
			service = IThemeService.Stub.asInterface(binder);
		}
	};
	
	
	private boolean bind(){
		try{
			boolean ret = ctx.bindService(new Intent(DIST_PACKAGE_NAME), conn, Context.BIND_AUTO_CREATE);
			Log.d(TAG,"bindservice reuslt : " + ret);
			return ret;
		}catch(Exception e){
			Log.e(TAG,"bindservice with exception : " + Log.getStackTraceString(e));
		}
		return false;
	}
	
	
	private void unbind(){
		ctx.unbindService(conn);
	}
	
	
	/**
	 * wrapper for aidl
	 * @param path
	 */
	public void setWallPaper(String path){
		if(service != null){
			try {
				service.saveIcon(path);
			} catch (RemoteException e) {
				throw new RuntimeException(e);
			}
		}
	}
	
	
	
	

	
}
