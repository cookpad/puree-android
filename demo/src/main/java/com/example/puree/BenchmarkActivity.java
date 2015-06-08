package com.example.puree;

import com.cookpad.puree.Puree;
import com.example.puree.logs.BenchmarkLog;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class BenchmarkActivity extends AppCompatActivity {

    ArrayList<SendLogs> tasks = new ArrayList<>();


    ArrayAdapter<String> listAdapter;

    ListView listView;

    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_benchmark);

        listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        listView = (ListView) findViewById(android.R.id.list);
        listView.setAdapter(listAdapter);

        for (int i = 0; i < 10; i++) {
            tasks.add(new SendLogs());
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                System.gc();
                tasks.remove(0).execute();
            }
        }, 100);
    }

    class SendLogs extends AsyncTask<Void, Void, Void> {

        long t0;

        @Override
        protected Void doInBackground(Void... params) {
            for (int i = 0; i < 100; i++) {
                Puree.send(new BenchmarkLog("foo", "bar"));
            }
            Puree.flush();
            return null;
        }

        @Override
        protected void onPreExecute() {
            t0 = System.currentTimeMillis();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            listAdapter.add(++count + ": " + (System.currentTimeMillis() - t0) + "ms");

            if (!tasks.isEmpty()) {
                tasks.remove(0).execute();
            }
        }
    }

}