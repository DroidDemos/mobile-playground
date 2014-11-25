package cn.beiru.annotation;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

public class Inspector {
	
	public void inspect(Object o){
		List<Class<?>> allowTypes = Arrays.asList(new Class<?>[]{
				Activity.class,
				ViewGroup.class,
		});
		
		
		Field[] fields = o.getClass().getDeclaredFields();
		for(Field f : fields){
			if(f.isAnnotationPresent(Views.class)){
				Views annotationData = f.getAnnotation(Views.class);
				int value = annotationData.field();
				
				// f.set(o, v);
				
				/*
				if(!TextUtils.isEmpty(field)){
					Object value = getValueDispatchType(f.getType(),field,c);
					try {
						f.set(o, value);
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					}
				}
				*/
				
			}
		}
	}

}
