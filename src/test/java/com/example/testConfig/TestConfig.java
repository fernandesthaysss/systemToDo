package com.example.testConfig;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.NativeWebRequest;

import static org.mockito.Mockito.mock;

@SpringBootTest
@TestConfiguration
public class TestConfig {

    @Bean
    public NativeWebRequest nativeWebRequest() {
        return mock(NativeWebRequest.class);
    }
}