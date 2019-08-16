package com.gehj.greendao;

import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by lenovo on 2017/1/15.
 * 读取propertise文件
 */
public class PropertiseReader {//读出映射关系
    public  Map<String, String> getProperties() {

        Properties props = new Properties();//就是一个hashTable；
        Map<String, String> map = new HashMap<String, String>();
        try {
            //this相当于一个本身的一个实例，指向自己；this.getClass()与匿名类.getClass()一样；
            //this在静态方法内；
            InputStream in = getClass().getResourceAsStream("type.properties");
            props.load(in);
            Enumeration en = props.propertyNames();
            while (en.hasMoreElements()) {
                String key = (String) en.nextElement();
                String property = props.getProperty(key);
                map.put(key, property);
//				System.out.println(key + "  " + property);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }
}
