package com.ihelp101.dontswipe;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Set;

public class Respect extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.respect);

        Set<String> categories = getIntent().getCategories();
        if (categories != null) {
            if (categories.contains("de.robv.android.xposed.category.MODULE_SETTINGS")) {
                Intent intent = new Intent("android.intent.action.MAIN");
                intent.setComponent(ComponentName.unflattenFromString("com.ihelp101.dontswipe/com.ihelp101.dontswipe.Activity"));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }

        SharedPreferences prfs = getSharedPreferences("Prefs", Context.MODE_WORLD_READABLE);
        String text = prfs.getString("Text", "Default");

        try {
            if (!text.equals("Default")) {
                TextView textView = (TextView) findViewById(R.id.text);
                textView.setText(text);
            }
        } catch (Exception e) {

        }
    }

    @Override
    public void onBackPressed() {

    }
}
