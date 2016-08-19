package com.liangmayong.reform;

import com.liangmayong.reform.error.ReformError;

/**
 * OnReformListener
 *
 * @author LiangMaYong
 * @version 1.0
 */
public interface OnReformListener {

    /**
     * onResponse
     *
     * @param response response
     */
    public void onResponse(ReformResponse response);

    /**
     * onFailure
     *
     * @param error error
     */
    public void onFailure(ReformError error);

}
