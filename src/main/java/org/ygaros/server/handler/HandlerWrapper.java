package org.ygaros.server.handler;

import org.ygaros.server.request.HttpMethod;

public final class HandlerWrapper {
    private final HttpMethod method;
    private final Handler handler;
    private final String contentType;

    public HandlerWrapper(HttpMethod method, String contentType, Handler handler) {
        this.method = method;
        this.contentType = contentType;
        this.handler = handler;
    }

    public String getContentType() {
        return contentType;
    }

    public HttpMethod getMethod() {
        return method;
    }


    public Handler getHandler() {
        return handler;
    }
}
