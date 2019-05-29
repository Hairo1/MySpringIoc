package com.hairo.service.impl;

import com.hairo.annotation.HairoAnnotation;
import com.hairo.service.HairoService;


/**
 * 项目名称： Hairomvc
 * 作 者   ： Hairo
 * 创建时间: 2018/10/12 13:50
 * 作用描述:
 */
@com.hairo.annotation.HairoService
public class HairoServiceImpl implements HairoService {

 /*   @HairoAnnotation
    private HairoService hairoService;
*/
    public String getName(){

        return  "成功";
    }
}
