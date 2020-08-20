package com.github.sensitive.plugin;

import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Invocation;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.rmi.MarshalException;
import java.util.Optional;
import java.util.Properties;

public abstract class AbstractInterceptor implements Interceptor {

    public Properties properties;
    /**
     * 该四个常量，是标识在Executor#query(...)方法中，各个参数的索引位置
     * eg: index:0处，放置的是MappedStatement，
     * index:1处，放置的是Object，即是Parameter
     * index:2处，放置的是RowBounds，
     * index:3处，放置的是ResultHandler，
     */
    static final int MAPPED_STATEMENT_INDEX = 0;
    static final int PARAMETER_INDEX = 1;
    static final int ROW_BOUNDS_INDEX = 2;
    static final int RESULT_HANDLER_INDEX = 3;

    /**
     * 获取MappedStatement
     * 特定于当前所处的环境
     * @param invocation
     * @return
     * @throws Throwable
     */
    abstract public MappedStatement obtainMappedStatement(Invocation invocation) throws Throwable;

    /**
     * 获取调用的方法的Method
     * @param invocation
     * @return
     * @throws Throwable
     */
    public Optional<Method> obtainCalledMethod(Invocation invocation) throws Throwable{
        Optional<Method> methodOops = Optional.<Method>empty();
        MappedStatement mappedStatement = obtainMappedStatement(invocation);
        String namespace = mappedStatement.getId();
        String className = namespace.substring(0, namespace.lastIndexOf("."));
        String methodName = namespace.substring(namespace.lastIndexOf(".") + 1);
        // SQL内联SQL的标记符 ！
        int inlineMarker = methodName.indexOf("!");
        if (inlineMarker < 0) {
            Method[] methods = Class.forName(className).getMethods();
            for (Method method : methods) {
                if (method.getName().equals(methodName)) {
                    methodOops = Optional.of(method);
                    break;
                }
            }
        }
        return methodOops;
    }

}