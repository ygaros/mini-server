package org.ygaros.server;

import org.ygaros.server.impl.HttpServerBuilder;
import org.ygaros.server.request.HttpMethod;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

public class Example {
    public static void main(String[] args) throws FileNotFoundException {
        HttpServer server = new HttpServerBuilder()
                .withPort(9876)
                .withDefaultResourcesPath()
                .withFileMapping()
                .withRestHandler(
                        HttpMethod.GET,
                        "/rest",
                        (request) -> new HashMap<String, String>(){{
                            put("url", request.getUrl());
                            put("method", request.getMethod().toString());
                            put("content length", String.valueOf(request.getContentLength()));
                        }}
                )
                .withHTMLHandler(HttpMethod.GET, "/index", (request) -> "index.html")
                .withImageHandler(HttpMethod.GET, "/dir", (request) -> "kot.png")
                .withTextFileHandler(HttpMethod.GET, "/txt", (request) -> "plik.txt")
                .build();
        try {
            server.init();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
