package com.demo.flow.net;


public class VolleyManager {

    public static String getCookies() {
        String sessionId = null;
        String cookie = null;

        if (sessionId != null) {
            cookie = "WEILIAN2_SESSID=" + sessionId;
        }

        return cookie;
    }
}
