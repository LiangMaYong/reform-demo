package com.liangmayong.reform_demo.reform;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.liangmayong.reform.ReformConverter;
import com.liangmayong.reform.ReformResponse;

import java.util.List;

/**
 * Created by Administrator on 2016/8/19.
 */
public class ReformConv implements ReformConverter {
    @Override
    public boolean isSuccess(ReformResponse reformResponse) {
        if (reformResponse.parseJsonInt("result_code", -1) == 100000) {
            return true;
        }
        return false;
    }


    @Override
    public String parseErrorMessage(ReformResponse response) {
        return response.parseJsonString("description");
    }

    @Override
    public String parseErrorCode(ReformResponse response) {
        return response.parseJsonString("result_code");
    }

    @Override
    public <T> T parse(Class<T> aClass, ReformResponse reformResponse) {
        return null;
    }

    @Override
    public <T> T parse(String s, Class<T> aClass, ReformResponse reformResponse) {
        return null;
    }

    @Override
    public <T> List<T> parseList(Class<T> aClass, ReformResponse reformResponse) {
        return null;
    }

    @Override
    public <T> List<T> parseList(String s, Class<T> aClass, ReformResponse reformResponse) {
        return null;
    }

}
