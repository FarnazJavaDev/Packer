package com.mobiquity.packer.validation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CapacityValidatorTest {

    @Test
    void validateValidCapacity() {
        Validator<Integer> validator=new CapacityValidator();
        assertTrue(validator.validate(50));

    }
    @Test
    void validateInvalidCapacity() {
        Validator<Integer> validator=new CapacityValidator();
        assertFalse(validator.validate(150));
    }
    @Test
    void validateBorderCapacity() {
        Validator<Integer> validator=new CapacityValidator();
        assertTrue(validator.validate(100));
    }
}