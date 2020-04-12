package com.xhh.utils;

import java.io.File;

/**
 * @description
 * @author: weiXhh
 * @create: 2020-04-02 15:35
 **/
public class FileDeleteUtils {
    public static void deleteFile(File file){
        if(file.exists()){
            // 数组中只有一个jpg,该路径下
            File[] files = file.listFiles();
            files[0].delete();
        }
    }
}
