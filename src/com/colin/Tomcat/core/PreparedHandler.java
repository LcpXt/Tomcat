package com.colin.Tomcat.core;

import com.colin.servlet.annotation.WebServlet;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 2024年06月24日17:04
 * 预处理方法
 */
public class PreparedHandler {

    private static final List<String> allClasses = new ArrayList<String>();

    private static final Map<String, String> URIMapping = new HashMap<String, String>();

    /**
     * 递归地获取当前工程下所有类的全限定类名，放入一个数组集合中
     * @param folder
     * @return
     */
    public static List<String> getAllClasses(File folder){
        if(folder.isDirectory()){
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        getAllClasses(file);//递归处理子文件夹
                    }else if(file.getName().endsWith(".java")){
                        //如果是java源文件，则获取类的信息
                        String className = getClassesInfo(file);
//                        System.out.println("当前类的全限定类名："+className);
                        allClasses.add(className);
                    }
                }
            }
        }
        return allClasses;
    }

    /**
     * 获取类的全限定类名
     * @param temp
     * @return
     */
    private static String getClassesInfo(File temp){
        String parent = temp.getParent();
        String src = parent.substring(parent.indexOf("src") + 4);
        String replace = src.replace("\\", ".");
        String containsJavaClassName = replace + "." + temp.getName();
        int i = containsJavaClassName.lastIndexOf(".");
        return containsJavaClassName.substring(0, i);
    }

    /**
     * 遍历当前工程下所有类的全限定类名
     * 通过反射的方式，获取存在@WebServlet注解的类
     * 并将当前@WebServlet注解的值value 和 这个类的全限定名称 做绑定
     * @param currentProjectAllClassesName
     * @throws ClassNotFoundException
     */
    public static Map<String, String> initURIMapping(List<String> currentProjectAllClassesName) throws ClassNotFoundException {
        for (String className : currentProjectAllClassesName) {
            Class<?> aClass = Class.forName(className);
            WebServlet annotation = aClass.getAnnotation(WebServlet.class);
            if(annotation != null){
                URIMapping.put(annotation.value(), className);
            }
        }
//        System.out.println(URIMapping);
        return URIMapping;
    }
}
