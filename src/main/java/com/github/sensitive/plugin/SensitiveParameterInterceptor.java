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

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.Properties;

/**
 * 加密、擦除入参拦截器
 */
@Intercepts(
        {
                @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
                @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
                @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class})
        }
)
public class SensitiveParameterInterceptor extends AbstractInterceptor {

    public static final String CIPHER_MARK_METHOD_NAME = "";


    /**
     * 该拦截器主要处理查询命令中入参的加密
     * PS:无法处理纯字符串的入参
     *
     * @param invocation
     * @return
     * @throws Throwable
     */
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        // 根据Parameter的类型，选择适当的策略对其进行加密
        Optional<Method> method = obtainCalledMethod(invocation);
        if (!method.isPresent()){
            throw new Exception("an not obtain method from invocation !!! Please check your dao method name is valid or visible ");
        }
        // 创建上下文
        Context context = Context.of(method.get());
        // 请求加密
        args[PARAMETER_INDEX] = context.ask(args[PARAMETER_INDEX], Purpose.ENCRYPT);
        // Invoke SQL
        Object result = invocation.proceed();
        // 请求擦除加密
        args[PARAMETER_INDEX] = context.ask(args[PARAMETER_INDEX], Purpose.ERASURE);
        return result;
    }

    /**
     * 插件代理
     *
     * @param target
     * @return
     */
    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    /**
     * properties配置该项目所使用的BaseEntity的全限定名
     * key =
     *
     * @param properties
     */
    @Override
    public void setProperties(Properties properties) {
//        if (properties != null)
//            this.properties = properties;
    }

    @Override
    public MappedStatement obtainMappedStatement(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        // mappedStatement
        MappedStatement mappedStatement = (MappedStatement) args[MAPPED_STATEMENT_INDEX];
        return mappedStatement;
    }
}