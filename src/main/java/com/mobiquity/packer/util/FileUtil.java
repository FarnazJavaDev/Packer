package com.mobiquity.packer.util;

import com.mobiquity.packer.Packer;
import com.mobiquity.packer.exception.InvalidInputException;
import com.mobiquity.packer.model.FileItem;
import com.mobiquity.packer.exception.APIException;
import com.mobiquity.packer.exception.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileUtil{
    private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);

    /**
     * @param path
     * @return List of valid FileItems
     * @throws APIException in case of invalid input and validation violation
     * @throws IOException any problem in accessing file
     */
    public static List<FileItem> readFile(String path) throws APIException, IOException {
        var lines = Files.lines(Paths.get(path));
        return getFileItems(lines);
    }

    /**
     * Converts file data to FileItem object which contains a list of candidate items and maximum capacity of package
     * @param lines lines of file
     * @return List of FileItems
     * @throws APIException in case of invalid input and validation violation
     */
    static List<FileItem> getFileItems(Stream<String> lines) throws APIException {
        try {
            List<FileItem> fileItems = lines.map(s -> {
                FileItem fileItem;
                String[] split = s.split(":");
                if(split.length != 2)
                    throw new InvalidInputException("Invalid input");
                fileItem = new FileItem(Integer.parseInt(split[0].strip()));
                Arrays.stream(split[1].trim().split(" +")).forEach(fileItem::addItem);
                fileItem.normalize();
                return fileItem;
            }).collect(Collectors.toList());
            return fileItems;
        }catch (ValidationException | InvalidInputException e){
            logger.error("Constrain violation error :{}",e.getMessage());
            throw new APIException(e.getMessage(), e);
        }
    }
}