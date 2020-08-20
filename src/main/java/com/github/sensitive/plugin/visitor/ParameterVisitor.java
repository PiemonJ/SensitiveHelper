package com.github.sensitive.plugin.visitor;

import com.github.sensitive.annotation.SensitiveArray;
import com.github.sensitive.annotation.SensitiveCollection;
import com.github.sensitive.common.Pair;
import com.github.sensitive.common.SensitiveMetadataEntry;
import com.github.sensitive.common.SensitiveMetadataTable;
import com.github.sensitive.enums.Purpose;
import com.github.sensitive.plugin.strategy.Message;
import com.github.sensitive.plugin.strategy.StrategyMediator;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.session.defaults.DefaultSqlSession;

import java.lang.annotation.Annotation;
import java.util.*;

public abstract class ParameterVisitor implements Visitor{

    protected abstract Purpose purpose();

    @Override
    public Object visit(SensitiveMetadataTable metadata, Object sandbox) throws Throwable {

        if (sandbox instanceof DefaultSqlSession.StrictMap){
            //单参数,且是List,Mybatis将其包装为DefaultSqlSession.StrictMap
            return visitStrictMap(metadata,(DefaultSqlSession.StrictMap) sandbox);
        } else if (sandbox instanceof MapperMethod.ParamMap){
            // 多参数,Mybatis将其包装为MapperMethod.ParamMap
            return visitParamMap(metadata,(MapperMethod.ParamMap) sandbox);
        } else {
            // 单参数,Mybatis并未做额外的处理，一般情况是Model/Number/String
            return visitUniversal(metadata,sandbox);
        }
    }

    /**
     * 抽象对DefaultSqlSession.StrictMap数据类型的访问
     * @param sandbox
     */
    protected Object visitStrictMap(SensitiveMetadataTable metadata,DefaultSqlSession.StrictMap<Object> sandbox) throws Throwable {
        List<SensitiveMetadataEntry> entries = metadata.sensitive();
        for (SensitiveMetadataEntry entry : entries) {
            Annotation sensitiveAnnotation = entry.getSensitiveAnnotation().get();
            if (entry.getParameterType().isArray()){
                assert sensitiveAnnotation instanceof SensitiveArray;
                Message message = Message.of(
                        metadata.getMethod(),
                        sandbox.get("array"),
                        purpose(),
                        entry.getSensitiveAnnotation(),
                        Collections.emptyList()
                );

                sandbox.put("array",StrategyMediator.mediator.communicate(message));
            } else {
                assert sensitiveAnnotation instanceof SensitiveCollection;
                Message message = Message.of(
                        metadata.getMethod(),
                        sandbox.get("list"),
                        purpose(),
                        entry.getSensitiveAnnotation(),
                        Collections.emptyList()
                );
                sandbox.put("list",StrategyMediator.mediator.communicate(message));

            }
        }
        return sandbox;
    }

    /**
     * 抽象对MapperMethod.ParamMap数据类型的访问
     * @param sandbox
     */
    protected Object visitParamMap(SensitiveMetadataTable metadata,MapperMethod.ParamMap<Object> sandbox)  throws Throwable {
        List<SensitiveMetadataEntry> entries = metadata.sensitive();
        for (SensitiveMetadataEntry entry : entries) {
            String paramName = entry.getParamAnnotation().map(annotation -> ((Param)annotation).value()).orElseGet(() -> "param" + entry.getIndex());
            Object payload = sandbox.get(paramName);
            Message message = Message.of(
                    metadata.getMethod(),
                    payload,
                    purpose(),
                    entry.getSensitiveAnnotation(),
                    Collections.emptyList()
            );
            sandbox.put(paramName,StrategyMediator.mediator.communicate(message));
        }
        return sandbox;
    }

    /**
     * 抽象对Map数据类型的访问
     * @param sandbox
     */
    protected Object visitUniversal(SensitiveMetadataTable metadata,Object sandbox) throws Throwable {
        List<SensitiveMetadataEntry> entries = metadata.sensitive();
        for (SensitiveMetadataEntry entry : entries) {
            // Only One
            Message message = Message.of(
                    metadata.getMethod(),
                    sandbox,
                    purpose(),
                    entry.getSensitiveAnnotation(),
                    Collections.emptyList()
            );
            sandbox = StrategyMediator.mediator.communicate(message);
        }
        return sandbox;

    }

}
