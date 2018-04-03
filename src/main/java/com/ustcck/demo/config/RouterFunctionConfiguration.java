package com.ustcck.demo.config;

import com.ustcck.demo.demain.User;
import com.ustcck.demo.handler.UserHandler;
import com.ustcck.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * 路由器函数 配置
 */
@Configuration
public class RouterFunctionConfiguration {

    private UserHandler userHandler;

    public RouterFunctionConfiguration(UserHandler userHandler) {
        this.userHandler = userHandler;
    }

    /**
     * 请求接口 ServerRequest
     * 响应接口 ServerResponse
     * 定义 GET 请求，并且返回所有的用户对象，URL：/person/find/all
     */
    @Bean
    public RouterFunction<?> routerFunction() {
        return route(GET("/api/user").and(accept(MediaType.APPLICATION_JSON)), userHandler::handleGetUsers)
                .and(route(GET("/api/user/{id}").and(accept(MediaType.APPLICATION_JSON)), userHandler::handleGetUserById));
    }


    @Bean
    @Autowired
    public RouterFunction<ServerResponse> personFindAll(UserRepository userRepository) {
        return RouterFunctions.route(RequestPredicates.GET("/person/find/all"),
                request -> {
                    Flux<User> userFlux = userRepository.getUsers();
                    return ServerResponse.ok().body(userFlux, User.class);
                });
    }

}