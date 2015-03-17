package com.carolinamanzanares.configchange;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class MainActivity extends ActionBarActivity {

    private final String DATA = "data";

    private String op = null;
    private Double result = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("CHANGE", "MainActivity onCreate");
        setContentView(R.layout.activity_main);
        //en este momento la interfaz ya está lista

        if(savedInstanceState != null){
            String data = savedInstanceState.getString(DATA);
            Log.d("CHANGE", "ActivityMain saving data" + data);
        }

        Button btnOpenB = (Button)findViewById(R.id.btnOpenB);
        Button btnCloseMain = (Button)findViewById(R.id.btnCloseMain);

        btnOpenB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("CHANGE", "ActivityMain onClick() btnOpenB");

                Intent openB;
                openB = new Intent(MainActivity.this, ActivityB.class);
                startActivity(openB);
            }
        });

        btnCloseMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("CHANGE", "ActivityMain onClick() btnCloseMain");
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        Log.d("CHANGE", "MainActivity OnStart");
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        Log.d("CHANGE", "MainActivity onRestart");
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d("CHANGE", "MainActivity onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.d("CHANGE", "MainActivity onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();

        Log.d("CHANGE", "MainActivity onStop");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.d("CHANGE", "ActivityMain onSaveInstanceState");

        //el orden importante, primero el put y luego el super
        outState.putString(DATA, "datos");
        super.onSaveInstanceState(outState);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.d("CHANGE", "MainActivity onDestroy");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
