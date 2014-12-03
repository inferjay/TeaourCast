/**
 *   Copyright 2014 inferjay (http://www.inferjay.com)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.inferjay.teahour.fm.android.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.inferjay.teahour.fm.android.R;

public class TFMNavDrawerListAdapter extends BaseAdapter {
	
	private ArrayList<String> mRssItemTitleList;
	
	private LayoutInflater mInflater;
	
	private ColorStateList mTitleDfaultColor;
	private ColorStateList mTitleSelectColor;
	
	private ListView mListView;
	
	public TFMNavDrawerListAdapter (Context context, ListView listView,  ArrayList<String> list) {
		this.mListView = listView;
		mRssItemTitleList = list;
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mTitleDfaultColor = context.getResources().getColorStateList(R.color.color_drawer_list_item_text_normal);
		mTitleSelectColor = context.getResources().getColorStateList(R.color.color_drawer_list_item_text_select);
	}

	@Override
	public int getCount() {
		return mRssItemTitleList != null ? mRssItemTitleList.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		return mRssItemTitleList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ItemViewHolder itemViewHolder;
		if (convertView == null) {
			itemViewHolder = new ItemViewHolder();
			itemViewHolder.itemView = (TextView)mInflater.inflate(R.layout.item_nav_drawer_list, parent, false);
			convertView = itemViewHolder.itemView;
			convertView.setTag(itemViewHolder);
		} else {
			itemViewHolder = (ItemViewHolder) convertView.getTag(); 
		}
		bindDataToView(convertView, itemViewHolder, position);
		return convertView;
	}
	
	
	private void bindDataToView(View convertView, ItemViewHolder itemViewHolder, int position) {
		itemViewHolder.itemView.setText(mRssItemTitleList.get(position));
		if (mListView.isItemChecked(position)) {
			itemViewHolder.itemView.setTextColor(mTitleSelectColor);
//            convertView.setBackgroundResource(R.drawable.nav_drawer_list_item_gray_rectangle);
		} else {
			itemViewHolder.itemView.setTextColor(mTitleDfaultColor);
//            convertView.setBackgroundDrawable(null);
		}

	}



	static class ItemViewHolder {
		TextView itemView;		
	}
}
