package org.srv.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 *
 * @ClassName   : BeanUtil.java
 * @Description : Bean Object 관련 유틸리티 서비스
 * @author ADM기술팀
 * @since 2016. 6. 29.
 * @version 1.0
 * @see
 * @Modification Information
 * <pre>
 *     since          author              description
 *  ===========    =============    ===========================
 *  2016. 6. 29.     ADM기술팀     	최초 생성
 * </pre>
 */
public class BeanUtil {
	//bean1 : 원본 bean , bean : 복사 대상 bean
	public static void copyValue(Object bean1,Object bean2) throws Exception{
		Class<?> beanClass1	= bean1.getClass();
		Class<?> beanClass2	= bean2.getClass();

		Field[]	fieldArr1	= beanClass1.getDeclaredFields();
		Field	field2		= null;
		
		@SuppressWarnings("unused")
		Method  method1		= null;
		@SuppressWarnings("unused")
		Method  method2		= null;
		
		for(int i=0;i<fieldArr1.length;i++){
			method1	= null;
			method2	= null;
			field2	= null;
			//if not exists field in bean2 then ignore
			try{field2=beanClass2.getDeclaredField(fieldArr1[i].getName());}catch(NoSuchFieldException nsfe){}
			if(field2==null)
				continue;

			if(fieldArr1[i].getModifiers()==Modifier.PUBLIC && field2.getModifiers()==Modifier.PUBLIC ){
				field2.set(bean2, fieldArr1[i].get(bean1));
			}else if(fieldArr1[i].getModifiers()==Modifier.PUBLIC){
				getSetter(beanClass2,field2).invoke(bean2, new Object[]{fieldArr1[i].get(bean1)});
			}else if(field2.getModifiers()==Modifier.PUBLIC){
				field2.set(bean2, getGetter(beanClass1,fieldArr1[i]).invoke(bean1, new Object[0]));
			}else{
				getSetter(beanClass2,field2).invoke(bean2, new Object[]{getGetter(beanClass1,fieldArr1[i]).invoke(bean1, new Object[0])});
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public static Method getGetter(Class beanClass,Field field) throws NoSuchMethodException{
		String fieldName	= field.getName();
		Method method	= null;
		try{method	= beanClass.getMethod("get"+fieldName.substring(0,1).toUpperCase()+fieldName.substring(1),new Class<?>[0]);}catch(NoSuchMethodException nsme){}
		if(method==null){
			try{method	= beanClass.getMethod("get"+fieldName,new Class<?>[0]);}catch(NoSuchMethodException nsme){}
		}
		if(method==null){
			try{method	= beanClass.getMethod("get"+fieldName.toUpperCase(),new Class<?>[0]);}catch(NoSuchMethodException nsme){}
		}
		if(method==null){
			try{method	= beanClass.getMethod("get"+fieldName.toLowerCase(),new Class<?>[0]);}catch(NoSuchMethodException nsme){}
		}
		if(method==null)
			throw new NoSuchMethodException("Can not find method "+beanClass.getName()+".get["+fieldName+"]()");
		return method;
	}

	@SuppressWarnings("unchecked")
	public static Method getSetter(Class beanClass,Field field) throws NoSuchMethodException{
		String fieldName	= field.getName();
		Method method	= null;
		Class  fClass	= field.getType();

		try{method	= beanClass.getMethod("set"+fieldName.substring(0,1).toUpperCase()+fieldName.substring(1),new Class<?>[]{fClass});}catch(NoSuchMethodException nsme){}
		if(method==null){
			try{method	= beanClass.getMethod("set"+fieldName,new Class<?>[]{fClass});}catch(NoSuchMethodException nsme){}
		}
		if(method==null){
			try{method	= beanClass.getMethod("set"+fieldName.toUpperCase(),new Class<?>[]{fClass});}catch(NoSuchMethodException nsme){}
		}
		if(method==null){
			try{method	= beanClass.getMethod("set"+fieldName.toLowerCase(),new Class<?>[]{fClass});}catch(NoSuchMethodException nsme){}
		}

		if(method==null)
			throw new NoSuchMethodException("Can not find method "+beanClass.getName()+".set["+fieldName+"]("+fClass.getName()+")");
		return method;
	}
}