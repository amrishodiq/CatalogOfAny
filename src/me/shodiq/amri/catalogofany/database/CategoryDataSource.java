package me.shodiq.amri.catalogofany.database;

import java.util.ArrayList;

import me.shodiq.amri.catalogofany.Config;
import me.shodiq.amri.catalogofany.model.Item;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class CategoryDataSource {
	private static CategoryDataSource instance;
	public static CategoryDataSource getInstance(Context context) {
		if (instance == null) instance = new CategoryDataSource(context);
		return instance;
	}
	
	private SQLiteDatabase db;
	private String[] columns = {Config.COLUMN_CATEGORY_ID, Config.COLUMN_CATEGORY_NAME};
	
	private CategoryDataSource(Context context) {
		db = DatabaseHelper.getInstance(context).getWritableDatabase();
	}
	
	public String create(String categoryName) {
		ContentValues values = new ContentValues();
		values.put(Config.COLUMN_CATEGORY_NAME, categoryName);
		long insertId = db.insert(Config.TABLE_CATEGORY, null, values);
		if (insertId >= 0) {
			return categoryName;
		} else return null;
	}
	
	public ArrayList<String> readAll() {
		ArrayList<String> result = new ArrayList<String>();
		Cursor cursor = db.query(Config.TABLE_CATEGORY, columns, null, null, null, null, null);
		cursor.moveToFirst();
		String item;
		while (!cursor.isAfterLast()) {
			item = cursor.getString(1);
			result.add(item);
			cursor.moveToNext();
		}
		return result;
	}
	
	
}
