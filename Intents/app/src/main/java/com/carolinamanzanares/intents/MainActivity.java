package com.carolinamanzanares.intents;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {

    private Button btnSend;
    private Button btnPhoto;
    private EditText input1;
    private EditText input2;
    private ImageView photoView;

    public static final String VALUE1_ITEM = "VALUE1_ITEM";
    public static final int WRITE_VALUE = 1;
    public static final int TAKE_PHOTO = 2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        obtainViews();
        addEventListeners();



    }

    private void addEventListeners() {
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String value1 = input1.getText().toString();
                if (value1.length() > 0) {
                    Intent valuesIntent = new Intent(MainActivity.this, ActivityB.class);
                    valuesIntent.putExtra(VALUE1_ITEM, value1);
                    startActivityForResult(valuesIntent, WRITE_VALUE);
                } else {
                    Toast toast = Toast.makeText(MainActivity.this, getResources().getString(R.string.user_to_text), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();

                }
            }
        });

        btnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(photoIntent, TAKE_PHOTO);
            }
        });

    }

    private void obtainViews() {
        btnSend = (Button) findViewById(R.id.btnSend);
        btnPhoto = (Button) findViewById(R.id.btnPhoto);
        input1 = (EditText) findViewById(R.id.input1);
        input2 = (EditText) findViewById(R.id.input2);
        photoView = (ImageView) findViewById(R.id.photoView);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK){

            switch(requestCode){
                case WRITE_VALUE:
                    input2.setText(data.getStringExtra(VALUE1_ITEM));
                    break;
                case TAKE_PHOTO:
                    Bundle extras = data.getExtras();
                    Bitmap bitmap = (Bitmap) extras.get("data");
                    photoView.setImageBitmap(bitmap);

            }

    }



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
