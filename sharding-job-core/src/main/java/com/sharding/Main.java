package com.sharding;

import com.sharding.core.SparkleJobFactory;
import com.sharding.core.ZkClient;
import com.sharding.job.SparkleScheduler;
import com.sharding.job.SparkleSimpleJob;
import org.apache.zookeeper.KeeperException;
import org.quartz.Job;
import org.quartz.SchedulerException;

import java.io.IOException;

/**
 * <B>Description:</B>  <br>
 * <B>Create on:</B> 18:03 <br>
 *
 * @author tonghao
 */
public class Main {
    public static void main(String[] args) throws SchedulerException, InterruptedException, IOException, KeeperException {
        ZkClient zkClient = new ZkClient("10.23.140.22:2181", 500);
        SparkleJobFactory factory = SparkleJobFactory.init(zkClient);

        //定义JOB
        TestJob job = new TestJob();
        SparkleSimpleJob simpleJob = new SparkleSimpleJob(job, "xxx", "zzz", "0/5 * * * * ?");
        SparkleScheduler scheduler = new SparkleScheduler(simpleJob);
        factory.addJob(TestJob.class.getName(), scheduler);

        TestJob2 job2 = new TestJob2();
        SparkleSimpleJob simpleJob2 = new SparkleSimpleJob(job2, "xxx1", "zzz1", "0/3 * * * * ?");
        SparkleScheduler scheduler2 = new SparkleScheduler(simpleJob2);
        factory.addJob(TestJob2.class.getName(), scheduler2);
    }
}
