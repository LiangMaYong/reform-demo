package com.liangmayong.reform;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * ReformResponse
 *
 * @author LiangMaYong
 * @version 1.0
 */
public final class ReformResponse {

    /**
     * ReformResponse
     *
     * @param url  url
     * @param body body
     */
    public ReformResponse(String url, String body) {
        this.url = url;
        this.body = body;
    }

    // url
    private String url = "";
    // body
    private String body = "";
    // bodyJSON
    private volatile JSONObject bodyJSON = null;
    // converter
    private ReformConverter converter;
    // headers
    private Map<String, String> headers = null;
    // params
    private Map<String, String> params = null;

    /**
     * getBodyJSON
     *
     * @return bodyJSON
     * @throws JSONException errpr
     */
    public JSONObject getBodyJSON() throws JSONException {
        if (!"".equals(body) && !"".equals(body)) {
            if (bodyJSON == null) {
                bodyJSON = new JSONObject(body);
            }
        }
        return bodyJSON;
    }

    /**
     * setParams
     *
     * @param params params
     */
    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    /**
     * setHeaders
     *
     * @param headers headers
     */
    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    /**
     * getHeaders
     *
     * @return headers
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
        return headers;
    }

    /**
     * getUrl
     *
     * @return url
     */
    public String getUrl() {
        return url;
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
     * setConverter
     *
     * @param converter converter
     */
    public void setConverter(ReformConverter converter) {
        this.converter = converter;
    }

    /**
     * get body
     *
     * @return response
     */
    public String getBody() {
        return body;
    }

    @Override
    public String toString() {
        if (params == null || params.isEmpty()) {
            return "Response {" + "url=" + url + ",body=" + body + '}';
        }
        return "Response {" + "url=" + url + "@params=" + params + ",body=" + body + '}';
    }

    /**
     * isSuccess
     *
     * @return response status
     */
    public boolean isSuccess() {
        if (getConverter() == null) {
            return true;
        }
        return getConverter().isSuccess(this);
    }

    /**
     * getErrorMessage
     *
     * @return message
     */
    public String getErrorMessage() {
        if (getConverter() == null) {
            return "";
        }
        return getConverter().parseErrorMessage(this);
    }

    /**
     * getErrorCode
     *
     * @return message
     */
    public String getErrorCode() {
        if (getConverter() == null) {
            return "";
        }
        return getConverter().parseErrorCode(this);
    }

    /**
     * parse
     *
     * @param entityClass entityClass
     * @return instance
     */
    public <T> T parse(Class<T> entityClass) {
        if (getConverter() == null) {
            return null;
        }
        return getConverter().parse(entityClass, this);
    }

    /**
     * parse
     *
     * @param key         key
     * @param entityClass entityClass
     * @return
     */
    public <T> T parse(String key, Class<T> entityClass) {
        if (getConverter() == null) {
            return null;
        }
        return getConverter().parse(key, entityClass, this);
    }

    /**
     * parseList
     *
     * @param entityClass entityClass
     * @return list
     */
    public <T> List<T> parseList(Class<T> entityClass) {
        if (getConverter() == null) {
            return null;
        }
        return getConverter().parseList(entityClass, this);
    }

    /**
     * parseList
     *
     * @param entityClass entityClass
     * @return list
     */
    public <T> List<T> parseList(String key, Class<T> entityClass) {
        if (getConverter() == null) {
            return null;
        }
        return getConverter().parseList(key, entityClass, this);
    }

    /**
     * parseJsonInt
     *
     * @param key key
     * @return value
     */
    public int parseJsonInt(String key, int defualtValue) {
        try {
            if (getBodyJSON() != null) {
                try {
                    return Integer.parseInt(getBodyJSON().getString(key));
                } catch (Exception e) {
                }
            }
            return defualtValue;
        } catch (JSONException e) {
            return defualtValue;
        }
    }

    /**
     * parseJsonKey
     *
     * @param key key
     * @return value
     */
    public String parseJsonString(String key) {
        try {
            if (getBodyJSON() != null) {
                return getBodyJSON().getString(key);
            }
            return "";
        } catch (JSONException e) {
            return "";
        }
    }

    /**
     * parseJsonObject
     *
     * @param key key
     * @return value
     */
    public JSONObject parseJsonObject(String key) throws JSONException {
        if (getBodyJSON() != null) {
            return getBodyJSON().getJSONObject(key);
        }
        throw new JSONException("body not json string");
    }

    /**
     * parseJsonArray
     *
     * @param key key
     * @return value
     */
    public JSONArray parseJsonArray(String key) throws JSONException {
        if (getBodyJSON() != null) {
            return getBodyJSON().getJSONArray(key);
        }
        throw new JSONException("body not json string");
    }
}