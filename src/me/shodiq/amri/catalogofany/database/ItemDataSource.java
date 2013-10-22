package me.shodiq.amri.catalogofany.database;

import java.util.ArrayList;

import me.shodiq.amri.catalogofany.Config;
import me.shodiq.amri.catalogofany.model.Item;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class ItemDataSource {
	private static ItemDataSource instance;
	public static ItemDataSource getInstance(Context context) {
		if (instance == null) instance = new ItemDataSource(context);
		return instance;
	}
	
	private SQLiteDatabase db;
	private String[] columns = {Config.COLUMN_ITEM_ID, Config.COLUMN_ITEM_CATEGORY, Config.COLUMN_ITEM_TITLE, Config.COLUMN_ITEM_CONTENT, Config.COLUMN_ITEM_PICTURE_PATH};
	
	private ItemDataSource(Context context) {
		db = DatabaseHelper.getInstance(context).getWritableDatabase();
	}
	
	public Item create(String category, String title, String content, String picturePath) {
		ContentValues values = new ContentValues();
		values.put(Config.COLUMN_ITEM_CATEGORY, category);
		values.put(Config.COLUMN_ITEM_TITLE, title);
		values.put(Config.COLUMN_ITEM_CONTENT, content);
		values.put(Config.COLUMN_ITEM_PICTURE_PATH, picturePath);
		long insertId = db.insert(Config.TABLE_ITEM, null, values);
	
		if (insertId >= 0) {
			Item item = new Item(category, title, content);
			item.setPicturePath(picturePath);
			item.setId(insertId);
			
			return item;
		} return null;
	}
	
	public ArrayList<Item> readAll() {
		ArrayList<Item> result = new ArrayList<Item>();
		Cursor cursor = db.query(Config.TABLE_ITEM, columns, null, null, null, null, null);
		cursor.moveToFirst();
		Item item;
		while (!cursor.isAfterLast()) {
			item = new Item();
			item.setId(cursor.getLong(0));
			item.setCategory(cursor.getString(1));
			item.setTitle(cursor.getString(2));
			item.setContent(cursor.getString(3));
			item.setPicturePath(cursor.getString(4));
			result.add(item);
			cursor.moveToNext();
		}
		return result;
	}
	
	public int update(Item item) {
//		Log.i(getClass().getName(), "update "+item.getComplete());
		
		ContentValues values = new ContentValues();
		values.put(Config.COLUMN_ITEM_CATEGORY, item.getCategory());
		values.put(Config.COLUMN_ITEM_TITLE, item.getTitle());
		values.put(Config.COLUMN_ITEM_CONTENT, item.getContent());
		values.put(Config.COLUMN_ITEM_PICTURE_PATH, item.getPicturePath());
		int result = db.update(Config.TABLE_ITEM, values, "id="+item.getId(), null);
		
//		Log.i(getClass().getName(), "result: "+result);
		
		return result;
	}
	
	public boolean delete(Item item) {
		long id = item.getId();
		int result = db.delete(Config.TABLE_ITEM, Config.COLUMN_ITEM_ID+"="+id, null);
		if (result == 0) return false;
		else return true;
	}
}
