package com.smonline.virtual.client.core;

/**
 * @author Lody
 */

public interface CrashHandler {

    void handleUncaughtException(Thread t, Throwable e);

}
