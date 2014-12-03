package com.inferjay.teahour.fm.android.ui.callback;

import java.util.ArrayList;

/**
 * Created by inferjay on 11/2/14.
 */
public interface UpdateDrawerListStateCallback {
    public void showDrawerListEmptyDataState();
    public void showDrawerListFillWithDataState(ArrayList<String> feeddatas);
}
