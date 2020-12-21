package com.hlcy.yun.sip;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

public class CallableTest {


    public static void main(String[] args) throws Exception {
        System.out.println(getCallableResult());
        System.out.println("main over");
    }

    static String getCallableResult() throws Exception {
        final FutureTask<String> futureTask = new FutureTask<>(getResult());
        new Thread(futureTask).start();
        return futureTask.get();
    }

    static Callable<String> getResult(){
        return ()->{
            final Thread callThread = Thread.currentThread();
            System.out.println("tid: " + callThread.getId() + ", tname:" + callThread.getName());
            TimeUnit.MINUTES.sleep(10);
            return "ok";
        };
    }
}
