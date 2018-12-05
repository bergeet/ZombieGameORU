package com.example.pc.zombiegame;

import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.net.UnknownHostException;

public class LoginActivity extends AppCompatActivity {

    private Button loginBtn;
    private TextView registerText;
    private EditText userText;
    private EditText pwText;

    private ServerHandlerThread serverHandlerThread = null;

    public static int seq_number = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        registerText = findViewById(R.id.registerText);
        userText = findViewById(R.id.usernameEdit);
        pwText = findViewById(R.id.passwordEdit);

        loginBtn = findViewById(R.id.loginBtn);

        if(serverHandlerThread == null){
            serverHandlerThread = new ServerHandlerThread(LoginActivity.this);
            serverHandlerThread.execute();
        }


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!userText.getText().equals("") && !pwText.equals("")){

                    String login = "LOGIN " + userText.getText() + " " + pwText.getText();

                    final Sender sender = new Sender();
                    if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB)
                        sender.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, login);
                    else
                        sender.execute();

                }


            }
        });
    }


    private class Sender extends AsyncTask<String, Void, Void> {

        public void send_to_server(String input) {
            if (serverHandlerThread == null) {
                //TODO: ADD NOT CONNECTED
            }
            input = ++LoginActivity.seq_number + " " + input;
            //TODO: LOG WINDOW??
            serverHandlerThread.send_message(input);
        }



        @Override
        protected Void doInBackground(String... strings) {

            int count = strings.length;
            for(int i = 0; i < count; i++){
                try {

                    send_to_server(strings[i]);
                    Log.d("Sender run", strings[i]);

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("Run", e.toString());
                }
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            super.onPostExecute(aVoid);
        }
    }

    public void received_lines(String line){
        registerText.setText(line);
    }




}
