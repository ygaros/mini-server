package org.ygaros.server.handler;

import org.ygaros.server.request.Request;

@FunctionalInterface
public interface FileHandler extends Handler {
    @Override
    String parseRequest(Request request);
}
