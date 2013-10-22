package me.shodiq.amri.catalogofany.ui.adapter;

import java.util.ArrayList;

import me.shodiq.amri.catalogofany.Data;
import me.shodiq.amri.catalogofany.model.Item;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Filter;

public class ItemFilter extends Filter {
	private String category;
	private ArrayAdapter<Item> owner;
	private ArrayList<Item> nlist;
	
	public ItemFilter(String category, ArrayAdapter<Item> owner) {
		this.category = category;
		this.owner = owner;
	}
	
	public void setCategory(String category) {
		this.category = category;
		filter("");
	}

	@Override
	protected FilterResults performFiltering(CharSequence constraint) {
		Log.i(getClass().getName(), "performFiltering "+constraint);
		String filterString = constraint.toString().toLowerCase();
		
		FilterResults results = new FilterResults();
		
		int count = Data.getInstance().getAllItems(owner.getContext()).size();
		nlist = new ArrayList<Item>(count);
		nlist.clear();

		String filterableString;
		Log.i(getClass().getName(), "Category \""+category+"\"");
		
		Item item;
		for (int i = 0; i < count; i++) {
			item = (Item)Data.getInstance().getItem(owner.getContext(), i);
			filterableString = item.getFilterable();
			
//			Log.i(getClass().getName(), "Category len "+item.getCategory().length());
			
			if (category.length()>0) {
				if (item.getCategory().equals(category) && filterableString.toLowerCase().contains(filterString)) {
					Log.i(getClass().getName(), "Add item "+item.toString());
					nlist.add(item);
				}
			} else {
				// if category == '' then always add item
				Log.i(getClass().getName(), "Add item "+item.toString());
				nlist.add(item);
			}
			
		}
		
		results.values = nlist;
		results.count = nlist.size();

		return results;
	}

	@Override
	protected void publishResults(CharSequence constraint, FilterResults results) {
		owner.clear();
		owner.addAll(nlist);
		owner.notifyDataSetChanged();
	}

}
