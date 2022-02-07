package com.bald9.utils.file;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;

/**
 * @author bald9
 * @create 2022-02-06 23:50
 */
public class FilesPatch {
    public static Path createForce(Path path) throws IOException {
        Path realPath=path.toAbsolutePath().normalize();
        if(!Files.exists(realPath)){
            if(!Files.isDirectory(realPath)){
                Path realDir=realPath.getParent();
                if(!Files.exists(realDir)){
                    Files.createDirectories(realDir);
                }
            }
            Files.createFile(realPath);
        }
        return realPath;
    }
    public static Path createFileForce(Path path) throws IOException {
        Path realPath=path.toAbsolutePath().normalize();
        if(Files.isDirectory(realPath)){
            throw new FileAlreadyExistsException("无法创建该文件（可能是已有同名的文件夹）");
        }
        if(!Files.exists(realPath)) {
            Path realDir = realPath.getParent();
            if (!Files.exists(realDir)) {
                Files.createDirectories(realDir);
            }
            Files.createFile(realPath);
        }
        return realPath;
    }
    public static Path createDirectoryForce(Path path) throws IOException {
        Path realPath=path.toAbsolutePath().normalize();
        if(!Files.isDirectory(realPath)){
            throw new FileAlreadyExistsException("无法创建该文件夹（可能是已有同名的文件夹）");
        }
        if(!Files.exists(realPath)){
            Files.createFile(realPath);
        }
        return realPath;
    }
}
