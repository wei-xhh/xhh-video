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
public class VideoMergeAudio {

    private String ffmpegTXT;

    public VideoMergeAudio(String ffmpegTXT) {
        this.ffmpegTXT = ffmpegTXT;
    }
    // 消除原声
    public String removeOriginalSound(String videoInputPath, String videoOutputPath) throws IOException{
//        ffmpeg.exe -i video.mp4 -vcodec copy -an new.mp4
        List<String> comList = new ArrayList<>();
        comList.add(ffmpegTXT);
        comList.add("-i");
        comList.add(videoInputPath);
        comList.add("-vcodec");
        comList.add("copy");
        comList.add("-an");
        comList.add(videoOutputPath);

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
        return videoOutputPath;
    }

    // 转换
    public void convert(String videoInputPath, String audioInputPath,
                        double seconds, String videoOutputPath) throws IOException {
//        ffmpeg.exe -i new.mp4 -i pm.m4r -t 4 -y new1.mp4
        List<String> comList = new ArrayList<>();
        comList.add(ffmpegTXT);
        comList.add("-i");
        comList.add(videoInputPath);
        comList.add("-i");
        comList.add(audioInputPath);
        comList.add("-t");
        comList.add(String.valueOf(seconds));
        comList.add("-y");
        comList.add(videoOutputPath);
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
        VideoMergeAudio fFmpegTest = new VideoMergeAudio("F:\\ffmpeg\\bin\\ffmpeg.exe");
        try {
            String removeSoundPath = fFmpegTest.removeOriginalSound("E:\\workspace-wxscx\\video.mp4", "E:\\workspace-video\\new.mp4");
            System.out.println(removeSoundPath);
            fFmpegTest.convert(removeSoundPath,"E:\\workspace-video\\videos-userFace\\bgm\\pm.m4r",3,"E:\\workspace-video\\new1.mp4");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
