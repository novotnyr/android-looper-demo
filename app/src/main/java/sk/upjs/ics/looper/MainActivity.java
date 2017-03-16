package sk.upjs.ics.looper;

import android.os.Handler;
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

public class MainActivity extends AppCompatActivity implements ComputingTask.OnResultListener {
    public static final String TAG = MainActivity.class.getName();

    private ListView listView;

    private ExecutorService executorService;
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

        executorService = Executors.newFixedThreadPool(3);
    }

    public void startTask() {
        Handler handler = new Handler(getMainLooper());

        int taskId = this.adapter.getCount();
        this.adapter.add(0);

        executorService.submit(new ComputingTask(taskId, handler, this));
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    public void finish() {
        Log.i(TAG, "Finishing activity");
        List<Runnable> incompleteTasks = this.executorService.shutdownNow();
        Log.i(TAG, "There are " + incompleteTasks.size() + " incomplete tasks");
        super.finish();
    }

    @Override
    public void onResult(int id, int result) {
        this.adapter.setProgress(id, result);
    }

    public void onFabClick(View view) {
        startTask();
    }

}
