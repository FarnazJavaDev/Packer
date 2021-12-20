package com.mobiquity.packer.validation;


public interface Validator<T> {
    Boolean validate(T t);
}
