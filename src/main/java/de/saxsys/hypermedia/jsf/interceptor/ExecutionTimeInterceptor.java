package de.saxsys.hypermedia.jsf.interceptor;

import java.io.Serializable;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Interceptor
@MeasureTime
public class ExecutionTimeInterceptor implements Serializable {

    private static final long serialVersionUID = 1L;

    @AroundInvoke
    public Object spyMethodCall(InvocationContext ctx) throws Exception {
        long start = System.currentTimeMillis();

        // execute method
        Object result = ctx.proceed();

        long end = System.currentTimeMillis();
        Logger logger = LoggerFactory.getLogger(ctx.getMethod().getDeclaringClass());
        logger.info("{}: {}ms", ctx.getMethod().getName(), end - start);
        return result;
    }

}
