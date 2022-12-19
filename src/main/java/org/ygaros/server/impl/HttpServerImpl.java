package org.ygaros.server.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.ygaros.server.HttpServer;
import org.ygaros.server.IllegalRequestException;
import org.ygaros.server.UrlMatcher;
import org.ygaros.server.UrlType;
import org.ygaros.server.handler.HandlerWrapper;
import org.ygaros.server.request.Headers;
import org.ygaros.server.request.HttpCode;
import org.ygaros.server.request.MimeType;
import org.ygaros.server.request.Request;
import org.ygaros.server.request.RequestParser;
import org.ygaros.server.request.Response;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

public class HttpServerImpl implements HttpServer {
    private final static Logger log = Logger.getLogger(HttpServerImpl.class.getName());
    private final static String HTTP_VER = "HTTP/1.1";
    private final int port;
    private ServerSocket serverSocket;
    private final String rootDirPath;
    private final boolean fileMapping;
    private final Map<String, HandlerWrapper> handlers;
    private final ObjectMapper mapper;

    HttpServerImpl(int port, Map<String, HandlerWrapper> handlers, String rootDirPath, boolean fileMapping) {
        this.port = port;
        this.handlers = handlers;
        this.rootDirPath = rootDirPath;
        this.fileMapping = fileMapping;
        this.mapper = new ObjectMapper();
    }

    @Override
    public void init() throws IOException {
        this.serverSocket = new ServerSocket(this.port);
        log.info(String.format("Http Server at %d is running", this.port));
        while(true){
            Socket socket = serverSocket.accept();
            Headers headers = new Headers();
            Response response = new Response(HTTP_VER, headers);
            try {
                Request request = RequestParser.parse(socket.getInputStream());
                String requestUrl = request.getUrl();
                try {
                    HandlerWrapper handler = this.getHandlerWrapper(request);
                    handleResponse(headers, response, request, handler);
                }catch (UnsupportedOperationException e) {
                    if(this.fileMapping && UrlMatcher.getUrlType(requestUrl) == UrlType.FILE) {
                        if(requestUrl.endsWith("index")){
                            requestUrl = requestUrl.concat(".html");
                        }
                        try {
                            handleStandardFileResponse(headers, response, requestUrl);
                        } catch (IOException ex) {
                            response.setCode(HttpCode.NOT_FOUND);
                        }
                    }
                }
                log.info(request.toString());
            } catch (IllegalRequestException e) {
                response.setCode(HttpCode.BAD_REQUEST);
            }finally {
                log.info(response.toString());
                OutputStream outputStream = socket.getOutputStream();
                outputStream.write(response.toByteResponse());
                outputStream.flush();
                socket.close();
            }
        }
    }


    private void handleResponse(Headers headers, Response response, Request request, HandlerWrapper handler) throws IOException {
        if(!handler.getContentType().equals(MimeType.JSON)){
            handleFileResponse(headers, response, request, handler);
        }else {
            handleRestResponse(headers, response, request, handler);
        }
    }

    private void handleRestResponse(Headers headers, Response response, Request request, HandlerWrapper handler) throws JsonProcessingException {
        Object handled = handler.getHandler().parseRequest(request);
        byte[] indexBuilder = mapper.writeValueAsBytes(handled);
        int contentLength = indexBuilder.length;

        headers.setContentLength(contentLength);
        headers.setContentType(handler.getContentType());

        response.setBody(indexBuilder);
        response.setCode(HttpCode.OK);
    }

    private void handleFileResponse(Headers headers, Response response, Request request, HandlerWrapper handler) throws IOException {
        String path = handler.getHandler().parseRequest(request).toString();
        File file = new File(this.rootDirPath + File.separator + path);
        String filename = file.getName();
        setContentType(headers, filename);
        byte[] fileContent = Files.readAllBytes(file.toPath());
        headers.setContentLength(fileContent.length);
        response.setBody(fileContent);
        response.setCode(HttpCode.OK);
    }
    private void handleStandardFileResponse(Headers headers, Response response, String url) throws IOException {
        String filename = url.substring(1);
        File file = new File(this.rootDirPath + File.separator + filename);

        setContentType(headers, filename);
        byte[] fileContent = Files.readAllBytes(file.toPath());
        headers.setContentLength(fileContent.length);
        response.setBody(fileContent);
        response.setCode(HttpCode.OK);
    }
    private void setContentType(Headers headers, String filename) {
        int dotIndex = filename.indexOf('.');
        if(dotIndex != -1 && dotIndex + 1 != filename.length()){
            String extension = filename.substring(dotIndex + 1);
            if(MimeType.MimeTypeUtils.map.containsKey(extension)){
                headers.setContentType(MimeType.MimeTypeUtils.map.get(extension));
                return;
            }
        }
        headers.setContentType(MimeType.PLAIN);

    }

    private HandlerWrapper getHandlerWrapper(Request request) {
        return this.handlers.entrySet().stream()
                .filter(entry -> entry.getValue().getMethod().equals(request.getMethod()) && (entry.getKey().equals(request.getUrl()) || entry.getKey().equals("/**")))
                .findFirst()
                .orElseThrow(() -> new UnsupportedOperationException("bad request"))
                .getValue();
    }

    @Override
    public int getLocalPort() {
        return Optional.ofNullable(this.serverSocket).map(ServerSocket::getLocalPort).orElse(this.port);
    }
}
