package sk.upjs.ics.looper;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import java.util.List;

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
