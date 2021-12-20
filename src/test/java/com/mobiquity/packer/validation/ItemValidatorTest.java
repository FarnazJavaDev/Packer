package com.mobiquity.packer.validation;

import com.mobiquity.packer.model.Item;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ItemValidatorTest {

    @Test
    void validateValidItem() {
        Validator<Item> validator=new ItemValidator();
        assertTrue(validator.validate(new Item("(1,2,€2)")));

    }

    @Test
    void validateValidBorderItem() {
        Validator<Item> validator=new ItemValidator();
        assertTrue(validator.validate(new Item("(1,100,€100)")));

    }

    @Test
    void validateValidBorder1Item() {
        Validator<Item> validator=new ItemValidator();
        assertTrue(validator.validate(new Item("(1,99,€100)")));

    }

    @Test
    void validateValidBorder2Item() {
        Validator<Item> validator=new ItemValidator();
        assertTrue(validator.validate(new Item("(1,100,€99)")));

    }

    @Test
    void validateItemWithInvalidCost() {
        Validator<Item> validator=new ItemValidator();
        assertFalse(validator.validate(new Item("(1,22,€200)")));
    }
    @Test
    void validateItemWithInvalidWeight() {
        Validator<Item> validator=new ItemValidator();
        assertFalse(validator.validate(new Item("(1,220,€20)")));
    }
}