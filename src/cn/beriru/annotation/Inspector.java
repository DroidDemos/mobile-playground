package cn.beriru.annotation;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.renderscript.RenderScript;
import android.view.View;
import android.view.View.OnClickListener;

public class Inspector {
	
	public static void inspect(Object o){
		findViewById(o);
		setOnClickListener(o);
	}
	
	private static void findViewById(Object o){
		try {
			Method m = o.getClass().getMethod("findViewById", int.class);
			Field[]  fields = o.getClass().getDeclaredFields();
			for(Field f : fields){
				if(f.isAnnotationPresent(InjectView.class)){
					f.setAccessible(true);
					InjectView viewId = f.getAnnotation(InjectView.class);
					int value = viewId.value();
					View dstView = (View) m.invoke(o, value);
					f.set(o, dstView);
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	
	private static View findViewById(Object o,int id){
		try{
			Method m = o.getClass().getMethod("findViewById", int.class);
			View view = (View) m.invoke(o, id);
			return view;
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	private static void setOnClickListener(final Object o){
		try{
			Method[] ms = o.getClass().getDeclaredMethods();
			for(final Method m : ms){
				if(m.isAnnotationPresent(Click.class)){
					int elementId = m.getAnnotation(Click.class).value();
					View v = findViewById(o, elementId);
					v.setOnClickListener((OnClickListener) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class<?>[]{
						OnClickListener.class
					}, new InvocationHandler() {
						@Override
						public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
							Class<?>[] params = m.getParameterTypes();
							if(params.length == 0){
								return m.invoke(o);
							}
							return m.invoke(o, args);
						}
					}));
				}
			}
		}catch(Exception e){
			throw new RuntimeException(e);
		}
		
	}
	
	
	
	
}
