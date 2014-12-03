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

import java.lang.ref.WeakReference;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.*;
import android.os.Process;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.inferjay.teahour.fm.android.R;
import com.inferjay.teahour.fm.android.constant.TFMConstant;
import com.inferjay.teahour.fm.android.service.TFMFeedDataService;
import com.inferjay.teahour.fm.android.service.TFMFeedDataService.OnLoadFeedDataListener;
import com.inferjay.teahour.fm.android.service.TFMPlayService;
import com.inferjay.teahour.fm.android.service.TFMPlayService.TeahourFMPlayBinder;
import com.inferjay.teahour.fm.android.ui.TFMPlayerFragment.InitPlayBarDetailViewCallback;
import com.inferjay.teahour.fm.android.ui.callback.UpdateDrawerListStateCallback;
import com.inferjay.teahour.fm.android.ui.widget.TFMLaodingLayerCreator;
import com.lsjwzh.loadingeverywhere.GenericStatusLayout;
import com.inferjay.teahour.fm.android.utils.TFMLinkUtil;
import com.inferjay.teahour.fm.android.utils.TFMTimeUtils;
import com.umeng.update.UmengUpdateAgent;



public class TFMMainActivity extends TFMBaseActivity
        implements TFMNavigationDrawerFragment.NavigationDrawerCallbacks, 
        OnClickListener , OnLoadFeedDataListener, InitPlayBarDetailViewCallback, OnSeekBarChangeListener{

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private TFMNavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mActionBarTitle;	
	private ImageButton mPlayButton;	
	private ImageButton mPlayNextButton;
	private ImageButton mPlayPreviousButton;	
	private SeekBar		mPlaySeekBar;	
	private TextView	mPlayStartTimeView;	
	private TextView	mPlayEndTimeView;
	private GenericStatusLayout mGenericStatusLayout;
	
	private DrawerLayout mDrawerLayout;
	
	private TFMPlayerFragment mTFMPlayFragment;
	
	private int mCurrentPlayIndex;
	private int mFeedItemTotalCont;
	private Intent mServiceIntent;
	
	private RefreshPlayBarViewHandler mRefreshPlayBarViewHandler;
		
	private boolean isBindService;
	
	private TeahourFMPlayBinder mPlayBinder;

    private boolean isPlayFinish;

	private ServiceConnection mServiceConnection = new ServiceConnection() {
		
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			mPlayBinder = (TeahourFMPlayBinder) service;
			mPlayBinder.setServiceHander(mRefreshPlayBarViewHandler);
			isBindService = true;
		}
		
		@Override
		public void onServiceDisconnected(ComponentName name) {			
		}
	};

    private UpdateDrawerListStateCallback mUpdateDrawerListStateCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teahour_fm_main);
        UmengUpdateAgent.update(this);
        init();
        loadFeedData();
    }

    private void init() {
        mRefreshPlayBarViewHandler = new RefreshPlayBarViewHandler(this);
        mServiceIntent = new Intent(this, TFMPlayService.class);
        mNavigationDrawerFragment = (TFMNavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mUpdateDrawerListStateCallback = mNavigationDrawerFragment;
        mActionBarTitle = getTitle();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mGenericStatusLayout = new GenericStatusLayout(this);
        mGenericStatusLayout.setLayerCreator(new TFMLaodingLayerCreator(this));
        mGenericStatusLayout.attachTo(findViewById(R.id.container));
        // Set up the drawer.
        mNavigationDrawerFragment.setUp(R.id.navigation_drawer,mDrawerLayout);
        mGenericStatusLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mGenericStatusLayout.hideEmpty();
                mGenericStatusLayout.hideError();
                loadFeedData();
            }
        });
    }

	@Override
	public void initPlayBarDetailView(View rootView) {
		mPlayButton 		= (ImageButton) rootView.findViewById(R.id.ibtn_play);
    	mPlayNextButton 	= (ImageButton) rootView.findViewById(R.id.ibtn_play_next);
    	mPlayPreviousButton = (ImageButton) rootView.findViewById(R.id.ibtn_play_previous);
    	mPlaySeekBar 		= (SeekBar) rootView.findViewById(R.id.sb_paly_progress);
    	mPlayStartTimeView 	= (TextView) rootView.findViewById(R.id.tv_paly_start_time);
    	mPlayEndTimeView 	= (TextView) rootView.findViewById(R.id.tv_paly_end_time);
    	
    	if (mPlaySeekBar != null) {
    		mPlaySeekBar.setOnSeekBarChangeListener(this);
		}
    	
    	if (mPlayButton != null) {
    		mPlayButton.setOnClickListener(this);
    	}
    	
    	if(mPlayNextButton != null) {
    		mPlayNextButton.setOnClickListener(this);
    	}
    	
    	if (mPlayNextButton != null) {
    		mPlayPreviousButton.setOnClickListener(this);
    	}
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.teahour_fm_main_actionbar, menu);
            restoreActionBar();           
        }
        return true;
    }

    public void restoreActionBar() {
        if (mActionBar != null) {
            mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
            mActionBar.setDisplayShowTitleEnabled(true);
            mActionBar.setTitle(mActionBarTitle);
        }
    }

	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        handleOverflowMenuItemSelected(id);
        handleShareMenuItemSelected(id);
        return super.onOptionsItemSelected(item);
    }

    /**
     *
     * @param id menu item id
     */
    private void handleShareMenuItemSelected(int id) {
        switch (id) {
            case R.id.action_podcast :
                TFMLinkUtil.openLinkWithInlinBrowser(this, getString(R.string.url_podcat));
                break;
            case R.id.action_newsletter :
                Intent intent = new Intent();
                intent.setClass(this, TFMNewsletterActivity.class);
                startActivity(intent);
                break;
            case R.id.action_open_source :
                TFMLinkUtil.openLinkWithInlinBrowser(this, getString(R.string.url_open_source));
                break;
            case R.id.action_exit_app :
                showExitAppDialog();
                break;
            default :
                break;
        }
    }

    /**
     *
     * @param id menu item id
     */
    private void handleOverflowMenuItemSelected(int id) {
        switch (id) {
            case R.id.action_share_to_weibo:
                TFMFeedDataService.getInstance().shareToWeibo(this, mCurrentPlayIndex);
                break;
            case R.id.action_share_to_wechat:
                TFMFeedDataService.getInstance().shareToWechatMoments(this, mCurrentPlayIndex);
                break;
            default:
                break;
        }
    }
	
	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
			case R.id.ibtn_play :
				play();
				break;
			case R.id.ibtn_play_next :
				playNext();
				break;
			case R.id.ibtn_play_previous :
				playPrevious();
				break;
			default :
				break;
		}
	}

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        if (mPlayBinder != null && position == mCurrentPlayIndex) {
            return;
        }
        if (mTFMPlayFragment == null) {
            mTFMPlayFragment = TFMPlayerFragment.newInstance(position);
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, mTFMPlayFragment)
                    .commitAllowingStateLoss();
        } else {
            mTFMPlayFragment.updateFeedDataToView(position);
        }


        mCurrentPlayIndex = position;

        if (mPlayBinder == null) {
            mServiceIntent.setAction(TFMConstant.ACTION_FM_PLAY);
            mServiceIntent.putExtra(TFMConstant.KEY_PLAY_INDEX, position);
            bindService(mServiceIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
            startService(mServiceIntent);

        } else {
            resetPlayerSeekBarToInitSate();
            mPlayBinder.play(position);
        }
    }


    private void resetPlayerSeekBarToInitSate() {
        mPlaySeekBar.setProgress(0);
        mPlaySeekBar.setSecondaryProgress(0);
        mPlaySeekBar.setMax(0);
        mPlayButton.setBackgroundResource(R.drawable.btn_play_start_selector);
        mPlayEndTimeView.setText(R.string.text_play_time_default);
        mPlayStartTimeView.setText(R.string.text_play_time_default);
    }


    private void playNext() {		
		if (mPlayBinder != null && mCurrentPlayIndex < mFeedItemTotalCont) {
			++mCurrentPlayIndex;
			mPlayBinder.next();
            resetPlayerSeekBarToInitSate();
			resetPlayTimeTextToDefaultText();
			mTFMPlayFragment.updateFeedDataToView(mCurrentPlayIndex);
		}
	}

	/**
	 * <p>Description: </p>
	 *
	 * @since Oct 24, 2014 7:52:23 PM
	 * @author inferjay
	 */
	private void resetPlayTimeTextToDefaultText() {
		if (mPlayStartTimeView != null) {
			mPlayStartTimeView.setText(R.string.text_play_time_default);
		}
		
		if (mPlayEndTimeView != null) {
			mPlayEndTimeView.setText(R.string.text_play_time_default);
		}
	}

	private void playPrevious() {
		if (mPlayBinder != null && mCurrentPlayIndex > 0) {
			--mCurrentPlayIndex;
            resetPlayerSeekBarToInitSate();
			resetPlayTimeTextToDefaultText();
			mPlayBinder.previous();
			mTFMPlayFragment.updateFeedDataToView(mCurrentPlayIndex);
		}	
	}

	private void play() {
    	if (mPlayBinder != null) {
    		boolean isPlaying = mPlayBinder.isPlaying();
    		if (isPlaying) {
                isPlaying = false;
    			mPlayBinder.pause();
    		} else {
                isPlaying = true;
    			mPlayBinder.rePlay();
			}
    		setPlayBtnCurrentState(isPlaying);
    	}
    	
    }
    
    private void setPlayBtnCurrentState(boolean isPlaying) {
    	if (mPlayButton == null) return;
    	if (isPlaying) {
    		mPlayButton.setBackgroundResource(R.drawable.btn_play_stop_selector);
    	} else {
    		mPlayButton.setBackgroundResource(R.drawable.btn_play_start_selector);
    	}
    }
    
    
    private void loadFeedData() {
    	if (TFMFeedDataService.getInstance().isExistFeedData()) {
    		mNavigationDrawerFragment.showDrawerListFillWithDataState(TFMFeedDataService.getInstance().getFeedItemTitleList());
    	} else {
    		mGenericStatusLayout.showLoading();
    		TFMFeedDataService.getInstance().setLoadFeedDataListener(this);
    		TFMFeedDataService.getInstance().loadTFMFeedData();
    	}  	
	}

	@Override
	public void OnFeedDataLoadSuccess() {
        mGenericStatusLayout.hideLoading();
        if (TFMFeedDataService.getInstance().isExistFeedData()) {
            mFeedItemTotalCont = TFMFeedDataService.getInstance().getFeedItemTotalCount();
            mNavigationDrawerFragment.showDrawerListFillWithDataState(TFMFeedDataService.getInstance().getFeedItemTitleList());
        } else {
            mGenericStatusLayout.showEmpty();
            mUpdateDrawerListStateCallback.showDrawerListEmptyDataState();
        }
	}

	@Override
	public void OnFeedDataLoadError(String err) {
        mGenericStatusLayout.hideLoading();
		mGenericStatusLayout.showError();
	}
	
    
	private static final class RefreshPlayBarViewHandler extends Handler {
		
		private WeakReference<TFMMainActivity> mMainActivityWeakReference; 
		
		public RefreshPlayBarViewHandler(TFMMainActivity activity) {
			mMainActivityWeakReference = new WeakReference<TFMMainActivity>(activity);
		}
		
		public void handleMessage(Message msg) {
			if (mMainActivityWeakReference.get() == null) return;
			switch (msg.what) {
                case TFMConstant.MAG_WHAT_PLAY_START :
                    mMainActivityWeakReference.get().setPlayBtnCurrentState(true);
                    removeMessages(TFMConstant.MAG_WHAT_PLAY_START);
                    break;
				case TFMConstant.MSG_WHAT_PLAY_DURATION :
					mMainActivityWeakReference.get().updatePlayEndTimeTextView();
					removeMessages(TFMConstant.MSG_WHAT_PLAY_DURATION);
					break;
				case TFMConstant.MSG_WHAT_PLAY_BUFFER_PERCENT :
					mMainActivityWeakReference.get().updateSeekbarSecondaryProgress();
					break;
				case TFMConstant.MAG_WHAT_PLAY_CURRENT_PREOGRESS :
					mMainActivityWeakReference.get().updateCurrentPlayProgress();
					sendEmptyMessage(TFMConstant.MAG_WHAT_PLAY_CURRENT_PREOGRESS);
					break;
                case TFMConstant.MAG_WHAT_PLAY_FINISH :
                    mMainActivityWeakReference.get().refreshPlayBarViewFinishState();
                    removeMessages(TFMConstant.MAG_WHAT_PLAY_CURRENT_PREOGRESS);
                    break;
				default :
					break;
			}
		}
	}

	public void updateCurrentPlayProgress() {
		if (mPlayBinder != null) {
			int currentPro = mPlayBinder.getCurrentPlayPosition();
			if(mPlaySeekBar != null) {
				mPlaySeekBar.setProgress(currentPro);
			}
            if (isPlayFinish) {
                updatePlayStartTimeTextView(0);
            } else {
                updatePlayStartTimeTextView(currentPro);
            }
		}
		
	}

	public void updatePlayStartTimeTextView(int currentPro) {
		if (mPlayStartTimeView != null) {
            mPlayStartTimeView.setText(TFMTimeUtils.formatTimeToString(currentPro));
		}
	}
	
	public void updatePlayEndTimeTextView() {
		if (mPlayEndTimeView != null) {
			int duration = mPlayBinder.getPlayDuration();
			mPlaySeekBar.setMax(duration);
			mPlayEndTimeView.setText(TFMTimeUtils.formatTimeToString(duration));
		}
	}
	
	public void updateSeekbarSecondaryProgress() {
		if (mPlaySeekBar != null && mPlayBinder != null) {
			mPlaySeekBar.setSecondaryProgress(getSeekbarSecondaryProgress());
		}		
	}

    public void refreshPlayBarViewFinishState() {
        isPlayFinish = true;
        playNext();
    }

	private int getSeekbarSecondaryProgress() {
		int secondary;
		if (mPlayBinder != null) {
			secondary = mPlayBinder.getCurrentBufferPercent() * (mPlayBinder.getPlayDuration() / 100);
		} else {
			secondary = mPlaySeekBar.getSecondaryProgress();
		}
		return secondary;
	}
	
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		if (fromUser && mPlayBinder != null) {
			mPlayBinder.seekToPosition(progress);
			updateCurrentPlayProgress();			
		}		
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
	}

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            showExitAppDialog();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    private void showExitAppDialog() {
        new AlertDialog.Builder(this)
                .setMessage(R.string.text_msg_exit_app)
                .setTitle(R.string.title_exit_app_dialog)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        exitApp();
                        dialog.cancel();
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .create().show();
    }

    private void exitApp() {
        finish();
        System.exit(0);
        android.os.Process.killProcess(Process.myPid());
    }

    @Override
    protected void onDestroy() {
    	super.onDestroy();
    	if (mPlayBinder != null) {
            if (mPlayBinder.isPlaying()) {
                mPlayBinder.stop();
            }
			mPlayBinder.release();
            mPlayBinder.cleanPlayingNotification();
		}
    	stopService(mServiceIntent);
    	if (isBindService) {
    		unbindService(mServiceConnection);
    	}
    	if (mRefreshPlayBarViewHandler != null) {
    		mRefreshPlayBarViewHandler.removeCallbacksAndMessages(null);
    	}
   	
    }

}
