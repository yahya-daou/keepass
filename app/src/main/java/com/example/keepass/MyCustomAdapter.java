package com.example.keepass;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class MyCustomAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final List<String> values;

    public MyCustomAdapter(Context context, List<String> values) {
        super(context, -1, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_items, parent, false);

        // Obtenez des références aux vues dans list_item_layout.xml
        ImageView imageView = rowView.findViewById(R.id.list_item_image);
        TextView textView = rowView.findViewById(R.id.list_item_text);

        // Définissez le texte avec la valeur actuelle de la liste
        textView.setText(values.get(position));

        // Définissez l'image par défaut (à remplacer par votre image)
        imageView.setImageResource(R.drawable.key5);

        return rowView;
    }
}
