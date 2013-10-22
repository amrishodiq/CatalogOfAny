package me.shodiq.amri.catalogofany.ui.adapter;

import java.io.File;

import me.shodiq.amri.catalogofany.Data;
import me.shodiq.amri.catalogofany.R;
import me.shodiq.amri.catalogofany.model.Item;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

public class ItemAdapter extends ArrayAdapter<Item> implements Filterable {
	private String category = "";
	
	public ItemAdapter(Context context, String category) {
		super(context, android.R.layout.simple_list_item_1);
		filter = new ItemFilter(category, this);
		
		this.category = category;
	}
	
	public void setCategory(String category) {
		((ItemFilter)getFilter()).setCategory(category);
		getFilter().filter("");
	}
	
	private Filter filter;
	@Override
	public Filter getFilter() {
		return filter;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
//		return super.getItemId(position);
		return getItem(position).getIndex();
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		final Item item = getItem(position);
		
//		if (convertView == null) {
			// sorry I must create new object in order not to show wrong async picture
			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService( Context.LAYOUT_INFLATER_SERVICE );
			convertView = inflater.inflate(R.layout.item_adapter, null);
//		}
		
		TextView title = (TextView) convertView.findViewById(R.id.item_title);
		TextView category = (TextView) convertView.findViewById(R.id.item_category);
		TextView content = (TextView) convertView.findViewById(R.id.item_content);
		final ImageView picture = (ImageView) convertView.findViewById(R.id.item_picture);
		View pictureHolder = convertView.findViewById(R.id.picture_holder);
		
		title.setText(item.getTitle());
		category.setText(item.getCategory());
		content.setText(item.getContent());
		
		if (item.getPicturePath() != null && item.getPicturePath().length() > 0) {
			Data.getInstance().loadBitmap(item.getId(), item.getPicturePath(), picture);
		} else {
			pictureHolder.setVisibility(View.GONE);
		}
		
		return convertView;
	}
	
	private static class ResizePictureTask extends AsyncTask<Void, Void, Void> {
		private ImageView pictureView;
		private String picturePath;
		private Bitmap scaledBitmap;
		public ResizePictureTask(ImageView pictureView, String picturePath) {
			this.pictureView = pictureView;
			this.picturePath = picturePath;
		}
		
		@Override
		protected Void doInBackground(Void... params) {
			File file = new File(picturePath);
			if (file.exists()) {
				Bitmap picture = BitmapFactory.decodeFile(picturePath);
				
				int width = picture.getWidth();
				int height = picture.getHeight();
				
				int preferredWidth = pictureView.getWidth();
				
				if (preferredWidth > 0) {
					Log.i(getClass().getName(), "Original width: "+width+". Preferred: "+preferredWidth);
					
					float scale = ((float)preferredWidth / (float)width);
					Log.i(getClass().getName(), "Scale to: "+scale);
					Matrix matrix = new Matrix();
				    matrix.postScale(scale, scale);
				    scaledBitmap = Bitmap.createBitmap(picture, 0, 0, width, height, matrix, true);
				}
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			if (scaledBitmap != null)
				pictureView.setImageBitmap(scaledBitmap);
		}
	}
	
}
