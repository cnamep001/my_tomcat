手写web应用服务

思路：主线程启动Socket服务，循环接收客户端请求，
接收到请求后，将输入流中的数据取出拼接成字符串，
在控制台打印。响应时判断请求的资源是否存在，
若存在，将资源通过输出流响应给客户端，若资源不存在，
将404错误信息通过输出流响应给客户端。

创建4个类

MyHttpServer：定义Socket服务，循环接收请求。

MyHttpRequest：自定义请求类，解析请求。

MyHttpResponse：自定义响应类，根据请求做出响应。

Test：主线程中启动Socket服务。

<br/>

####用谷歌或火狐测试：



>http://localhost:8080/login?username=zhangsan&password=123123