package com.liangmayong.reform;

import android.os.Bundle;

import java.util.HashMap;
import java.util.Map;

/**
 * ReformParameter
 *
 * @author LiangMaYong
 * @version 1.0
 */
public class ReformParameter {

    /**
     * Method
     *
     * @author LiangMaYong
     * @version 1.0
     */
    public static enum Method {
        POST, GET, PUT, DELETE;
    }

    /**
     * CacheType
     *
     * @author LiangMaYong
     * @version 1.0
     */
    public static enum CacheType {
        NET, NET_LOCAL, LOCAL, LOCAL_NET;
    }

    //method
    private Method method = Method.GET;
    //params
    private Map<String, String> params = null;
    //headers
    private Map<String, String> headers = null;
    //commonheaders
    private Map<String, String> commonheaders = null;
    //converter
    private ReformConverter converter = null;
    //interceptor
    private ReformInterceptor interceptor = null;

    //cache enable
    private boolean cacheEnable = false;
    //local cache
    private boolean localCache = false;
    //extras
    private Bundle extras = null;

    public ReformParameter() {
        params = new HashMap<String, String>();
        headers = new HashMap<String, String>();
    }


    /**
     * getConverter
     *
     * @return converter
     */
    public ReformConverter getConverter() {
        return converter;
    }

    /**
     * getInterceptor
     *
     * @return interceptor
     */
    public ReformInterceptor getInterceptor() {
        return interceptor;
    }

    /**
     * setConverter
     *
     * @param converter converter
     * @return this
     */
    public ReformParameter setConverter(ReformConverter converter) {
        this.converter = converter;
        return this;
    }


    /**
     * setInterceptor
     *
     * @param interceptor interceptor
     */
    public void setInterceptor(ReformInterceptor interceptor) {
        this.interceptor = interceptor;
    }

    /**
     * setExtras
     *
     * @param extras extras
     * @return this
     */
    public ReformParameter setExtras(Bundle extras) {
        this.extras = extras;
        return this;
    }

    /**
     * getExtras
     *
     * @return extras
     */
    public Bundle getExtras() {
        if (extras == null) {
            extras = new Bundle();
        }
        return extras;
    }

    /**
     * addParams
     *
     * @param params params
     * @return this
     */
    public ReformParameter addParams(Map<String, String> params) {
        this.params.putAll(params);
        return this;
    }

    /**
     * addParam
     *
     * @param key   key
     * @param value value
     * @return this
     */
    public ReformParameter addParam(String key, String value) {
        if (value == null) {
            if (this.params.containsKey(key)) {
                this.params.remove(key);
            }
        } else {
            this.params.put(key, value);
        }
        return this;
    }

    /**
     * setHeaders
     *
     * @param headers headers
     * @return this
     */
    public ReformParameter setHeaders(Map<String, String> headers) {
        this.headers.clear();
        this.headers.putAll(headers);
        return this;
    }

    /**
     * setCommonHeaders
     *
     * @param commonheaders commonheaders
     * @return this
     */
    public ReformParameter setCommonHeaders(Map<String, String> commonheaders) {
        this.commonheaders = commonheaders;
        return this;
    }

    /**
     * setMethod
     *
     * @param method method
     * @return this
     */
    public ReformParameter setMethod(Method method) {
        this.method = method;
        return this;
    }

    /**
     * setCacheEnable
     *
     * @param cacheEnable cacheEnable
     * @return this
     */
    public ReformParameter setCacheEnable(boolean cacheEnable) {
        this.cacheEnable = cacheEnable;
        return this;
    }

    /**
     * isCacheEnable
     *
     * @return cacheEnable
     */
    public boolean isCacheEnable() {
        return cacheEnable;
    }

    /**
     * getParams
     *
     * @return params
     */
    public Map<String, String> getParams() {
        return params;
    }

    /**
     * getHeaders
     *
     * @return headers
     */
    public Map<String, String> getHeaders() {
        Map<String, String> newheaders = new HashMap<String, String>();
        if (commonheaders != null) {
            newheaders.putAll(commonheaders);
        }
        newheaders.putAll(this.headers);
        return newheaders;
    }

    /**
     * getMethod
     *
     * @return method
     */
    public Method getMethod() {
        return method;
    }


    /**
     * setLocalCache
     *
     * @param localCache localCache
     * @return this
     */
    public ReformParameter setLocalCache(boolean localCache) {
        this.localCache = localCache;
        return this;
    }

    public boolean isLocalCache() {
        return localCache;
    }
}
