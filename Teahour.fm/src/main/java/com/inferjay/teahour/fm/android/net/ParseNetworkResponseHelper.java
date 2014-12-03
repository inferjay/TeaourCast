package com.inferjay.teahour.fm.android.net;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import org.apache.http.protocol.HTTP;

import java.io.UnsupportedEncodingException;
import java.nio.charset.UnsupportedCharsetException;

/**
 * Created by ZHC on 11/30/14.
 */
public class ParseNetworkResponseHelper {
    public static Response<String> parseRespons(NetworkResponse response) {
        String parsed;
        try {
            parsed = new String(response.data, HTTP.UTF_8);
        } catch (UnsupportedEncodingException e) {
            parsed = new String(response.data);
        }
        return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));
    }
}
