package com.m;


import com.m.server.MyHttpServer;


public class Test{
    public static void main(String[] args) {
        System.out.println("服务器已启动...");
        MyHttpServer myHttpServer = new MyHttpServer();
        myHttpServer.receing();

    }
}
