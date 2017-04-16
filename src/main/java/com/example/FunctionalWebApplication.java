package com.example;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.catalina.LifecycleException;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.http.server.reactive.ReactorHttpHandlerAdapter;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import reactor.core.publisher.Mono;
import reactor.ipc.netty.http.server.HttpServer;

import java.io.IOException;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

class Hello {

    private final String name;

    Hello(@JsonProperty("name") String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Hello)) return false;

        Hello hello = (Hello) o;

        return getName() != null ? getName().equals(hello.getName()) : hello.getName() == null;
    }

    @Override
    public int hashCode() {
        return getName() != null ? getName().hashCode() : 0;
    }
}

public class FunctionalWebApplication {

    static RouterFunction getRouter() {
        HandlerFunction hello = request -> ok().body(Mono.just("Hello"), String.class);

        return route(GET("/"), hello)
                .and(route(GET("/json"), req -> ok().contentType(APPLICATION_JSON).body(Mono.just(new Hello("world")), Hello.class)));
    }

    public static void main(String[] args) throws IOException, LifecycleException, InterruptedException {

        RouterFunction router = getRouter();

        HttpHandler httpHandler = RouterFunctions.toHttpHandler(router);

        HttpServer
                .create("localhost", 8080)
                .newHandler(new ReactorHttpHandlerAdapter(httpHandler))
                .block();

        Thread.currentThread().join();
    }
}
