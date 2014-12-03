package com.inferjay.teahour.fm.android.ui.widget.htmltextview;

import android.text.Layout;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.view.MotionEvent;
import android.widget.TextView;
/**
 * Implementation of LinkMovementMethod to allow the loading of 
 * a link clicked inside text inside an Android application
 * without exiting to an external browser.
 * 
 * @author Isaac Whitfield
 * @version 25/08/2013
 * {@link https://gist.github.com/iwhitfield/8676560}
 */
public class TFMLinkMovementMethod extends LinkMovementMethod {
		
		// A new LinkMovementMethod
		private static TFMLinkMovementMethod linkMovementMethod  = new TFMLinkMovementMethod();
		
		private OnClickLinkMovemethodListener mOnClickLinkCallback;
		
		public static TFMLinkMovementMethod getInstance(){
			// Return this movement method
			return linkMovementMethod;
		}

		public boolean onTouchEvent(TextView widget, Spannable buffer, MotionEvent event){
			// Get the event action
			int action = event.getAction();
			
			// If action has finished
			if(action == MotionEvent.ACTION_UP) {
				// Locate the area that was pressed
				int x = (int) event.getX();
				int y = (int) event.getY();
				x -= widget.getTotalPaddingLeft();
				y -= widget.getTotalPaddingTop();
				x += widget.getScrollX();
				y += widget.getScrollY();
				
				// Locate the URL text
				Layout layout = widget.getLayout();
				int line = layout.getLineForVertical(y);
				int off = layout.getOffsetForHorizontal(line, x);

				// Find the URL that was pressed
				URLSpan[] link = buffer.getSpans(off, off, URLSpan.class);
				// If we've found a URL
				if (link.length != 0) {
					// Find the URL
					String url = link[0].getURL();
					// If it's a valid URL
					if (url.contains("https") | url.contains("tel") | url.contains("mailto") | url.contains("http") | url.contains("https") | url.contains("www")){
						// call on click link callback
						if (mOnClickLinkCallback != null) {
							mOnClickLinkCallback.onClickLink(url);
						}					
					} 
					// If we're here, something's wrong
					return true;
				}
			}
			return super.onTouchEvent(widget, buffer, event);
		}
		
	public interface OnClickLinkMovemethodListener {
		public void onClickLink(String link);
	}

	public OnClickLinkMovemethodListener getOnClickLinkCallback() {
		return mOnClickLinkCallback;
	}

	public TFMLinkMovementMethod setOnClickLinkCallback(OnClickLinkMovemethodListener callback) {
		this.mOnClickLinkCallback = callback;
		return linkMovementMethod;
	}
	
	
}
