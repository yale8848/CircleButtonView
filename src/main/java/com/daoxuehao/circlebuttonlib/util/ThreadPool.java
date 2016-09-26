package com.daoxuehao.circlebuttonlib.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Yale on 2016/9/26.
 */
public class ThreadPool {


    private static class Holder{
        public static ThreadPool INSTANCE = new ThreadPool();
    }
    public static  ThreadPool getInstance(){
        return Holder.INSTANCE;
    }
    private ExecutorService mExecutorService;
    public ThreadPool(){
        mExecutorService  = Executors.newFixedThreadPool(1);
    }
    public void run(Runnable run){
        mExecutorService.execute(run);
    }
}
