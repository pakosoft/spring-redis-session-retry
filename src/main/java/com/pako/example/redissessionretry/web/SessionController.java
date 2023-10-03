package com.pako.example.redissessionretry.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RestController
public class SessionController {

    @GetMapping({"/","/counter"})
    public Mono<String> getCounter() {
        String msg = "Counter: %s" +
                "<br><br>" +
                "<a href=\"/counter\">Refresh</a><br>" +
                "<a href=\"/logout\">Logout</a>";
        return getSessionCounter().map(counter -> String.format(msg, counter));
    }

    private Mono<Integer> getSessionCounter() {
        return Mono.deferContextual(contextView -> contextView.get(ServerWebExchange.class)
                .getSession().flatMap(session -> {
                    Integer counter = session.getAttribute("counter");
                    counter = counter == null ? 1 : counter+1;
                    session.getAttributes().put("counter", counter);
                    return Mono.just(counter);
                })
        );
    }
}
