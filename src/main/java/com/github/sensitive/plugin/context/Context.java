package com.github.sensitive.plugin.context;

import com.github.sensitive.cache.Cache;
import com.github.sensitive.cache.CacheFactory;
import com.github.sensitive.common.SensitiveMetadataTable;
import com.github.sensitive.enums.Purpose;
import com.github.sensitive.plugin.visitor.decrypt.ResultSetDecryptVisitor;
import com.github.sensitive.plugin.visitor.encrypt.ParameterEncryptVisitor;
import com.github.sensitive.plugin.visitor.erasure.ParameterErasureVisitor;
import java.lang.reflect.Method;
import java.util.Optional;


public class Context {

    public static final Cache<String,SensitiveMetadataTable> cache = CacheFactory.instance(null);

    final Method method;

    /**
     * 全参数构造器
     * @param method
     */
    public Context(Method method) {
        this.method = method;
    }

    /**
     * Helper Method
     *
     * @param method
     * @return
     */
    public static Context of(Method method) {
        return new Context(method);
    }

    /**
     * ASK 加密/解密/擦除
     * @param data
     * @param purpose
     * @return
     * @throws Throwable
     */
    public Object ask(Object data, Purpose purpose) throws Throwable {
        String className = method.getDeclaringClass().getName();
        String methodName = method.getName();
        String key = className + "." + methodName;
        Optional<SensitiveMetadataTable> oops = cache.cas(key, () -> SensitiveMetadataTable.of(method));

        SensitiveMetadataTable metadata = oops.get();

        if (metadata.isSensitive()){
            switch (purpose) {
                case ENCRYPT:
                    data = ParameterEncryptVisitor.singleton().visit(metadata,data);
                    break;
                case ERASURE:
                    data = ParameterErasureVisitor.singleton().visit(metadata,data);
                    break;
                case DECRYPT:
                    data = ResultSetDecryptVisitor.singleton().visit(metadata,data);
                    break;
                default:
                    break;
            }
        }

        return data;
    }

}