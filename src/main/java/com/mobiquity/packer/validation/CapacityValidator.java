package com.mobiquity.packer.validation;

public class CapacityValidator implements Validator<Integer>{
    /**
     * @param capacity of package that must be validated
     * @return boolean value which indicates that the given capacity is valid or not
     */
    @Override
    public Boolean validate(Integer capacity) {
      return capacity <= 100;
    }
}
