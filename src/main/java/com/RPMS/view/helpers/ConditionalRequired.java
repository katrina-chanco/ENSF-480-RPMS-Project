package com.RPMS.view.helpers;

import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.data.binder.Validator;
import com.vaadin.flow.data.binder.ValueContext;

import java.util.function.Function;

public class ConditionalRequired<Form, Value> implements Validator<Value>
{
    private static final long serialVersionUID = 1L;
    private Function<Form, Boolean> funcRequired;

    private Boolean required = false;

    private String error;
    private Form f;

    public ConditionalRequired(Form f, Function<Form, Boolean> isRequiredFunction, String error)
    {
        this.f = f;
        this.funcRequired = isRequiredFunction;
        this.error = error;
    }

    public ConditionalRequired(Boolean required, String error)
    {
        this.required = required;
        this.error = error;
    }


    @Override
    public ValidationResult apply(Value value, ValueContext context)
    {
        if (required || (funcRequired != null && funcRequired.apply(f)))
            if (value == null || value.toString().trim() == "")
                return ValidationResult.error(error);

        return ValidationResult.ok();
    }

}
