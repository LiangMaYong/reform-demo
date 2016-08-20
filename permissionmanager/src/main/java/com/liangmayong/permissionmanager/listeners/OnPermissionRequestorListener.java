package com.liangmayong.permissionmanager.listeners;

import java.util.List;

/**
 * OnPermissionRequestorListener
 *
 * @author LiangMaYong
 * @version 1.0
 */
public interface OnPermissionRequestorListener {

    /**
     * gotPermissions
     */
    void gotPermissions();

    /**
     * rejectPermissions
     *
     * @param rejects rejects
     */
    void rejectPermissions(List<String> rejects);
}
