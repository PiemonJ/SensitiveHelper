package com.github.sensitive.plugin.strategy;


import com.github.sensitive.common.Either;
import com.github.sensitive.common.Try;

/**
 * 策略中介者
 *
 */
public interface StrategyMediator {

    StrategyMediator mediator = new MuxStrategyMediator();

    /**
     * 策略间通信
     * @param message
     * @return
     */
    Try<Object> communicate(Message message) throws Throwable;

}
