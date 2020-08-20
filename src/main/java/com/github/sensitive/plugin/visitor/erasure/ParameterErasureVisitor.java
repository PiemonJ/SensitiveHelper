package com.github.sensitive.plugin.visitor.erasure;

import com.github.sensitive.enums.Purpose;
import com.github.sensitive.plugin.visitor.ParameterVisitor;
import com.github.sensitive.plugin.visitor.Visitor;

public class ParameterErasureVisitor extends ParameterVisitor {

    public static Visitor singleton(){
        return new ParameterErasureVisitor();
    }

    @Override
    protected Purpose purpose() {
        return Purpose.ERASURE;
    }
}
