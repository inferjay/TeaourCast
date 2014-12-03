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

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.inferjay.teahour.fm.android.R;
import com.inferjay.teahour.fm.android.service.TFMSubscibeNewsLetterService;
import com.inferjay.teahour.fm.android.service.TFMSubscibeNewsLetterService.OnSubscibeNewsLetterListener;
import com.inferjay.teahour.fm.android.utils.TFMLogUtils;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class TFMNewsletterActivity extends TFMBaseActivity implements TextWatcher,
        OnSubscibeNewsLetterListener {

   @InjectView(R.id.ed_teahour_fm_newsletter) EditText mSubscibeEmialEditText;
   @InjectView(R.id.btn_newsletter_subscibe) Button mSubscibeButton;
//   @InjectView(R.id.tv_news_letter_msg) HtmlTextView mSubscibeMsg;
    @InjectView(R.id.tv_news_letter_msg) TextView mSubscibeMsg;
    ProgressDialog mSubmitProgressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_teahur_fm_newsletter);
        ButterKnife.inject(this);
        mSubscibeButton.setEnabled(false);
        mSubscibeEmialEditText.addTextChangedListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.global, menu);
		return true;
	}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
                    finish();
                    return true;
                } else {
                    break;
                }
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        checkEmailIsValid(s.toString());
    }

    private void checkEmailIsValid(String email) {
        if (TextUtils.isEmpty(email)) {
            mSubscibeEmialEditText.setError(getString(R.string.text_msg_please_inmput_email));
        } else if(!isEmailValid(email)) {
            mSubscibeEmialEditText.setError(getString(R.string.text_msg_invalid_email));
        } else {
            mSubscibeButton.setEnabled(true);
        }
    }

    private boolean isEmailValid(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    @OnClick(R.id.btn_newsletter_subscibe)
    public void submitEmail() {
        attemptSumbitEmail(mSubscibeEmialEditText.getText().toString());
    }

    private void attemptSumbitEmail(String email) {
        new AsyncTask<String, Void, Void>(){

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                showSubmitProgressDialog();
            }

            @Override
            protected Void doInBackground(String... params) {
                TFMSubscibeNewsLetterService.getInstance().setSubscibeNewsLetteListener(TFMNewsletterActivity.this);
                TFMSubscibeNewsLetterService.getInstance().attemptSubscibeNewsLetter(params[0]);
                return null;
            }

        }.execute(email);
    }

    private void showSubmitProgressDialog() {
        if (mSubmitProgressDialog == null) {
            mSubmitProgressDialog = new ProgressDialog(this);
            mSubmitProgressDialog.setMessage(getString(R.string.text_mag_newsletter_subscibing));
        }
        mSubmitProgressDialog.show();
    }

    @Override
    public void onSubscibeSuccess(String success) {
        mSubmitProgressDialog.dismiss();
        int msg = R.string.text_msg_newsletter_subscibe_success;
        if (!TextUtils.isEmpty(success)) {
            if (success.contains("Almost finished")) {
                success = getString(R.string.text_msg_newsletter_subscibe_almost_finished);
            } else if (success.contains("already subscribed")) {
               success = String.format(getString(R.string.text_msg_newslette_already_subscibed),
                                                        mSubscibeEmialEditText.getText().toString());
                 msg = R.string.text_msg_newsletter_already_subscibe_success;
            } else {
                success = "";
                msg = R.string.text_msg_newsletter_already_subscibe_success;
            }
        }
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

        if (!TextUtils.isEmpty(success)) {
            mSubscibeMsg.setVisibility(View.VISIBLE);
            TFMLogUtils.printLogE(TFMNewsletterActivity.class.getSimpleName(),success);
            mSubscibeMsg.setText(Html.fromHtml(success));
            mSubscibeMsg.setMovementMethod(LinkMovementMethod.getInstance());
        } else {
            mSubscibeMsg.setVisibility(View.GONE);
        }
    }

    @Override
    public void onSubscibeError(String err) {
        mSubmitProgressDialog.dismiss();
        Toast.makeText(this, R.string.text_msg_newsletter_subscibe_fail, Toast.LENGTH_SHORT).show();
    }

}
