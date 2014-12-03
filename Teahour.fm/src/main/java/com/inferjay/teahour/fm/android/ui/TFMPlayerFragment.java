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
package com.inferjay.teahour.fm.android.ui;

import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.inferjay.teahour.fm.android.R;
import com.inferjay.teahour.fm.android.service.TFMFeedDataService;
import com.inferjay.teahour.fm.android.ui.widget.CircleImageView;
import com.inferjay.teahour.fm.android.ui.widget.htmltextview.HtmlTextView;
import com.inferjay.teahour.fm.android.ui.widget.htmltextview.TFMLinkMovementMethod;
import com.inferjay.teahour.fm.android.ui.widget.htmltextview.TFMLinkMovementMethod.OnClickLinkMovemethodListener;
import com.inferjay.teahour.fm.android.utils.TFMLinkUtil;
import com.squareup.picasso.Picasso;


/**
 *
 * @since 2014-8-22 下午8:35:07
 * @author inferjay
 * @version 1.0
 */
public class TFMPlayerFragment extends Fragment implements OnClickLinkMovemethodListener{

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    
	private TextView	mHostNameView;	
	private TextView	mGuestsNamesView;	
	private TextView	mTopicContentView;
	private HtmlTextView	mShowNotesView;
	private HtmlTextView	mMoreResourcesView;
	private HorizontalScrollView mHostAvatarBarHScrollView;
    private LinearLayout mHostAvatarBarLayout;
	
	private InitPlayBarDetailViewCallback mCreatePlayBarCallback;

    public TFMPlayerFragment() {
    }
    
    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static TFMPlayerFragment newInstance(int sectionNumber) {
    	TFMPlayerFragment fragment = new TFMPlayerFragment();
        fragment.updateArguments(sectionNumber);
        return fragment;
    }


    public void updateArguments(int sectionNumber) {
    	Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        this.setArguments(getArguments());
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_teahour_fm_player, container, false); 
        mHostNameView = (TextView) rootView.findViewById(R.id.tv_host_content);	
    	mGuestsNamesView = (TextView) rootView.findViewById(R.id.tv_guests_content);	
    	mTopicContentView = (TextView) rootView.findViewById(R.id.tv_tipcs_content);
    	mShowNotesView = (HtmlTextView) rootView.findViewById(R.id.tv_show_notes_content);
    	mMoreResourcesView = (HtmlTextView) rootView.findViewById(R.id.tv_more_resources_content);
    	mHostAvatarBarHScrollView = (HorizontalScrollView) rootView.findViewById(R.id.hs_fm_host_avatar_bar);
    	mHostAvatarBarLayout = (LinearLayout) rootView.findViewById(R.id.ll_fm_host_avatar_bar);
    	mCreatePlayBarCallback.initPlayBarDetailView(rootView);
        return rootView;
    }
    
    @Override
    public void onAttach(Activity activity) {
    	super.onAttach(activity);
    	mCreatePlayBarCallback = (InitPlayBarDetailViewCallback) activity;
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
    	super.onActivityCreated(savedInstanceState);
    	
    	int index = getArguments() != null ? getArguments().getInt(ARG_SECTION_NUMBER) : 0;
    	updateFeedDataToView(index);
    }

	public void updateFeedDataToView(int index) {
		mHostNameView.setText(TFMFeedDataService.getInstance().getFeedItemAuthorByIndex(index));
        mGuestsNamesView.setText(TFMFeedDataService.getInstance().getFeedItemGuestNamesByIndex(index));		
        mTopicContentView.setText(TFMFeedDataService.getInstance().getFeedItemKeywordsByIndex(index));
        mShowNotesView.setHtmlFromString(TFMFeedDataService.getInstance().getFeedItemSummaryByIndex(index), false);
        mShowNotesView.setMovementMethod(getTeahourFMLinkMovementMethod());
        String resources = TFMFeedDataService.getInstance().getFeedItemMoreResources(index);
        if (!TextUtils.isEmpty(resources)) {
	        mMoreResourcesView.setHtmlFromString(resources, false);
	        mMoreResourcesView.setMovementMethod(getTeahourFMLinkMovementMethod());	        		
        }
        updateHostAndGuestAvatar(index);
	}
	

	private void updateHostAndGuestAvatar(int index) {
		int existCount = mHostAvatarBarLayout.getChildCount();
		List<String> avatarUrlList = TFMFeedDataService.getInstance().getFeedItemAuthorAvatarURLs(index);
		int count = avatarUrlList.size();
		
		
		if (count > 0) {
			if (!mHostAvatarBarHScrollView.isShown()) {
				mHostAvatarBarHScrollView.setVisibility(View.VISIBLE);
			}
			if (existCount > 0) {
				mHostAvatarBarLayout.removeAllViews();
			}
			addHostAndGuestAvatarImageView(avatarUrlList);
		} else {
			mHostAvatarBarHScrollView.setVisibility(View.GONE);
		}
	}


	private void addHostAndGuestAvatarImageView(List<String> urlList) {
		boolean isAddHost = false;
		for (String urlItem : urlList) {
			if (!TextUtils.isEmpty(urlItem)) {
				ImageView avatarView = null;
				if (!isAddHost) {
					isAddHost = true;
					View hostAvatarView = LayoutInflater.from(getActivity()).inflate(
										R.layout.item_host_avatar_layout, mHostAvatarBarLayout, false);
					avatarView = (CircleImageView)hostAvatarView.findViewById(R.id.civ_host_avatar);
					mHostAvatarBarLayout.addView(hostAvatarView);
				} else {
					avatarView = (CircleImageView) LayoutInflater.from(getActivity()).inflate(
							R.layout.item_guest_avatar_layout, mHostAvatarBarLayout, false);
					mHostAvatarBarLayout.addView(avatarView);
				}
				Picasso.with(getActivity()).load(urlItem).placeholder(R.drawable.ic_user_default)
						.error(R.drawable.ic_user_default).into(avatarView);
			}
		}
	}

	private TFMLinkMovementMethod getTeahourFMLinkMovementMethod() {
		return TFMLinkMovementMethod.getInstance().setOnClickLinkCallback(this);
	}
    
	@Override
	public void onClickLink(String link) {
		TFMLinkUtil.openLinkWithInlinBrowser(getActivity(), link);
	}
	
	public interface InitPlayBarDetailViewCallback {
		public void initPlayBarDetailView(View rootView);
	}
	
	public void setCreatePlayBarCallback(InitPlayBarDetailViewCallback callback) {
		mCreatePlayBarCallback = callback;
	}

}
