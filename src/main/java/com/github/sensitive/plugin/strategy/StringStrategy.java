package com.github.sensitive.plugin.strategy;

import com.github.sensitive.common.SensitiveMetadataEntry;
import com.github.sensitive.crypto.Cipher;
import com.github.sensitive.enums.Purpose;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Optional;

public class StringStrategy extends AbstractStrategy {

    /**
     * 字符串策略是一个特殊的策略
     * 该策略不能由策略之外的对象触发
     *
     * @return
     */

    @Override
    protected Object doAction(Message message) throws Throwable {
        Object data = message.getPayload();
        if (data == null) {
            return null;
        }
        assert data instanceof String;

        Cipher cipher = new Cipher() {
            @Override
            public String encryption(String literal) {
                return null;
            }

            @Override
            public String decryption(String literal) {
                return null;
            }
        };

        switch (message.getPurpose()) {
            case ENCRYPT:
                data = cipher.encryption((String)data);
                break;
            case ERASURE:
            case DECRYPT:
                data = cipher.decryption((String)data);
                break;
            default:
                break;
        }
        return data;
    }
}
