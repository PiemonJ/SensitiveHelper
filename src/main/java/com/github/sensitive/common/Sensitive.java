package com.github.sensitive.common;

public class Sensitive {
    /**
     * 数据敏感版本号
     */
    Integer sensitiveVersion;

    public Sensitive() {
    }

    public Sensitive(Integer sensitiveVersion) {
        this.sensitiveVersion = sensitiveVersion;
    }

    public Integer getSensitiveVersion() {
        return sensitiveVersion;
    }

    public void setSensitiveVersion(Integer sensitiveVersion) {
        this.sensitiveVersion = sensitiveVersion;
    }
}
