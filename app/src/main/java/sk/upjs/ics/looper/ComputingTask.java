package sk.upjs.ics.looper;

import android.os.Handler;
import android.util.Log;

public class ComputingTask implements Runnable {
    public static final String TAG = ComputingTask.class.getName();

    private int id;

    private Handler handler;

    private OnResultListener onResultListener;

    private int progress;

    public ComputingTask(int id, Handler handler, OnResultListener onResultListener) {
        this.id = id;
        this.handler = handler;
        this.onResultListener = onResultListener;
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            try {
                Thread.sleep(1000);
                Log.i(TAG, "Running " + this.id);

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        onResultListener.onResult(ComputingTask.this.id, progress);
                    }
                });

                if (progress >= 100) {
                    break;
                }
                this.progress = this.progress + 10;
            } catch (InterruptedException e) {
                Log.i(TAG, "Interrupting via exception " + this.id);
                break;
            }
        }
    }

    public void setOnResultListener(OnResultListener onResultListener) {
        this.onResultListener = onResultListener;
    }

    public void removeOnResultListener() {
        this.onResultListener = null;
    }

    public interface OnResultListener {
        void onResult(int id, int result);
    }
}