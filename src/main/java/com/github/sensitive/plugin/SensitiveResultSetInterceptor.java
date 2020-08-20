package com.github.sensitive.plugin;

import com.github.sensitive.enums.Purpose;
import com.github.sensitive.plugin.context.Context;
import org.apache.ibatis.executor.resultset.DefaultResultSetHandler;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Statement;
import java.util.Optional;
import java.util.Properties;


/**
 * 敏感信息解密器
 */

@Intercepts({
        @Signature(type = ResultSetHandler.class, method = "handleResultSets", args = {Statement.class})
})
public class SensitiveResultSetInterceptor extends AbstractInterceptor {


    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object result = invocation.proceed();
        if (result == null) {
            return null;
        }
        // 根据Parameter的类型，选择适当的策略对其进行加密

        Optional<Method> method = obtainCalledMethod(invocation);

        if (!method.isPresent()) {
            throw new Exception("Can not obtain method from invocation !!! Please check whether ResultSetHandler is DefaultResultSetHandler");
        }

        Context context = Context.of(method.get());
        result = context.ask(result, Purpose.DECRYPT);
        return result;
    }

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
        if (properties != null) {
            this.properties = properties;
        }
    }

    @Override
    public MappedStatement obtainMappedStatement(Invocation invocation) throws Throwable {
        assert invocation.getTarget() instanceof DefaultResultSetHandler;
        DefaultResultSetHandler resultSetHandler = (DefaultResultSetHandler) invocation.getTarget();
        Field mappedStatementField = resultSetHandler.getClass().getDeclaredField("mappedStatement");
        mappedStatementField.setAccessible(true);
        assert mappedStatementField.get(resultSetHandler) instanceof MappedStatement;
        return (MappedStatement) mappedStatementField.get(resultSetHandler);

    }
}
