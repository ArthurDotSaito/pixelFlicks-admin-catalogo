package com.pixelflicks.admin.catalogo.domain.validation;

public abstract class Validator {
    private ValidationHandler handler;
    protected Validator(final ValidationHandler aHandler){
        this.handler = aHandler;
    }

    public abstract void validate();

    protected ValidationHandler validationHandler(){
        return this.handler;
    }
}
