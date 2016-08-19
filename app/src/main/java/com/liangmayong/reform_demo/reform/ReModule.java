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
        ReformParameter parameter = new ReformParameter();
        parameter.setMethod(ReformParameter.Method.POST);
        enqueue(context, "./get_app_config", parameter, listener);
    }
    public void getOSS(Context context, OnReformListener listener) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Expert-Id2", "1");
        ReformParameter parameter = new ReformParameter();
        parameter.setHeaders(headers);
        parameter.setMethod(ReformParameter.Method.POST);
        enqueue(context, "http://ssssssssssss..com/get_app_config", parameter, listener);
    }
}
