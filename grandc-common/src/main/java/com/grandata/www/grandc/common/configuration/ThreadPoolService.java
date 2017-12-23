package com.grandata.www.grandc.common.configuration;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 
 * tomcat关闭时，需要调用shutdown方法
 * 
 *
 */
public class ThreadPoolService {

    /**
     * the ThreadPoolService logger
     */

    /**
     * the minimum number of threads
     */
    private final static int corePoolSize = 4;

    /**
     * the maximum number of threads
     */
    private final static int maxPoolSize = 10;

    /**
     * the idle time of the thread if the thread idle time is more than this, it will be shutdown
     */
    private final static int keepAliveTime = 5;

    /**
     * the size of the queue
     */
    private final static int workQueueSize = 10;

    /**
     * ThreadPoolExecutor
     */
    private static ThreadPoolExecutor executor;

    /**
     * ThreadPoolService
     */
    private static ThreadPoolService threadPoolService;

    /**
     * private constructor
     */
    private ThreadPoolService() {
    }

    /**
     * getInstance
     * 
     * @return ThreadPoolService
     */
    public static synchronized ThreadPoolService getInstance() {
        if (threadPoolService == null) {
            threadPoolService = new ThreadPoolService();

            // create thread pool instance
            executor = new ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAliveTime, TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(workQueueSize), new ThreadPoolExecutor.CallerRunsPolicy());
        }
        return threadPoolService;
    }

    /**
     * add the task to the thread pool
     * 
     * @param task
     */
    public void execute(Runnable task) {
        executor.execute(task);
    }

    /**
     * getThreadExecutor
     * 
     * @return ThreadPoolExecutor
     */
    public ThreadPoolExecutor getThreadExecutor() {
        return executor;
    }
    
    public void shutdown() {
    	if (executor != null) {
    		executor.shutdown();
    	}
    }

}
