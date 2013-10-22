package me.shodiq.amri.catalogofany.ui;

import me.shodiq.amri.catalogofany.Config;
import me.shodiq.amri.catalogofany.Data;
import me.shodiq.amri.catalogofany.R;
import me.shodiq.amri.catalogofany.model.Item;
import me.shodiq.amri.catalogofany.ui.adapter.ItemAdapter;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

public class MainListFragment extends Fragment implements OnItemClickListener {
	
	public MainListFragment() {
		
	}
	
	private ArrayAdapter<Item> listAdapter;
	private ListView list;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// this will enable the fragment to put another action menu
		setHasOptionsMenu(true);
	}
	
	public void setCategory(String categoryName) {
		((ItemAdapter)listAdapter).setCategory(categoryName);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.fragment_main_list,
				container, false);
	
		ImageButton addItemButton = (ImageButton) root.findViewById(R.id.add_item_button);
		addItemButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	showAddItemActivity();
            }
        });
		
		list = (ListView) root.findViewById(R.id.main_list);
		
		listAdapter = new ItemAdapter(getActivity(), "");
		list.setAdapter(listAdapter);
		
		list.setOnItemClickListener(this);
		
		TextView emptyIndicator = (TextView) root.findViewById(R.id.empty_list);
		list.setEmptyView(emptyIndicator);
		
		return root;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		refreshList();
	}
	
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.main_list_fragment, menu);
	}
	
	private void showAddItemActivity() {
		Intent intent = new Intent(getActivity(), EditItemActivity.class);
    	intent.putStringArrayListExtra(Config.INTENT_MAIN_DROPDOWN_ITEMS, Data.getInstance().getAllCategories(getActivity()));
    	intent.putExtra(Config.FIELD_POSITION, -1);
    	
    	startActivityForResult(intent, Config.ADD_ITEM_REQUEST);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.addItem) {
			
			showAddItemActivity();
        	
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void refreshList() {
		listAdapter.clear();
		listAdapter.addAll(Data.getInstance().getAllItems(getActivity()));
		
		if (listAdapter instanceof ItemAdapter) listAdapter.getFilter().filter("");
		listAdapter.notifyDataSetChanged();
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == Config.ADD_ITEM_REQUEST) {
				String category = data.getExtras().getString(Config.FIELD_CATEGORY);
				String title = data.getExtras().getString(Config.FIELD_TITLE);
				String content = data.getExtras().getString(Config.FIELD_CONTENT);
				String picturePath = data.getExtras().getString(Config.FIELD_PICTURE);
				
//				if (picturePath != null) Log.i(getClass().getName(), "Picture path: "+picturePath);
				
//				Item item = new Item(category, title, content);
				Item item = (Item) data.getExtras().get(Config.FIELD_SELECTED_ITEM);
				if (picturePath != null) item.setPicturePath(picturePath);
				
				Data.getInstance().addItem(getActivity(), item);
				refreshList();
				
			} else if (requestCode == Config.EDIT_ITEM_REQUEST) {
				int pos = data.getExtras().getInt(Config.FIELD_POSITION);
				String category = data.getExtras().getString(Config.FIELD_CATEGORY);
				String title = data.getExtras().getString(Config.FIELD_TITLE);
				String content = data.getExtras().getString(Config.FIELD_CONTENT);
				String picturePath = data.getExtras().getString(Config.FIELD_PICTURE);
				
//				if (picturePath != null) Log.i(getClass().getName(), "Picture path: "+picturePath);
				
//				Item item = new Item(category, title, content);
				Item item = (Item) data.getExtras().get(Config.FIELD_SELECTED_ITEM);
				if (picturePath != null) item.setPicturePath(picturePath);
				
				
				if (pos != -1) {
					Data.getInstance().updateItem(getActivity(), item);
					refreshList();
				} else {
					Data.getInstance().addItem(getActivity(), item);
					refreshList();
				}
			} 
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	@Override
	public void onItemClick(AdapterView<?> adapter, View view, int position, long arg3) {
		if (adapter == list) {
			
			Item selectedItem = listAdapter.getItem(position);
			
			Intent intent = new Intent(getActivity(), ItemDetailActivity.class);
//			intent.putStringArrayListExtra(Config.INTENT_MAIN_DROPDOWN_ITEMS, Data.getInstance().getAllCategories(getActivity()));
			
//			intent.putExtra(Config.FIELD_POSITION, position);

			intent.putExtra(Config.FIELD_SELECTED_ITEM, selectedItem);
			
			startActivity(intent);
		}
	}
}
