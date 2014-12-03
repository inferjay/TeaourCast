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

import android.os.Bundle;
import android.view.MenuItem;

import butterknife.ButterKnife;

/**
 * 
 * <p>Description: 没有父级Activity的BaseActivity</p>
 * <p>Copyright: Copyright (c) 2014</p>
 * <p>Company: inferjay.com </p>
 *
 * @since Oct 24, 2014 8:02:33 PM
 * @author inferjay
 * @version 1.0
 */
public abstract class TFMNoParentNavBaseActivity extends TFMBaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(getContentViewResId());
        ButterKnife.inject(this);
	}

	protected abstract int getContentViewResId();


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == android.R.id.home) {
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
