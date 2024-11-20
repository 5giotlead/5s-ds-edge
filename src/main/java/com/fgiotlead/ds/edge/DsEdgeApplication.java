package com.fgiotlead.ds.edge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
public class DsEdgeApplication {

    public static void main(String[] args) {
        SpringApplication.run(DsEdgeApplication.class, args);
    }

}
