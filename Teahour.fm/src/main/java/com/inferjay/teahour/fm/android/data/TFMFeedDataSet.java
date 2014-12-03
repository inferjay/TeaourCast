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


public class TFMFeedDataSet {
	
	private String summary;
	
	private String pubDate;
	
	private String copyright;
	
	private String id;
	
	private String author;
	
	private String title;
	
	private String updated;
	
	private String description;

	private List<TFMFeedItem> items;
	
	private String lastBuildDate;
	
	private String subtitle;
	
	private String language;

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getPubDate() {
		return pubDate;
	}

	public void setPubDate(String pubDate) {
		this.pubDate = pubDate;
	}

//	public List<Link> getLink() {
//		return link;
//	}
//
//	public void setLink(List<Link> link) {
//		this.link = link;
//	}


	public String getCopyright() {
		return copyright;
	}

	public void setCopyright(String copyright) {
		this.copyright = copyright;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

//	public List<Category> getCategory() {
//		return category;
//	}
//
//	public void setCategory(List<Category> category) {
//		this.category = category;
//	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUpdated() {
		return updated;
	}

	public void setUpdated(String updated) {
		this.updated = updated;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<TFMFeedItem> getItem() {
		return items;
	}

	public void setItem(List<TFMFeedItem> items) {
		this.items = items;
	}

	public String getLastBuildDate() {
		return lastBuildDate;
	}

	public void setLastBuildDate(String lastBuildDate) {
		this.lastBuildDate = lastBuildDate;
	}

	public String getSubtitle() {
		return subtitle;
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public void addFeedItemToList(TFMFeedItem item) {
		if (items == null) {
			items = new ArrayList<TFMFeedItem>();
		}
		items.add(item);
	}

	public ArrayList<String> getFeedItemTitleList() {
		ArrayList<String> titles = new ArrayList<String>();
		if ( items != null) {
			for(TFMFeedItem item : items) {
			    titles.add(item.getTitle());
			}
		}		
		return titles;
	}
	
	public String[] getFeedItemTitleArray() {
		int count = getFeedItemTitleList().size();		
		return getFeedItemTitleList().toArray(new String[count]);
	}
	
	public TFMFeedItem findFeedItemByIndex(int index) {
		return items != null && (index >=0 && index < items.size()) ? items.get(index) : null;
	}
	
	public String findFeedItemTitleByIndex(int index) {
		return  items != null && (index >=0 && index < items.size())  ? items.get(index).getTitle() : "";
	}
	
	public boolean isExistFeedItems(){
		return items != null && items.size() > 0;
	}
	
	public List<String> getFeedItemAuthorAvatarURLs(int index) {
		return isExistFeedItems() ? findFeedItemByIndex(index).getAuthorAvatarURLList() : null;
	}
	
	@Override
	public String toString() {
		return "TFMFeedData [summary=" + summary + ", pubDate=" + pubDate
				+ ", copyright=" + copyright + ", id="
				+ id + ", author=" + author + ", title=" + title + ", updated="
				+ updated + ", description=" + description + ", items=" + items.toString()
				+ ", lastBuildDate=" + lastBuildDate + ", subtitle=" + subtitle
				+ ", language=" + language + "]";
	}
	
	
}
