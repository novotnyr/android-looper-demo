package sk.upjs.ics.looper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

public class ProgressBarAdapter extends BaseAdapter {
    private final LayoutInflater inflater;

    private final int itemResource = R.layout.item_progressbar;

    private List<Integer> progresses = new ArrayList<>();

    public ProgressBarAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
    }

    public void add(Integer progress) {
        this.progresses.add(progress);
        notifyDataSetChanged();
    }

    public void setProgress(int position, Integer progress) {
        if (position >= this.progresses.size()) {
            throw new IllegalArgumentException("Out of bounds position");
        }
        this.progresses.set(position, progress);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return this.progresses.size();
    }

    @Override
    public Object getItem(int position) {
        return this.progresses.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null) {
            view = inflater.inflate(itemResource, parent, false);
        } else {
            view = convertView;
        }
        ProgressBar progressBar = (ProgressBar) view;
        progressBar.setProgress(this.progresses.get(position));

        return view;
    }
}
