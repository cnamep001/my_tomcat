package com.m.server;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class MyHttpRequest {
    private InputStream inputStream;
    private String uri;
    private static Map<String, String> paramsMap = new HashMap<>();

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public MyHttpRequest(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public void parse() {
        byte[] bytes = new byte[2048];
        try {
            int length = this.inputStream.read(bytes);
            String message = new String(bytes);
            System.out.println(message);
            this.uri = parseUri(message);
            if (this.uri.indexOf("?") > 0) {
                //解析参数
                String[] params = this.uri.split("[?]")[1].split("&");
                for (String str : params) {
                    params = str.split("=");
                    paramsMap.put(params[0], params[1]);
                }
                this.uri = uri.substring(0, uri.indexOf("?"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
//            try {
//                this.inputStream.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }
    }

    public String parseUri(String message) {
        int index1 = message.indexOf("/");

        int index2 = message.indexOf(" ", index1 + 1);


        return message.substring(index1 + 1, index2);

    }

    public String getParameter(String name) {
        return paramsMap.get(name);
    }

    public static void main(String[] args) {
        String uri = "login?username=lisi&password=456789";
        if (uri.indexOf("?") > 0) {
            uri = uri.substring(0, uri.indexOf("?"));
        }
        System.out.println(uri);
    }
}
