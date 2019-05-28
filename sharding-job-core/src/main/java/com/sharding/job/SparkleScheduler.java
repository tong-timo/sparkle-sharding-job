package com.sharding.job;

/**
 * <B>Description:</B>  <br>
 * <B>Create on:</B> 17:39 <br>
 *
 * @author tonghao
 */
public class SparkleScheduler {
    protected SparkleSimpleJob job;
    protected String nodeName;
    protected boolean execute;

    public SparkleScheduler(SparkleSimpleJob job) {
        this.job = job;
        execute = false;
    }

    public SparkleSimpleJob getJob() {
        return this.job;
    }

    public void setExecute(boolean execute){
        this.execute = execute;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }
}
