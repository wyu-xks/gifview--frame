package com.wy.gifview.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by User on 2016/7/4.
 */
public class ThreadPoolUtils {

    private static ThreadPoolUtils instance = new ThreadPoolUtils();

//    private ExecutorService fiexdPool = Executors.newCachedThreadPool();
    private ExecutorService fiexdPool = Executors.newFixedThreadPool(10);

    private ThreadPoolUtils(){

    }

    public static ThreadPoolUtils getInstance(){
        return instance;
    }

    public ExecutorService getThread(){
        return fiexdPool;
    }
}
