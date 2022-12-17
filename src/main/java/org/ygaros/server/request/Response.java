package org.ygaros.server.request;

import java.nio.charset.StandardCharsets;

public class Response {
    private String httpVersion;
    private HttpCode code;
    private Headers headers;
    private byte[] body;

    public Response(){}

    public Response(String httpVersion, Headers headers) {
        this(httpVersion, headers, null, HttpCode.OK);

    }

    public Response(String httpVersion, Headers headers, byte[] body, HttpCode code) {
        this.httpVersion = httpVersion;
        this.headers = headers;
        this.body = body;
        this.code = code;
    }

    public void setCode(HttpCode code) {
        this.code = code;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "Response{" +
                "httpVersion='" + httpVersion + '\'' +
                ", code=" + code +
                ", headers=" + headers +
                ", body length='" + (body != null ? String.valueOf(body.length) : "null") + '\'' +
                '}';
    }

    public String toRawHeaderString(){
        return this.httpVersion + " " +
                this.code.getCode() + " " + this.code + "\r\n" +
                this.headers.getParsedHeaders() +
                "\r\n";
    }
    public byte[] toByteResponse(){
        int bodyLength = this.body != null ? this.body.length : 0;
        byte[] responseHeader = this.toRawHeaderString().getBytes(StandardCharsets.UTF_8);
        byte[] packedResponse = new byte[responseHeader.length + bodyLength];
        System.arraycopy(responseHeader, 0, packedResponse, 0, responseHeader.length);
        if(bodyLength > 0) {
            System.arraycopy(this.body, 0, packedResponse, responseHeader.length, this.body.length);
        }
        return packedResponse;
    }
}
