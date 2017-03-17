package sk.upjs.ics.looper;

import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static android.R.attr.id;

public class MainActivity extends AppCompatActivity implements Handler.Callback {
    public static final String TAG = MainActivity.class.getName();

    private ListView listView;

    private ComputingTaskHandler computingTaskHandler;

    private ProgressBarAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.listView = (ListView) findViewById(R.id.listView);
        adapter = new ProgressBarAdapter(this);
        this.listView.setAdapter(adapter);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        this.computingTaskHandler = new ComputingTaskHandler();
    }

    public void startTask() {
        Handler handler = new Handler(getMainLooper(), this);

        int taskId = this.adapter.getCount();
        this.adapter.add(0);

        this.computingTaskHandler.submit(taskId, handler);
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.computingTaskHandler.resume();
    }

    @Override
    protected void onPause() {
        this.computingTaskHandler.pause();
        super.onPause();
    }


    @Override
    public void finish() {
        Log.i(TAG, "Finishing activity");
        List<Runnable> incompleteTasks = this.computingTaskHandler.shutdownNow();
        Log.i(TAG, "There are " + incompleteTasks.size() + " incomplete tasks");
        super.finish();
    }

    public void onFabClick(View view) {
        startTask();
    }

    @Override
    public boolean handleMessage(Message message) {
        int id = message.what;
        Integer progress = (Integer) message.obj;

        this.adapter.setProgress(id, progress);

        return true;
    }
}
