package sk.upjs.ics.looper;

import android.os.Handler;
import android.os.Message;
import android.support.annotation.MainThread;
import android.support.annotation.WorkerThread;
import android.util.Log;

import java.util.concurrent.Semaphore;

/*
Scenarios:

    *   Semaphore is acquired in the run()
    *   Activity is paused
    *   upon pause, activity either sets the semaphore to 0 or does nothing. Furthermore,
    *   a flag is raised indicating a paused status.
    *   in the next iteration of while(), the semaphore will be awaited until the activity is resuumed
    *   if the activity is paused in the middle of the run iteration, the semaphore
    *   will not be raised at the end of the iteration. (Otherwise, it can be acquired in
    *   the next run, thus reactivating the task).

 */
public class ComputingTask implements Runnable {
    public static final String TAG = ComputingTask.class.getName();

    /**
     * Task ID, arbirary number
     */
    private int id;

    /**
     * Handler that shall be posted with the progress.
     */
    private Handler handler;

    /**
     * Semaphore indicationg that the task can proceed
     */
    private Semaphore semaphore = new Semaphore(1);

    /**
     * Paused flag that is raised by the activity.
     * Volatile, since it can be accessed across threads.
     */
    private volatile boolean paused;

    /**
     * Completed task flag that is raised by the activity.
     * Volatile, since it can be accessed across threads.
     */
    private volatile boolean completed;

    public ComputingTask(int id, Handler handler) {
        this.id = id;
        this.handler = handler;
    }

    @Override
    @WorkerThread
    public void run() {
        int progress = 0;
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
                progress += 10;
            } catch (InterruptedException e) {
                Log.i(TAG, "Interrupting via exception " + this.id);
                Thread.currentThread().interrupt();
                break;
            } finally {
                if(!paused) {
                    this.semaphore.release();
                }
            }
        }
        this.completed = true;
    }

    @MainThread
    public void pause() {
        this.paused = true;
        this.semaphore.tryAcquire();

        Log.i(TAG, "Computing task " + this.id + " is paused");
    }

    @MainThread
    public void resume() {
        this.semaphore.release();
        this.paused = false;

        Log.i(TAG, "Computing task " + this.id + " is resumed");
    }

    public boolean isCompleted() {
        return completed;
    }
}
