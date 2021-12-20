package com.mobiquity.packer.util;

import com.mobiquity.packer.exception.APIException;
import com.mobiquity.packer.model.FileItem;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class FileUtilTest {

    @Test
    void readFile() throws APIException, IOException {
        String path = Objects.requireNonNull(ClassLoader.getSystemClassLoader().getResource("dataSet")).getPath();
        List<FileItem> fileItems = FileUtil.readFile(path);
        assertNotNull(fileItems);
        assertEquals(fileItems.size(),4);
        assertNotNull(fileItems.get(0).getItems());
        assertEquals(fileItems.get(0).getItems().size(),6);
        assertNotNull(fileItems.get(1).getItems());
        assertEquals(fileItems.get(1).getItems().size(),1);
        assertEquals(fileItems.get(1).getItems().get(0).getWeight(),153);
        assertEquals(fileItems.get(1).getItems().get(0).getCost(),34);
        assertNotNull(fileItems.get(2).getItems());
        assertEquals(fileItems.get(2).getItems().size(),9);
        assertNotNull(fileItems.get(3).getItems());
        assertEquals(fileItems.get(3).getItems().size(),9);
    }
    @Test
    void readFileWithEmptyFirstRow(){
        String path = Objects.requireNonNull(ClassLoader.getSystemClassLoader().getResource("dataset-empty-line")).getPath();
        assertThrows(APIException.class, () -> FileUtil.readFile(path));
    }
    @Test
    void readFileWithEmptyLineBetweenRow(){
        String path = Objects.requireNonNull(ClassLoader.getSystemClassLoader().getResource("dataset-empty-line-between-rows")).getPath();
        assertThrows(APIException.class, () -> FileUtil.readFile(path));
    }

    @Test
    void getFileItems() throws APIException {
        Stream<String> lines = Stream.of("8 : (1,2,€2) (2,3,€7) (3,2,€1)");
        List<FileItem> fileItems = FileUtil.getFileItems(lines);
        assertNotNull(fileItems);
        assertEquals(fileItems.size(), 1);
        assertEquals(fileItems.get(0).getItems().size(), 3);
        assertEquals(fileItems.get(0).getCapacity(), 8);
        assertEquals(fileItems.get(0).getItems().get(0).getIndex(), 1);
        assertEquals(fileItems.get(0).getItems().get(0).getWeight(), 2);
        assertEquals(fileItems.get(0).getItems().get(0).getCost(), 2);
    }
    @Test
    void getFileItemsNoItem() {
        Stream<String> lines = Stream.of("");
        assertThrows(APIException.class, ()->FileUtil.getFileItems(lines));
    }
    @Test
    void getFileItemsExceedMaxSize() {
        Stream<String> lines = Stream.of("800 : (1,2,€2) (2,3,€7) (3,2,€1)");
        assertThrows(APIException.class, ()->FileUtil.getFileItems(lines));
    }
    @Test
    void getFileItemsConstraintViolationItems(){
        Stream<String> lines = Stream.of("8 : (1,200,€2) (2,3,€7) (3,2,€1)");
        assertThrows(APIException.class, ()->FileUtil.getFileItems(lines));
    }
}