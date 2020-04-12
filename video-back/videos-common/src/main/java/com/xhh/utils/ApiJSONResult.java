package com.xhh.utils;

/**
 * @description：调用接口时json返回
 *   属性：
 *      1.status:接口使用状态
 *      2.msg:调用接口申明参数
 *      3.data:成功后返回的对象
 *   方法:
 *      1.build(Integer status, String msg, Object data):构建该对象
 *      2.ok():不返回数据
 *      3.ok(Object data):返回数据
 *
 *      200：表示成功
 *      500：表示错误，错误信息在msg字段中
 *      501：bean验证错误，不管多少个错误都以map形式返回
 *      502：拦截器拦截到用户token出错
 *      55：异常抛出信息
 *
 * @author: weiXhh
 * @create: 2020-03-31 11:37
 **/
public class ApiJSONResult {
    private Integer status;
    private String msg;
    private Object data;

    public static ApiJSONResult build(Integer status, String msg, Object data){
        return new ApiJSONResult(status, msg, data);
    }
    public static ApiJSONResult ok(){
        return new ApiJSONResult(null);
    }
    public static ApiJSONResult ok(Object data){
        return new ApiJSONResult(data);
    }
    public static ApiJSONResult errorMsg(String msg){
        return new ApiJSONResult(500, msg, null);
    }
    public static ApiJSONResult errorMap(Object data){
        return new ApiJSONResult(501, "error", data);
    }
    public static ApiJSONResult errorTokenMsg(String msg){
        return new ApiJSONResult(502, msg, null);
    }
    public static ApiJSONResult errorException(String msg){
        return new ApiJSONResult(555,msg,null);
    }

    public ApiJSONResult() {
    }

    public ApiJSONResult(Object data) {
        this.status = 200;
        this.msg = "OK";
        this.data = data;
    }

    public ApiJSONResult(Integer status, String msg, Object data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public Boolean isOk(){
        return this.status == 200;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
