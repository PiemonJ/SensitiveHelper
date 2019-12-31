package com.github.sensitive.plugin.strategy;

import com.github.sensitive.enums.Purpose;

/**
 * 策略中介者
 *
 */
public interface StrategyMediator {


    /**
     * 策略间通信
     * @param message
     * @return
     */
    Object communicate(Message message);

}
