package com.mobiquity.packer.model;

import com.mobiquity.packer.exception.ValidationException;
import com.mobiquity.packer.validation.CapacityValidator;
import com.mobiquity.packer.validation.ItemValidator;
import com.mobiquity.packer.validation.Validator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FileItem implements Serializable {

    private Integer capacity;
    private int multiplication = 1;
    private List<Item> items = new ArrayList<>();

    public FileItem(Integer capacity) throws ValidationException {
        Validator<Integer> validator = new CapacityValidator();
        if (validator.validate(capacity))
            this.capacity = capacity;
        else {
            throw new ValidationException(String.format("Capacity {%d} is not valid",capacity));
        }
    }

    public void addItem(String s) throws ValidationException {
        Validator<Item> validator = new ItemValidator();
        var item=new Item(s);
        if (validator.validate(item)){
            items.add(item);
        } else {
            throw new ValidationException(String.format("%s is not valid",item));
        }
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "FileItem{" +
                "capacity=" + capacity +
                ", items=" + items +
                '}';
    }


    /**
     * this method is used to remove the fractional part of the weight value to be used as index of the solution's 3D-matrix
     */
    public void normalize() {
        if (items.size() > 0) {
            Optional<Integer> max = items.stream().map(Item::getOriginalWeight).map(i -> {
                var split = i.split("\\.");
                if (split.length > 1) {
                    return split[1].length();
                }
                return 0;
            }).max(Integer::compareTo);
            max.ifPresent(integer -> multiplication = (int) Math.pow(10, integer));
            items.stream().forEach(it -> it.setWeight((int) (Float.parseFloat(it.getOriginalWeight()) * multiplication))
            );
            capacity =  multiplication * capacity;
        }
    }
}
