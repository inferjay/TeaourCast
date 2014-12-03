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
package com.inferjay.teahour.fm.android.data;

import java.util.ArrayList;
import java.util.List;

import android.text.TextUtils;


public class TFMFeedItem {
	
	
	private TFMFeedItemSummary summary;
	
	private String guid;
	
	private String author;
	
	private String pubDate;
	
	private List<TFMFeedItemAuthor> authors;
	
	private String title;
	
	private String duration;
	
	private String keywords;

	
	private TFMFeedItemEnclosure enclosure;

	
	private TFMFeedItemDescription description;

	
	private String link;

	public TFMFeedItemSummary getSummary() {
		return summary;
	}
	
	public String getSummaryContent() {
		return summary != null ? summary.getContent() : "";
	}

	public void setSummary(TFMFeedItemSummary summary) {
		this.summary = summary;
	}

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getPubDate() {
		return pubDate;
	}

	public void setPubDate(String pubDate) {
		this.pubDate = pubDate;
	}

	public List<TFMFeedItemAuthor> getAuthors() {
		return authors;
	}

	public void setAuthors(List<TFMFeedItemAuthor> authors) {
		this.authors = authors;
	}

	public void addItemAuthorToList(TFMFeedItemAuthor author) {
		if (authors != null && author != null) {
			authors.add(author);
		}
	}
	
	public String getGuesteNames() {
		if (authors != null && !authors.isEmpty()) {
			int count = authors.size();
			String splitTag = ",";
			StringBuffer resultBuffer = new StringBuffer(count * 2);
			for (int i=1 ; i <count ; i++) {
				TFMFeedItemAuthor author = authors.get(i);
				if (author != null && !TextUtils.isEmpty(author.getName())) {
					resultBuffer.append(author.getName());
					if (i != count - 1) {
						resultBuffer.append(splitTag);
					}
				}
			}
			return resultBuffer.toString();
		} else {
			return "";
		}
	}
	
	public List<String> getAuthorAvatarURLList() {
		if (authors != null && !authors.isEmpty()) {
			int count = authors.size();
			List<String> avatorURLs = new ArrayList<String>(count); 
			for (int i=0 ; i <count ; i++) {
				TFMFeedItemAuthor author = authors.get(i);
				if (author != null && !TextUtils.isEmpty(author.getAvatar())) {
					avatorURLs.add(author.getAvatar());
				}
			}
			return avatorURLs;
		} else {
			return null;
		}
	}
	
	public TFMFeedItemAuthor getFeedItemAuthorForAuthorList(int index) {
		return authors != null && authors.size() > 0 && index >= 0 && index < authors.size() ? authors.get(index) : null;
	}
	
	
	public String getFeedItemAuthorName(int index) {
		return isExistFeedItemAutor(index) ? getFeedItemAuthorForAuthorList(index).getName() : "";
	}
	
	public String getFeedItemAuthorAvatarURL(int index) {
		return isExistFeedItemAutor(index) ? getFeedItemAuthorForAuthorList(index).getAvatar() : null;
	}
	
	public boolean isExistFeedItemAutor(int index) {
		return getFeedItemAuthorForAuthorList(index) != null;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public TFMFeedItemEnclosure getEnclosure() {
		return enclosure;
	}

	public String getEnclosureURL() {
		return enclosure != null ? enclosure.getUrl() : null;
	}
	
	
	public void setEnclosure(TFMFeedItemEnclosure enclosure) {
		this.enclosure = enclosure;
	}

	public TFMFeedItemDescription getDescription() {
		return description;
	}

	public String getDescriptionContent() {
		return description != null ? description.getContent() : "";
	}
	
	public void setDescription(TFMFeedItemDescription description) {
		this.description = description;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	@Override
	public String toString() {
		return "TFMFeedItem [summary=" + summary.toString() + ", guid=" + guid
				+ ", author=" + author + ", pubDate=" + pubDate + ", authors="
				+ authors.toString() + ", title=" + title + ", duration=" + duration
				+ ", keywords=" + keywords + ", enclosure=" + enclosure.toString()
				+ ", description=" + description.toString() + ", link=" + link + "]";
	}

}
