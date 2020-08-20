package com.github.sensitive.plugin.strategy;

import com.github.sensitive.common.Try;
import java.util.Optional;

public class MuxStrategyMediator implements StrategyMediator {

    @Override
    public Try<Object> communicate(Message message) {

        return Optional.of(message)
                .filter(msg -> msg.getAnnotation().isPresent())
                .filter(msg -> msg.getIgnoredTypeKind().contains(message.getTypeKind()))
                .map(this::matching)
                .orElseGet(() -> Try.success(message.getPayload()));

    }

    /**
     * Low版模式匹配
     * @param message
     * @return
     */
    public Try<Object> matching(Message message) {
        switch (message.getTypeKind()) {
            case ARRAY:
                return Strategy.ARRAY_STRATEGY.action(message);
            case LIST:
                // 递归
                return Strategy.LIST_STRATEGY.action(message);
            case MAP:
                // 可能会出现这种情况,但是一般不会出现
                return Strategy.MAP_STRATEGY.action(message);
            case STRING:
                // List列表中数据是基础数据类型
                return Strategy.STRING_STRATEGY.action(message);
            case MODEL:
                // 假定是领域模型
                return Strategy.MODEL_STRATEGY.action(message);
            case PRIMITIVE:
            case UNKNOWN:
            default:
                return Try.success(message.getPayload());
        }
    }
}
