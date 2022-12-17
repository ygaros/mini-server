package org.ygaros.server.request;

import java.util.Map;
import java.util.Optional;

public class Request {
    private final HttpMethod method;
    private final String url;
    private final String httpVersion;
    private final Map<String, String> headers;

    //TODO Parse query params if they appear.
    private final Map<String, String> queryParam = null;
    private final String body;

    public Request(HttpMethod method, String url, String httpVersion, Map<String, String> headers) {
        this(method, url, httpVersion, headers, null);
    }

    public Request(HttpMethod method, String url, String httpVersion, Map<String, String> headers, String body) {
        this.method = method;
        this.url = url;
        this.httpVersion = httpVersion;
        this.headers = headers;
        this.body = body;
    }

    public Optional<Integer> getContentLength(){
        String contentStr = this.headers.get("Content-Length");
        if(null == contentStr){
            return Optional.empty();
        }
        return Optional.of(Integer.parseInt(contentStr));
    }
    public String getUrl(){
        return this.url;
    }
    public HttpMethod getMethod(){
        return this.method;
    }

    @Override
    public String toString() {
        return "Request{" +
                "method=" + method +
                ", url='" + url + '\'' +
                ", httpVersion='" + httpVersion + '\'' +
                ", headers=" + headers +
                ", body='" + body + '\'' +
                '}';
    }
    public String toRawString(){
        StringBuilder builder = new StringBuilder();
        builder.append(this.method.toString()).append(" ").append(this.url).append(" ").append(this.httpVersion).append("\r\n");
        this.headers.forEach((key, value) ->{
            builder.append(key).append(": ").append(value).append("\r\n");
        });
        builder.append("\r\n");
        if(this.body != null) {
            builder.append(this.body).append("\r\n");
        }
        return builder.toString();
    }
}
