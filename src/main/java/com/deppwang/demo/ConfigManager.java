package com.deppwang.demo;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;
import java.util.Set;
import java.util.Timer;

import org.springframework.beans.factory.support.MethodReplacer;

public class ConfigManager implements FileListener, MethodReplacer {
    private static final long FILE_MONITOR_INTERVAL = 500;
    private static final String MONITOR_CONF_FILE_PATH = "/monitor.properties";
    private FileMonitor monitor;
    private Timer timer = new Timer("Timer", true);

    private Properties properties = new Properties();


    private ConfigManager() throws IOException {
        properties.load(getClass().getResourceAsStream("/monitor.properties"));

        // monitor the configuration file change
        monitor = new FileMonitor(getFileByClassPath(MONITOR_CONF_FILE_PATH), this);
        timer.schedule(monitor, FILE_MONITOR_INTERVAL, FILE_MONITOR_INTERVAL);

    }

    private File getFileByClassPath(String filepath) {
        URL url = getClass().getResource(
                filepath);
        if (url == null) {
            System.err.println("failed to find the file " + filepath);
            return null;
        }

        try {
            File file = new File(url.toURI());
            return file;
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public synchronized void onFileChanged(File file) {
        Properties newProperties = new Properties();
        try {
            newProperties.load(new FileInputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        Set<String> keys = properties.stringPropertyNames();
        for (String key : keys) {
            String newValue = (String) newProperties.get(key);
            String oldValue = properties.getProperty(key);
            System.out.println("newValue:" + newValue + " oldValue:" + oldValue);
            if (newValue != null) {
                properties.setProperty(key, newValue);
            } else {
                properties.remove(key);
            }
        }
    }

    public synchronized String getProperty(String key) {
        return properties.getProperty(key);
    }

    @Override
    public Object reimplement(Object arg0, Method arg1, Object[] arg2)
            throws Throwable {
        String methodName = arg1.getName();
        String tmp = methodName.substring("get".length());
        char ch = tmp.charAt(0);
        ch = Character.toLowerCase(ch);
        tmp = ch + tmp.substring(1);
        return getProperty(tmp);
    }

}
