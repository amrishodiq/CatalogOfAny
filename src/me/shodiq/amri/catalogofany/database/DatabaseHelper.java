package me.shodiq.amri.catalogofany.database;

import me.shodiq.amri.catalogofany.Config;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
	private static DatabaseHelper instance;
	public static DatabaseHelper getInstance(Context context) {
		if (instance == null) instance = new DatabaseHelper(context);
		return instance;
	}
	
	private DatabaseHelper(Context context) {
		super(context, Config.DATABASE_NAME, null, Config.DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(Config.CREATE_TABLE_ITEM);
		db.execSQL(Config.CREATE_TABLE_CATEGORY);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS "+Config.TABLE_ITEM);
		db.execSQL("DROP TABLE IF EXISTS "+Config.TABLE_CATEGORY);
		onCreate(db);
	}

}
