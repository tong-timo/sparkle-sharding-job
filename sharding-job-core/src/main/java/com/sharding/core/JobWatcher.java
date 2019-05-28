package com.sharding.core;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

/**
 * <B>Description:</B>  <br>
 * <B>Create on:</B> 13:33 <br>
 *
 * @author tonghao
 */
public class JobWatcher implements Watcher {
    public void process(WatchedEvent event) {
        System.out.println(event);
        try {
            SparkleJobFactory.getFactory().enableJob(event.getPath());
        } catch (Exception e) {
            System.out.println("Exception :" + e.getMessage());
        }

    }
}
