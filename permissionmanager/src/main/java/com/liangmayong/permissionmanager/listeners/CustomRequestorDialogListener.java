package com.liangmayong.permissionmanager.listeners;

import android.app.Activity;

/**
 * CustomRequestorDialogListener
 *
 * @author LiangMaYong
 * @version 1.0
 */
public interface CustomRequestorDialogListener {
    /**
     * showRequestDialog
     *
     * @param activity        activity
     * @param needPermissions needPermissions
     * @param requestId       requestId
     * @param title           title
     * @param message         message
     * @param sureName        sureName
     * @param cancelName      cancelName
     */
    void showRequestDialog(final Activity activity, String[] needPermissions, int requestId, String title, String message, String sureName, String cancelName);
}
