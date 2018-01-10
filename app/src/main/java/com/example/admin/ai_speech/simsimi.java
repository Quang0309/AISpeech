package com.example.admin.ai_speech;

/**
 * Created by ADMIN on 19-Jul-17.
 */

public class simsimi {
    public String response;
    public String msg;
    public int result;
    public int id;

    public simsimi(String response, String msg, int result, int id) {
        this.response = response;
        this.msg = msg;
        this.result = result;
        this.id = id;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
