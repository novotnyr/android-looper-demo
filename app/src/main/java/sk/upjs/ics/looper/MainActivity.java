package sk.upjs.ics.looper;

import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getName();

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.listView = (ListView) findViewById(R.id.listView);
        this.listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1));
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        Handler handler = new Handler(getMainLooper());

        ExecutorService executorService = Executors.newFixedThreadPool(3);
        executorService.submit(new ComputingTask(handler, this));
    }

    @Override
    public void finish() {
        Log.i(TAG, "Finishing activity");
        super.finish();
    }
}
