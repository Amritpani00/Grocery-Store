package com.grocery.backend.tracking;

import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/tracking")
@CrossOrigin(origins = "*")
public class TrackingController {

    private final Map<String, Sinks.Many<String>> orderSinks = new ConcurrentHashMap<>();

    @GetMapping(value = "/stream/{orderId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> stream(@PathVariable String orderId) {
        Sinks.Many<String> sink = orderSinks.computeIfAbsent(orderId, k -> Sinks.many().multicast().onBackpressureBuffer());
        return sink.asFlux()
                .map(data -> ServerSentEvent.builder(data).event("status").build())
                .mergeWith(Flux.interval(Duration.ofSeconds(15))
                        .map(i -> ServerSentEvent.<String>builder().event("keepalive").data("ping").build()));
    }

    // Mock endpoint to push updates (would be called by delivery service)
    @PostMapping("/push/{orderId}")
    public void push(@PathVariable String orderId, @RequestBody Map<String, String> body) {
        var sink = orderSinks.computeIfAbsent(orderId, k -> Sinks.many().multicast().onBackpressureBuffer());
        sink.tryEmitNext(body.getOrDefault("status", "OUT_FOR_DELIVERY"));
    }
}

