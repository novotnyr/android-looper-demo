package sk.upjs.ics.looper;

import android.os.Handler;
import android.support.annotation.MainThread;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ComputingTaskHandler {
    public static final String TAG = ComputingTaskHandler.class.getName();

    private ExecutorService executorService = Executors.newFixedThreadPool(3);

    private List<ComputingTask> tasks = new ArrayList<>();

    private boolean isStarted;

    public void submit(int taskId, Handler handler) {
        ComputingTask computingTask = new ComputingTask(taskId, handler);
        this.executorService.submit(computingTask);
        this.tasks.add(computingTask);
        isStarted = true;
    }

    @MainThread
    public void resume() {
        if(!isStarted) {
            return;
        }

        Iterator<ComputingTask> iter = this.tasks.iterator();
        while(iter.hasNext()) {
            ComputingTask task = iter.next();
            if(task.isCompleted()) {
                iter.remove();
            } else {
                task.resume();
            }
        }
    }

    @MainThread
    public void pause() {
        if(!isStarted) {
            return;
        }

        Iterator<ComputingTask> iter = this.tasks.iterator();
        while(iter.hasNext()) {
            ComputingTask task = iter.next();
            if(task.isCompleted()) {
                iter.remove();
            } else {
                task.pause();
            }
        }
    }


    @MainThread
    public List<Runnable> shutdownNow() {
        return this.executorService.shutdownNow();
    }
}
