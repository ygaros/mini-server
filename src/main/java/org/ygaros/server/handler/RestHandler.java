package org.ygaros.server.handler;

import org.ygaros.server.request.Request;

@FunctionalInterface
public interface RestHandler extends Handler{
    @Override
    Object parseRequest(Request request);
}
