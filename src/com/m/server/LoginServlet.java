package com.m.server;

public class LoginServlet {
    private String username = "zhangsan";
    private String password = "123123";
    public void service(MyHttpRequest request,MyHttpResponse response){
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        System.out.println(username);
        System.out.println(password);
        System.out.println("service...");

        response.write(username,password);
    }
}