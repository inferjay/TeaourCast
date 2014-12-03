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
package com.inferjay.teahour.fm.android.service;

import java.io.IOException;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.inferjay.teahour.fm.android.R;
import com.inferjay.teahour.fm.android.constant.TFMConstant;
import com.inferjay.teahour.fm.android.ui.TFMMainActivity;
import com.inferjay.teahour.fm.android.utils.TFMLogUtils;

public class TFMPlayService extends Service implements OnPreparedListener, OnErrorListener,
								OnBufferingUpdateListener, OnCompletionListener{
	private final static String TAG = TFMPlayService.class.getSimpleName();
	
	private MediaPlayer mMediaPlayer;
	
	private int mPlayingIndex;

	private int mCurrentBufferPercent;

	private boolean isPause;
	
	private Handler mRefreshPlayBarViewHandler;
	
	private int mDuration;

	private int mCurrentPosition;
	
	public TFMPlayService() {
	}

	@Override
	public IBinder onBind(Intent intent) {
        return new TeahourFMPlayBinder();
	}
	
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
		if (intent != null) {
			String action = intent.getAction();
			if (TFMConstant.ACTION_FM_PLAY.equals(action)) {
				playing();
			}
			if (TFMConstant.ACTION_FM_STOP.equals(action)) {
				stop();
			} 
			if (TFMConstant.ACTION_FM_NEXT.equals(action)) {
				playNext();
			}
			
			if (TFMConstant.ACTION_FM_PREVIOUS.equals(action)) {
				playPrevious();
			}
		}
		return START_STICKY;
	}
	
	
	private void playing() {
		
		try {
            if (mMediaPlayer != null) {
                stop();
                mMediaPlayer.release();
            }
			mMediaPlayer = new MediaPlayer();
			mMediaPlayer.setOnBufferingUpdateListener(this);
			mMediaPlayer.setOnErrorListener(this);
			mMediaPlayer.setOnPreparedListener(this);
            mMediaPlayer.setOnCompletionListener(this);
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mMediaPlayer.setDataSource(TFMFeedDataService.getInstance().findFeedAudioURLByIndex(mPlayingIndex));
			mMediaPlayer.prepareAsync();
			mMediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
			
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void rePlay() {
		if (mMediaPlayer != null && isPause) {
			isPause = false;
			mRefreshPlayBarViewHandler.sendEmptyMessage(TFMConstant.MAG_WHAT_PLAY_CURRENT_PREOGRESS);
			mRefreshPlayBarViewHandler.sendEmptyMessage(TFMConstant.MSG_WHAT_PLAY_BUFFER_PERCENT);
			mMediaPlayer.start();
		}
	}
	
	private void stop() {
		if (mMediaPlayer != null) {
			mRefreshPlayBarViewHandler.removeCallbacksAndMessages(null);
			mMediaPlayer.stop();
		}
	}


	private void playNext() {
		if (mPlayingIndex < TFMFeedDataService.getInstance().getFeedItemTotalCount()) {
			mPlayingIndex += 1;
			playing();
		}
	}


	private void playPrevious() {
		if (mPlayingIndex > 0) {
			mPlayingIndex -=1;
			playing();
		}
	}

	private void playFinish() {
		mRefreshPlayBarViewHandler.sendEmptyMessage(TFMConstant.MAG_WHAT_PLAY_FINISH);
	}
	
	public boolean isPlaying() {
		return mMediaPlayer != null && mMediaPlayer.isPlaying();
	}
	
	public int getCurrentPosition() {
		return isPlaying() ? mCurrentPosition = mMediaPlayer.getCurrentPosition() : mCurrentPosition;
	}
	
	public int getDuration() {
		return mDuration;
	}
	
	public void seekTo(int msec) {
		if (mMediaPlayer != null) {
			mMediaPlayer.seekTo(msec);
		}			
	}

	public void pause() {
		if (isPlaying()) {
			isPause = true;
			mRefreshPlayBarViewHandler.removeMessages(TFMConstant.MAG_WHAT_PLAY_CURRENT_PREOGRESS);
			mMediaPlayer.pause();
		}
	}
	
	public void releaseMediaPlayer(){
		if (mMediaPlayer != null) {
			mMediaPlayer.release();	
		}	
	}	
	
	@Override
	public void onBufferingUpdate(MediaPlayer mp, int percent) {
		TFMLogUtils.printLogD(TAG, "percent=" + percent);
		mCurrentBufferPercent = percent;
		mRefreshPlayBarViewHandler.sendEmptyMessage(TFMConstant.MSG_WHAT_PLAY_BUFFER_PERCENT);
	}


	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {
        mRefreshPlayBarViewHandler.sendEmptyMessage(TFMConstant.MSG_WHAT_PLAY_ERROR);
		return false;
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		playFinish();
	}
	
	@Override
	public void onPrepared(MediaPlayer mp) {
		mDuration = mMediaPlayer.getDuration();
		mp.start();
        mRefreshPlayBarViewHandler.sendEmptyMessage(TFMConstant.MAG_WHAT_PLAY_START);
		mRefreshPlayBarViewHandler.sendEmptyMessage(TFMConstant.MAG_WHAT_PLAY_CURRENT_PREOGRESS);
		mRefreshPlayBarViewHandler.sendEmptyMessage(TFMConstant.MSG_WHAT_PLAY_DURATION);
		showPlayingNotification();
	}
	
	private void showPlayingNotification() {
		Intent notifiIntent = new Intent(getApplicationContext(), TFMMainActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, notifiIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		int icon = R.drawable.ic_launcher;
		String tickerText = getString(R.string.app_name);
		String title = TFMFeedDataService.getInstance().getFeedItemTitleList().get(mPlayingIndex);
		//String contentText = TFMFeedDataService.getInstance().getFeedItemSummaryByIndex(mPlayingIndex);
		NotificationCompat.Builder notifiBuilder = new NotificationCompat.Builder(getApplicationContext());
		notifiBuilder.setContentTitle(title)
					 .setTicker(tickerText)
					 .setSmallIcon(icon)
					 .setContentIntent(pendingIntent);
		NotificationManagerCompat.from(getApplicationContext()).notify(icon, notifiBuilder.build());
	}
	
	public class TeahourFMPlayBinder extends Binder {
		
		
		public TFMPlayService getTeahourFMPlayService(){
			return TFMPlayService.this;
		}
		
		public boolean isPlaying() {
			return getTeahourFMPlayService().isPlaying();
		}
		
		public int getCurrentPlayPosition() {
			return getCurrentPosition();
		}
		
		public int getPlayDuration() {
			return getDuration();
		}
		
		public void seekToPosition(int msec) {
			seekTo(msec);			
		}
		
		public void play(int index) {
			mPlayingIndex = index;
			playing();
		}
		
		public void rePlay() {
			getTeahourFMPlayService().rePlay();
		}
		
		public void stop() {
			getTeahourFMPlayService().stop();			
		}
		
		public void pause() {
			getTeahourFMPlayService().pause();
		}
		
		public void next() {
			playNext();
		}
		
		public void previous() {
			playPrevious();
		}
		
		public int getCurrentBufferPercent() {
			return mCurrentBufferPercent;
		}
		
		public void setServiceHander (Handler handler) {
			mRefreshPlayBarViewHandler = handler;
		}
		
		public void release() {
			releaseMediaPlayer();
		}

        public void cleanPlayingNotification() {
            NotificationManagerCompat.from(getApplicationContext()).cancel(R.drawable.ic_launcher);
        }
	}

	/**
	 * <p>Description: </p>
	 *
	 * @since 2014-8-24 下午5:17:06
	 * @author inferjay
	 */
	@Override
	public void onDestroy() {
		super.onDestroy();
		stopForeground(false);
	}
}
