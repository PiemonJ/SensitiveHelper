package com.github.sensitive.plugin;

import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Invocation;

import java.lang.reflect.Method;

public abstract class AbstractInterceptor implements Interceptor {

    /**
     * 该四个常亮，是标识在Executor#query(...)方法中，各个参数的索引位置
     * eg: index:0处，放置的是MappedStatement，
     *     index:1处，放置的是Object，即是Parameter
     *     index:2处，放置的是RowBounds，
     *     index:3处，放置的是ResultHandler，
     */
    static int MAPPED_STATEMENT_INDEX = 0;
    static int PARAMETER_INDEX = 1;
    static int ROW_BOUNDS_INDEX = 2;
    static int RESULT_HANDLER_INDEX = 3;

    public Method obtainCalledMethod(Invocation invocation){
        Method action = null;
        try {
            Object[] args = invocation.getArgs();
            // mappedStatement
            MappedStatement mappedStatement = (MappedStatement) args[MAPPED_STATEMENT_INDEX];

            String namespace = mappedStatement.getId();
            String className = namespace.substring(0,namespace.lastIndexOf("."));
            String methodName = namespace.substring(namespace.lastIndexOf(".") + 1,namespace.length());

            Method[] methods = Class.forName(className).getMethods();
            for(Method method : methods){
                if(method.getName().equals(methodName)){
                    action = method;
                    break;
                }
            }
        } catch (Exception e){

        }

        return action;
    }
}
