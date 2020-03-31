package com.example.mobi1;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.IBinder;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.Random;

import static android.widget.Toast.makeText;

public class MainActivity extends AppCompatActivity {
    LogicService logicService;
    boolean mbound = false;
    private ProgressBar progressBar;
    private EditText first;
    private EditText second;
    private EditText result;

    private ServiceConnection logicConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LogicService.LocalBinder binder = (LogicService.LocalBinder) service;
            logicService = binder.getService();
            mbound = true;
            makeText(MainActivity.this, "Logic Service Connected!", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            logicService = null;
            mbound = false;
            makeText(MainActivity.this, "Logic Service Disconnected!", Toast.LENGTH_SHORT).show();

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        first = findViewById(R.id.editText1);
        second = findViewById(R.id.editText2);
        result = findViewById(R.id.editText3);
        Button add = findViewById(R.id.button2);
        Button sub = findViewById(R.id.button3);
        Button mul = findViewById(R.id.button4);
        Button div = findViewById(R.id.button5);
        Button pi = findViewById(R.id.button6);
        progressBar = findViewById(R.id.progressBar);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double num1 = getDoubleFromText(first);
                double num2 = getDoubleFromText(second);
                result.setText(String.valueOf(logicService.add(num1, num2)));
            }
        });

        sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double num1 = getDoubleFromText(first);
                double num2 = getDoubleFromText(second);
                result.setText(String.valueOf(logicService.sub(num1, num2)));
            }
        });

        mul.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                double num1 = getDoubleFromText(first);
                double num2 = getDoubleFromText(second);
                result.setText(String.valueOf(logicService.mul(num1, num2)));
            }
        });

        div.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double num1 = getDoubleFromText(first);
                double num2 = getDoubleFromText(second);
                if(num2 == 0) {
                    Toast toast = makeText(MainActivity.this, "You can't divide by zero", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP, 0, 0);
                    toast.show();
                } else {
                    result.setText(String.valueOf(logicService.div(num1, num2)));
                }
            }
        });

        pi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setProgress(0);
                new PiComputeTask().execute();
            }
        });

    }

    public double getDoubleFromText(EditText editText) {
        double value = 0;
        String text = editText.getText().toString();
        if (!text.isEmpty())
            try {
                value = Double.parseDouble(text);
                // it means it is double
            } catch (Exception e1) {
                Toast toast = makeText(MainActivity.this, "Please input a number", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP, 0, 0);
                toast.show();
                e1.printStackTrace();
            }
        return value;
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

    @Override
    protected void onStart() {
        super.onStart();
        if (!mbound) {
            this.bindService(new Intent(MainActivity.this, LogicService.class), logicConnection, Context.BIND_AUTO_CREATE);
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mbound) {
            mbound = false;
            this.unbindService(logicConnection);
        }
    }

    private class PiComputeTask extends AsyncTask<Void, Integer, Double> {

        @Override
        protected Double doInBackground(Void... voids) {
            double pointsInCircle = 0;
            int amount = 10000000;
            Random rand = new Random();
            for (double i = 0; i < amount; i++) {
                double x = rand.nextDouble();
                double y = rand.nextDouble();
                if(Math.pow(x, 2) + Math.pow(y, 2) < 1){
                    pointsInCircle++;
                }
                if (i%(amount/100) == 0) publishProgress(1);
            }
            return 4*pointsInCircle/amount;

        }

        protected void onPostExecute(Double result){
            first.setText(result.toString());
            progressBar.setProgress(0);
        }

        @Override
        protected  void onProgressUpdate(Integer... values){
            super.onProgressUpdate(values);
            progressBar.incrementProgressBy(values[0]);
        }
    }
}
