package com.liangmayong.reform_demo;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.liangmayong.eventsink.EventSink;
import com.liangmayong.preferences.Preferences;
import com.liangmayong.reform.interfaces.OnReformListener;
import com.liangmayong.reform.Reform;
import com.liangmayong.reform.ReformResponse;
import com.liangmayong.reform.errors.ReformError;
import com.liangmayong.reform_demo.reform.ReModule;

import org.json.JSONException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Preferences.init(getApplication(), true);
        EventSink.init(getApplication(), true);

        EventSink.getDefault().register(this, new String[]{"main"}, new EventSink.OnEventListener() {
            @Override
            public void onEvent(Context context, EventSink.EventContent content) {
                Toast.makeText(getApplicationContext(), content.getAction() + "/" + content.getWhat(), Toast.LENGTH_SHORT).show();
            }
        });

        EventSink.getDefault().getSender("main").send(0).sendDelayed(1, 10000);
        Toast.makeText(getApplicationContext(), Preferences.getDefaultPreferences().getString("testBody", ""), Toast.LENGTH_SHORT).show();
        Reform.getModuleInstance(ReModule.class).getConfig(this, new OnReformListener() {
            @Override
            public void onResponse(ReformResponse response) {
                Preferences.getDefaultPreferences().setString("testBody", response.getBody());
                if (response.isSuccess()) {
                    try {
                        if (response.parseJsonArray("return_value").length() > 0) {
                            String name = response.parseJsonArray("return_value").getJSONObject(0).getString("name");
                            String color = response.parseJsonArray("return_value").getJSONObject(0).getString("color");
                            String fullname = response.parseJsonArray("return_value").getJSONObject(0).getString("fullname");
                            String logo_url = response.parseJsonArray("return_value").getJSONObject(0).getString("logo_url");
                            Toast.makeText(getApplicationContext(), name + "\n" + color + "\n" + fullname + "\n" + logo_url, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(getApplicationContext(), response.parseJsonInt("timestamp", 0) + "", Toast.LENGTH_SHORT).show();
                    Toast.makeText(getApplicationContext(), response.getBody(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(getApplicationContext(), response.getConsumingTime() + "", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), response.getErrorMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(ReformError reformError) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Reform.getModuleInstance(ReModule.class).destroy(this);
        EventSink.unregisterAll(this);
    }
}
