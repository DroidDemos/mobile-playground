package cn.beriru.annotation;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.view.View;

public class Inspector {
	
	public static void inspect(Object o){
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
		} catch (NoSuchMethodException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}
}
