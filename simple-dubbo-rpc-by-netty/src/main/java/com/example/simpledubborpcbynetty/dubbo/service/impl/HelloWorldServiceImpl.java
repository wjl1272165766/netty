package com.example.simpledubborpcbynetty.dubbo.service.impl;

import com.example.simpledubborpcbynetty.dubbo.service.HelloWorldService;

/**
 * @Description:
 * @Author wjl
 * @Date 2024/1/18
 * @Copyright: Linewell Software Co., Ltd. All rights reserved.
 * 南威软件股份有限公司
 */
public class HelloWorldServiceImpl implements HelloWorldService {
    @Override
    public String helloWorld(String name) {
        return name + "hello world!!!";
    }
}
