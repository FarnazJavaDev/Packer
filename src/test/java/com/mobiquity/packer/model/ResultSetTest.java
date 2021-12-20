package com.mobiquity.packer.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResultSetTest {

    @Test
    void addItem() {
        ResultSet resultSet=new ResultSet();
        Item item=new Item("(1,2,€2)");
        resultSet.addItem(item);
        assertNotNull(resultSet.getItems());
        assertEquals(resultSet.getItems().size(),1);
        assertTrue(resultSet.getItems().contains(item));
    }

    @Test
    void testToStringOneItem() {
        ResultSet resultSet=new ResultSet();
        Item item=new Item("(1,2,€2)");
        resultSet.addItem(item);
        resultSet.setTotal(2);
        assertEquals("1", resultSet.toString());
    }
    @Test
    void testToStringMultipleItems() {
        ResultSet resultSet=new ResultSet();
        Item item=new Item("(1,2,€2)");
        Item item2=new Item("(2,3,€7)");
        resultSet.addItem(item);
        resultSet.addItem(item2);
        resultSet.setTotal(9);
        assertEquals("1,2", resultSet.toString());
    }
    @Test
    void testToStringZeroItem() {
        ResultSet resultSet=new ResultSet();
        assertEquals("-", resultSet.toString());
    }
}