package com.liangmayong.reform;

import android.content.Context;

import com.liangmayong.reform.error.ReformError;
import com.liangmayong.reform.error.ReformUnkownError;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * ReformModule
 *
 * @author LiangMaYong
 * @version 1.0
 */
public class ReformModule {

    public ReformModule() {
    }

    private Reform reform;
    private ReformConverter converter;

    public void setConverter(ReformConverter converter) {
        this.converter = converter;
    }

    public void setReform(Reform reform) {
        this.reform = reform;
    }

    /**
     * getConverter
     *
     * @return converter
     */
    private ReformConverter getConverter() {
        return converter;
    }

    /**
     * getReform
     *
     * @return reform
     */
    private Reform getReform() {
        return reform;
    }

    /**
     * enqueue
     *
     * @param context   context
     * @param url       url
     * @param parameter parameter
     * @param listener  listener
     */
    protected void enqueue(Context context, String url, ReformParameter parameter, final OnReformListener listener) {
        if (getReform() != null) {
            getReform().enqueue(context, getConverter(), parseUrl(url), parameter, listener);
            return;
        }
        listener.onFailure(new ReformUnkownError("reform is null"));
    }

    /**
     * execute
     *
     * @param context   context
     * @param url       url
     * @param parameter parameter
     * @return response
     * @throws ReformError error
     */
    protected ReformResponse execute(Context context, String url, ReformParameter parameter) throws ReformError {
        if (getReform() != null) {
            return getReform().execute(context, getConverter(), parseUrl(url), parameter);
        }
        throw new ReformUnkownError("reform is null");
    }

    /**
     * parseUrl
     *
     * @param url url
     * @return url
     */
    private String parseUrl(String url) {
        if (url.startsWith("http://") || url.startsWith("https://")) {
            return url;
        } else if (url.startsWith("/")) {
            int sunStart = 0;
            if (getReform().getBaseUrl().endsWith("/")) {
                sunStart = 1;
            }
            return getReform().getBaseUrl() + url.substring(sunStart);
        } else if (url.startsWith("./")) {
            int sunStart = 1;
            if (getReform().getBaseUrl().endsWith("/")) {
                sunStart = 2;
            }
            return getReform().getBaseUrl() + url.substring(sunStart);
        } else {
            return getReform().getBaseUrl() + url;
        }
    }

    /**
     * getUrlHost
     *
     * @param url url
     * @return url
     */
    private String getUrlHost(String url) {
        try {
            return new URL(url).getHost();
        } catch (MalformedURLException e) {
        }
        return url;
    }

    /**
     * destroy
     *
     * @param context context
     */
    public void destroy(Context context) {
        if (getReform() != null) {
            getReform().destroy(context);
        }
    }
}
