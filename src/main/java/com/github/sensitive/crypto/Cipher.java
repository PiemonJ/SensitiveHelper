package com.github.sensitive.crypto;

public interface Cipher {

    /**
     * 加密
     * @param literal
     * @return
     */
    public String encryption(String literal);

    /**
     * 解密
     * @param literal
     * @return
     */
    public String decryption(String literal);
}
