package com.github.sensitive.plugin.visitor.encrypt;

import com.github.sensitive.enums.Purpose;

import com.github.sensitive.plugin.visitor.ParameterVisitor;
import com.github.sensitive.plugin.visitor.Visitor;


public class ParameterEncryptVisitor extends ParameterVisitor {

    public static Visitor singleton(){
        return new ParameterEncryptVisitor();
    }

    @Override
    protected Purpose purpose() {
        return Purpose.ENCRYPT;
    }
}
