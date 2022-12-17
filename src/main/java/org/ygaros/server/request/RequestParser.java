package org.ygaros.server.request;

import org.ygaros.server.IllegalRequestException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

public class RequestParser {

    public static Request parse(InputStream in) throws IOException, IllegalRequestException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String requestType = reader.readLine();
        if(requestType == null || requestType.length() == 0){
            throw new IllegalRequestException("Bad request");
        }
        String[] requestParts = requestType.split(" ");

        HttpMethod method = HttpMethod.valueOf(requestParts[0]);
        String url = requestParts[1];
        String httpVersion = requestParts[2];

        Map<String, String> headersMap = new HashMap<>();
        String headerLine;
        while(true){
            headerLine = reader.readLine();
            if(headerLine == null || headerLine.length() == 0){
                break;
            }
            String[] headerParts = headerLine.split(": ");
            headersMap.put(headerParts[0], headerParts[1]);
        }
        int contentLength = 0;
        if(headersMap.containsKey("Content-Length")){
            contentLength = Integer.parseInt(headersMap.get("Content-Length"));
        }
        String body = null;
        if(contentLength > 0){
            char[] buffer = new char[contentLength];
            int read = reader.read(buffer, 0, contentLength);
            if(read == contentLength){
                body = new String(buffer);
            }
        }
        return new Request(method, url, httpVersion, headersMap, body);
    }

}
