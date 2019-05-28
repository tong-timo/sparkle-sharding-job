package com.sharding.core;

import com.sharding.job.SparkleScheduler;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;

import java.util.Collections;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <B>Description:</B>  <br>
 * <B>Create on:</B> 18:04 <br>
 *
 * @author tonghao
 */
public class SparkleJobFactory {
    private Map<String, SparkleScheduler> schedulerConfig;
    private Scheduler scheduler;
    private ZkClient zkClient;
    private ZooKeeper zooKeeper;
    private static final String BASE_PATH = "/dev_service/sparkle_sharding_job";
    private static SparkleJobFactory factory;

    public static SparkleJobFactory init(ZkClient zkClient) throws SchedulerException {
        if (factory == null) {
            synchronized (SparkleJobFactory.class) {
                if (factory == null) {
                    factory = new SparkleJobFactory(zkClient);
                }
            }
        }
        return factory;
    }

    public static SparkleJobFactory getFactory() {
        return factory;
    }

    private SparkleJobFactory(ZkClient zkClient) throws SchedulerException {
        schedulerConfig = new HashMap<String, SparkleScheduler>();
        scheduler = new StdSchedulerFactory().getScheduler();
        this.zkClient = zkClient;
        zooKeeper = zkClient.zooKeeper;
    }

    public void addJob(String jobPath, SparkleScheduler sparkleScheduler) throws KeeperException, InterruptedException, SchedulerException {
        String path = BASE_PATH + "/" + jobPath;
        String ip = "index_";
        String absolute = path + "/" + ip;
        String nodePath = null;
        if (zooKeeper.exists(absolute, false) == null) {
            if (zooKeeper.exists(BASE_PATH, false) == null) {
                zooKeeper.create(BASE_PATH, ip.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
            if (zooKeeper.exists(path, false) == null) {
                zooKeeper.create(path, ip.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
            nodePath = zooKeeper.create(absolute, ip.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);

        }
        String nodeName = nodePath.substring(path.length() + 1);
        initJobSch(path, nodeName, sparkleScheduler);
        scheduler.start();
        sparkleScheduler.setNodeName(nodeName);
        this.schedulerConfig.put(jobPath, sparkleScheduler);
    }

    private boolean initJobSch(String path, String nodeName, SparkleScheduler sparkleScheduler) throws KeeperException, InterruptedException, SchedulerException {
        System.out.println("当前Node:" + nodeName);
        boolean lock = false;
        List<String> childs = null;
        if (zooKeeper.exists(path, false) != null) {
            childs = zkClient.zooKeeper.getChildren(path, false);
        }
        System.out.println(childs);
        if (childs == null || childs.size() == 0) {
            return false;
        }
        Collections.sort(childs);
        int pos = childs.indexOf(nodeName);
        if (pos == 0) {
            //
            lock = true;
        } else if (pos > 0) {
            //监听前一个节点
            zkClient.zooKeeper.exists(path + "/" + childs.get(pos - 1), new JobWatcher());
            lock = false;
        }
        if (lock) {
            sparkleScheduler.getJob().trigger();
            scheduler.scheduleJob(sparkleScheduler.getJob().getDetail(), sparkleScheduler.getJob().getTrigger());
            System.out.println("初始化任务success");
        } else {
            System.out.println("获取锁lock失败");
        }
        sparkleScheduler.setExecute(lock);
        return lock;
    }

    protected void enableJob(String nodeName) throws InterruptedException, SchedulerException, KeeperException {
        String path = nodeName.substring(0, nodeName.lastIndexOf("/"));
        String key = path.substring(path.lastIndexOf("/") + 1);
        SparkleScheduler scheduler = this.schedulerConfig.get(key);
        if (scheduler == null) {
            System.out.println("scheduler is not exists");
            return;
        }
        initJobSch(path, scheduler.getNodeName(), scheduler);
    }
}
