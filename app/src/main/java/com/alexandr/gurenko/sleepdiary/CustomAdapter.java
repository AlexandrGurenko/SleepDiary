package com.alexandr.gurenko.sleepdiary;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.TimeZone;

public class CustomAdapter extends ArrayAdapter<DreamList> {

    private List<DreamList> data;
    private Context context;

    CustomAdapter(Context context, List<DreamList> data) {
        super(context, 0, data);
        this.context = context;
        this.data = data;
        Collections.reverse(this.data);
    }

    @SuppressLint({"SetTextI18n", "InflateParams"})
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert vi != null;
            convertView = vi.inflate(R.layout.adapter, null);
        }
        TextView text1 = convertView.findViewById(R.id.mytext1);
        TextView text2 = convertView.findViewById(R.id.mytext2);
        TextView text3 = convertView.findViewById(R.id.mytext3);

        Calendar calendar = Calendar.getInstance();

        calendar.setTimeInMillis(data.get(position).getStartSleep());
        text1.setText("Уснул: " + calendar.get(Calendar.DAY_OF_MONTH) + "." +
                (calendar.get(Calendar.MONTH) + 1) + "." +
                calendar.get(Calendar.YEAR) + " в " +
                calendar.get(Calendar.HOUR_OF_DAY) + ":" +
                calendar.get(Calendar.MINUTE));

        calendar.setTimeInMillis(data.get(position).getEndSleep());
        text2.setText("Проснулся: " + calendar.get(Calendar.DAY_OF_MONTH) + "." +
                (calendar.get(Calendar.MONTH) + 1) + "." +
                calendar.get(Calendar.YEAR) + " в " +
                calendar.get(Calendar.HOUR_OF_DAY) + ":" +
                calendar.get(Calendar.MINUTE));

        calendar.setTimeZone(TimeZone.getTimeZone("GMT"));
        calendar.setTimeInMillis(data.get(position).getDuration());
        text3.setText("Длительность сна: " + calendar.get(Calendar.HOUR) + "ч. и " +
                calendar.get(Calendar.MINUTE) + "м.");
        return convertView;
    }
}
