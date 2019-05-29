package com.hairo.reflect;

import com.hairo.annotation.HairoAnnotation;
import com.hairo.annotation.HairoController;
import com.hairo.annotation.HairoService;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.sql.SQLOutput;
import java.util.*;

/**
 * 项目名称： Hairomvc
 * 作 者   ： Hairo
 * 创建时间: 2018/10/12 13:16
 * 作用描述:
 * 反射
 */
@SuppressWarnings("all")
public class JavaReflect {


    private List<String> classNames = new ArrayList<>();//存储类名称,不带后缀


    private Map<String, Object> bases = new HashMap<>();//key-自定义 key/类的首字母小写  value 反射后的对象


    /**
     * 根据指定的包获取包下的所有.class文件
     *
     * @param baseaPckage
     */
    public void scanClass(String baseaPckage) {
        URL url = this.getClass().getClassLoader().getResource(baseaPckage.replaceAll("\\.", "/"));//获取类路径：file:/F:/IDEA/ideaProjects/Hairomvc/target/classes/
        //System.out.println("classpath: "+url);
        String filePathStr = url.getFile();
        File file = new File(filePathStr);
        String[] files = file.list();//文件名
        for (String str : files) {
            File filePath = new File(filePathStr + "/" + str);
            if (filePath.isDirectory()) {
                scanClass(baseaPckage + "." + str);
            } else {
                System.out.println("扫描到类：" + filePath.getName());
                classNames.add(baseaPckage + "." + filePath.getName());// 包名.类名  添加到集合
            }
        }
    }


    /**
     * 根据扫描得到的类进行反射实例化
     */
    public void instance() {
        start();
        //去除后缀.class
        for (String className : classNames) {
            //className = com.hairo.xx.xx.xx.class
            String cn = className.replace(".class", "");//cn = 去除了后缀
            try {
                Class<?> clazz = Class.forName(cn);
                //判断是否有@HairoService注解
                if (clazz.isAnnotationPresent(HairoService.class)) {
                    Object obj = clazz.newInstance();//反射创建实例
                    HairoService hairoService = clazz.getAnnotation(HairoService.class);//获取HairoService注解类
                    String key = hairoService.value();//获取指定HairoService('名称');
                    //HairoService("Hairo")   按指定名称作为key
                    //获取该实例所实现的接口类型做key
                    for (Class<?> c : clazz.getInterfaces()) {
                        if (bases.get(c.getName())==null) {
                            bases.put(c.getName(), obj);//接口首字母小写
                            System.out.println("按接口类型创建Service的实现类:" + c.getName() + "\t为实现类为:" + obj);
                        } else {
                            System.out.println("存在接口" + c.getName() + "不创建");
                        }
                    }
                    if (!key.equals("") && key != null) {
                        System.out.println("按指定名称创建Service:" + key);
                        bases.put(key, obj);//按自定义名称存储到IOC容器
                    } else {
                        System.out.println("按类型创建Service:" + cn);
                        bases.put(cn, obj);//按类型存储到IOC容器
                    }


                    //是否存在@HairoController
                } else if (clazz.isAnnotationPresent(HairoController.class)) {//如果类存在@HairoController注解
                    //HairoMapping hairoMapper = clazz.getAnnotation(HairoMapping.class);
                    Object obj = clazz.newInstance();//反射创建实例
                    HairoController hairoController = clazz.getAnnotation(HairoController.class);//获取HairoService注解类
                    //HairoController("")  默认类名称首字母小写作为key
                    int len = cn.lastIndexOf(".") + 1;
                    System.out.println("按类型--创建Controller:" + lowerFirst(cn.substring(len)));
                    bases.put(lowerFirst(cn.substring(len)), obj);//存储到IOC容器

                } else {
                    continue;
                }
            } catch (ClassNotFoundException e) {
                System.out.println("反射找不到类:" + cn);
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        }
        end();
    }


    /**
     * 开始注入
     */
    public void annotation() {
        for (Map.Entry<String, Object> entry : bases.entrySet()) {
            Object obj = entry.getValue();//获取实例（类）

            //System.out.println(obj + "======");
            Class<?> clazz = obj.getClass();
            Field[] fields = clazz.getDeclaredFields();//获取所有属性
            for (Field field : fields) {
                //  System.out.println(field.getName() + "-------field");
                if (field.isAnnotationPresent(HairoAnnotation.class)) {//是否存在HairoController注解
                    field.setAccessible(true);//因为属性是私有的,所以获取的私有构造进行暴力访问，如果想要访问请打开权限为[true].
                    HairoAnnotation hairoAnnotation = field.getAnnotation(HairoAnnotation.class);//获取注解类
                    String keyService = hairoAnnotation.value();//获取自定义名称@HairoAnnotion("自定义的名称")
                    try {
                        //按指定名称注入
                        if (keyService != null && !keyService.equals("")) {
                            field.set(obj, bases.get(keyService));//自定义名称注入
                            System.out.println("指定@HairoAnnotation名称注入："+keyService );
                            //按类型注入
                        } else {
                            field.set(obj, bases.get(field.getType().getName()));//按类型注入
                            System.out.println(field.getName() + "按类型注入成功"+field.getType().getName());
                        }
                    } catch (IllegalAccessException e) {
                        System.out.println(field + "注入失败");
                        e.printStackTrace();
                    }
                } else {
                    continue;
                }
                //@Hairo
            /*}else if (clazz.isAnnotationPresent(HairoService.class)) {
                Field[] fields = clazz.getDeclaredFields();//获取所有属性
                for (Field field : fields) {
                    if (field.isAnnotationPresent(HairoService.class)) {//是否存在HairoController注解
                        HairoAnnotation hairoAnnotation = field.getAnnotation(HairoAnnotation.class);
                        field.setAccessible(true);//因为属性是私有的,所以获取的私有构造进行暴力访问，如果想要访问请打开权限为[true].
                        try {
                            field.set(obj, bases.get(""));//开始注入
                        } catch (IllegalAccessException e) {
                            System.out.println(field + "注入失败");
                            e.printStackTrace();
                        }
                    } else {
                        continue;
                    }
                }*/
            }/*else{
                continue;
            }*/

        }
    }

    /**
     * 转换首字母为小写
     *
     * @param oldStr 字符串
     * @return 首字母为小写
     */
    public static String lowerFirst(String oldStr) {
        char[] chars = oldStr.toCharArray();
        chars[0] += 32;
        return String.valueOf(chars);
    }

    public static void main(String[] arg) {
        JavaReflect javaReflect = new JavaReflect();
        javaReflect.scanClass("com.hairo");
        javaReflect.instance();
        javaReflect.annotation();

        /*try {
            Class<?> clazz = Class.forName("com.hairo.service.impl.HairoServiceImpl");
            Class<?>[] interfaces = clazz.getInterfaces();
            for (Class<?> c : interfaces) {
                System.out.println("实现接口：" + c);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }*/
    }

    public void start() {
        System.out.println("*****************************************************华丽的分割线start*****************************************************+\n");
    }

    public void end() {
        System.out.println("\n*****************************************************华丽的分割线end******************************************************");
    }
}
