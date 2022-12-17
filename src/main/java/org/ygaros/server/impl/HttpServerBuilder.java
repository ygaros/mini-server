package org.ygaros.server.impl;

import org.ygaros.server.handler.FileHandler;
import org.ygaros.server.handler.HandlerWrapper;
import org.ygaros.server.handler.RestHandler;
import org.ygaros.server.request.HttpMethod;

import java.io.FileNotFoundException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class HttpServerBuilder {
    private int port;

    private boolean fileMapping = false;
    private final Map<String, HandlerWrapper> handlers;
    private String wwwDirPath;

    public HttpServerBuilder(){
        handlers = new HashMap<>();
    }

    public HttpServerBuilder withPort(int port){
        this.port = port;
        return this;
    }
    public HttpServerBuilder withRestHandler(HttpMethod method, String url, RestHandler handler){
        this.handlers.put(url, new HandlerWrapper(method, "application/json", handler));
        return this;
    }
    public HttpServerBuilder withTextFileHandler(HttpMethod method, String url, FileHandler handler){
        this.handlers.put(url, new HandlerWrapper(method, "text", handler));
        return this;
    }
    public HttpServerBuilder withHTMLHandler(HttpMethod method, String url, FileHandler handler){
        return this.withTextFileHandler(method, url, handler);
    }
    public HttpServerBuilder withImageHandler(HttpMethod method, String url, FileHandler handler){
        this.handlers.put(url, new HandlerWrapper(method, "image", handler));
        return this;
    }
    public HttpServerBuilder withRootRestHandler(RestHandler handler){
        return this.withRestHandler(HttpMethod.GET, "/", handler);
    }
    public HttpServerBuilder withWwwDirPath(String path, boolean absolutePath) throws FileNotFoundException {
        String resourcePath = System.getProperty("user.dir").concat("\\src\\main\\resources");
        if(path.startsWith("\\")){
            resourcePath = resourcePath.concat("\\");
        }
        if(path.endsWith("\\")){
            path = path.substring(0, path.length() - 1);
        }
        resourcePath = resourcePath.concat(path);
        this.wwwDirPath = resourcePath;
        return this;
    }
    public HttpServerBuilder withFileMapping(){
        this.fileMapping = true;
        return this;
    }
    public HttpServerBuilder withDefaultResourcesPath() throws FileNotFoundException {
        this.wwwDirPath = System.getProperty("user.dir").concat("\\src\\main\\resources");
        return this;
    }
    public HttpServerImpl build(){
        if(this.wwwDirPath == null || this.wwwDirPath.length() == 0){
            this.wwwDirPath = HttpServerBuilder.class.getProtectionDomain().getCodeSource().getLocation().getFile();
        }
        return new HttpServerImpl(this.port, this.handlers, this.wwwDirPath, this.fileMapping);
    }

}
