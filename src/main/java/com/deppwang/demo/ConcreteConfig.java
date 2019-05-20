package com.deppwang.demo;

public class ConcreteConfig {
    private String zookeeperQuorum;
    private String zookeeperPort;

    /**
     * @param zookeeperQuorum the zookeeperQuorum to set
     */
    public void setZookeeperQuorum(String zookeeperQuorum) {
        this.zookeeperQuorum = zookeeperQuorum;
    }

    /**
     * @return the zookeeperQuorum
     */
    public String getZookeeperQuorum() {
        return zookeeperQuorum;
    }

    /**
     * @param zookeeperPort the zookeeperPort to set
     */
    public void setZookeeperPort(String zookeeperPort) {
        this.zookeeperPort = zookeeperPort;
    }

    /**
     * @return the zookeeperPort
     */
    public String getZookeeperPort() {
        return zookeeperPort;
    }

}