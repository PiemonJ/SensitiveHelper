package com.github.sensitive.plugin;

import com.github.sensitive.enums.Purpose;
import com.github.sensitive.plugin.strategy.Context;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.lang.reflect.Method;
import java.util.Properties;

/**
 * 加密拦截器
 *
 */

@Intercepts(
        {
                @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
                @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
                @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class})
        }
)
public class SensitiveInterceptor extends AbstractInterceptor {

    private Properties properties;

    /**
     * 该拦截器主要处理查询命令中入参的加密
     * PS:无法处理纯字符串的入参
     * @param invocation
     * @return
     * @throws Throwable
     */
    @Override
    public Object intercept(Invocation invocation) throws Throwable {

        Object[] args = invocation.getArgs();

        // 根据Parameter的类型，选择适当的策略对其进行加密
        Method method = obtainCalledMethod(invocation);
        // 创建上下文
        Context context = Context.of(method);
        // 请求加密
        args[PARAMETER_INDEX] = context.ask(args[PARAMETER_INDEX], Purpose.ENCRYPT);
        // Invoke SQL
        Object result = invocation.proceed();
        // 请求擦除加密
        args[PARAMETER_INDEX] = context.ask(args[PARAMETER_INDEX],Purpose.ERASURE);
        // 请求解密
        result = context.ask(result, Purpose.DECRYPT);

        return result;
    }

    /**
     * 插件代理
     * @param target
     * @return
     */
    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target,this);
    }

    /**
     * 自定义配置源头
     * @param properties
     */
    @Override
    public void setProperties(Properties properties) {
        if (properties != null)
            this.properties = properties;

    }
}
