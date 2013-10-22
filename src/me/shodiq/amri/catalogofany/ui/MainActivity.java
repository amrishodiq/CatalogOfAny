package me.shodiq.amri.catalogofany.ui;

import java.util.ArrayList;

import me.shodiq.amri.catalogofany.Config;
import me.shodiq.amri.catalogofany.Data;
import me.shodiq.amri.catalogofany.R;
import me.shodiq.amri.catalogofany.R.id;
import me.shodiq.amri.catalogofany.R.layout;
import me.shodiq.amri.catalogofany.R.menu;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends FragmentActivity implements
		ActionBar.OnNavigationListener {

	private ArrayAdapter<String> mainDropdownAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

		mainDropdownAdapter = new ArrayAdapter<String>(
			actionBar.getThemedContext(), 
			android.R.layout.simple_list_item_1,
			android.R.id.text1,
			Data.getInstance().getAllCategories(this)
		);
		actionBar.setListNavigationCallbacks(mainDropdownAdapter, this);
	}
	
	private AlertDialog categoryInputDialog;
	private void showCategoryInputDialog() {
		if (categoryInputDialog == null) {
			final EditText categoryName = new EditText(this);
			categoryName.setHint("New category name");
			categoryName.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
			
			AlertDialog.Builder builder = new AlertDialog.Builder(this)
				.setTitle("Add Category")
				.setView(categoryName)
				.setPositiveButton("Save", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (categoryName.getText().toString().trim().length() > 0) {
							Data.getInstance().addCategory(getBaseContext(), categoryName.getText().toString());
							categoryName.setText("");
						}
						return;
					}
				})
				.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						return;
					}
				});
			categoryInputDialog = builder.create();
				
		}
		categoryInputDialog.show();
	}
	
	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		if (savedInstanceState.containsKey(Config.STATE_SELECTED_NAVIGATION_ITEM)) {
			getActionBar().setSelectedNavigationItem(
					savedInstanceState.getInt(Config.STATE_SELECTED_NAVIGATION_ITEM));
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putInt(Config.STATE_SELECTED_NAVIGATION_ITEM, getActionBar()
				.getSelectedNavigationIndex());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.addCategory) {
			showCategoryInputDialog();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private MainListFragment fragment = null;

	@Override
	public boolean onNavigationItemSelected(int position, long id) {
		if (fragment == null) {
			fragment = new MainListFragment();
			Bundle bundle = new Bundle();
			bundle.putString(Config.BUNDLE_KEY_TITLE, Data.getInstance().getAllCategories(this).get(position));
			
			fragment.setArguments(bundle);
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.container, fragment).commit();
		} else {
			if (Data.getInstance().getAllCategories(this).get(position).equals("All Items")) 
				fragment.setCategory("");
			else
				fragment.setCategory(Data.getInstance().getAllCategories(this).get(position));
		}
		
		return true;
	}



}
