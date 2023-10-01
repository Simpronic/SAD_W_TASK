package com.example.uigateway;

import com.example.uigateway.service.AuthJwtToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.PathContainer;
import org.springframework.http.server.RequestPath;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
public class CustomFilter implements GlobalFilter, Ordered {

    @Autowired
    private AuthJwtToken authTokenService;
    private static final List<String> exclusionList = Collections.unmodifiableList(Arrays.asList("login","loginAdmin","register","","api"));

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        var request = exchange.getRequest();
        System.out.println("[DEBUG]: subpath: "+request.getPath().subPath(1).toString());
        System.out.println("[DEBUG]: /api "+!request.getPath().subPath(1).toString().equals("api"));
        System.out.println("[DEBUG]: exclusionList "+!exclusionList.contains(request.getPath().subPath(1).toString()));
        System.out.println("[DEBUG]: "+request.getURI());
        if(!checkIfExclusionList(request.getPath()) && !checkIfApiRequest(request.getPath().subPath(1))){
            System.out.println("[DEBUG]: Controllo...");
            if(authTokenService.verifyToken(getJwtCookieValue(request))){
                return chain.filter(exchange);
            }else{
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }
        }
        return chain.filter(exchange);
    }

    public static Boolean checkIfApiRequest(PathContainer subpathReq){
        String field = subpathReq.toString().split("\\/")[0];
        System.out.println("[DEBUG]: "+field);
        return field.equals("api");
    }
    public static Boolean checkIfExclusionList(RequestPath url){
        String field = url.subPath(1).toString();
        field = field.split("\\.")[0];
        System.out.println("[DEBUG]: "+field);
        return exclusionList.contains(field);
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
