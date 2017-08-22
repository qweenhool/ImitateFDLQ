package com.ydl.imitatefdlq.entity;

/**
 * Created by qweenhool on 2017/8/21.
 */

public class ResultJson {
    private int errorCode; //错误代码
    private String errorString; //错误原因
    private String result; //成功失败
    private String data; //返回的数据

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorString() {
        return errorString;
    }

    public void setErrorString(String errorString) {
        this.errorString = errorString;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

}
