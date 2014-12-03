package com.inferjay.teahour.fm.android.net;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.http.protocol.HTTP;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;

public abstract class TFMBasePostRequest<T> extends Request<T> {

    /** Charset for request. */
    protected static final String PROTOCOL_CHARSET 	= HTTP.UTF_8;


    private Listener<T> mListener;

    protected Map<String, String> mRequestBody;

    public TFMBasePostRequest(String url, Map<String, String> requestBody, Listener<T> listener,
                       ErrorListener errorListener) {
        super(Method.POST, url, errorListener);
        mListener = listener;
        mRequestBody = requestBody;
    }

    @Override
    protected void deliverResponse(T response) {
        mListener.onResponse(response);
    }

    @Override
    abstract protected Response<T> parseNetworkResponse(NetworkResponse response);

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> baseHeaderMap = new HashMap<String, String>();
        baseHeaderMap.put("Content-Type", getBodyContentType());
        return baseHeaderMap;
    }

    @Override
    public byte[] getBody() {
        byte[] bodyByte = null;
        if (mRequestBody != null && mRequestBody.size() > 0) {
            Set<String> keySet = mRequestBody.keySet();
            StringBuilder bodyBuilder = new StringBuilder();
            for (String key : keySet) {
                String value = mRequestBody.get(key);
                bodyBuilder.append(key);
                bodyBuilder.append("=");
                bodyBuilder.append(value);
                bodyBuilder.append("&");
            }
            try {
                bodyByte = bodyBuilder.deleteCharAt(bodyBuilder.length() - 1).toString().getBytes(getParamsEncoding());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return bodyByte;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return mRequestBody;
    }
}
