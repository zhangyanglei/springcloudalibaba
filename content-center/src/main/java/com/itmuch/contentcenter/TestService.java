package com.itmuch.contentcenter;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TestService {

    @SentinelResource
    public String common() {
        log.info("common....");
        return "common";
    }
}
