package com.deppwang.demo;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.support.FileSystemXmlApplicationContext;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        FileSystemXmlApplicationContext context = new FileSystemXmlApplicationContext(
                new String[]{"classpath:monitorContext.xml"});
        ConcreteConfig config = (ConcreteConfig) context.getBean("concreteConfig");
        try {
            while (true) {
                System.out.println(config.getZookeeperQuorum());
                System.out.println(config.getZookeeperPort());
                Thread.sleep(5000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
