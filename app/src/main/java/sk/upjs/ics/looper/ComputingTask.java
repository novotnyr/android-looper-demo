package sk.upjs.ics.looper;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class ComputingTask implements Runnable {
    public static final String TAG = ComputingTask.class.getName();

    private int id;

    private Handler handler;

    private int progress;

    public ComputingTask(int id, Handler handler) {
        this.id = id;
        this.handler = handler;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Thread.sleep(1000);
                Log.i(TAG, "Running " + this.id);

                Message message = handler.obtainMessage(ComputingTask.this.id, progress);
                handler.sendMessage(message);

                if (progress >= 100) {
                    break;
                }
                this.progress = this.progress + 10;
            } catch (InterruptedException e) {
                Log.i(TAG, "Interrupting via exception " + this.id);
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}
