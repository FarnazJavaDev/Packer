package com.mobiquity.packer;

import com.mobiquity.packer.exception.APIException;
import com.mobiquity.packer.model.FileItem;
import com.mobiquity.packer.model.Item;
import com.mobiquity.packer.model.ResultSet;
import com.mobiquity.packer.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class is the entry point of the library
 */
public class Packer {
    static int maxElementCount = 15;
    private static final Logger logger = LoggerFactory.getLogger(Packer.class);
    /**
     * @param filePath absolute path of a file
     * @return index of added items to package per file line
     * @throws APIException in case of invalid input and validation violation
     */
    public static String pack(String filePath) throws APIException {
        try {
            logger.info("packer method is called! The input filePath is: {}",filePath);
            List<FileItem> fileItems = FileUtil.readFile(filePath);
            var resultSetStream = fileItems.parallelStream().map(Packer::calculateMaxCost);
            var result = resultSetStream.map(ResultSet::toString).collect(Collectors.joining("\r\n"));
            logger.info("result of packer method is : {}",result);
            return result;
        } catch (IOException e) {
            logger.error("Error in packing items: " + e.getMessage());
            throw new APIException("Could not pack Items", e);
        }
    }

    /**
     * This method calculates the max cost for each cell of 3D matrix
     Algorithm:

   -  If  (Item weight > package capacity),
     We cannot pick the item in this case as it is violating the constraint of Max Package Capacity.
     So we will not pick the item in this case and max cost will remain same as cost we got in previous step without working on this item.

   -  If  (Item weight <= Package capacity),
     Now there are 2 options open, whether we should pick the item or should not pick it.
     What we will do is, Check the cost we can earn by,
        1.  Picking the item
        2.  Not picking the item.
     Compare cost in both the case and in which ever case the cost is maximum, we will choose that as Max Cost we can get at this point.
     (This Max cost we got is either by including the item or excluding it.)

     * @param currentItem candidate item to check that should be picked up or not
     * @param remainingCapacityWeight cost value which is related to remaining capacity weight of package
     * @param excludingCurrentItem cost value by excluding current item
     * @return result of calculated max cost
     */
    static ResultSet getMax(Item currentItem, ResultSet remainingCapacityWeight, ResultSet excludingCurrentItem) {
        int max = Math.max(currentItem.getCost() + remainingCapacityWeight.getTotal(), excludingCurrentItem.getTotal());
        ResultSet resultSet = new ResultSet();
        resultSet.setTotal(max);
        if (currentItem.getCost() + remainingCapacityWeight.getTotal() > excludingCurrentItem.getTotal()) {
            resultSet.addItem(currentItem);
            resultSet.setTotal(max);
            remainingCapacityWeight.getItems().forEach(resultSet::addItem);
        } else {
            resultSet = excludingCurrentItem;
        }
        return resultSet;
    }

    /**
     * @param itemCount
     * @param capacity
     * @param maxElementCount
     * @return returns initialized ResultSet to avoiding NPE
     */
    static ResultSet[][][] createResultSet(int itemCount, Integer capacity, int maxElementCount) {
        ResultSet[][][] resultSets = new ResultSet[itemCount + 1][(capacity + 1)][maxElementCount + 1];
        for (int i = 0; i <= itemCount; i++) {
            for (int j = 0; j <= capacity; j++) {
                for (int k = 0; k <= maxElementCount; k++) {

                    resultSets[i][j][k] = new ResultSet();
                }
            }
        }
        return resultSets;
    }


    /**
     * Solution is based on dynamic programming approach
     * cost earned for each type of input will calculate and results of each calculation will store.
     * So that whenever same type of input calculation is required further in Algorithm,
     * no need to calculate it again and, we can directly refer to stored result of previous calculation.
     *
     * a 3-dimensional matrix result[N][W][K] is considered, where N is the number of elements, W is the maximum weight
     * capacity and K is the maximum number of items allowed in the package
     * a state result[i][j][k] is defined where i shows that we are considering the ith element, j shows the current
     * weight filled, and k shows the number of items filled until now.
     * For every entry result[i][j][k], the cost is either that of the previous entry (when the current entry is not included)
     * or the cost of the current item added to that of the previous step
     * (when the current item is selected).

     * @param fileItem
     * @return result of max cost for all given items
     */
    static ResultSet calculateMaxCost(FileItem fileItem) {
        int itemCount = fileItem.getItems().size();
        Integer capacity = fileItem.getCapacity();
        int allowedItemCount = Math.min(maxElementCount, itemCount);
        ResultSet[][][] costCalcResult = createResultSet(itemCount, capacity, allowedItemCount);

        // for each item
        for (int i = 1; i <= itemCount; i++) {
            // For each possible weight
            for (int j = 1; j <= capacity; j++) {
                // For each case where the total elements are less than the constraint
                for (int k = 1; k <= allowedItemCount; k++) {
                    Item currentItem = fileItem.getItems().get(i - 1);
                    if (j >= currentItem.getWeight()) {
                        //calculating max cost of each state
                        costCalcResult[i][j][k] = getMax(currentItem, costCalcResult[i - 1][(j - currentItem.getWeight())][k - 1], costCalcResult[i - 1][j][k]);
                    } else {
                        costCalcResult[i][j][k] = costCalcResult[i - 1][j][k];
                    }
                }
            }
        }

        return costCalcResult[itemCount][capacity][allowedItemCount];
    }
}
