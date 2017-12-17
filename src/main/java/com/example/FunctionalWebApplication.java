package com.example;

import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.http.server.reactive.ReactorHttpHandlerAdapter;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.ipc.netty.http.server.HttpServer;

import java.util.Objects;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.BodyInserters.fromObject;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

final class Hello {
    public String name;

    Hello() {

    }

    Hello(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Hello hello = (Hello) o;
        return Objects.equals(name, hello.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}

public class FunctionalWebApplication {

    static RouterFunction getRouter() {
        HandlerFunction<ServerResponse> hello = request -> ok().body(fromObject("Hello"));

        return
            route(
                GET("/"), hello)
            .andRoute(
                GET("/json"), req -> ok().contentType(APPLICATION_JSON).body(fromObject(new Hello("world"))));
    }

    public static void main(String[] args) throws InterruptedException {

        RouterFunction router = getRouter();

        HttpHandler httpHandler = RouterFunctions.toHttpHandler(router);

        HttpServer
                .create("localhost", 8080)
                .newHandler(new ReactorHttpHandlerAdapter(httpHandler))
                .block();

        Thread.currentThread().join();
    }
}
