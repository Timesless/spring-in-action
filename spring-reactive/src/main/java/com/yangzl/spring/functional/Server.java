package com.yangzl.spring.functional;

import com.yangzl.spring.service.UserService;
import com.yangzl.spring.service.UserServiceImpl;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.http.server.reactive.ReactorHttpHandlerAdapter;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.netty.http.server.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;

/**
 * @author yangzl
 * @date 2020/12/19 15:39
 * @desc
 */

public class Server {
    
    /**
     * 2020/12/19 路由断言配置
     * 
	 * @param () v
     * @return ServerResponse
     */
    public RouterFunction<ServerResponse> routerFunction() {
        
        UserService userService = new UserServiceImpl();
        UserHandler handler = new UserHandler(userService);
        return RouterFunctions.route(GET("/user/{id}").and(accept(APPLICATION_JSON)), handler::queryById)
                .andRoute(GET("/users").and(accept(APPLICATION_JSON)), handler::queryList)
                .andRoute(POST("/user/save").and(accept(APPLICATION_JSON)), handler::save);
    }
    
    /**
     * 2020/12/19 创建并启动 Reactor Netty服务器
     * 
	 * @param () v
     * @return void
     */
    public void startReactorServer() {

        // 路由
        final RouterFunction<ServerResponse> routes = routerFunction();
        // 请求处理器
        final HttpHandler httpHandler = RouterFunctions.toHttpHandler(routes);
        // 请求处理器适配器
        final ReactorHttpHandlerAdapter adapter = new ReactorHttpHandlerAdapter(httpHandler);
        // Reactor Netty 服务器
        HttpServer.create()
                .handle(adapter)
                .bindAddress(() -> new InetSocketAddress(8082))
                .bindNow();
    }

    /**
     * 2020/12/19 启动服务进行测试，前往浏览器访问127.0.0.1:8082/users
     * 
	 * @param args args
     * @return void
     */
    public static void main(String[] args) throws IOException {
        final Server server = new Server();
        server.startReactorServer();
        System.out.println("enter any to exit");
        System.in.read();
    }

}
