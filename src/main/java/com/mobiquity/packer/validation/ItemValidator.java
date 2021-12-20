package com.mobiquity.packer.validation;

import com.mobiquity.packer.model.Item;

public class ItemValidator implements Validator<Item> {
    /**
     * @param item that must be validated and checked the amount of weight and cost
     * @return boolean value which indicates that The Item is valid or not
     */
    @Override
    public Boolean validate(Item item) {
        return item.getWeight() <= 100 && item.getCost() <= 100;
    }
}
