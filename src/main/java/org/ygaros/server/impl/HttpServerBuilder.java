package org.ygaros.server.impl;

import org.ygaros.server.handler.FileHandler;
import org.ygaros.server.handler.HandlerWrapper;
import org.ygaros.server.handler.RestHandler;
import org.ygaros.server.request.HttpMethod;
import org.ygaros.server.request.MimeType;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

public class HttpServerBuilder {
    private int port;

    private boolean parallel = false;
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

    /**
     * Set HTTPServer to work on all available threads on this machine.
     * IMPORTANT -> THIS IS DISABLED ATM.
     */
    public HttpServerBuilder parallel(){
        this.parallel = true;
        return this;
    }

    /**
     * Add route to a rest endpoint with will result as json response.
     * @param method
     * @param url
     * @param handler
     * @return
     */
    public HttpServerBuilder withRestHandler(HttpMethod method, String url, RestHandler handler){
        this.handlers.put(url, new HandlerWrapper(method, MimeType.JSON, handler));
        return this;
    }

    /**
     * Add route to a text file with will result in plan text response.
     * Handler should return relative to root www path to the text file.
     * @param method
     * @param url
     * @param handler
     * @return
     */
    public HttpServerBuilder withTextFileHandler(HttpMethod method, String url, FileHandler handler){
        this.handlers.put(url, new HandlerWrapper(method, MimeType.PLAIN, handler));
        return this;
    }

    /**
     * Add route to a html file.
     * Handler should return relative to root www path to the html file.
     * @param method
     * @param url
     * @param handler
     * @return
     */
    public HttpServerBuilder withHTMLHandler(HttpMethod method, String url, FileHandler handler){
        this.handlers.put(url, new HandlerWrapper(method, MimeType.HTML, handler));
        return this;
    }
    /**
     * Add route to a js file.
     * Handler should return relative to root www path to the js file.
     * @param method
     * @param url
     * @param handler
     * @return
     */
    public HttpServerBuilder withJSHandler(HttpMethod method, String url, FileHandler handler){
        this.handlers.put(url, new HandlerWrapper(method, MimeType.JS, handler));
        return this;
    }
    //TODO HANDLE VARIOUS IMAGE EXTENSION
    public HttpServerBuilder withImageHandler(HttpMethod method, String url, FileHandler handler){
        this.handlers.put(url, new HandlerWrapper(method, MimeType.PNG, handler));
        return this;
    }

    /**
     * Based on given @param absolutePath sets www root dir to absolute
     * or relative (from [project_name]/src/main/resources + @param path) path
     */
    public HttpServerBuilder withWWWDirPath(String path, boolean absolutePath) throws FileNotFoundException {
        String resourcePath = System.getProperty("user.dir").concat(File.separator+"src" + File.separator+"main"+File.separator+"resources");
        if(path.startsWith(File.separator)){
            resourcePath = resourcePath.concat(File.separator);
        }
        if(path.endsWith(File.separator)){
            path = path.substring(0, path.length() - 1);
        }
        resourcePath = resourcePath.concat(path);
        this.wwwDirPath = resourcePath;
        return this;
    }

    /**
     * Activate file mapping with means that you dont have to specify
     * end point for each stylesheet of js files linked to your html.
     * If this is disables any requst that are not specified with a handler
     * will result in 405 BAD_REQUEST.
     */
    public HttpServerBuilder withFileMapping(){
        this.fileMapping = true;
        return this;
    }

    /**
     * Set the root www dir to [project_name]/src/main/resources
     */
    public HttpServerBuilder withDefaultResourcesPath(){
        this.wwwDirPath = System.getProperty("user.dir").concat(File.separator+"src"+File.separator+"main"+File.separator+"resources");
        return this;
    }
    public HttpServerImpl build(){
        if(this.wwwDirPath == null || this.wwwDirPath.length() == 0){
            this.withDefaultResourcesPath();
        }
        return new HttpServerImpl(this.port, this.handlers, this.wwwDirPath, this.fileMapping);
    }

}
