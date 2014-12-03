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
package com.inferjay.teahour.fm.android.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import com.inferjay.teahour.fm.android.data.TFMFeedDataSet;
import com.inferjay.teahour.fm.android.data.TFMFeedItem;
import com.inferjay.teahour.fm.android.data.TFMFeedItemAuthor;
import com.inferjay.teahour.fm.android.data.TFMFeedItemDescription;
import com.inferjay.teahour.fm.android.data.TFMFeedItemEnclosure;
import com.inferjay.teahour.fm.android.data.TFMFeedItemSummary;


public class TFMFeedDataParser {

	public static TFMFeedDataSet getTeahourFMFeedData(String data) throws XmlPullParserException, IOException {
        TFMFeedDataSet feedData = new TFMFeedDataSet();
		XmlPullParser xmlPullParser = XmlPullParserFactory.newInstance().newPullParser();
		xmlPullParser.setInput(new ByteArrayInputStream(data.getBytes()),"UTF-8");
		int eventType = xmlPullParser.getEventType();
		TFMFeedItem item = null;
		TFMFeedItemAuthor author = null;

		while (eventType != XmlPullParser.END_DOCUMENT) {
			String tagName = xmlPullParser.getName();

			switch (eventType) {
				case XmlPullParser.START_TAG :
					if ("title".equals(tagName)) {
						String title = xmlPullParser.nextText();
						if (item != null) {
							item.setTitle(title);
						} else {
							feedData.setTitle(title);
						}
					} else if ("itunes:summary".equals(tagName)) {
						String summaryStr = xmlPullParser.nextText();
						if (item != null) {
							TFMFeedItemSummary summary = new TFMFeedItemSummary();
							if (xmlPullParser.getAttributeCount() > 0) {
								summary.setType(xmlPullParser
										.getAttributeName(0));
							}
							item.setSummary(summary);
							summary.setContent(summaryStr);
						} else {
							feedData.setSummary(summaryStr);
						}
					} else if ("updated".equals(tagName)) {
						feedData.setUpdated(xmlPullParser.nextText());
					} else if ("lastBuildDate".equals(tagName)) {
						feedData.setLastBuildDate(xmlPullParser.nextText());
					} else if ("itunes:author".equals(tagName)) {
						String authorStr = xmlPullParser.nextText();
						if (item != null) {
							item.setAuthor(authorStr);
						} else {
							feedData.setAuthor(authorStr);
						}
					} else if ("item".equals(tagName)) {
						item = new TFMFeedItem();
						feedData.addFeedItemToList(item);
					} else if ("description".equals(tagName)) {
						String descriptionStr = xmlPullParser.nextText();
						if (item != null) {
							TFMFeedItemDescription description = new TFMFeedItemDescription();
							item.setDescription(description);
							description.setContent(descriptionStr);
							if (xmlPullParser.getAttributeCount() > 0) {
								description.setType(xmlPullParser
										.getAttributeName(0));
							}
						} else {
							feedData.setDescription(descriptionStr);
						}
					} else if ("link".equals(tagName) && item != null) {
						item.setLink(xmlPullParser.nextText());
					} else if ("enclosure".equals(tagName)) {
						TFMFeedItemEnclosure enclosure = new TFMFeedItemEnclosure();
						item.setEnclosure(enclosure);
						enclosure.setUrl(xmlPullParser.nextText());
						enclosure.setType(xmlPullParser.getAttributeName(0));
					} else if ("guid".equals(tagName)) {
						item.setGuid(xmlPullParser.nextText());
					} else if ("pubDate".equals(tagName)) {
						String pubdate = xmlPullParser.nextText();
						if (item != null) {
							item.setPubDate(pubdate);
						} else {
							feedData.setPubDate(pubdate);
						}
					} else if ("itunes:duration".equals(tagName)) {
						item.setDuration(xmlPullParser.nextText());
					} else if ("itunes:keywords".equals(tagName)) {
						item.setKeywords(xmlPullParser.nextText());
					} else if ("authors".equals(tagName)) {
						item.setAuthors(new ArrayList<TFMFeedItemAuthor>());
					} else if ("author".equals(tagName) && item != null) {
						author = new TFMFeedItemAuthor();
						item.addItemAuthorToList(author);
					} else if ("name".equals(tagName) && item != null) {
						author.setName(xmlPullParser.nextText());
					} else if ("avatar".equals(tagName)) {
						author.setAvatar(xmlPullParser.nextText());
					}
					break;
				default :
					break;
			}
			eventType = xmlPullParser.next();
		}
		return feedData;
	}

}
