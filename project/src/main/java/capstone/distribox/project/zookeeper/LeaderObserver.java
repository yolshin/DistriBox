package capstone.distribox.project.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderLatchListener;
import org.apache.curator.retry.RetryOneTime;

import java.nio.charset.StandardCharsets;

public class LeaderObserver {


    private final String clientId;
    private CuratorFramework client;

    public LeaderObserver(String connectString, String clientId) {
        this.clientId = clientId;
        CuratorFramework client = CuratorFrameworkFactory.newClient(connectString, new RetryOneTime(1000));
        client.start();
    }

    public String getLeaderId() throws Exception {
        byte[] data = client.getData().forPath("/leader");
        return new String(data, StandardCharsets.UTF_8);
    }

}
