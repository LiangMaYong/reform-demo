package com.liangmayong.reform.annotation;

import com.liangmayong.reform.ReformInterceptor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Interceptor
 *
 * @author LiangMaYong
 * @version 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Interceptor {
    Class<? extends ReformInterceptor> value();
}
