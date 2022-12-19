package org.ygaros.server.handler;

import org.ygaros.server.request.HttpMethod;
import org.ygaros.server.request.MimeType;

public final class HandlerWrapper {
    private final HttpMethod method;
    private final Handler handler;
    private final MimeType contentType;

    public HandlerWrapper(HttpMethod method, MimeType contentType, Handler handler) {
        this.method = method;
        this.contentType = contentType;
        this.handler = handler;
    }

    public MimeType getContentType() {
        return contentType;
    }

    public HttpMethod getMethod() {
        return method;
    }


    public Handler getHandler() {
        return handler;
    }
}
