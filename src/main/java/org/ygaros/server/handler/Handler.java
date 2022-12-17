package org.ygaros.server.handler;

import org.ygaros.server.request.Request;

@FunctionalInterface
public interface Handler {
    Object parseRequest(Request request);
}
