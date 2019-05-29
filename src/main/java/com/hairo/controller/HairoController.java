package com.hairo.controller;

import com.hairo.annotation.HairoAnnotation;
import com.hairo.reflect.JavaReflect;
import com.hairo.service.HairoService;
import com.hairo.service.impl.HairoServiceImpl;
import com.hairo.service.impl.HairoServiceImpl1;


/**
 * 项目名称： Hairomvc
 * 作 者   ： Hairo
 * 创建时间: 2018/10/12 13:49
 * 作用描述:
 */
//@com.hairo.annotation.HairoController
public class HairoController {

    @HairoAnnotation
    private static HairoService hairoService;


    public static void main(String[] arg) {
        JavaReflect javaReflect = new JavaReflect();
        javaReflect.scanClass("com.hairo");
        javaReflect.instance();
        javaReflect.annotation();

        System.out.println("hairoService："+ hairoService.getName());

    }

}
