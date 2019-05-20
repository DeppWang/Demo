package com.deppwang.demo;

import java.io.File;
import java.util.TimerTask;


/**
 * a class to monitor if the file has been changed
 * 一个TimeTask的子类，检查文件有无改动
 */
public class FileMonitor extends TimerTask {
    private FileListener listener;
    private File file;
    private long lastModified;

    /**
     * constructor
     *
     * @param file     a file which will be monitor
     * @param listener a listener which will be notified when the file has been
     *                 changed
     */
    public FileMonitor(File file, FileListener listener) {
        if (file == null || listener == null) {
            throw new NullPointerException();
        }

        this.file = file;
        this.lastModified = this.file.lastModified();
        this.listener = listener;
    }

    @Override
    public void run() {
        long lastModified = this.file.lastModified();
        if (this.lastModified != lastModified) {
            this.lastModified = lastModified;
            this.listener.onFileChanged(this.file);
        }
    }
}
