package com.example.demo.config.validnes;

import java.io.IOException;

public interface ValidService {
    String setProxyValidnes() throws IOException;
    Boolean getProxyValidnes() throws IOException;
    String setGetProxyValidnes() throws IOException;
}
