package com.example.simpledubborpcbynetty.dubbo;

import com.example.simpledubborpcbynetty.dubbo.netty.ServerNetty;

/**
 * @Description:
 * @Author wjl
 * @Date 2024/1/18
 * @Copyright: Linewell Software Co., Ltd. All rights reserved.
 * 南威软件股份有限公司
 */
public class ServiceMain {
    public static void main(String[] args) {
        new ServerNetty().start(9016);
    }
}
