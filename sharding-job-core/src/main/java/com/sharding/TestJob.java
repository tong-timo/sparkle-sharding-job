package com.sharding;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * <B>Description:</B>  <br>
 * <B>Create on:</B> 18:04 <br>
 *
 * @author tonghao
 */
public class TestJob implements Job {
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println("execute job");
    }
}
