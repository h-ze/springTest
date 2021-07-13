package com.hz.design.builder.abstractfactory.factory;

import java.util.concurrent.TimeUnit;

/**
 * 这个类的主要作用是让所有集群的提供方，能在统一的方法名称下进行操作，同时也方便后续的拓展
 */
public interface ICacheAdapter {

    String get(String key);

    void set(String key, String value);

    void set(String key, String value, long timeout, TimeUnit timeUnit);

    void del(String key);

}
