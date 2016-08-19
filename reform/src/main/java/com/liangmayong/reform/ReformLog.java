package com.liangmayong.reform;

import android.util.Log;

/**
 * ReformLog
 *
 * @author LiangMaYong
 * @version 1.0
 */
public class ReformLog {

    private ReformLog() {
    }

    private static final String TAG = "Reform";

    /**
     * Send a DEBUG log message and log the exception.
     *
     * @param msg The message you would like logged.
     */
    public static void d(String msg) {
        if (Reform.isDebug()) {
            Log.d(TAG, msg);
        }
    }

    /**
     * Send a DEBUG log message and log the exception.
     *
     * @param msg The message you would like logged.
     * @param tr  An exception to log
     */
    public static void d(String msg, Throwable tr) {
        if (Reform.isDebug()) {
            Log.d(TAG, msg, tr);
        }
    }

    /**
     * Send a ERROR log message and log the exception.
     *
     * @param msg The message you would like logged.
     */
    public static void e(String msg) {
        if (Reform.isDebug()) {
            Log.e(TAG, msg);
        }
    }

    /**
     * Send a ERROR log message and log the exception.
     *
     * @param msg The message you would like logged.
     * @param tr  An exception to log
     */
    public static void e(String msg, Throwable tr) {
        if (Reform.isDebug()) {
            Log.e(TAG, msg, tr);
        }
    }

    /**
     * Send a INFO log message and log the exception.
     *
     * @param msg The message you would like logged.
     */
    public static void i(String msg) {
        if (Reform.isDebug()) {
            Log.i(TAG, msg);
        }
    }

    /**
     * Send a INFO log message and log the exception.
     *
     * @param msg The message you would like logged.
     * @param tr  An exception to log
     */
    public static void i(String msg, Throwable tr) {
        if (Reform.isDebug()) {
            Log.i(TAG, msg, tr);
        }
    }

    /**
     * Send a VERBOSE log message and log the exception.
     *
     * @param msg The message you would like logged.
     */
    public static void v(String msg) {
        if (Reform.isDebug()) {
            Log.v(TAG, msg);
        }
    }

    /**
     * Send a VERBOSE log message and log the exception.
     *
     * @param msg The message you would like logged.
     * @param tr  An exception to log
     */
    public static void v(String msg, Throwable tr) {
        if (Reform.isDebug()) {
            Log.v(TAG, msg, tr);
        }
    }

    /**
     * Send a WARN log message and log the exception.
     *
     * @param msg The message you would like logged.
     */
    public static void w(String msg) {
        if (Reform.isDebug()) {
            Log.w(TAG, msg);
        }
    }

    /**
     * Send a WARN log message and log the exception.
     *
     * @param msg The message you would like logged.
     * @param tr  An exception to log
     */
    public static void w(String msg, Throwable tr) {
        if (Reform.isDebug()) {
            Log.w(TAG, msg, tr);
        }
    }
}
