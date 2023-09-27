package com.example.apigateway;

import com.example.apigateway.service.AuthJwtToken;
import io.netty.handler.codec.http.cookie.Cookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.netty.http.server.HttpServerRequest;

import java.sql.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


@Component
public class CustomFilter implements GlobalFilter, Ordered {

    @Autowired
    private AuthJwtToken authTokenService;

    private static final List<String> exclusionList = Collections.unmodifiableList(Arrays.asList("/login","/loginAdmin"));


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        var request = exchange.getRequest();
        if( !exclusionList.contains(request.getPath().subPath(2).toString()) && request.getPath().subPath(1).toString().equals("/api")){
            if(authTokenService.verifyToken("getJwtCookieValue(request)")){
                return chain.filter(exchange);
            }else{

                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }
        }
        return chain.filter(exchange);
    }

    public static String getJwtCookieValue( org.springframework.http.server.reactive.ServerHttpRequest request){
        MultiValueMap<String, HttpCookie> cookies = request.getCookies();
        if (cookies.isEmpty()) {
            return null;
        }
        List<HttpCookie> jwtCookies = cookies.get("jwt");
        if(jwtCookies == null || jwtCookies.isEmpty()){
            return null;
        }
        return jwtCookies.get(0).getValue();
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
