package com.liangmayong.reform_demo.reform;

import android.content.Context;

import com.android.volley.VolleyError;
import com.liangmayong.reform.interfaces.OnReformListener;
import com.liangmayong.reform.interfaces.ReformInterceptor;
import com.liangmayong.reform.ReformParameter;
import com.liangmayong.reform.ReformResponse;
import com.liangmayong.reform.errors.ReformAuthFailureError;
import com.liangmayong.reform.errors.ReformError;
import com.liangmayong.reform.errors.ReformNetworkError;
import com.liangmayong.reform.errors.ReformParseError;
import com.liangmayong.reform.errors.ReformServerError;
import com.liangmayong.reform.errors.ReformUnkownError;
import com.liangmayong.volleyutils.VolleyErrorHelper;
import com.liangmayong.volleyutils.VolleyUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/8/19.
 */
public class ReformInter implements ReformInterceptor {
    @Override
    public String getBaseUrl() {
        return "http://test.b.api.vitabee.cn/app/";
    }

    @Override
    public void destroy(Context context) {
        VolleyUtils.destroy(context);
    }

    @Override
    public Map<String, String> getCommonHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("App-Version", "2.0.0");
        headers.put("App-Id", "2");
        headers.put("Expert-Id", "1");
        return headers;
    }

    @Override
    public Map<String, String> getCommonParams() {
        return null;
    }

    @Override
    public void enqueue(Context context, final String url, final ReformParameter reformParameter, final OnReformListener onReformListener) {
        VolleyUtils.postStringRequest(context, url, reformParameter.getParams(), reformParameter.getHeaders(), reformParameter.isCacheEnable(), new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                ReformResponse reformResponse = new ReformResponse(url, s);
                reformResponse.setParams(reformParameter.getParams());
                onReformListener.onResponse(reformResponse);
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                String errorType = VolleyErrorHelper.getErrorType(volleyError);
                if (VolleyErrorHelper.AUTH_FAILURE_ERROR.equals(errorType)) {
                    onReformListener.onFailure(new ReformAuthFailureError(volleyError));
                } else if (VolleyErrorHelper.SERVER_ERROR.equals(errorType)) {
                    onReformListener.onFailure(new ReformServerError(volleyError));
                } else if (VolleyErrorHelper.PARSE_ERROR.equals(errorType)) {
                    onReformListener.onFailure(new ReformParseError(volleyError));
                } else if (VolleyErrorHelper.NETWORK_ERROR.equals(errorType)) {
                    onReformListener.onFailure(new ReformNetworkError(volleyError));
                } else if (VolleyErrorHelper.NO_CONNECTION_ERROR.equals(errorType)) {
                    onReformListener.onFailure(new ReformNetworkError(volleyError));
                } else if (VolleyErrorHelper.UNKOWN_ERROR.equals(errorType)) {
                    onReformListener.onFailure(new ReformUnkownError(volleyError));
                }
            }
        });
    }

    @Override
    public ReformResponse execute(Context context, String s, ReformParameter reformParameter) throws ReformError {
        return null;
    }
}
