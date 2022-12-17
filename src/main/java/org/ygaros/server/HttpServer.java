package org.ygaros.server;

import java.io.IOException;

public interface HttpServer {
    void init() throws IOException;
    int getLocalPort();
}
