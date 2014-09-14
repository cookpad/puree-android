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
import com.example.loghouse.logs.plugins.OutDisplay;

import org.json.JSONObject;


public class MainActivity extends ActionBarActivity {
    private TextView logDisplayTextView;
    private Button logDisplayButton;

    private final OutDisplay.Callback outDisplayCallback = new OutDisplay.Callback() {
        @Override
        public void onEmit(JSONObject serializedLog) {
            logDisplayTextView.setText(new StringBuilder()
                    .append(serializedLog.toString())
                    .append(System.getProperty("line.separator"))
                    .append(logDisplayTextView.getText())
                    .toString());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        OutDisplay.register(outDisplayCallback);
        LogHouse.in(new PvLog(this));
        setupViews();
    }

    @Override
    protected void onDestroy() {
        OutDisplay.unregister();
        super.onDestroy();
    }

    private void findViews() {
        logDisplayTextView = (TextView) findViewById(R.id.log_display);
        logDisplayButton = (Button) findViewById(R.id.track_button);
    }

    private void setupViews() {
        logDisplayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                trackClickEvent();
            }
        });
    }

    private void trackClickEvent() {
        LogHouse.in(new ClickLog("MainActivity", "track"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
