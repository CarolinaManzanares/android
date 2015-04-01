package com.carolinamanzanares.intents;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class ActivityB extends ActionBarActivity {

    private EditText input2;
    private EditText input1;
    private Button btnSend;
    private Button btnCancel;

    public static final String VALUE1_ITEM = "VALUE1_ITEM";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_b);

        setValue();

        input1 = (EditText) findViewById(R.id.input1);
        btnSend = (Button) findViewById(R.id.btnSend);
        btnCancel = (Button) findViewById(R.id.btnCancel);

        final Intent valuesIntent = new Intent();
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String value1 = input1.getText().toString();
                if (value1.length() > 0){
                    valuesIntent.putExtra(VALUE1_ITEM, value1);
                    setResult(RESULT_OK, valuesIntent);
                    finish();
                }
                else{
                    Toast toast = Toast.makeText(ActivityB.this, getResources().getString(R.string.user_to_text), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();

                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED, valuesIntent);
                finish();
            }
        });




    }

    private void setValue() {
        Intent valuesIntent = getIntent();
        String value = valuesIntent.getStringExtra(MainActivity.VALUE1_ITEM);

        input2 = (EditText) findViewById(R.id.input2);
        input2.setText(value);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_b, menu);
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
