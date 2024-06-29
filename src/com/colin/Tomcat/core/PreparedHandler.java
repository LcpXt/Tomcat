package com.colin.Tomcat.core;

import com.colin.Tomcat.exception.NotListenerException;
import com.colin.Tomcat.exception.NotServeletException;
import com.colin.Tomcat.impl.TomcatFilterChain;
import com.colin.servlet.annotation.WebFilter;
import com.colin.servlet.filter.Filter;
import com.colin.servlet.filter.FilterChain;
import com.colin.servlet.servlet.HttpServletRequest;
import com.colin.servlet.servlet.HttpServletResponse;
import com.colin.servlet.servlet.Servlet;
import com.colin.servlet.annotation.WebListener;
import com.colin.servlet.annotation.WebServlet;

import java.io.File;
import java.io.IOException;
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

    private static final List<TomcatFilterChain> filterChainList = new ArrayList<>();

    public static TomcatFilterChain firstFilterChain;

    public static TomcatFilterChain lastFilterChain;

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
    public static Map<String, String> initURIMapping(List<String> currentProjectAllClassesName) throws ClassNotFoundException, NotServeletException, NotListenerException, InstantiationException, IllegalAccessException {
        initFilterChain();
        for (String className : currentProjectAllClassesName) {
            Class<?> aClass = Class.forName(className);
            WebServlet webServlet = aClass.getAnnotation(WebServlet.class);
            WebListener webListener = aClass.getAnnotation(WebListener.class);
            WebFilter webFilter = aClass.getAnnotation(WebFilter.class);
            if(webServlet != null){
                //判断当前类是不是servlet的子类
                if (Servlet.class.isAssignableFrom(aClass)){
                    URIMapping.put(webServlet.value(), className);
                }else {
                    throw new NotServeletException("当前类型不是一个servlet类");
                }
            }
            if (webListener != null){
                System.out.println("===============================");
                System.out.println(aClass.getName());
                ListenerFactory.init(aClass);
            }
            if (webFilter != null && Filter.class.isAssignableFrom(aClass)){
                Filter filter = (Filter) aClass.newInstance();
                String value = webFilter.value();
                TomcatFilterChain tomcatFilterChain = new TomcatFilterChain();
                tomcatFilterChain.setCurrentFilter(filter);
                tomcatFilterChain.setUrlPattern(value);

                TomcatFilterChain previousFilterChain = filterChainList.get(filterChainList.size()-1);
                previousFilterChain.setNextFilterChain(tomcatFilterChain);
                tomcatFilterChain.setPreviousFilterChain(previousFilterChain);
                tomcatFilterChain.setFirstFilterChain(firstFilterChain);
                tomcatFilterChain.setLastFilterChain(lastFilterChain);
                filterChainList.add(tomcatFilterChain);
            }
        }
//        System.out.println(URIMapping);
        filterChainList.add(lastFilterChain);
        TomcatFilterChain tomcatFilterChain = filterChainList.get(filterChainList.size() - 2);
        tomcatFilterChain.setNextFilterChain(lastFilterChain);
        lastFilterChain.setPreviousFilterChain(tomcatFilterChain);
        return URIMapping;
    }

    private static void initFilterChain(){
        firstFilterChain = new TomcatFilterChain();
        lastFilterChain = new TomcatFilterChain();
        firstFilterChain.setFirstFilterChain(firstFilterChain);
        firstFilterChain.setPreviousFilterChain(null);
        firstFilterChain.setCurrentFilter(new Filter() {
            @Override
            public void doFilter(HttpServletRequest req, HttpServletResponse resp, FilterChain filterChain) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
                filterChain.doFilter(req, resp);
            }
        });
        firstFilterChain.setLastFilterChain(lastFilterChain);
        firstFilterChain.setNextFilterChain(lastFilterChain);

        lastFilterChain.setFirstFilterChain(firstFilterChain);
        lastFilterChain.setPreviousFilterChain(firstFilterChain);
        lastFilterChain.setCurrentFilter(new Filter() {
            @Override
            public void doFilter(HttpServletRequest req, HttpServletResponse resp, FilterChain filterChain) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
                filterChain.doFilter(req, resp);
            }
        });

        lastFilterChain.setNextFilterChain(null);
        lastFilterChain.setLastFilterChain(lastFilterChain);
        filterChainList.add(firstFilterChain);

    }

}
