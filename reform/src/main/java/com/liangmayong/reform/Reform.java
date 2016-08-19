package com.liangmayong.reform;

import android.content.Context;

import com.liangmayong.reform.annotation.Converter;
import com.liangmayong.reform.annotation.Interceptor;
import com.liangmayong.reform.error.ReformError;
import com.liangmayong.reform.error.ReformUnkownError;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

/**
 * Reform
 *
 * @author LiangMaYong
 * @version 1.0
 */
public final class Reform {

    // debug
    private static boolean DEBUG = true;
    // reformMap
    private static Map<String, Reform> reformMap = new HashMap<String, Reform>();

    /**
     * setDebug
     *
     * @param debug debug
     */
    public static void setDebug(boolean debug) {
        Reform.DEBUG = debug;
    }

    /**
     * isDebug
     *
     * @return debug
     */
    public static boolean isDebug() {
        return DEBUG;
    }

    /**
     * create
     *
     * @param clazz clazz
     * @return reform module
     */
    public static final <T extends ReformModule> T createModule(Class<T> clazz) {
        if (clazz == null) {
            // clazz == null
            ReformLog.e("module class == null");
            return null;
        }

        try {
            Constructor<T> classConstructor = clazz.getDeclaredConstructor();
            classConstructor.setAccessible(true);
            T t = (T) classConstructor.newInstance();
            Interceptor interceptor = clazz.getAnnotation(Interceptor.class);
            if (interceptor == null) {
                // must set interceptor
                ReformLog.e("module must set interceptor");
                return t;
            }
            Class<? extends ReformInterceptor> interceptorClass = interceptor.value();
            if (interceptorClass == ReformInterceptor.class) {
                // interceptor must extends ReformInterceptor
                ReformLog.e("interceptor must extends ReformInterceptor");
                return t;
            }
            Reform reform = interceptor(interceptorClass);
            ReformConverter reformConverter = null;
            Converter converter = clazz.getAnnotation(Converter.class);
            if (converter != null) {
                try {
                    Class<? extends ReformConverter> converterType = converter.value();
                    reformConverter = converterType.newInstance();
                } catch (Exception e) {
                }
            }
            t.setReform(reform);
            t.setConverter(reformConverter);
            return t;
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * interceptor
     *
     * @param interceptorClass interceptorClass
     * @return reform
     */
    private static Reform interceptor(Class<? extends ReformInterceptor> interceptorClass) {
        if (interceptorClass == null) {
            return null;
        }
        String key = interceptorClass.getName();
        if (reformMap.containsKey(key)) {
            Reform reform = reformMap.get(key);
            return reform;
        } else {
            try {
                Reform reform = new Reform(interceptorClass);
                reformMap.put(key, reform);
                return reform;
            } catch (Exception e) {
                return null;
            }
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // reform interceptor
    private ReformInterceptor interceptor;

    /**
     * Reform
     *
     * @param interceptorClass interceptorClass
     * @throws Exception Exception
     */
    private Reform(Class<? extends ReformInterceptor> interceptorClass) throws Exception {
        this.interceptor = interceptorClass.newInstance();
    }

    /**
     * getInterceptor
     *
     * @return interceptor
     */
    protected ReformInterceptor getInterceptor() {
        return interceptor;
    }

    /**
     * enqueue
     *
     * @param context   context
     * @param url       url
     * @param parameter parameter
     * @param listener  listener
     */
    protected void enqueue(Context context, final ReformConverter converter, String url, final ReformParameter parameter,
                           final OnReformListener listener) {
        if (getInterceptor() == null) {
            listener.onFailure(new ReformUnkownError("ReformInterceptor is null"));
            return;
        }
        ReformInterceptor interceptor = parameter.getInterceptor();
        if (interceptor == null) {
            interceptor = getInterceptor();
        }
        parameter.setCommonHeaders(interceptor.getCommonHeaders());
        interceptor.enqueue(context, url, parameter, new OnReformListener() {
            @Override
            public void onResponse(ReformResponse response) {
                if (listener != null) {
                    if (parameter != null && parameter.getConverter() != null) {
                        response.setConverter(parameter.getConverter());
                    } else {
                        response.setConverter(converter);
                    }
                    listener.onResponse(response);
                }
                ReformLog.d("onResponse:" + response.getUrl());
                ReformLog.d("onResponse:" + response.getBody());
            }

            @Override
            public void onFailure(ReformError e) {
                if (listener != null) {
                    listener.onFailure(e);
                }
                ReformLog.d("onFailure", e);
            }
        });
    }

    /**
     * execute
     *
     * @param context   context
     * @param url       url
     * @param parameter parameter
     * @return Response
     * @throws ReformError error
     */
    protected ReformResponse execute(Context context, ReformConverter converter, String url, ReformParameter parameter)
            throws ReformError {
        if (getInterceptor() == null) {
            throw new ReformUnkownError("ReformInterceptor is null");
        }
        ReformInterceptor interceptor = parameter.getInterceptor();
        if (interceptor == null) {
            interceptor = getInterceptor();
        }
        parameter.setCommonHeaders(interceptor.getCommonHeaders());
        ReformResponse response = interceptor.execute(context, url, parameter);
        if (parameter != null && parameter.getConverter() != null) {
            response.setConverter(parameter.getConverter());
        } else {
            response.setConverter(converter);
        }
        return response;
    }

    /**
     * getBaseUrl
     *
     * @return base url
     */
    protected String getBaseUrl() {
        if (getInterceptor() == null) {
            return "";
        }
        return getInterceptor().getBaseUrl();
    }

    /**
     * destroy
     *
     * @param context context
     */
    protected void destroy(Context context) {
        if (getInterceptor() == null) {
            return;
        }
        getInterceptor().destroy(context);
    }
}
