package com.liangmayong.reform_demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.liangmayong.reform.OnReformListener;
import com.liangmayong.reform.Reform;
import com.liangmayong.reform.ReformResponse;
import com.liangmayong.reform.error.ReformError;
import com.liangmayong.reform_demo.reform.ReModule;

import org.json.JSONException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Reform.createModule(ReModule.class).getConfig(this, new OnReformListener() {
            @Override
            public void onResponse(ReformResponse response) {
                if (response.isSuccess()) {

                    try {
                        response.parseJsonString("description");
                        String name = response.parseJsonArray("return_value").getJSONObject(0).getString("name");
                        String color = response.parseJsonArray("return_value").getJSONObject(0).getString("color");
                        String fullname = response.parseJsonArray("return_value").getJSONObject(0).getString("fullname");
                        String logo_url = response.parseJsonArray("return_value").getJSONObject(0).getString("logo_url");
                        Toast.makeText(getApplicationContext(), name + "\n" + color + "\n" + fullname + "\n" + logo_url, Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(getApplicationContext(), response.parseJsonInt("timestamp", 0) + "", Toast.LENGTH_SHORT).show();
                    Toast.makeText(getApplicationContext(), response.getBody(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), response.getErrorMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(ReformError reformError) {

            }
        });
    }
}