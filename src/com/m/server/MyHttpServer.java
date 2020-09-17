package com.m.server;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MyHttpServer {
    private Map<String,String> urlMap = new HashMap<>();
    private Map<String,String> servletMap = new HashMap<>();
    public void receing(){
        parseXML();
        ServerSocket serverSocket = null;
        Socket socket = null;
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            //启动Socket服务
            serverSocket = new ServerSocket(8080);
            //接收请求
            while (true){
                socket = serverSocket.accept();
                inputStream = socket.getInputStream();
                outputStream = socket.getOutputStream();
                //解析请求
                MyHttpRequest myHttpRequest = new MyHttpRequest(inputStream);
                myHttpRequest.parse();
                MyHttpResponse myHttpResponse = new MyHttpResponse(outputStream);
                //判断请求是访问静态资源，还是逻辑请求
                String uri = myHttpRequest.getUri();
                if(uri.indexOf(".html") > 0){
                    //响应
                    myHttpResponse.sendReource(myHttpRequest);
                }else{
                    uri = "/"+uri;
                    String servletName = urlMap.get(uri);
                    String servletClass = servletMap.get(servletName);
                    //通过反射机制实例化Servlet对象
                    try {
                        Class clazz = Class.forName(servletClass);
                        Constructor constructor = clazz.getConstructor(null);
                        Object object = constructor.newInstance(null);
                        Method method = clazz.getMethod("service",MyHttpRequest.class,MyHttpResponse.class);
                        method.invoke(object,myHttpRequest,myHttpResponse);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
//            try {
//                inputStream.close();
//                outputStream.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }
    }

    public void parseXML(){
        SAXReader saxReader = new SAXReader();
        String path = System.getProperty("user.dir")+"/web/WEB-INF/web.xml";
        try {
            Document document = saxReader.read(path);
            Element root = document.getRootElement();
            Iterator<Element> rootIter = root.elementIterator();
            while(rootIter.hasNext()){
                Element element = rootIter.next();
                if(element.getName().equals("servlet")){
                    Iterator<Element> servletIter = element.elementIterator();
                    String key = null;
                    String value = null;
                    while(servletIter.hasNext()){
                        Element item = servletIter.next();
                        if(item.getName().equals("servlet-name")){
                            key = item.getText();
                        }
                        if(item.getName().equals("servlet-class")){
                            value = item.getText();
                        }
                    }
                    servletMap.put(key,value);
                }
                if(element.getName().equals("servlet-mapping")){
                    Iterator<Element> servletIter = element.elementIterator();
                    String key = null;
                    String value = null;
                    while(servletIter.hasNext()){
                        Element item = servletIter.next();
                        if(item.getName().equals("url-pattern")){
                            key = item.getText();
                        }
                        if(item.getName().equals("servlet-name")){
                            value = item.getText();
                        }
                    }
                    urlMap.put(key,value);
                }
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        MyHttpServer myHttpServer = new MyHttpServer();
        myHttpServer.parseXML();
    }

}
