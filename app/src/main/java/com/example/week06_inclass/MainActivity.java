package com.example.week06_inclass;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends Activity {

    EditText edtWork;
    TextView txtPercent;
    ProgressBar progressBar;
    Button btnDoIt;

    int globalVar = 0, accum = 0, progressStep = 1;
    int nWork=0;
    boolean isRunning = false;
    final int MAX_PROGRESS = 100;

    Handler myHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtWork = (EditText) findViewById(R.id.edtWork);
        edtWork.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                nWork=Integer.parseInt(edtWork.getText().toString());

            }
        });
        txtPercent = (TextView) findViewById(R.id.txtPercent);

        btnDoIt = (Button) findViewById(R.id.btnDoIt);
        btnDoIt.setEnabled(true);
        progressBar = (ProgressBar) findViewById(R.id.progress_horizontal);

        btnDoIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (nWork>0) {
                    btnDoIt.setEnabled(false);
                    onStart();
                }

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        accum = 0;
        txtPercent.setText("0%");
        progressBar.setMax(MAX_PROGRESS);
        progressBar.setProgress(0);
        progressBar.setVisibility(View.VISIBLE);
        Thread myBackgroundThread = new Thread(backgroundTask, "backAlias1");
        myBackgroundThread.start();
    }

    private Runnable foregroundRunnable=new Runnable() {
        @Override
        public void run() {

            progressBar.incrementProgressBy(progressStep);
            accum+=progressStep;
            int percent=accum*(MAX_PROGRESS)/nWork;
            progressBar.setProgress(percent);
            txtPercent.setText(percent + "%");
            if (percent>=progressBar.getMax()){
                btnDoIt.setEnabled(true);
            }
            else {
                btnDoIt.setEnabled(false);
            }
        };
    };
    private Runnable backgroundTask=new Runnable() {
        @Override
        public void run() {
            try{
                for (int n=0;n<nWork;n++){
                    Thread.sleep(1);
                    globalVar++;
                    myHandler.post(foregroundRunnable);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    };

}

