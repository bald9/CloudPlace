package com.bald9.utils;

import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author bald9
 * @create 2022-02-05 6:05
 */
public class ClassFile {
    public static void main(String[] args) {
        write("src/main/java/resources/","jjjjjjjffffff");
        String nnnmmhhh =(String) read("nnnmmhhh");
        System.out.println(nnnmmhhh);
    }
    public static void write(Path path, Object o){
        OutputStream outputStream = null;
        ObjectOutputStream objectOutputStream=null;
        try {
            outputStream = Files.newOutputStream(path);
            objectOutputStream=new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(o);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(objectOutputStream!=null){
                try {
                    objectOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(outputStream!=null){
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public static <T> @Nullable T read(Path path){
        InputStream inputStream = null;
        ObjectInputStream objectInputStream=null;
        try {
            inputStream = Files.newInputStream(path);
            objectInputStream=new ObjectInputStream(inputStream);
            try {
                Object o = objectInputStream.readObject();
                return (T)o;
            } catch (ClassNotFoundException e) {
                //e.printStackTrace();
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(objectInputStream!=null){
                try {
                    objectInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(inputStream!=null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
    public static void write(String path,Object o){
        FileOutputStream fileStream=null;
        //创建对象输出流
        ObjectOutputStream oStream=null;
        //可能会产生异常
        try {
            File file = new File(path);
            //打开文件
            fileStream=new FileOutputStream(file);
            //创建对象输出流
            oStream=new ObjectOutputStream(fileStream);

            oStream.writeObject(o);

        }catch (Exception e) {
            e.printStackTrace();// : handle exception
            throw new RuntimeException("写入文件失败",e);
        }finally {
            //关闭对象输出流
            if(oStream!=null){
                try {
                    oStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(fileStream!=null){
                try {
                    fileStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public static Object read(String path){
        FileInputStream fileStream=null;
        ObjectInputStream iStream=null;
        try {
            //打开文件
             fileStream=new FileInputStream(path);
            //创建对象输入流
             iStream=new ObjectInputStream(fileStream);
            //读取对象
            Object oneObject=iStream.readObject();

            return oneObject;


        } catch (Exception e) {
            e.printStackTrace();// TODO: handle exception
            throw new RuntimeException("读取文件失败",e);
        }finally {
            //关闭对象输出流
            if(fileStream!=null){
                try {
                    fileStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(iStream!=null){
                try {
                    iStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
