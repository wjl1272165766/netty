package com.example.simpledubborpcbynetty.dubbo.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;

/**
 * @Description:
 * @Author wjl
 * @Date 2024/1/18
 * @Copyright: Linewell Software Co., Ltd. All rights reserved.
 * 南威软件股份有限公司
 */

@Slf4j
public class ClientHandler extends SimpleChannelInboundHandler<String> implements Callable {

    private ChannelHandlerContext ctx;
    private String param;
    private String result;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("连接成功,发送消息");
        this.ctx = ctx;
    }

    @Override
    protected synchronized void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        log.info("消费者收到消息:{}，唤醒线程",msg);
        result = msg.toString();
        notify();
    }

    @Override
    public synchronized Object call() throws Exception {
        ctx.writeAndFlush(param);
        wait();
        return result;
    }

    public void setParam(String param) {
        this.param = param;
    }
}
