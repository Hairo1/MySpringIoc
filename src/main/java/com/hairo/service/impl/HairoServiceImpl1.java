package com.hairo.service.impl;


import com.hairo.service.HairoService;

/**
 * 项目名称： Hairomvc
 * 作 者   ： Hairo
 * 创建时间: 2018/10/12 20:51
 * 作用描述:
 */
@com.hairo.annotation.HairoService
public class HairoServiceImpl1 implements HairoService {
    @Override
    public String getName() {
        return "HairoServiceImpl1";
    }
}
