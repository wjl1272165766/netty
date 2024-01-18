package com.example.simpledubborpcbynetty.dubbo.netty;


import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Description:
 * @Author wjl
 * @Date 2024/1/18
 * @Copyright: Linewell Software Co., Ltd. All rights reserved.
 * 南威软件股份有限公司
 */
public class ClientNetty {

    private ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(2,8,5 * 1000, TimeUnit.MILLISECONDS,new ArrayBlockingQueue<>(20));
    private String host;
    private Integer port;
    private ClientHandler clientHandler;

    public ClientNetty(String host, Integer port) throws Exception{
        this.host = host;
        this.port = port;
    }

    public Object getBean(Class<?> serviceClass,String protocol){
        return Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[]{serviceClass}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                if(Objects.isNull(clientHandler)){
                    init();
                }
                clientHandler.setParam(protocol + args[0]);
                return threadPoolExecutor.submit(clientHandler).get();
            }
        });
    }


    private void init() throws Exception{
        clientHandler = new ClientHandler();
        NioEventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap().group(group).channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline()
                                .addLast(new StringDecoder())
                                .addLast(new StringEncoder())
                                .addLast(clientHandler);
                    }
                });
        bootstrap.connect(host,port).sync();
    }




}
