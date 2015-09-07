package com.ihelp101.dontswipe;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class Activity extends ActionBarActivity {

    int selected;
    RadioButton crash;
    RadioButton home;
    RadioButton text;
    SharedPreferences prfs;
    SharedPreferences hooksPrefs;
    String app;
    String data = "Default";
    String previousChoice;
    String version = "123";

    class RequestTask extends AsyncTask<String, String, String> {

        String packageName;

        @Override
        protected String doInBackground(String... uri) {

            packageName = uri[1];

            if (packageName.equals("com.cyngn.gallerynext")) {
                hooksPrefs = getSharedPreferences("Cyanogen", Context.MODE_WORLD_READABLE);
                app = "Cyanogen";
            }

            if (packageName.equals("com.google.android.apps.photos")) {
                hooksPrefs = getSharedPreferences("Google", Context.MODE_WORLD_READABLE);
                app = "Google";
            }

            if (packageName.equals("com.diune.media")) {
                hooksPrefs = getSharedPreferences("Pikture", Context.MODE_WORLD_READABLE);
                app = "Pikture";
            }


            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response;
            String responseString = null;
            try {
                response = httpclient.execute(new HttpGet(uri[0]));
                StatusLine statusLine = response.getStatusLine();
                if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    response.getEntity().writeTo(out);
                    responseString = out.toString();
                    out.close();
                } else {
                    //Closes the connection.
                    response.getEntity().getContent().close();
                    throw new IOException(statusLine.getReasonPhrase());
                }
            } catch (ClientProtocolException e) {
                //TODO Handle problems..
            } catch (IOException e) {
                //TODO Handle problems..
            }
            return responseString;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            List<PackageInfo> packs = getPackageManager().getInstalledPackages(0);
            for (int i = 0; i < packs.size(); i++) {
                PackageInfo p = packs.get(i);
                if (p.packageName.equals(packageName)) {
                    version = Integer.toString(p.versionCode);
                    version = version.substring(0, version.length() - 1);
                }
            }

            Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.Hooks_Updated), Toast.LENGTH_LONG);

            String[] html = result.split("<p>");

            String matched = "No";

            int count = 0;
            int max = 0;
            for (String data : html) {
                max++;
            }

            for (String data : html) {
                count++;

                String finalCheck = "123";

                String hookInfo = hooksPrefs.getString("Hooks", "11");

                if (!data.isEmpty()) {
                    String[] PasteVersion = data.split(";");
                    finalCheck = PasteVersion[0];
                }

                System.out.println("Info: " + version + " Version: " + finalCheck);

                if (version.equals(finalCheck) && !data.isEmpty()) {
                    data = data.replace("<p>", "");
                    data = data.replace("</p>", "");
                    if (data.trim().equals(hookInfo.trim())) {
                        toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.Hooks_Latest), Toast.LENGTH_LONG);
                    } else {
                        Hooks(data);
                    }
                    matched = "Yes";
                } else {
                    if (count == max && matched.equals("No")) {
                        System.out.println("Trying default hook!");
                        String fallback = html[1];
                        fallback = fallback.replace("<p>", "");
                        fallback = fallback.replace("</p>", "");
                        Hooks(fallback);
                        toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.Hooks_Updated), Toast.LENGTH_LONG);
                    }
                }
            }
            toast.show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        prfs = getSharedPreferences("Prefs", Context.MODE_WORLD_READABLE);
        previousChoice = prfs.getString("Text", "Default");

        crash = (RadioButton) findViewById(R.id.crash);
        home = (RadioButton) findViewById(R.id.home);
        text = (RadioButton) findViewById(R.id.text);

        UpdateRadioButtons();

        crash.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                if (arg1) {
                    data = "Crash";
                    SaveAction();
                }
            }
        });

        home.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                if (arg1) {
                    data = "Home";
                    SaveAction();
                }
            }
        });

        text.setOnClickListener(new CompoundButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                Alert();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(UiUtils.getActivityVisibleInDrawer(Activity.this) ? getResources().getString(R.string.Hide) : getResources().getString(R.string.Show));
        menu.add(R.string.Cyanogen);
        menu.add(R.string.Google);
        menu.add(R.string.Piktures);

        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(final MenuItem menuItem) {
        String clicked = (String) menuItem.getTitle();

        if (clicked.equals("Cyanogen Gallery")) {
            new RequestTask().execute("https://raw.githubusercontent.com/iHelp101/Don-t-Swipe/master/Cyanogen.txt", "com.cyngn.gallerynext");
        }

        if (clicked.equals("Google Photos")) {
            new RequestTask().execute("https://raw.githubusercontent.com/iHelp101/Don-t-Swipe/master/Google.txt", "com.google.android.apps.photos");
        }

        if (clicked.equals("Hide App")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(Activity.this);
            builder.setMessage(getResources().getString(R.string.Warning));
            builder.setPositiveButton(getResources().getString(R.string.Okay), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    UiUtils.setActivityVisibleInDrawer(Activity.this, false);
                    menuItem.setTitle(R.string.Show);
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }

        if (clicked.equals("Piktures Gallery")) {
            new RequestTask().execute("https://raw.githubusercontent.com/iHelp101/Don-t-Swipe/master/Piktures.txt", "com.diune.media");
        }

        if (clicked.equals("Show App")) {
            UiUtils.setActivityVisibleInDrawer(Activity.this, true);
            menuItem.setTitle(R.string.Hide);
        }

        return false;
    }


    public void Alert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Activity.this);
        builder.setTitle("Provide the text to display.");

        final EditText input = new EditText(Activity.this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        try {
            if (!previousChoice.equals("Default") && !previousChoice.equals("Crash") && !previousChoice.equals("Home")) {
                input.setText(previousChoice);
            }
        } catch (Exception e) {

        }

        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                data = input.getText().toString();
                if (!data.equals("Default") && !data.equals("Crash") && !data.equals("Home")) {
                    SaveAction();
                } else {
                    dialog.cancel();
                    Alert();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                UpdateRadioButtons();
            }
        });
        builder.show();
    }

    public void SaveAction() {
        SharedPreferences.Editor editor = getSharedPreferences("Prefs", MODE_WORLD_READABLE).edit();
        editor.putString("Text", data);
        editor.apply();
        UpdateRadioButtons();
    }

    public void UpdateRadioButtons() {
        previousChoice = prfs.getString("Text", "Default");

        if (previousChoice.equals("Crash")) {
            crash.setChecked(true);
            home.setChecked(false);
            text.setChecked(false);
            selected = 0;
        } else {
            if (previousChoice.equals("Home")) {
                crash.setChecked(false);
                home.setChecked(true);
                text.setChecked(false);
                selected = 1;
            } else {
                if (!previousChoice.equals("Default")) {
                    crash.setChecked(false);
                    home.setChecked(false);
                    text.setChecked(true);
                    selected = 2;
                }
            }
        }
    }

    public void Hooks(String data) {
        SharedPreferences.Editor editor = getSharedPreferences(app, MODE_WORLD_READABLE).edit();
        editor.putString("Hooks", data);
        editor.apply();
    }
}
