package com.example.simpledubborpcbynetty.dubbo.netty;

import com.example.simpledubborpcbynetty.dubbo.service.impl.HelloWorldServiceImpl;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Description:
 * @Author wjl
 * @Date 2024/1/18
 * @Copyright: Linewell Software Co., Ltd. All rights reserved.
 * 南威软件股份有限公司
 */
@Slf4j
public class ServiceHandler extends SimpleChannelInboundHandler<String> {

    private final String dubboProtocol = "dubbo#";
    private HashMap<String,String> cache;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("接收连接:{}",ctx.channel().remoteAddress());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        if (Objects.isNull(cache)) {
            initCache();
        }
        msg = msg.trim();
        log.info("收到消息：{}",msg);
        if (msg.startsWith(dubboProtocol)){
            log.info("命中dubbo协议");
            msg = msg.substring(msg.indexOf(dubboProtocol) + dubboProtocol.length());
            String serviceName = msg.substring(0, msg.indexOf("#"));
            String classPath = cache.get(serviceName);
            if (!StringUtils.isEmpty(classPath)){
                msg = msg.substring(msg.indexOf(serviceName) + serviceName.length() + 1);
                log.info("缓存命中注册实例");
                Class<?> target = Class.forName(classPath);
                String methodName = msg.substring(0, msg.indexOf("#"));
                // 此处参数类型获取一样的道理
                Method method = target.getDeclaredMethod(methodName, String.class);
                String name = msg.substring(msg.lastIndexOf("#") + 1);
                Object instance = target.newInstance();
                Object result = method.invoke(instance, name);
                if (Objects.nonNull(result)){
                    ctx.writeAndFlush(result);
                    log.info("响应结果:{}",result);
                }
            }
        }
    }

    private void initCache(){
        cache = new HashMap<>(1);
        // 跳过 获取META-INF.services下文件内容过程
        cache.put("HelloWorldService","com.example.simpledubborpcbynetty.dubbo.service.impl.HelloWorldServiceImpl");
    }





}
