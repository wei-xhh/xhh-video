package com.xhh.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @description
 * @author: weiXhh
 * @create: 2020-04-04 19:01
 **/
public class FetchVideoCover {

    private String ffmpegTXT;

    public FetchVideoCover(String ffmpegTXT) {
        this.ffmpegTXT = ffmpegTXT;
    }
    // 截取视频第一秒的图片
    public void getCover(String videoInputPath, String coverOutputPath) throws IOException{
//        ffmpeg.exe -ss 00:00:01 -i video.mp4 -vframes 1 new.jpg
        List<String> comList = new ArrayList<>();
        comList.add(ffmpegTXT);
        comList.add("-ss");
        comList.add("00:00:01");
        comList.add("-i");
        comList.add(videoInputPath);
        comList.add("-vframes");
        comList.add("1");
        comList.add(coverOutputPath);

        // 执行命令
        ProcessBuilder processBuilder = new ProcessBuilder(comList);
        Process start = processBuilder.start();
        InputStream errorStream = start.getErrorStream();
        InputStreamReader inputStreamReader = new InputStreamReader(errorStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String line = "";
        while((line = bufferedReader.readLine()) != null){
        }
        if(bufferedReader != null){
            bufferedReader.close();
        }
        if(inputStreamReader != null){
            inputStreamReader.close();
        }
        if(errorStream != null){
            errorStream.close();
        }
    }

    // window测试
    public static void main(String[] args) {
        FetchVideoCover fFmpegTest = new FetchVideoCover("F:\\ffmpeg\\bin\\ffmpeg.exe");
        try {
            fFmpegTest.getCover("E:\\workspace-wxscx\\video.mp4", "E:\\workspace-video\\hello.jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
