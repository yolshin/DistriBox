package capstone.distribox.project.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderLatch;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListener;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class ZooKeeperConfig {

    private CuratorFramework client;
    @Value("${zookeeper.connectionString}")
    private String connectionString;

    @Value("${server.port}")
    private String port;

    @Bean
    public CuratorFramework curatorFramework() throws Exception {
        client = CuratorFrameworkFactory.builder()
                .connectString(connectionString)
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .build();
        client.start();

        // Create the LeaderLatch
        LeaderLatch leaderLatch = new LeaderLatch(client, "/example/leader-latch", port);
        leaderLatch.start();

        // Run the code in a loop
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        executorService.submit(() -> {
            while (true) {
                if (!leaderLatch.hasLeadership()) {
                    System.out.println("I am not the leader on port " + port);
                } else {
                    System.out.println("I am the leader on port " + port + " and I do nothing");
                    client.setData().forPath("/leader", port.getBytes());
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

//        LeaderSelectorListener listener = new LeaderSelectorListenerAdapter() {
//            public void takeLeadership(CuratorFramework client) throws Exception {
//                // this callback will get called when you are the leader
//                // do whatever leader work you need to and only exit
//                // this method when you want to relinquish leadership
//                while(true) {
//                    System.out.println("I'm the leader now and my port is " + port);
//                    Thread.sleep(1000);
//                }
//            }
//        };
//        LeaderSelector selector = new LeaderSelector(client, "/web-app", listener);
//        selector.start();

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
        ZooKeeperConfig zkClient = new ZooKeeperConfig();
        CuratorFramework client = zkClient.curatorFramework();
        LeaderSelectorListener listener = new LeaderSelectorListenerAdapter() {
            public void takeLeadership(CuratorFramework client) throws Exception {
                // this callback will get called when you are the leader
                // do whatever leader work you need to and only exit
                // this method when you want to relinquish leadership
                while(true) {
                    System.out.println("I'm the leader now ");
                    Thread.sleep(1000);
                }
            }
        };
        zkClient.start();
        LeaderSelector selector = new LeaderSelector(client, "/web-app", listener);
        selector.start();
        // Use the client to interact with ZooKeeper
        while (true) {
            System.out.print(".");
            Thread.sleep(1000);
        }
//        System.out.println("PRINTING OUT ALL THE CHILDREN " + client.getChildren().forPath("/"));
//
//        zkClient.stop();
    }
}
