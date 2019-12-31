package com.github.sensitive.plugin.strategy;


import com.github.sensitive.enums.Purpose;

public final class Message {

    Object payload;
    Purpose purpose;

    public Message(Object payload, Purpose purpose) {
        this.payload = payload;
        this.purpose = purpose;
    }

    public static Message of(Object payload, Purpose purpose){
        return new Message(payload,purpose);
    }

}
