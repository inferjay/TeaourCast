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
import java.util.List;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.view.MenuItem;
import android.widget.Toast;

import com.inferjay.teahour.fm.android.R;
import com.inferjay.teahour.fm.android.utils.AppUtils;
import com.inferjay.teahour.fm.android.utils.TFMActionBarUtils;
import com.inferjay.teahour.fm.android.utils.TFMLinkUtil;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;

/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class TFMSettingsActivity extends PreferenceActivity{
	/**
	 * Determines whether to always show the simplified settings UI, where
	 * settings are presented in a single list. When false, settings are shown
	 * as a master/detail two-pane view on tablets. When true, a single pane is
	 * shown on tablets.
	 */
	private static final boolean ALWAYS_SIMPLE_PREFS = false;
	
	private static String KEY_CLEAN_DOWNLOAD_CACHE;
	private static String KEY_FEEDBACK;
	private static String KEY_CHECK_NEW_VERSION;
	private static String KEY_ABOUT_TEAHOUR_FM;
	private static String KEY_ABOUT_CREATER;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        TFMActionBarUtils.setActionBarIcon(getActionBar());
		setupActionBar();
		initPerferenceKey();
	}

	/**
	 * <p>Description: </p>
	 *
	 * @since 2014年9月8日 下午9:26:12
	 * @author inferjay
	 */
	private void initPerferenceKey() {		
		KEY_CLEAN_DOWNLOAD_CACHE = getString(R.string.key_pref_clean_download_cache);
		KEY_FEEDBACK = getString(R.string.key_pref_feedback);
		KEY_CHECK_NEW_VERSION = getString(R.string.key_pref_check_new_version);
		KEY_ABOUT_TEAHOUR_FM = getString(R.string.key_pref_about_teahour_fm);
		KEY_ABOUT_CREATER = getString(R.string.key_pref_about_creater);
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                // Show the Up button in the action bar.
                actionBar.setHomeButtonEnabled(true);
                actionBar.setDisplayHomeAsUpEnabled(true); // show back arrow on title icon
                actionBar.setDisplayShowHomeEnabled(true);
            }
        }
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == android.R.id.home) {
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			// TODO: If Settings has multiple levels, Up should navigate up
			// that hierarchy.
//			NavUtils.navigateUpFromSameTask(this);
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

		setupSimplePreferencesScreen();
	}

	/**
	 * Shows the simplified settings UI if the device configuration if the
	 * device configuration dictates that a simplified, single-pane UI should be
	 * shown.
	 */
	private void setupSimplePreferencesScreen() {
		if (!isSimplePreferences(this)) {
			return;
		}

		addPreferencesFromResource(R.xml.pref_settings);
		
//		bindPreferenceOnClickListener(findPreference(KEY_CLEAN_DOWNLOAD_CACHE));
		
		bindPreferenceOnClickListener(findPreference(KEY_FEEDBACK));
		Preference versionPreference = findPreference(KEY_CHECK_NEW_VERSION);
		versionPreference.setSummary(versionPreference.getSummary() + AppUtils.getAppVersionNumberString(this));
		bindPreferenceOnClickListener(versionPreference);
		bindPreferenceOnClickListener(findPreference(KEY_ABOUT_TEAHOUR_FM));
		bindPreferenceOnClickListener(findPreference(KEY_ABOUT_CREATER));
	}

	/** {@inheritDoc} */
	@Override
	public boolean onIsMultiPane() {
		return isXLargeTablet(this) && !isSimplePreferences(this);
	}

	/**
	 * Helper method to determine if the device has an extra-large screen. For
	 * example, 10" tablets are extra-large.
	 */
	private static boolean isXLargeTablet(Context context) {
		return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
	}

	/**
	 * Determines whether the simplified settings UI should be shown. This is
	 * true if this is forced via {@link #ALWAYS_SIMPLE_PREFS}, or the device
	 * doesn't have newer APIs like {@link PreferenceFragment}, or the device
	 * doesn't have an extra-large screen. In these cases, a single-pane
	 * "simplified" settings UI should be shown.
	 */
	private static boolean isSimplePreferences(Context context) {
		return ALWAYS_SIMPLE_PREFS
				|| Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB
				|| !isXLargeTablet(context);
	}

	/** {@inheritDoc} */
	@Override
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public void onBuildHeaders(List<Header> target) {
		if (!isSimplePreferences(this)) {
			loadHeadersFromResource(R.xml.pref_headers, target);
		}
	}


	private static void bindPreferenceOnClickListener(Preference preference) {
		// Set the listener to watch for value changes.
		preference.setOnPreferenceClickListener(mPreferenceClickListener);
	}

	private static OnPreferenceClickListener mPreferenceClickListener = new OnPreferenceClickListener() {

		@Override
		public boolean onPreferenceClick(final Preference preference) {
			String key = preference.getKey();

			if (KEY_CLEAN_DOWNLOAD_CACHE.equals(key)) {
				
			}
			if (KEY_FEEDBACK.equals(key)) {
				TFMLinkUtil.openLinkWithInlinBrowser(preference.getContext(), preference.getContext().getString(R.string.url_feedback));
			}
			if (KEY_CHECK_NEW_VERSION.equals(key)) {
                UmengUpdateAgent.setUpdateAutoPopup(false);
                final ProgressDialog update = new ProgressDialog(preference.getContext());
                update.show();
                UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {

                    @Override
                    public void onUpdateReturned(int updateStatus,UpdateResponse updateInfo) {
                        update.dismiss();
                        switch (updateStatus) {
                            case UpdateStatus.Yes: // has update
                                UmengUpdateAgent.showUpdateDialog(preference.getContext(), updateInfo);
                                break;
                            case UpdateStatus.No: // has no update
                                Toast.makeText(preference.getContext(), R.string.text_msg_is_new_version, Toast.LENGTH_SHORT).show();
                                break;
                            case UpdateStatus.NoneWifi: // none wifi
                                Toast.makeText(preference.getContext(), R.string.text_msg_none_wifi, Toast.LENGTH_SHORT).show();
                                break;
                            case UpdateStatus.Timeout: // time out
                                Toast.makeText(preference.getContext(), R.string.text_msg_timeout, Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                });
                UmengUpdateAgent.update(preference.getContext());
			};
			if (KEY_ABOUT_TEAHOUR_FM.equals(key)) {
				Intent intent = new Intent(preference.getContext(), TFMAboutActivity.class);
                preference.getContext().startActivity(intent);
			}
			if (KEY_ABOUT_CREATER.equals(key)) {
				TFMLinkUtil.openLinkWithInlinBrowser(preference.getContext(), preference.getContext().getString(R.string.url_creater_site));
			}
			return true;
		}
	};
	/**
	 * This fragment shows general preferences only. It is used when the
	 * activity is showing a two-pane settings UI.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static class NetworkPreferenceFragment extends PreferenceFragment {
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.pref_network);

			// Bind the summaries of EditText/List/Dialog/Ringtone preferences
			// to their values. When their values change, their summaries are
			// updated to reflect the new value, per the Android Design
			// guidelines.
			bindPreferenceOnClickListener(findPreference(getString(R.string.key_pref_wifi_auto_play)));
			bindPreferenceOnClickListener(findPreference(getString(R.string.key_pref_2g3g4g_auto_play)));
		}
	}

	/**
	 * This fragment shows notification preferences only. It is used when the
	 * activity is showing a two-pane settings UI.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static class AboutPreferenceFragment extends PreferenceFragment {
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.pref_about);

			// Bind the summaries of EditText/List/Dialog/Ringtone preferences
			// to their values. When their values change, their summaries are
			// updated to reflect the new value, per the Android Design
			// guidelines.
			bindPreferenceOnClickListener(findPreference(getString(R.string.key_pref_check_new_version)));
			bindPreferenceOnClickListener(findPreference(getString(R.string.key_pref_about_teahour_fm)));
			bindPreferenceOnClickListener(findPreference(getString(R.string.key_pref_about_creater)));
		}
	}

	/**
	 * This fragment shows data and sync preferences only. It is used when the
	 * activity is showing a two-pane settings UI.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static class ProblemPreferenceFragment extends PreferenceFragment {

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.pref_problem);

			// Bind the summaries of EditText/List/Dialog/Ringtone preferences
			// to their values. When their values change, their summaries are
			// updated to reflect the new value, per the Android Design
			// guidelines.
			bindPreferenceOnClickListener(findPreference(getString(R.string.key_pref_feedback)));
		}
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static class DownloadPreferenceFragment extends PreferenceFragment {
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.pref_download);

			// Bind the summaries of EditText/List/Dialog/Ringtone preferences
			// to their values. When their values change, their summaries are
			// updated to reflect the new value, per the Android Design
			// guidelines.
			bindPreferenceOnClickListener(findPreference(getString(R.string.key_pref_clean_download_cache)));
		}
	}
}
