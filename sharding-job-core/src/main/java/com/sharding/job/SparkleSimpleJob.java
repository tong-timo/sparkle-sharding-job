package com.sharding.job;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.TriggerBuilder;

/**
 * <B>Description:</B>  <br>
 * <B>Create on:</B> 17:34 <br>
 *
 * @author tonghao
 */
public class SparkleSimpleJob {
    private Job job;
    private JobDetail detail;
    private String name;
    private String group;
    private String cron;
    protected CronTrigger trigger;


    public SparkleSimpleJob(Job job, String name, String group, String cron) {
        this.job = job;
        this.name = name;
        this.group = group;
        this.cron = cron;
        buildJob();
    }

    public void trigger() {
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(this.cron).withMisfireHandlingInstructionDoNothing();
        this.trigger = TriggerBuilder.newTrigger().withIdentity(getName(), getGroup()).withSchedule(scheduleBuilder).build();
    }


    private void buildJob() {
        detail = JobBuilder.newJob(job.getClass()).withIdentity(name, group).build();
    }

    public String getName() {
        return name;
    }


    public String getGroup() {
        return group;
    }

    public JobDetail getDetail() {
        return detail;
    }

    public CronTrigger getTrigger() {
        return trigger;
    }

}
