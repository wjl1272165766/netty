package com.example.simpledubborpcbynetty.dubbo;

import com.example.simpledubborpcbynetty.dubbo.netty.ClientNetty;
import com.example.simpledubborpcbynetty.dubbo.service.HelloWorldService;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description:
 * @Author wjl
 * @Date 2024/1/18
 * @Copyright: Linewell Software Co., Ltd. All rights reserved.
 * 南威软件股份有限公司
 */
@Slf4j
public class ClientMain {

    public static void main(String[] args) throws Exception{
        ClientNetty client = new ClientNetty("127.0.0.1", 9016);
        HelloWorldService helloWorldService = (HelloWorldService) client.getBean(HelloWorldService.class,"dubbo#HelloWorldService#helloWorld#");
        log.info("结果:" + helloWorldService.helloWorld("小王 "));
    }
}
