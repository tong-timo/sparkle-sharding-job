package com.sharding.core;

import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;

/**
 * <B>Description:</B>  <br>
 * <B>Create on:</B> 12:05 <br>
 *
 * @author tonghao
 */
public class ZkClient {
    protected ZooKeeper zooKeeper;
    private int timeout;
    private String connectString;

    public ZkClient(String connectString, int timeout) throws IOException {
        this.connectString = connectString;
        this.timeout = timeout;
        init();
    }

    private ZooKeeper init() throws IOException {
        if (zooKeeper != null) {
            return zooKeeper;
        }
        zooKeeper = new ZooKeeper(connectString, timeout, null);
        return zooKeeper;
    }

}
