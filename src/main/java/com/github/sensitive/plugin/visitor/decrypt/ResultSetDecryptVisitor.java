package com.github.sensitive.plugin.visitor.decrypt;

import com.github.sensitive.plugin.visitor.ResultSetVisitor;
import com.github.sensitive.plugin.visitor.Visitor;


public class ResultSetDecryptVisitor extends ResultSetVisitor {

    public static Visitor singleton(){
        return new ResultSetDecryptVisitor();
    }

}
