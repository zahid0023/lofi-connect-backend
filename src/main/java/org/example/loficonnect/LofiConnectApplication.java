package org.example.loficonnect;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class LofiConnectApplication {

    public static void main(String[] args) {
        SpringApplication.run(LofiConnectApplication.class, args);
    }

}
