package me.shodiq.amri.catalogofany.ui;

import java.io.File;
import java.util.ArrayList;

import me.shodiq.amri.catalogofany.Config;
import me.shodiq.amri.catalogofany.Data;
import me.shodiq.amri.catalogofany.R;
import me.shodiq.amri.catalogofany.R.layout;
import me.shodiq.amri.catalogofany.R.menu;
import me.shodiq.amri.catalogofany.model.Item;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class EditItemActivity extends Activity {
	private Spinner categoryDropdown;
	private EditText title, content;
	private ArrayAdapter<String> adapter;
	private String picturePath = null;
	private ImageView pictureView;
	private TextView emptyPicture;
	private long editedId = -1;
	
	private Item currentItem;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_item);

		Intent intent = getIntent();
		
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item);
//		adapter.addAll(intent.getStringArrayListExtra(Config.INTENT_MAIN_DROPDOWN_ITEMS));
		adapter.addAll(Data.getInstance().getAllCategories(this));
		
		categoryDropdown = (Spinner) findViewById(R.id.category_dropdown);
		categoryDropdown.setAdapter(adapter);
		
		title = (EditText) findViewById(R.id.add_item_title);
		content = (EditText) findViewById(R.id.add_item_content);
		
		View pictureHolder = findViewById(R.id.picture_holder);
		pictureHolder.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
		        intent.setType("image/*");
		        intent.setAction(Intent.ACTION_GET_CONTENT);
		        intent.addCategory(Intent.CATEGORY_OPENABLE);
		        startActivityForResult(intent, Config.ADD_PICTURE_REQUEST);
			}
		});
		
		pictureView = (ImageView) findViewById(R.id.picture);
		emptyPicture = (TextView) findViewById(R.id.empty_picture);
		
//		if (intent.getExtras().getString(Config.FIELD_TITLE) != null && 
//				intent.getExtras().getString(Config.FIELD_TITLE).trim().length()>0) {
//			
//			ArrayList<String> categories = intent.getStringArrayListExtra(Config.INTENT_MAIN_DROPDOWN_ITEMS); 
//			String itemCategory = intent.getExtras().getString(Config.FIELD_CATEGORY);
//			String itemTitle = intent.getExtras().getString(Config.FIELD_TITLE);
//			String itemContent = intent.getExtras().getString(Config.FIELD_CONTENT);
//			picturePath = intent.getExtras().getString(Config.FIELD_PICTURE);
//
//			int pos = categories.indexOf(itemCategory);
//			if (pos < 0) pos = 0;
//			
//			categoryDropdown.setSelection(pos);
//			title.setText(itemTitle);
//			content.setText(itemContent);
//			
//		}
		
		if (intent.getExtras().containsKey(Config.FIELD_SELECTED_ITEM)) {
			getActionBar().setTitle(R.string.title_activity_modify_item);
			
//			int position = intent.getExtras().getInt(Config.FIELD_POSITION);
			
//			Item item = Data.getInstance().getItem(this, position);
			Item item = (Item) intent.getExtras().get(Config.FIELD_SELECTED_ITEM);
			currentItem = item;
			
			editedId = item.getId();
			title.setText(item.getTitle());
			content.setText(item.getContent());
			picturePath = item.getPicturePath();
			String itemCategory = item.getCategory();
//			ArrayList<String> categories = intent.getStringArrayListExtra(Config.INTENT_MAIN_DROPDOWN_ITEMS);
			ArrayList<String> categories = Data.getInstance().getAllCategories(this);
			int pos = categories.indexOf(itemCategory);
			if (pos < 0) pos = 0;
			categoryDropdown.setSelection(pos);
		}
	}
	
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		
		if (hasFocus) showPicture();
	}
	
//	@Override
//	protected void onResume() {
//		super.onResume();
//		
//		if (picturePath != null && picturePath.length()>0) showPicture();
//	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_item, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.action_save) {
			
			Intent intent = new Intent();
//			intent.putExtra(Config.FIELD_CATEGORY, adapter.getItem(categoryDropdown.getSelectedItemPosition()));
//			intent.putExtra(Config.FIELD_TITLE, title.getText().toString());
//			intent.putExtra(Config.FIELD_CONTENT, content.getText().toString());
//			if (picturePath != null) intent.putExtra(Config.FIELD_PICTURE, picturePath);
			
//			currentItem = (Item) intent.getExtras().get(Config.FIELD_SELECTED_ITEM);
			
			if (currentItem == null) currentItem = new Item();
			
			currentItem.setCategory(adapter.getItem(categoryDropdown.getSelectedItemPosition()));
			currentItem.setTitle(title.getText().toString());
			currentItem.setContent(content.getText().toString());
			if (picturePath != null) currentItem.setPicturePath(picturePath);
		
//			Intent oldIntent = getIntent();
//			intent.putExtra(Config.FIELD_POSITION, oldIntent.getExtras().getInt(Config.FIELD_POSITION));
//			intent.putExtra(Config.FIELD_INDEX, editedId);
			
			intent.putExtra(Config.FIELD_SELECTED_ITEM, currentItem);
			
			if (getParent() == null) {
				setResult(Activity.RESULT_OK, intent);
			} else {
				getParent().setResult(Activity.RESULT_OK, intent);
			}
			
			finish();
			return true;
		} 
//		else if (item.getItemId() == R.id.action_add_picture) {
//			Intent intent = new Intent();
//	        intent.setType("image/*");
//	        intent.setAction(Intent.ACTION_GET_CONTENT);
//	        intent.addCategory(Intent.CATEGORY_OPENABLE);
//	        startActivityForResult(intent, Config.ADD_PICTURE_REQUEST);
//	        
//	        return true;
//		}
		return super.onOptionsItemSelected(item);
	}
	
	private void showPicture() {
		if (picturePath != null && pictureView != null && pictureView.getWidth()>0) {
			File file = new File(picturePath);
			if (file.exists() && pictureView != null && pictureView.getWidth()>0) {
//				Bitmap picture = BitmapFactory.decodeFile(picturePath);
//				Bitmap picture = Data.getInstance().getImage(picturePath, pictureView);
//				
//				int width = picture.getWidth();
//				int height = picture.getHeight();
//				
//				int preferredWidth = pictureView.getWidth();
//				
//				Log.i(getClass().getName(), "Original width: "+width+". Preferred: "+preferredWidth);
//				
//				float scale = ((float)preferredWidth / (float)width);
//				Log.i(getClass().getName(), "Scale to: "+scale);
//				Matrix matrix = new Matrix();
//			    matrix.postScale(scale, scale);
//			    Bitmap scaledBitmap = Bitmap.createBitmap(picture, 0, 0, width, height, matrix, true);
//
//			    pictureView.setImageBitmap(scaledBitmap);
				
				if (currentItem != null) Data.getInstance().loadBitmap(currentItem.getId(), picturePath, pictureView);
				else Data.getInstance().loadBitmap(0, picturePath, pictureView);
			    
				emptyPicture.setVisibility(View.GONE);
			}
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == Config.ADD_PICTURE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
			Uri uri = data.getData();
			picturePath = getPath(uri);
			
			showPicture();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	private String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
}
