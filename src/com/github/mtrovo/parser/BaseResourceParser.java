package com.github.mtrovo.parser;

import java.net.URL;


public abstract class BaseResourceParser implements ResourceParser{

    protected URL base;

    public BaseResourceParser() {
        super();
    }

    public URL getBase() {
        return base;
    }

    public void setBase(URL base) {
        this.base = base;
    }

}