package com.deppwang.demo;

import java.io.File;

/**
 * an interface to listen the notifications when the file has been changed
 */
public interface FileListener {

    /**
     * a notification when the file changed
     *
     * @param file the file which has been changed
     */
    void onFileChanged(File file);
}