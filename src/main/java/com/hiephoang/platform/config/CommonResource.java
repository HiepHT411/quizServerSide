package com.hiephoang.platform.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class CommonResource {

    @Value("${server.port}")
    private String serverPort;

    @Value("${server.host}")
    private String serverHost;

}
