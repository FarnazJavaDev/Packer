package com.mobiquity.packer.model;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class ResultSet {
   private Set<Item> items=new HashSet<>();
   private Integer total=0;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public void addItem(Item item){
        items.add(item);
    }

    public Set<Item> getItems() {
        return items;
    }

    @Override
    public String toString() {
        return this.getItems().isEmpty() ? "-" : items.stream().map(item -> item.getIndex().toString()).sorted().collect(Collectors.joining(","));
    }
}
