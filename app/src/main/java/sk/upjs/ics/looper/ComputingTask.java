package sk.upjs.ics.looper;

import android.app.Activity;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.lang.ref.WeakReference;
import java.util.List;

public class ComputingTask implements Runnable {
    public static final String TAG = ComputingTask.class.getName();

    private Handler handler;

    private WeakReference<Activity> weakActivity;

    public ComputingTask(Handler handler, Activity activity) {
        this.handler = handler;
        this.weakActivity = new WeakReference<Activity>(activity);
    }

    @Override
    public void run() {
        while(true) {
            try {
                Thread.sleep(5000);
                Log.i(TAG, "Running " + this.hashCode());
                final String s = String.valueOf((int) (Math.random() * 10));

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Activity activity = weakActivity.get();
                        if(activity == null) {
                            Log.w(TAG, "No activity attached for " + ComputingTask.this.hashCode());
                            return;
                        }
                        ListView listView = (ListView) activity.findViewById(R.id.listView);
                        ((ArrayAdapter<String>) listView.getAdapter()).add(s);
                    }
                });
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}