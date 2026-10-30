package com.devops.lab7;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class Application {

    public static void main(String[] args) throws IOException {
        int port = Integer.parseInt(
            System.getenv().getOrDefault("PORT", "3000")
        );

        HttpServer server = HttpServer.create(
            new InetSocketAddress(port), 0
        );

        server.createContext("/health", exchange ->
            sendJson(exchange, "{\"status\":\"OK\"}")
        );

        server.createContext("/", exchange -> {
            Calculator calculator = new Calculator();

            String json = String.format(
                "{\"message\":\"DevOps Lab 7 + Lab 8\","
                + "\"version\":\"1.0.0\","
                + "\"result\":%d}",
                calculator.add(2, 3)
            );

            sendJson(exchange, json);
        });

        server.start();
        System.out.println("Application running on port " + port);
    }

    private static void sendJson(
        HttpExchange exchange,
        String json
    ) throws IOException {
        byte[] body = json.getBytes(StandardCharsets.UTF_8);

        exchange.getResponseHeaders().set(
            "Content-Type", "application/json; charset=UTF-8"
        );

        exchange.sendResponseHeaders(200, body.length);

        try (var output = exchange.getResponseBody()) {
            output.write(body);
        }
    }
}
