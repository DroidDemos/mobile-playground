package cn.beriru.binder;

import miui.content.res.IThemeService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import cn.beriru.app.App;




/**
 *  <h3>brief summary for interactive with system service</h3>
 *  1. adb shell service list 
 *  	to get all system running/registered service, this will result in something like
 *  	<pre>
 *  	65      appops: [com.android.internal.app.IAppOpsService]
 *  	</pre>
 *  
 *  2. adbs shell dumpsys appops
 *  	dump service internal information if provided
 *  	<pre>
 *  	Uid u0a10103:
 *   	Package com.tencent.mm:
 *  	COARSE_LOCATION: mode=0; duration=0
 *     	FINE_LOCATION: mode=0; time=+5d14h14m37s526ms ago; duration=0
 *     	GPS: mode=0; duration=0
 *    	VIBRATE: mode=0; duration=0
 *     	READ_CONTACTS: mode=0; duration=0
 *     	WRITE_CONTACTS: mode=0; duration=0
 *     	READ_CALENDAR: mode=0; time=+29d16h41m55s981ms ago; duration=0
 *     	WRITE_CALENDAR: mode=0; duration=0
 *     	SYSTEM_ALERT_WINDOW: mode=0; rejectTime=+50d1h47m58s786ms ago; duration=0
 *     	Unknown(31): mode=0; duration=0
 *     	Unknown(35): mode=0; duration=0
 *     	Unknown(36): mode=0; duration=0
 *  	</pre>
 * 
 *  3. adb shell dumpsys meminfo cn.beriru.playground
 *   	dump (memory / other useful) information about package
 * 
 *  4. adb shell dumpsys activity broadcasts
 *  
 *  5. adb shell dumpsys 
 * 
 *  6. adb shell service call appops 12
 *  Result: Parcel(
 *		0x00000000: ffffffff 0000003f 00690075 00200064 '....?...u.i.d. .'
 * 		0x00000010: 00300032 00300030 00640020 0065006f '2.0.0.0. .d.o.e.'
 * 		0x00000020: 00200073 006f006e 00200074 00610068 's. .n.o.t. .h.a.'
 * 		0x00000030: 00650076 00610020 0064006e 006f0072 'v.e. .a.n.d.r.o.'
 * 		0x00000040: 00640069 0070002e 00720065 0069006d 'i.d...p.e.r.m.i.'
 * 		0x00000050: 00730073 006f0069 002e006e 00500055 's.s.i.o.n...U.P.'
 * 		0x00000060: 00410044 00450054 0041005f 00500050 'D.A.T.E._.A.P.P.'
 * 		0x00000070: 004f005f 00530050 0053005f 00410054 '_.O.P.S._.S.T.A.'
 * 		0x00000080: 00530054 0000002e                   'T.S.....        ')
 */



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
	public void setIcon(String path){
		if(service != null){
			try {
				service.saveIcon(path);
			} catch (RemoteException e) {
				throw new RuntimeException(e);
			}
		}
	}
	
	
	
	

	
}
