package com.example.keepass;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MyCustomAdapter extends ArrayAdapter<String> implements Filterable {
    private final Context context;
    private final List<String> values;
    private List<String> originalList;
    private List<String> filteredList;
    private ItemFilter filter = new ItemFilter();

    public MyCustomAdapter(Context context, List<String> values) {
        super(context, -1, values);
        this.context = context;
        this.values = values;
        this.originalList = new ArrayList<>(values);
        this.filteredList = new ArrayList<>(values);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_items, parent, false);

        ImageView imageView = rowView.findViewById(R.id.list_item_image);
        TextView textView = rowView.findViewById(R.id.list_item_text);

        textView.setText(filteredList.get(position));  // Utilisez filteredList ici
        imageView.setImageResource(R.drawable.key5);

        return rowView;
    }

    @Override
    public int getCount() {
        return filteredList.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if (constraint != null && constraint.length() > 0) {
                List<String> filteredItems = new ArrayList<>();
                for (String item : originalList) {
                    if (item.toLowerCase().contains(constraint.toString().toLowerCase())) {
                        filteredItems.add(item);
                    }
                }
                results.count = filteredItems.size();
                results.values = filteredItems;
            } else {
                results.count = originalList.size();
                results.values = originalList;
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredList = (List<String>) results.values;
            notifyDataSetChanged();
        }
    }
}
