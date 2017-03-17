package sk.upjs.ics.looper;

import android.os.Handler;
import android.util.Log;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ComputingTaskHandler {
    public static final String TAG = ComputingTaskHandler.class.getName();

    private ExecutorService executorService = Executors.newFixedThreadPool(3);;

    private List<ComputingTask> tasks = new CopyOnWriteArrayList<>();

    public void submit(int taskId, Handler handler) {
        ComputingTask computingTask = new ComputingTask(taskId, handler);
        this.executorService.submit(computingTask);
        this.tasks.add(computingTask);
    }

    public void pause() {
        for (final ComputingTask task : this.tasks) {
            task.pause();
        }
    }

    public void resume() {
        for (ComputingTask task : this.tasks) {
            task.resume();
        }
    }

    public List<Runnable> shutdownNow() {
        return this.executorService.shutdownNow();
    }
}
