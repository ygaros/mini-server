package org.ygaros.server.request;

import java.util.HashMap;
import java.util.Map;

public class Headers {
    private Map<String, String> headers;

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

    @Override
    public String toString() {
        return "Headers{" + headers + '}';
    }
}
