package com.mobiquity.packer;

import com.mobiquity.packer.exception.APIException;
import com.mobiquity.packer.model.FileItem;
import com.mobiquity.packer.model.Item;
import com.mobiquity.packer.model.ResultSet;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

public class PackerTest {

    @Test
    void pack() throws APIException {
        String path = Objects.requireNonNull(ClassLoader.getSystemClassLoader().getResource("dataSet2")).getPath();
        String result = Packer.pack(path);
        assertEquals(result, "1,2,3");
    }

    @Test
    void pack2() throws APIException {
        String path = Objects.requireNonNull(ClassLoader.getSystemClassLoader().getResource("dataSet")).getPath();
        String result = Packer.pack(path);
        assertEquals(result, "4\r\n" +
                "-\r\n" +
                "2,7\r\n" +
                "6,9");
    }

    @Test
    void packEmptyResult() throws APIException {
        String path = Objects.requireNonNull(ClassLoader.getSystemClassLoader().getResource("dataset3")).getPath();
        String result = Packer.pack(path);
        assertEquals(result, "-");
    }

    @Test
    void pack3() throws APIException {
        String path = Objects.requireNonNull(ClassLoader.getSystemClassLoader().getResource("dataset4")).getPath();
        String result = Packer.pack(path);
        assertEquals(result, "2,7");
    }
    @Test
    void packInvalidDate()  {
        String path = Objects.requireNonNull(ClassLoader.getSystemClassLoader().getResource("dataSet-invalid-data")).getPath();
        assertThrows(APIException.class, () -> Packer.pack(path));
    }

    @Test
    void packEmptyLine() {
        String path = Objects.requireNonNull(ClassLoader.getSystemClassLoader().getResource("dataset-empty-line")).getPath();
        assertThrows(APIException.class, () -> Packer.pack(path));
    }

    @Test
    void packEmptyLineBetweenRows() {
        String path = Objects.requireNonNull(ClassLoader.getSystemClassLoader().getResource("dataset-empty-line-between-rows")).getPath();
        assertThrows(APIException.class, () -> Packer.pack(path));
    }

    @Test
    void createResultSet() {
        ResultSet[][][] resultSet = Packer.createResultSet(2, 2, 2);
        for (int i = 0; i <= 2; i++) {
            for (int j = 0; j <= 2; j++) {
                for (int k = 0; k <= 2; k++) {
                    assertNotNull(resultSet[i][j][k], String.format("invalid initialization index: (%d, %d, %d)", i, j, k));
                }
            }
        }
    }

    @Test
    void calculateMaxCostFailToPickupItem() {
        FileItem fileItem=new FileItem(8);
        fileItem.addItem("(1,12,€2)");
        fileItem.addItem("(2,9,€7)");
        ResultSet resultSet = Packer.calculateMaxCost(fileItem);
        assertEquals(resultSet.getTotal(),0);
        assertNotNull(resultSet.getItems());
        assertEquals(resultSet.getItems().size(),0);
    }

    @Test
    void calculateMaxCostLimitedItemCountTest() {
        Packer.maxElementCount=2;
        FileItem fileItem=new FileItem(8);
        fileItem.addItem("(1,2,€2)");
        fileItem.addItem("(2,3,€7)");
        fileItem.addItem("(3,2,€1)");
        fileItem.addItem("(4,2,€5)");
        ResultSet resultSet = Packer.calculateMaxCost(fileItem);
        assertEquals(resultSet.getTotal(),12);
        assertNotNull(resultSet.getItems());
        assertFalse(resultSet.getItems().isEmpty());
        assertEquals(resultSet.getItems().size(),2);
        assertTrue(resultSet.getItems().contains(fileItem.getItems().get(1)));
        assertTrue(resultSet.getItems().contains(fileItem.getItems().get(3)));
    }

    @Test
    public void getMaxStep1() {
        Item currentItem = new Item("(1,2,€2)");
        ResultSet resultSet = new ResultSet();
        ResultSet excludingResultSet = new ResultSet();
        ResultSet result = Packer.getMax(currentItem, resultSet, excludingResultSet);
        assertEquals(result.getTotal(), 2);
        assertEquals(result.getItems().size(), 1);
        assertTrue(result.getItems().contains(currentItem));
    }

    @Test
    public void getMaxStep2() {
        Item currentItem = new Item("(2,3,€7)");
        Item excludingItem = new Item("(1,2,€2)");
        ResultSet resultSet = new ResultSet();
        ResultSet excludingResultSet = new ResultSet();
        excludingResultSet.setTotal(2);
        excludingResultSet.getItems().add(excludingItem);
        ResultSet result = Packer.getMax(currentItem, resultSet, excludingResultSet);
        assertEquals(result.getTotal(), 7);
        assertEquals(result.getItems().size(), 1);
        assertTrue(result.getItems().contains(currentItem));
    }

    @Test
    public void getMaxStep3() {
        Item currentItem = new Item("(2,3,€7)");
        Item excludingItem = new Item("(1,2,€2)");
        ResultSet excludingResultSet = new ResultSet();
        ResultSet remainingResultSet = new ResultSet();
        excludingResultSet.setTotal(2);
        excludingResultSet.getItems().add(excludingItem);
        remainingResultSet.getItems().add(excludingItem);
        ResultSet result = Packer.getMax(currentItem, remainingResultSet, excludingResultSet);
        assertEquals(result.getTotal(), 7);
        assertEquals(result.getItems().size(), 2);
        assertTrue(result.getItems().contains(currentItem));
        assertTrue(result.getItems().contains(excludingItem));
    }

    @Test
    public void getMaxStep4() {
        Item currentItem = new Item("(3,2,€1)");
        Item excludingItem = new Item("(1,2,€2)");
        ResultSet excludingResultSet = new ResultSet();
        ResultSet remainingResultSet = new ResultSet();
        excludingResultSet.setTotal(2);
        excludingResultSet.getItems().add(excludingItem);
        ResultSet result = Packer.getMax(currentItem, remainingResultSet, excludingResultSet);
        assertEquals(result.getTotal(), 2);
        assertEquals(result.getItems().size(), 1);
        assertTrue(result.getItems().contains(excludingItem));
    }
}
