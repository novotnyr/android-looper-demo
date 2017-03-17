package sk.upjs.ics.looper;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;

/*
Scenarios:

    *   semaphore is acquired in the run()
    *   activity is paused
    *   UI sets semaphore to 0
    *   semaphore CANNOT be released in the run(), since this will erroneously trigger the next run()
    *   therefore we toggle a boolean flag to prevent releasing the semaphore in the paused activity
 */
public class ComputingTask implements Runnable {
    public static final String TAG = ComputingTask.class.getName();

    private int id;

    private Handler handler;

    private int progress;

    private Semaphore semaphore = new Semaphore(1);

    private AtomicBoolean paused = new AtomicBoolean();

    public ComputingTask(int id, Handler handler) {
        this.id = id;
        this.handler = handler;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                this.semaphore.acquire();
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
            } finally {
                if(!paused.get()) {
                    this.semaphore.release();
                }
            }
        }
    }

    /**
     * Runs on UI thread
     */
    public void pause() {
        Log.i(TAG, "Acquiring on pause()");

        this.paused.set(true);
        this.semaphore.drainPermits();

        Log.i(TAG, "Acquired on pause()");
    }

    /**
     * Runs on UI thread
     */
    public void resume() {
        Log.i(TAG, "Releasing on resume()");

        this.semaphore.release();
        this.paused.set(false);

        Log.i(TAG, "Released on resume()");
    }
}
