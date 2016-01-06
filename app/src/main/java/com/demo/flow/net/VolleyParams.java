package com.demo.flow.net;


import java.util.HashMap;

public class VolleyParams extends HashMap<String, String> {

    /**
     * 如果放入了null参数，Volley就会抛出空指针异常
     * @param key
     * @param value
     * @return
     */
    @Override
    public String put(String key, String value) {
        if (key == null || value == null) {
            return null;
        }

        return super.put(key, value);
    }
}
