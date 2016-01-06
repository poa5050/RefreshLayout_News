package com.demo.flow.net;


import com.android.volley.DefaultRetryPolicy;

public class VolleyRetryPolicy {

    public static DefaultRetryPolicy getDefault() {
        return new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 50,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
    }
}
