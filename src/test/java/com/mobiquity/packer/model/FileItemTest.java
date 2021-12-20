package com.mobiquity.packer.model;

import com.mobiquity.packer.exception.InvalidInputException;
import com.mobiquity.packer.exception.ValidationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FileItemTest {

    @Test
    void addItemValidData() {
        FileItem fileItem = new FileItem(8);
        fileItem.addItem("(1,2,€2)");
        assertEquals(fileItem.getItems().size(), 1);
        assertEquals(fileItem.getItems().get(0).getWeight(), 2);
        assertEquals(fileItem.getItems().get(0).getCost(), 2);
        assertEquals(fileItem.getItems().get(0).getIndex(), 1);
    }

    @Test
    void addItemInValidData() {
        FileItem fileItem = new FileItem(8);
        assertThrows(ValidationException.class, () -> fileItem.addItem("(1,102,€202)"));
    }

    @Test
    void addItemInvalidStringFormat() {
        FileItem fileItem = new FileItem(8);
        assertThrows(InvalidInputException.class, () -> fileItem.addItem("(2,€2)"));
    }
    @Test
    void addItemInvalidStringDelimiter() {
        FileItem fileItem = new FileItem(8);
        assertThrows(InvalidInputException.class, () -> fileItem.addItem("(1-2-€2)"));
    }
    @Test
    void addItemInvalidStringValue() {
        FileItem fileItem = new FileItem(8);
        assertThrows(NumberFormatException.class, () -> fileItem.addItem("(a,2,€2)"));
    }

    @Test
    void normalizeBasedOnOneItem() {
        FileItem fileItem = new FileItem(20);
        fileItem.addItem("(1,12.58,€2)");
        fileItem.normalize();
        assertEquals(fileItem.getCapacity(),2000);
        assertNotNull(fileItem.getItems());
        assertNotNull(fileItem.getItems().get(0));
        assertEquals(fileItem.getItems().get(0).getWeight(),1258);
    }
    @Test
    void normalizeBasedOnMultipleNumbers() {
        FileItem fileItem = new FileItem(20);
        fileItem.addItem("(1,12.58,€2)");
        fileItem.addItem("(2,15.2,€2)");
        fileItem.normalize();
        assertEquals(fileItem.getCapacity(),2000);
        assertNotNull(fileItem.getItems());
        assertNotNull(fileItem.getItems().get(0));
        assertNotNull(fileItem.getItems().get(1));
        assertEquals(fileItem.getItems().get(0).getWeight(),1258);
        assertEquals(fileItem.getItems().get(1).getWeight(),1520);
    }
}