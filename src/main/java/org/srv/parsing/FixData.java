package org.srv.parsing;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class FixData {
    
    protected Logger log = LoggerFactory.getLogger(getClass());
    
    private Class<?> obj = null;

    private Constructor constructor = null;

    private ClassData classData = null;

    public FixData(String className) throws ClassNotFoundException {
        obj = Class.forName(className);
    }

    public FixData(Class<?> obj) throws ClassNotFoundException, NoSuchMethodException, SecurityException {

        Class[] parameterTypes = {};
        this.constructor = obj.getDeclaredConstructor(parameterTypes);

        this.obj = obj;

        classData = this.obj.getDeclaredAnnotation(ClassData.class);
        //System.out.println("classData " + classData.length());
    }

    public byte[] toByte(Object object) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException {


        byte[] desc = new byte[classData.length()];


        Field[] field = obj.getDeclaredFields();
        int i = 0;
        for(Field f : field) {
            basic b = f.getDeclaredAnnotation(basic.class);

            //System.out.println(f.getName() + ":" + b.start() + ":" + b.length());

            Method method = findMethod(f.getName(), "get");

            String getData = (String)method.invoke(object);

            byte[] src = getSpaceByte(b.length(), getData.getBytes());

            System.arraycopy(src, 0, desc, i, src.length);

            i += src.length;

        }

        return desc;
    }


    public Object toBean(byte[] str) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException {

        log.info(classData.length() + ":" + str.length );
        if(classData.length() != str.length){
            return null;
        }

        Field[] field = obj.getDeclaredFields();
        int i = 0;

        Object object = obj.newInstance();

        for(Field f : field) {
            basic b = f.getDeclaredAnnotation(basic.class);

            Method method = findMethod(f.getName(), "set");

            String data = new String(str, i, b.length());

            i += b.length();

            method.invoke(object, data);
        }

        return object;
    }

    public Method findMethod(String fieldName, String type) {

        Method[] method = obj.getDeclaredMethods();

        String name = type + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1, fieldName.length());

        for(Method m : method) {
            if(m.getName().indexOf(type) > -1 && m.getName().equals(name)) {
                return m;
            }
        }
        return null;
    }


    private byte[] getSpaceByte(int len, byte[] str) {


        if(len <= str.length) {
            return str;
        }else {
            byte[] temp = new byte[len];
            System.arraycopy(str, 0, temp, 0, str.length);


            for(int i = str.length; i < len; i++) {
                temp[i] = ' ';
            }

            return temp;
        }
    }

}
