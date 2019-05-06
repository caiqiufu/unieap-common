package com.unieap.base;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@ComponentScan(basePackages = { "com" })
@EnableAsync // 利用@EnableAsync注解开启异步任务支持
public class AsyncTaskConfig implements AsyncConfigurer {
	public Executor getAsyncExecutor() { // 配置类实现AsyncConfigurer接口并重写
											// getAsyncExecutor 方法,并返回一个
											// ThreadPoolTaskExecutor,这样我们就获得了一个线程池
											// taskExecutor
		ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
		taskExecutor.setCorePoolSize(5);
		taskExecutor.setMaxPoolSize(10);
		taskExecutor.setQueueCapacity(25);
		taskExecutor.initialize();

		return taskExecutor;
	}

	public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
		return null;
	}
}