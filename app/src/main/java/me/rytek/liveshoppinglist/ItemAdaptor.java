package me.rytek.liveshoppinglist;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ItemAdaptor extends ArrayAdapter<Item> {
    // View lookup cache
    private static class ViewHolder {
        TextView title;
        TextView addedBy;
        TextView comments;
    }

    int resource;

    public ItemAdaptor(@NonNull Context context, int resource, @NonNull List<Item> objects) {
        super(context, resource, objects);
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Item item = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            // If there's no view to re-use, inflate a brand new view for row
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(resource, parent, false);
            viewHolder.title = (TextView) convertView.findViewById(R.id.item_title);
            viewHolder.addedBy = (TextView) convertView.findViewById(R.id.item_added_by);
            viewHolder.comments = (TextView) convertView.findViewById(R.id.item_comments);
            // Cache the viewHolder object inside the fresh view
            convertView.setTag(viewHolder);
        }
        else {
            // View is being recycled, retrieve the viewHolder object from tag
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Populate the data into the template view using the data object
        viewHolder.title.setText(item.quantity < 2 ? item.item : item.quantity + " " + item.item);
        viewHolder.addedBy.setText(item.added_by);
        viewHolder.comments.setText(item.comments);

        if (item.bought) {
            viewHolder.title.setPaintFlags(viewHolder.title.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        else {
            viewHolder.title.setPaintFlags(viewHolder.title.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
        }
        // Return the completed view to render on screen
        return convertView;
    }
}
