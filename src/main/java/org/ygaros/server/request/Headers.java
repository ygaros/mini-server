package org.ygaros.server.request;

import java.util.HashMap;
import java.util.Map;

public class Headers {
    private Map<String, String> headers;
    private static final String CONTENT_LENGTH  = "Content-Length";
    private static final String CONTENT_TYPE  = "Content-Type";

    public Headers(){
        this.headers = new HashMap<>();
    }

    public void set(String header, Object value){
        this.headers.put(header, String.valueOf(value));
    }
    public String getParsedHeaders(){
        StringBuilder builder = new StringBuilder();
        this.headers.forEach((key, value) ->{
            builder.append(key).append(": ").append(value).append("\r\n");
        });
        return builder.toString();
    }
    public void setContentType(MimeType mimeType){
        this.set(CONTENT_TYPE, mimeType.toString());
    }
    public void setContentLength(int length){
        this.set(CONTENT_LENGTH, length);
    }

    @Override
    public String toString() {
        return "Headers{" + headers + '}';
    }
}
