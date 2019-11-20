package com.RPMS.view.helpers;


import java.util.function.Function;

import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.data.binder.Validator;
import com.vaadin.flow.data.binder.ValueContext;

/**
 * conditionally applies a validator if the 'required/funcRequired' argument is true.
 *
 * @param <T>
 * @author bsutton
 */
public class ConditionalValidator<T> implements Validator<T> {
    private static final long serialVersionUID = 1L;
    private Validator<T> validator;
    private Function<T, Boolean> funcRequired;

    private Boolean required = false;


    public ConditionalValidator(Function<T, Boolean> funcRequired, Validator<T> v) {
        this.funcRequired = funcRequired;
        this.validator = v;
    }


    public ConditionalValidator(Boolean required, Validator<T> v) {
        this.required = required;
        this.validator = v;
    }


    @Override
    public ValidationResult apply(T value, ValueContext context) {

        if (required || (funcRequired != null && funcRequired.apply(value)))
            return validator.apply(value, context);
        else
            return ValidationResult.ok();
    }
}
