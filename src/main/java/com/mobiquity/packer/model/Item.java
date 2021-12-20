package com.mobiquity.packer.model;

import com.mobiquity.packer.exception.InvalidInputException;

import java.io.Serializable;

public class Item implements Serializable {

    private final String originalWeight;
    private Integer index;
    private Integer weight;
    private Integer cost;

    public Item(String rawElement) {
        var simple = rawElement.replaceAll("(\\(|\\)|€)+", "");
        String[] splicedRowElement = simple.split(",");
        if (splicedRowElement.length != 3) {
            throw new InvalidInputException("Invalid input: "+ rawElement);
        }
        index = Integer.parseInt(splicedRowElement[0]);
        weight = (int) Float.parseFloat(splicedRowElement[1]);
        originalWeight = splicedRowElement[1];
        cost = Integer.parseInt(splicedRowElement[2]);
    }

    public String getOriginalWeight() {
        return originalWeight;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    @Override
    public String toString() {
        return "Item{" +
                "index=" + index +
                ", weight=" + weight +
                ", cost=" + cost +
                '}';
    }

}
