package me.shodiq.amri.catalogofany.ui;

import java.io.File;

import me.shodiq.amri.catalogofany.Config;
import me.shodiq.amri.catalogofany.Data;
import me.shodiq.amri.catalogofany.R;
import me.shodiq.amri.catalogofany.R.layout;
import me.shodiq.amri.catalogofany.R.menu;
import me.shodiq.amri.catalogofany.database.ItemDataSource;
import me.shodiq.amri.catalogofany.model.Item;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ItemDetailActivity extends Activity {
	private String picturePath = null;
	private View pictureHolder;
	private ImageView pictureView;
	private Item currentItem;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_item_detail);
		
		TextView categoryView = (TextView) findViewById(R.id.detail_item_category);
		TextView titleView = (TextView) findViewById(R.id.detail_item_title);
		TextView contentView = (TextView) findViewById(R.id.detail_item_content);
		
		pictureHolder = findViewById(R.id.picture_holder_view);
		pictureView = (ImageView) findViewById(R.id.picture_view);
		
		Intent intent = getIntent();
//		int position = intent.getExtras().getInt(Config.FIELD_POSITION);
//		Item selectedItem = (Item) intent.getExtras().get(Config.FIELD_SELECTED_ITEM);
		
//		String category = intent.getExtras().getString(Config.FIELD_CATEGORY);
//		String title = intent.getExtras().getString(Config.FIELD_TITLE);
//		String content = intent.getExtras().getString(Config.FIELD_CONTENT);
//		picturePath = intent.getExtras().getString(Config.FIELD_PICTURE);
//		if (picturePath == null || picturePath.length() == 0) pictureHolder.setVisibility(View.GONE);
		
		Item item = (Item) intent.getExtras().get(Config.FIELD_SELECTED_ITEM);
		currentItem = item;
		String category = item.getCategory();
		String title = item.getTitle();
		String content = item.getContent();
		picturePath = item.getPicturePath();
		if (picturePath == null || picturePath.length() == 0) pictureHolder.setVisibility(View.GONE);
		
		categoryView.setText(category);
		titleView.setText(title);
		contentView.setText(content);
		
		Log.i(getClass().getName(), "Picture path is not empty: "+picturePath);
		
	}
	
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		
		if (hasFocus) showPicture();
	}
	
//	@Override
//	protected void onResume() {
//		super.onResume();
//		showPicture();
//	}
	
	private void showPicture() {
		if (picturePath != null && pictureView != null && pictureView.getWidth()>0) {
			File file = new File(picturePath);
			if (file.exists()) {
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
				
//				Data.getInstance().loadBitmap(picturePath, pictureView);
				if (currentItem != null) Data.getInstance().loadBitmap(currentItem.getId(), picturePath, pictureView);
				else Data.getInstance().loadBitmap(0, picturePath, pictureView);
			}
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.item_detail, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.action_edit) {
			Intent intent = getIntent();
			
//			int pos = intent.getExtras().getInt(Config.FIELD_POSITION);
//			String category = intent.getExtras().getString(Config.FIELD_CATEGORY);
//			String title = intent.getExtras().getString(Config.FIELD_TITLE);
//			String content = intent.getExtras().getString(Config.FIELD_CONTENT);
//			String picturePath = intent.getExtras().getString(Config.FIELD_PICTURE);
//			Item selectedItem = Data.getInstance().getItem(this, pos);
			
			Intent newIntent = new Intent(this, EditItemActivity.class);
			
//			newIntent.putStringArrayListExtra(Config.INTENT_MAIN_DROPDOWN_ITEMS, intent.getStringArrayListExtra(Config.INTENT_MAIN_DROPDOWN_ITEMS));
			
//			newIntent.putExtra(Config.FIELD_POSITION, pos);
			Item currentItem = (Item) intent.getExtras().get(Config.FIELD_SELECTED_ITEM);
			newIntent.putExtra(Config.FIELD_SELECTED_ITEM, currentItem);
			
//			newIntent.putExtra(Config.FIELD_CATEGORY, selectedItem.getCategory());
//			newIntent.putExtra(Config.FIELD_TITLE, selectedItem.getTitle());
//			newIntent.putExtra(Config.FIELD_CONTENT, selectedItem.getContent());
//			if (picturePath != null) newIntent.putExtra(Config.FIELD_PICTURE, selectedItem.getPicturePath());
			
			startActivityForResult(newIntent, Config.EDIT_ITEM_REQUEST);
		} else if (item.getItemId() == R.id.action_delete) {
//			Intent intent = getIntent();
//			int pos = intent.getExtras().getInt(Config.FIELD_POSITION);
			showConfirmDialog("Delete Item", "Are you sure you want to delete this item?");
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void closeActivity() {
		finish();
	}
	
	private AlertDialog confirmDialog;
	private void showConfirmDialog(String title, String message) {
		if (confirmDialog == null) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this)
				.setTitle(title)
				.setMessage(message)
				.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
//						Data.getInstance().getAllItems().remove(pos);
//						Data.getInstance().deleteItemAt(getBaseContext(), pos);
						
						Data.getInstance().deleteItem(getBaseContext(), currentItem);
						closeActivity();
						return;
					}
				})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						return;
					}
				});
			confirmDialog = builder.create();
			
		}
		confirmDialog.show();
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == Config.EDIT_ITEM_REQUEST) {
//				int pos = data.getExtras().getInt(Config.FIELD_POSITION);
//				long id = data.getExtras().getLong(Config.FIELD_INDEX);
//				String category = data.getExtras().getString(Config.FIELD_CATEGORY);
//				String title = data.getExtras().getString(Config.FIELD_TITLE);
//				String content = data.getExtras().getString(Config.FIELD_CONTENT);
//				String picturePath = data.getExtras().getString(Config.FIELD_PICTURE);
				
//				Item item = new Item(category, title, content);
				
				Item item = (Item) data.getExtras().get(Config.FIELD_SELECTED_ITEM);
				
//				if (picturePath != null && picturePath.length()>0) item.setPicturePath(picturePath);
				if (item.getId() != -1) {
//					item.setId(id);
					Data.getInstance().updateItem(this, item);
				} else {
					Data.getInstance().addItem(this, item);
				}
				
//				if (pos != -1) {
//					
//				} else {
//					Data.getInstance().addItem(this, item);
//				}
				finish();
			}
		}
	}

}
