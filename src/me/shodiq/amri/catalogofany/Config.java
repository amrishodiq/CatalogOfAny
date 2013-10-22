package me.shodiq.amri.catalogofany;

public class Config {
	public static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";
	
	public static final String BUNDLE_KEY_TITLE = "title";
	
	public static final String INTENT_MAIN_DROPDOWN_ITEMS = "main_dropdown_items";
	
	public static final int ADD_ITEM_REQUEST = 1;
	public static final int EDIT_ITEM_REQUEST = 2;
	public static final int ADD_PICTURE_REQUEST = 3;
	
	public static final String FIELD_POSITION = "position";
	public static final String FIELD_INDEX = "index";
	public static final String FIELD_CATEGORY = "category";
	public static final String FIELD_TITLE = "title";
	public static final String FIELD_CONTENT = "content";
	public static final String FIELD_PICTURE = "picture";
	public static final String FIELD_SELECTED_ITEM = "selectedItem";
	
	public static final String DATABASE_NAME = "CatalogOfAny";
	public static final int DATABASE_VERSION = 1;
	
	public static final String TABLE_ITEM = "item";
	public static final String TABLE_CATEGORY = "category";
	
	public static final String COLUMN_ITEM_ID = "id";
	public static final String COLUMN_ITEM_CATEGORY = "category";
	public static final String COLUMN_ITEM_TITLE = "title";
	public static final String COLUMN_ITEM_CONTENT = "content";
	public static final String COLUMN_ITEM_PICTURE_PATH = "picturePath";
	
	public static final String COLUMN_CATEGORY_ID = "id";
	public static final String COLUMN_CATEGORY_NAME = "name";
	
	public static final String CREATE_TABLE_ITEM = "CREATE TABLE "+TABLE_ITEM+
		" ("+COLUMN_ITEM_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+COLUMN_ITEM_CATEGORY+" TEXT, "+COLUMN_ITEM_TITLE+" TEXT NOT NULL, "+COLUMN_ITEM_CONTENT+" TEXT, "+COLUMN_ITEM_PICTURE_PATH+" TEXT);";
	public static final String CREATE_TABLE_CATEGORY = "CREATE TABLE "+TABLE_CATEGORY+" ("+COLUMN_CATEGORY_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+COLUMN_CATEGORY_NAME+" TEXT NOT NULL)";
}
