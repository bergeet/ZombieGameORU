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

public class RegisterActivity extends AppCompatActivity {

    private Button regBtn;
    private EditText userReg;
    private EditText pwReg;

    private String server_response = "";

    private Player player;

    private ServerHandlerThread serverHandlerThread = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        regBtn = findViewById(R.id.RegBtn);
        userReg = findViewById(R.id.username_reg);
        pwReg = findViewById(R.id.pw_reg);


        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!userReg.getText().equals("") && !pwReg.equals("")){

                    String register = "REGISTER " + userReg.getText() + " " + pwReg.getText();

                    final Sender sender = new Sender();
                    if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB)
                        sender.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, register);
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

            serverHandlerThread = new ServerHandlerThread(RegisterActivity.this);
            serverHandlerThread.execute();

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

        server_response = line;
        if(server_response.contains("REGISTERED")){
            finish();
        }
    }


}
