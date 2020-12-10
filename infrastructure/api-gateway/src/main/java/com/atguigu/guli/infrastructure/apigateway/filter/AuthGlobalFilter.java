package com.atguigu.guli.infrastructure.apigateway.filter;

import com.atguigu.guli.service.base.helper.JwtHelper;
import com.atguigu.guli.service.base.result.ResultCodeEnum;
import com.google.gson.JsonObject;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class AuthGlobalFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        System.out.println("全局过滤器");
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();

        //谷粒学院api接口，校验用户必须登录
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        if (antPathMatcher.match("/api/**/auth/**", path)) {
            List<String> tokenList = request.getHeaders().get("token");
            //没有token
            if(null == tokenList) {
                ServerHttpResponse response = exchange.getResponse();
                return out(response);
            }

            //token校验失败
            Boolean isOK = JwtHelper.checkToken(tokenList.get(0));
            if (!isOK) {
                ServerHttpResponse response = exchange.getResponse();
                return out(response);
            }

        }

        //放行
        return chain.filter(exchange);
    }

    //定义当前过滤器的优先级，值越小，优先级越高
    @Override
    public int getOrder() {
        return 0;
    }


    private Mono<Void> out(ServerHttpResponse response) {

        JsonObject message = new JsonObject();
        message.addProperty("success", ResultCodeEnum.LOGIN_AUTH.getSuccess());
        message.addProperty("code", ResultCodeEnum.LOGIN_AUTH.getCode());
        message.addProperty("data", "");
        message.addProperty("message", ResultCodeEnum.LOGIN_AUTH.getMessage());
        byte[] bytes = message.toString().getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = response.bufferFactory().wrap(bytes);
        //指定编码，否则在浏览器中会中文乱码
        response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        //输出http响应
        return response.writeWith(Mono.just(buffer));
    }
}
