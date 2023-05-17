package com.distribox.fss.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Configuration
public class LeaderObserver {

    @Value("${zookeeper.connectionString}")
    private String connectString;

    private CuratorFramework client;

    @Bean
    public void startZookeeper() {
        this.client = CuratorFrameworkFactory.builder()
                .connectString(connectString)
                .retryPolicy(new ExponentialBackoffRetry(Integer.MAX_VALUE, 3))
                .build();
        client.start();
    }

    public String getLeaderId() {
        byte[] data = new byte[0];
        try {
            data = client.getData().forPath("/leader");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return new String(data, StandardCharsets.UTF_8);
    }

}
