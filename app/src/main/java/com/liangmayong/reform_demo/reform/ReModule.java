package com.liangmayong.reform_demo.reform;

import android.content.Context;

import com.liangmayong.reform.OnReformListener;
import com.liangmayong.reform.ReformModule;
import com.liangmayong.reform.ReformParameter;
import com.liangmayong.reform.annotation.Converter;
import com.liangmayong.reform.annotation.Interceptor;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/8/19.
 */
@Interceptor(ReformInter.class)
@Converter(ReformConv.class)
public class ReModule extends ReformModule {

    public void getConfig(Context context, OnReformListener listener) {
        Map<String, String> headers = new HashMap<>();
        headers.put("App-Version", "2.0.0");
        headers.put("App-Id", "2");
        headers.put("Expert-Id", "1");
        ReformParameter parameter = new ReformParameter();
        parameter.setHeaders(headers);
        parameter.setMethod(ReformParameter.Method.POST);
        enqueue(context, "get_app_config", parameter, listener);
    }
}
