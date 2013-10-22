package me.shodiq.amri.catalogofany.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Item implements Parcelable {
	private String category, title, content, picturePath;
	private int index;
	private long id = -1;

	public Item(String category, String title, String content) {
		this.category = category;
		this.title = title;
		this.content = content;
	}

	public Item() {
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String toString() {
		return title;
	}

	public String getFilterable() {
		return category + " | " + title;
	}
	
	public String getComplete() {
		return id + " | " + category + " | " + title + " | " + picturePath + " | " + content;
	}
	
	public String getPicturePath() {
		return picturePath;
	}

	public void setPicturePath(String picturePath) {
		this.picturePath = picturePath;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	/**
	 * For the sake of passing this class' object to another activity, we need it Parcelable
	 * @param in
	 */
	public Item(Parcel in) {
		id = in.readLong();
		category = in.readString();
		title = in.readString();
		content = in.readString();
		picturePath = in.readString();
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(id);
		dest.writeString(category);
		dest.writeString(title);
		dest.writeString(content);
		dest.writeString(picturePath);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<Item> CREATOR = new Creator<Item>() {
		@Override
		public Item createFromParcel(Parcel source) {
			return new Item(source);
		}

		@Override
		public Item[] newArray(int size) {
			return new Item[size];
		}
	};
}
