package com.distribox.fds.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderLatch;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class ZookeeperConfig {

    private CuratorFramework client;
    @Value("${zookeeper.connectionString}")
    private String connectionString;

    @Value("${server.url}")
    private String url;

    @Bean
    public CuratorFramework curatorFramework() throws Exception {
        System.out.println("Zookeeper connection string: " + connectionString + " port: " + url);
        client = CuratorFrameworkFactory.builder()
                .connectString(connectionString)
                .retryPolicy(new ExponentialBackoffRetry(Integer.MAX_VALUE, 3))
                .build();
        client.start();

        // Create the LeaderLatch
        //allows the FDS to participate in leader election
        LeaderLatch leaderLatch = new LeaderLatch(client, "/my-group", url);
        leaderLatch.start();
        // Run the code in a loop
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        executorService.submit(() -> {
            while (true) {
                if (!leaderLatch.hasLeadership()) {
                    System.out.println("I am NOT the leader on port " + url);
                } else {
                    System.out.println("I AM the leader on port " + url + " and I do nothing YET");
                    Stat stat = client.checkExists().forPath("/leader");
                    if (stat == null) {
                        client.create().forPath("/leader", url.getBytes());
                    } else {
                        client.setData().forPath("/leader", url.getBytes());
                    }
                }

                // Sleep for some time before running again
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // Handle the exception
                }
            }
        });

// Shutdown the executor service when no longer needed
        executorService.shutdown();

        return client;
    }

    public void start() throws Exception {
        // start the client
        client.start();
    }

    public CuratorFramework getClient() {
        return client;
    }

    public void stop() {
        // stop the client when you are done
        client.close();
    }

    public static void main(String[] args) throws Exception {
        CuratorFramework client = CuratorFrameworkFactory.builder()
                .connectString("localhost:2181,localhost:2182,localhost:2183")
                .retryPolicy(new ExponentialBackoffRetry(Integer.MAX_VALUE, 3))
                .build();
        client.start();
        client.delete().forPath("/leader");
        LeaderLatch leaderLatch = new LeaderLatch(client, "/example/leader-latch", "port");
        leaderLatch.start();
        // Run the code in a loop
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        executorService.submit(() -> {
            while (true) {
                if (!leaderLatch.hasLeadership()) {
                    System.out.println("I am NOT the leader on port ");
                } else {
                    System.out.println("I AM the leader on port "+ " and I do nothing YET");
                    client.setData().forPath("/leader", "port".getBytes());
                }

                // Sleep for some time before running again
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    // Handle the exception
                }
            }
        });

// Shutdown the executor service when no longer needed
        executorService.shutdown();
//        System.out.println("PRINTING OUT ALL THE CHILDREN " + client.getChildren().forPath("/"));
//
//        zkClient.stop();
    }
}

