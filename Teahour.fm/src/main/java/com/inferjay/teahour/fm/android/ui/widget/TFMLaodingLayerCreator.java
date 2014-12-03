package com.inferjay.teahour.fm.android.ui.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.inferjay.teahour.fm.android.R;
import com.lsjwzh.loadingeverywhere.GenericStatusLayout;

/**
 * Created by inferjay on 9/29/14.
 */
public class TFMLaodingLayerCreator implements GenericStatusLayout.ILayerCreator{
    Context mContext;
    public TFMLaodingLayerCreator(Context context) {
        mContext = context;
    }
    @Override
    public View createLoadingLayer() {
        return LayoutInflater.from(mContext).inflate(R.layout.loading, null);
    }

    @Override
    public View createEmptyLayer() {
        return LayoutInflater.from(mContext).inflate(R.layout.empty, null);
    }

    @Override
    public View createErrorLayer() {
        return LayoutInflater.from(mContext).inflate(R.layout.error, null);
    }
}
