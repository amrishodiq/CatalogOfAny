package me.shodiq.amri.catalogofany;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Hashtable;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.widget.ImageView;

import me.shodiq.amri.catalogofany.database.CategoryDataSource;
import me.shodiq.amri.catalogofany.database.ItemDataSource;
import me.shodiq.amri.catalogofany.model.Item;

public class Data {
	private static Data instance = null;
	public static Data getInstance() {
		if (instance == null) instance = new Data();
		return instance;
	}
	
	private Data() {
	}
	
	private ArrayList<String> allCategories;
	public ArrayList<String> getAllCategories(Context context) {
		if (allCategories == null) {
			allCategories = CategoryDataSource.getInstance(context).readAll();
			allCategories.add(0, "All Items");
		}
		return allCategories;
	}
	
	public synchronized void addCategory(Context context, String categoryName) {
		String category = CategoryDataSource.getInstance(context).create(categoryName);
		if (category != null) allCategories.add(category);
	}
	
	private ArrayList<Item> allItems;
	public ArrayList<Item> getAllItems(Context context) {
		if (allItems == null) {
			allItems = ItemDataSource.getInstance(context).readAll();
		}
		return allItems;
	}
	
	public synchronized void addItem(Context context, Item item) {
		Item insertedItem = ItemDataSource.getInstance(context).create(item.getCategory(), item.getTitle(), item.getContent(), item.getPicturePath());
		
		if (insertedItem != null) {
			getAllItems(context).add(insertedItem);
			insertedItem.setIndex(allItems.size()-1);
		}
	}
	
	public synchronized void deleteItem(Context context, Item item) {
		unregisterImage(item.getPicturePath());
		boolean succeed = ItemDataSource.getInstance(context).delete(item);
		if (succeed) {
			allItems = ItemDataSource.getInstance(context).readAll();
		}
//			getAllItems(context).remove(item.getIndex());
//			getAllItems(context).remove(item);
	}
	
	public synchronized void deleteItemAt(Context context, int index) {
		Item item = getAllItems(context).get(index);
		deleteItem(context, item);
//		getAllItems(context).remove(index);
	}
	
	public void updateItem(Context context, Item item) {
		Log.i(getClass().getName(), "updateItem "+item.getComplete());
		
		int rowNumber = ItemDataSource.getInstance(context).update(item);
		if (rowNumber == 1) {
			Log.i(getClass().getName(), "updateItem succeed "+item.getComplete());
			getAllItems(context).set(item.getIndex(), item);
		}
		if (rowNumber == 0) {}
		else allItems = ItemDataSource.getInstance(context).readAll();
	}
	
	public Item getItem(Context context, int index) {
		Item item = getAllItems(context).get(index); 
//		Log.i(getClass().getName(), "getItem "+item.getComplete());
		return item;
	}
	

//	// Harusnya ngikutin tutorial ini:
//	// http://developer.android.com/training/displaying-bitmaps/cache-bitmap.html
//	// Pake yang Disk Cache
//	private LruCache<String, Bitmap> images;
//	private final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
	
//	public Bitmap getImage(String path, ImageView pictureView) {
//		if (images == null) images = new LruCache<String, Bitmap>(maxMemory / 8) {
//			protected int sizeOf(String key, Bitmap bitmap) {
//	            return bitmap.getByteCount() / 1024;
//	        }
//		};
//		
//		if (images.get(path) != null) return images.get(path);
//		else {
//			if (images.get(path) == null) {
//				File file = new File(path);
//				if (file.exists()) {
////					Bitmap picture = BitmapFactory.decodeFile(path);
////					Bitmap picture = decodeSampledBitmapFromFile(path, pictureView.getWidth(), pictureView.getHeight());
//					loadBitmap(path, pictureView);
////					registerImage(path, picture);
//					return null;
//				} else return null;
//			}
//			return null;
//		}
//	}
	
	// Harusnya ngikutin tutorial ini:
		// http://developer.android.com/training/displaying-bitmaps/cache-bitmap.html
		// Pake yang Disk Cache
	private LruCache<String, Bitmap> images;
	private final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
	
	public void registerImage(String path, Bitmap bitmap) {
		if (getCache().get(path) == null) getCache().put(path, bitmap);
	}
	
	public void unregisterImage(String path) {
		if (getCache().get(path) == null) getCache().remove(path);
	}
	
	private LruCache<String, Bitmap> getCache() {
		if (images == null) images = new LruCache<String, Bitmap>(maxMemory / 8) {
			protected int sizeOf(String key, Bitmap bitmap) {
	            return bitmap.getByteCount() / 1024;
	        }
		};
		return images;
	}
	
	private int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
	    // Raw height and width of image
	    final int height = options.outHeight;
	    final int width = options.outWidth;
	    int inSampleSize = 1;
	
	    if (height > reqHeight || width > reqWidth) {
	
	        // Calculate ratios of height and width to requested height and width
	        final int heightRatio = Math.round((float) height / (float) reqHeight);
	        final int widthRatio = Math.round((float) width / (float) reqWidth);
	
	        // Choose the smallest ratio as inSampleSize value, this will guarantee
	        // a final image with both dimensions larger than or equal to the
	        // requested height and width.
	        inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
	    }
	
	    return inSampleSize;
	}
	
	private Bitmap decodeSampledBitmapFromFile(String filePath,
	        int reqWidth, int reqHeight) {

	    // First decode with inJustDecodeBounds=true to check dimensions
	    final BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inJustDecodeBounds = true;
//	    BitmapFactory.decodeResource(res, resId, options);
	    BitmapFactory.decodeFile(filePath);

	    // Calculate inSampleSize
	    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

	    // Decode bitmap with inSampleSize set
	    options.inJustDecodeBounds = false;
	    return BitmapFactory.decodeFile(filePath, options);
//	    return BitmapFactory.decodeResource(res, resId, options);
	}
	
	private Bitmap defaultImage;
	
	public void loadBitmap(long itemId, String filePath, ImageView imageView) {
		final Bitmap image = getCache().get(filePath);
		if (image != null) imageView.setImageBitmap(image);
		else if (cancelPotentialWork(filePath, imageView)) {
			final BitmapWorkerTask task = new BitmapWorkerTask(itemId, filePath, imageView);
			
			if (defaultImage == null) {
				defaultImage = BitmapFactory.decodeResource(imageView.getResources(), R.drawable.picture_holder);
			}
			
			final AsyncDrawable asyncDrawable = new AsyncDrawable(imageView.getResources(), defaultImage, task);
			imageView.setImageDrawable(asyncDrawable);
			task.execute();
		}
	}
	
	private boolean cancelPotentialWork(String filePath, ImageView imageView) {
	    final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);

	    if (bitmapWorkerTask != null) {
	        final String bitmapData = bitmapWorkerTask.filePath;
	        if (!bitmapData.equals(filePath)) {
	            // Cancel previous task
	            bitmapWorkerTask.cancel(true);
	        } else {
	            // The same work is already in progress
	            return false;
	        }
	    }
	    // No task associated with the ImageView, or an existing task was cancelled
	    return true;
	}
	
	private BitmapWorkerTask getBitmapWorkerTask(ImageView imageView) {
		   if (imageView != null) {
		       final Drawable drawable = imageView.getDrawable();
		       if (drawable instanceof AsyncDrawable) {
		           final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
		           return asyncDrawable.getBitmapWorkerTask();
		       }
		    }
		    return null;
	}
	
	private class AsyncDrawable extends BitmapDrawable {
	    private final WeakReference<BitmapWorkerTask> bitmapWorkerTaskReference;

	    public AsyncDrawable(Resources res, Bitmap bitmap,
	            BitmapWorkerTask bitmapWorkerTask) {
	        super(res, bitmap);
	        bitmapWorkerTaskReference =
	            new WeakReference<BitmapWorkerTask>(bitmapWorkerTask);
	    }

	    public BitmapWorkerTask getBitmapWorkerTask() {
	        return bitmapWorkerTaskReference.get();
	    }
	}
	
	private class BitmapWorkerTask extends AsyncTask<Void, Void, Bitmap> {
//	    private final WeakReference<ImageView> imageViewReference;
//	    private int data = 0;
		private ImageView imageView;
	    private String filePath;
	    private long itemId;

	    public BitmapWorkerTask(long itemId, String filePath, ImageView imageView) {
	        // Use a WeakReference to ensure the ImageView can be garbage collected
//	        imageViewReference = new WeakReference<ImageView>(imageView);
	        this.filePath = filePath;
	        this.itemId = itemId;
	        this.imageView = imageView;
	    }

	    // Decode image in background.
	    @Override
	    protected Bitmap doInBackground(Void... params) {
//	        data = params[0];
	    	// check if cached file exists
//	    	String cachedFilePath = CACHE_LOCATION + File.separator + itemId + ".jpg";
//	    	File file = new File(cachedFilePath);
//	    	if(file.exists()) return decodeSampledBitmapFromFile(cachedFilePath, 100, 100);
	    	
	    	Bitmap bitmap = decodeSampledBitmapFromFile(filePath, imageView.getWidth(), imageView.getHeight()); 
//	    	if (!filePath.startsWith(CACHE_LOCATION)) storeImage(itemId, bitmap);
        	
        	registerImage(filePath, bitmap);
	    	
	        return bitmap;
	    }

	    // Once complete, see if ImageView is still around and set bitmap.
	    @Override
	    protected void onPostExecute(Bitmap bitmap) {
//	        if (imageViewReference != null && bitmap != null) {
	    	if (imageView != null && bitmap != null) {
//	            ImageView imageView = imageViewReference.get();
	            if (imageView != null) {
	                imageView.setImageBitmap(bitmap);
	            }
	        }
	    }
	}

//	private String CACHE_LOCATION = Environment.getExternalStorageDirectory()
//            + "/Android/data/"
//            + "CatalogOfAny"
//            + "/Files";
//	
//	private void storeImage(long itemId, Bitmap image) {
//	    File pictureFile = getOutputMediaFile(itemId);
//	    if (pictureFile == null) {
//	        Log.d(getClass().getName(), "Error creating media file, check storage permissions: ");// e.getMessage());
//	        return;
//	    } 
//	    if (!pictureFile.exists()) {
//	    	try {
//		        FileOutputStream fos = new FileOutputStream(pictureFile);
//		        image.compress(Bitmap.CompressFormat.PNG, 90, fos);
//		        fos.close();
//		    } catch (FileNotFoundException e) {
//		        Log.d(getClass().getName(), "File not found: " + e.getMessage());
//		    } catch (IOException e) {
//		        Log.d(getClass().getName(), "Error accessing file: " + e.getMessage());
//		    }
//	    } 
//	}
//	
//	private  File getOutputMediaFile(long itemId){
//	    // To be safe, you should check that the SDCard is mounted
//	    // using Environment.getExternalStorageState() before doing this. 
//	    File mediaStorageDir = new File(CACHE_LOCATION); 
//
//	    // This location works best if you want the created images to be shared
//	    // between applications and persist after your app has been uninstalled.
//
//	    // Create the storage directory if it does not exist
//	    if (! mediaStorageDir.exists()){
//	        if (! mediaStorageDir.mkdirs()){
//	            return null;
//	        }
//	    } 
//	    // Create a media file name
//	    File mediaFile = new File(mediaStorageDir.getPath() + File.separator + itemId +".jpg");  
//	    return mediaFile;
//	}
	
}
