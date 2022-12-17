package org.ygaros.server.request;

public enum HttpCode {
    OK(200),
    BAD_REQUEST(400),
    NOT_FOUND(404),
    METHOD_NOT_ALLOWED(405),
    ACCESS_DENIED(403);

    private final int code;

    HttpCode (int code){
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
