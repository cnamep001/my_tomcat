package com.m.server;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MyHttpResponse {
    private OutputStream outputStream;
    public MyHttpResponse(OutputStream outputStream){
        this.outputStream = outputStream;
    }

    public void sendReource(MyHttpRequest myHttpRequest){
        //确定uri是否存在
        String path = System.getProperty("user.dir")+"/web/"+myHttpRequest.getUri();
        File file = new File(path);
        InputStream inputStream = null;
        String message = null;
        if(file.exists()){
            try {
                inputStream = new FileInputStream(file);
                byte[] bytes = new byte[2048];
                inputStream.read(bytes);
                String resource = new String(bytes);
                message = getMessage("200",resource);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e){
                e.printStackTrace();
            }
        }else{
            message = getMessage("404","Resource Not Found!");
        }
        try {
            this.outputStream.write(message.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
//            try {
//                inputStream.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }
    }

    public void write(String ...args){
        List<String> list = new ArrayList<>();
        for(String arg:args){
            list.add(arg);
        }
        String message = getMessage("200",list.toString());
        try {
            this.outputStream.write(message.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public String getMessage(String statusCode,String message){
        return "HTTP/1.1 "+statusCode+"\r\n"+"Content-Type: text/html\r\n"+
                "Content-Length: "+message.length()+"\r\n\r\n"+message;
    }
}
