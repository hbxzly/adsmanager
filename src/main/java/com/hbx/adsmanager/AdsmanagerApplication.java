package com.hbx.adsmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AdsmanagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdsmanagerApplication.class, args);
    }

}
