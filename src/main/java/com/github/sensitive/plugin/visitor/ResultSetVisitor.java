package com.github.sensitive.plugin.visitor;

import com.github.sensitive.common.Constant;
import com.github.sensitive.common.SensitiveMetadataTable;
import com.github.sensitive.enums.Purpose;
import com.github.sensitive.enums.TypeKind;
import com.github.sensitive.plugin.strategy.Message;
import com.github.sensitive.plugin.strategy.StrategyMediator;
import com.github.sensitive.utils.TypeClassifier;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

public abstract class ResultSetVisitor implements Visitor{

    @Override
    public Object visit(SensitiveMetadataTable metadataTable, Object sandbox) throws Throwable {
        // Only！！处理查询端的返回值，不处理命令端的返回值(因为命令端只返回影响条数)
        if (sandbox instanceof ArrayList) {
            Method method = metadataTable.getMethod();

            Class<?> returnType = metadataTable.getReturnType();
            TypeKind typeKind = TypeClassifier.classify(returnType);
            Message message;

            switch (typeKind) {
                case LIST:
                    // 1.方法签名中返回值是List
                    message = Message.of(method,sandbox, Purpose.DECRYPT, Optional.of(Constant.ANY), Collections.emptyList());
                    sandbox = StrategyMediator.mediator.communicate(message);
                    break;
                case MAP:
                case MODEL:
                    // 2.方法签名中返回值是Map
                    // 3.方法的返回值类型是领域模型
                    message = Message.of(method,((ArrayList) sandbox).get(0),Purpose.DECRYPT,Optional.of(Constant.ANY),Collections.emptyList());
                    Object result = StrategyMediator.mediator.communicate(message);
                    ((ArrayList) sandbox).set(0,result);
                    break;
                case STRING:
                case PRIMITIVE:
                case UNKNOWN:
                default:
                    // no-op
                    break;
            }

        }
        return sandbox;
    }
}
