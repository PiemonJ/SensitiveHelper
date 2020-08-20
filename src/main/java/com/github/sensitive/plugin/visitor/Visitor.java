package com.github.sensitive.plugin.visitor;

import com.github.sensitive.common.SensitiveMetadataTable;

public interface Visitor {

    /**
     * 对任何数据的访问
     * @param sandbox
     * @return
     */
    Object visit(SensitiveMetadataTable metadataTable,Object sandbox) throws Throwable;

}
