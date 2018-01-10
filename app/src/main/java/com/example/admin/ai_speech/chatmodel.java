package com.example.admin.ai_speech;

/**
 * Created by ADMIN on 28-Aug-17.
 */

public class chatmodel {
    public String message;
    public boolean isSend;

    public chatmodel(String message, boolean isSend) {
        this.message = message;
        this.isSend = isSend;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSend() {
        return isSend;
    }

    public void setSend(boolean send) {
        isSend = send;
    }
}
