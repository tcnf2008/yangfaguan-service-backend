package com.nau.util;

import com.nau.common.JwtTokenUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 便捷执行异步任务。 线程池平时运行数量最大为50个线程。本工具正在处理的任务不能积压超过10万个，超过后再提交任务时，将会抛出异常。 大批量刷新数据切勿使用本工具，请自行开线程或者线程池处理。
 *
 * @author Haipeng.wang NAU
 */
public class AsyncRunUtil {

  private static final Logger logger = LoggerFactory.getLogger(AsyncRunUtil.class);

  static ExecutorService executorService = new ThreadPoolExecutor(100, 200,
      60L, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
  static ExecutorService executorServiceForLongTask = new ThreadPoolExecutor(10, 10,
      60L, TimeUnit.SECONDS, new LinkedBlockingQueue<>());

  /**
   * 用于处理时间短的小任务。
   */
  public static Future safeSubmit(Runnable runnable) {
    return executorService.submit(() -> {
      try {
        runnable.run();
      } catch (Exception ex) {
        logger.error("AsyncTaskUtil, exception: {}", ex, ex);
      }
    });
  }

  /**
   * 用于处理时间短的小任务。
   */
  public static <T> Future<T> safeCall(Callable<T> callable) {
    return executorService.submit(() -> {
      try {
        return callable.call();
      } catch (Exception ex) {
        logger.error("AsyncTaskUtil.safeSubmit error: {}", ex.getMessage(), ex);
      }

      return null;
    });
  }

  /**
   * 用于处理时间长的大任务。
   */
  public static Future safeSubmitLongTask(Runnable runnable) {
    return executorServiceForLongTask.submit(() -> {
      try {
        runnable.run();
      } catch (Exception ex) {
        logger.error("AsyncTaskUtil safeSubmitLongTask exception: {}", ex, ex);
      }
    });
  }

  /**
   * 用于处理时间长的大任务。
   */
  public static <T> Future<T> safeCallLongTask(Callable<T> callable) {
    return executorServiceForLongTask.submit(() -> {
      try {
        return callable.call();
      } catch (Exception ex) {
        logger.error("AsyncTaskUtil safeSubmitLongTask error: {}", ex.toString(), ex);
      }
      return null;
    });
  }
}
