package com.liangmayong.reform_demo.reform;

import android.content.Context;

import com.android.volley.VolleyError;
import com.liangmayong.reform.OnReformListener;
import com.liangmayong.reform.ReformInterceptor;
import com.liangmayong.reform.ReformParameter;
import com.liangmayong.reform.ReformResponse;
import com.liangmayong.reform.error.ReformAuthFailureError;
import com.liangmayong.reform.error.ReformError;
import com.liangmayong.reform.error.ReformNetworkError;
import com.liangmayong.reform.error.ReformParseError;
import com.liangmayong.reform.error.ReformServerError;
import com.liangmayong.reform.error.ReformUnkownError;
import com.liangmayong.volleyutils.VolleyErrorHelper;
import com.liangmayong.volleyutils.VolleyUtils;

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
