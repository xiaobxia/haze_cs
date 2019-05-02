package com.info.back.task.clusterquartz.job;

import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.lang.reflect.Method;

@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class JobDetailBean extends QuartzJobBean {

    private String targetObject;
    private String targetMethod;
    private ApplicationContext ctx;

    @Override
    protected void executeInternal(JobExecutionContext context) {
        Object bean;
        Method m;
        try {
//            log.info("JobDetailBean targetObject=:{},targetMethod=:{}", targetObject, targetMethod);
            bean = ctx.getBean(targetObject);
            m = bean.getClass().getMethod(targetMethod);
            m.invoke(bean);
        } catch (Exception e) {
            log.error("JobDetailBean executeInternal error:{}", e);
        }
    }

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.ctx = applicationContext;
    }

    public void setTargetObject(String targetObject) {
        this.targetObject = targetObject;
    }

    public void setTargetMethod(String targetMethod) {
        this.targetMethod = targetMethod;
    }
}
