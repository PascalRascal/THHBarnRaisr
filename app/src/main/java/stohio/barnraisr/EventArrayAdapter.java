package stohio.barnraisr;

import android.app.usage.UsageEvents;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by csinko on 12/18/2015.
 */
public class EventArrayAdapter extends ArrayAdapter<event> {
    private final Context context;
    private final ArrayList<event> values;

    public EventArrayAdapter(Context context, ArrayList<event> values) {
        super(context, R.layout.event_preview, values);
        this.values = values;
        this.context = context;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View eventPreview = inflator.inflate(R.layout.event_preview, parent, false);

        event data = getItem(position);

        TextView title = (TextView) eventPreview.findViewById(R.id.title);
        TextView date = (TextView) eventPreview.findViewById(R.id.date);
        TextView location = (TextView) eventPreview.findViewById(R.id.location);
        ImageView profile = (ImageView) eventPreview.findViewById(R.id.thumbnail);

        title.setText(data.getEventTitle());
        date.setText(data.getEventDate());
        location.setText(Math.round(Double.parseDouble(data.getEventLat())) + ", " + Math.round(Double.parseDouble(data.getEventLong())));


        return eventPreview;
    }
}