package cn.eyeblue.blog.rest.common;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


//这个是异步处理器服务
@Slf4j
@Service
public class AsyncHandlerService {

    @Autowired
    DingdingService dingdingService;
    private ExecutorService executorService = Executors.newFixedThreadPool(20);

    //这里带有统一的全局异常处理
    public void submit(Runnable runnable) {
        this.submit(runnable, true);
    }

    //notifyDingding 表示是否需要用钉钉进行通知。DingDingService 必须是false，否则dingding异常会进入无穷循环
    public void submit(Runnable runnable, boolean notifyDingding) {

        Callable<Integer> callable = () -> {
            runnable.run();
            return 1;
        };

        Future future = executorService.submit(callable);
        //异常的捕获必须要一个异步线程中去进行，否则 future.get 会阻塞，失去了异步性。
        executorService.submit(() -> {
            try {
                future.get();
            } catch (Throwable throwable) {
                log.error("执行异步任务出错{}", ExceptionUtils.getRootCauseMessage(throwable), throwable);

                //错误消息直接顶顶报警。
                if (notifyDingding) {
                    dingdingService.sendHandlerExceptionInfo(throwable);
                }

            }
        });


    }


}
