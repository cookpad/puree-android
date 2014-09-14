package com.example.loghouse;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cookpad.android.loghouse.LogHouse;
import com.example.loghouse.logs.ClickLog;
import com.example.loghouse.logs.PvLog;
import com.example.loghouse.logs.plugins.OutBufferedDisplay;
import com.example.loghouse.logs.plugins.OutDisplay;

import org.json.JSONObject;

import java.util.List;

public class MainActivity extends ActionBarActivity {
    private TextView logDisplayTextView;
    private Button logDisplayButton;
    private Button logBufferedDisplayButton;

    private final OutDisplay.Callback outDisplayCallback = new OutDisplay.Callback() {
        @Override
        public void onEmit(JSONObject serializedLog) {
            preprendOutput(serializedLog.toString());
        }
    };

    private final OutBufferedDisplay.Callback outBufferedDisplayCallback = new OutBufferedDisplay.Callback() {
        @Override
        public void onEmit(List<JSONObject> serializedLogs) {
            preprendOutput(serializedLogs.toString());
        }
    };

    private void preprendOutput(String text) {
        logDisplayTextView.setText(new StringBuilder()
                .append(text)
                .append(System.getProperty("line.separator"))
                .append(logDisplayTextView.getText())
                .toString());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        OutDisplay.register(outDisplayCallback);
        OutBufferedDisplay.register(outBufferedDisplayCallback);
        LogHouse.in(new PvLog(this));
        setupViews();
    }

    @Override
    protected void onDestroy() {
        OutDisplay.unregister();
        OutBufferedDisplay.unregister();
        super.onDestroy();
    }

    private void findViews() {
        logDisplayTextView = (TextView) findViewById(R.id.log_display);
        logDisplayButton = (Button) findViewById(R.id.display_button);
        logBufferedDisplayButton = (Button) findViewById(R.id.buffered_display_button);
    }

    private void setupViews() {
        logDisplayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogHouse.in(new ClickLog("MainActivity", "track", OutDisplay.TYPE));
            }
        });
        logBufferedDisplayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogHouse.in(new ClickLog("MainActivity", "track", OutBufferedDisplay.TYPE));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
