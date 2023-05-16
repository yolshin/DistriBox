package com.distribox.fss.controllers;

import com.distribox.fss.zookeeper.LeaderObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ZookeeperController {

    @Autowired
    private LeaderObserver leaderObserver;

    @GetMapping("/leader")
    public String getLeader() throws Exception {
        return "This is the leader ID: " + leaderObserver.getLeaderId();
    }

}
