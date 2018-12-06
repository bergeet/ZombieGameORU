package com.example.pc.zombiegame;

import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

//Klass som hanterar trådarna för sändning/hämtning till servern
public class ServerHandlerThread extends AsyncTask<Void, Void, Void> {

    private RegisterActivity registerActivity;
    private boolean stop = true;
    private Socket socket;
    private PrintWriter printWriter;
    private BufferedReader bufferedReader;
    private GameClientActivity gameClientActivity;
    private LoginActivity loginActivity;

    private Button mapBtn;

    //private String DEFAULT_HOST = "basen.oru.se";
    //private static final int PORT = 2002;

    //TEST SERVER
    private String DEFAULT_HOST = "192.168.43.42";
    private static final int PORT = 2002;

    public ServerHandlerThread(GameClientActivity parent)
    {
        this.gameClientActivity = parent;

    }

    public ServerHandlerThread(LoginActivity parent)
    {
        this.loginActivity = parent;

    }

    public ServerHandlerThread(RegisterActivity parent) {

        this.registerActivity = parent;
    }

    public void send_message(String line){
        printWriter.println(line);
        Log.d("Handler", "Skickar " + line);
    }

    public String get_message(){

        String response = "";
        try {
            response = bufferedReader.readLine();
            Log.d("ReadLine", response);
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("ReadLine", e.toString());
        }
        return response;
    }

    public boolean isStopped() {
        return stop;
    }


    public void setStop(boolean stopped) {
        stop = stopped;
    }


    //Stoppar anslutningen mot servern
    public void end_connection() {

        setStop(true);
        try {
            if (socket != null) {
                printWriter.flush();
                socket.shutdownInput();
//                gameClientActivity.print("Du är inte längre ansluten till servern.");
                Log.d("ServerHandlerThread", "not connected");
//                gameClientActivity.print("Anslut igen för att kunna skriva");
            }

        } catch (IOException e) {
            e.printStackTrace();

//            gameClientActivity.print("fel.");
        }
    }

    //Körs i bakgrunden efter execute()
    @Override
    protected Void doInBackground(Void... arg0) {

        try {
//            gameClientActivity.print("Ansluter...");
            socket = new Socket(DEFAULT_HOST, PORT);
            setStop(false);

            if (socket != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
                bufferedReader = new BufferedReader(inputStreamReader); //Används för att hämta paket från servern
                printWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true); //Används för att skicka till servern

//                gameClientActivity.print("Ansluten!");
                Log.d("ServerHandlerThread", "Connected");

            } else {
//                gameClientActivity.print("Serverns port är inte 2002.");
            }

        } catch (IOException e) {
            e.printStackTrace();
//            gameClientActivity.print("Kunde inte ansluta till " + DEFAULT_HOST);
            Log.d("ServerHandlerThread", "Could not Connected");
        }
        return null;
    }

    //Körs efter execute, en gång.
    @Override
    protected void onPostExecute(Void result) {

        if (socket != null) {

            ScheduledExecutorService scheduler =
                    Executors.newSingleThreadScheduledExecutor();

            scheduler.scheduleAtFixedRate
                    (new Runnable() {
                        public void run() {
                            if (!isStopped()) {
                                Log.d("Connect", "Inne");
                                final Sender sender = new Sender();

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                                    sender.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                                } else {
                                    sender.execute(); //Executar senders metod vilket i sin tur skickar meddelandet till servern
                                }
                            }
                        }
                    }, 0, 1, TimeUnit.SECONDS);


            //TODO: LÄGG TILL INLOGGNINGEN HÄR
//                        //Möjliggör användningen av det digitala tangentbordet på telefonen
//                        inputText.setOnKeyListener(new View.OnKeyListener() {
//                            @Override
//                            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                                if (!isStopped()) {
//                                    if (v == inputText) {
//
//                                        if (event.getAction() == KeyEvent.ACTION_DOWN) {
//                                            switch (keyCode) {
//                                                case KeyEvent.KEYCODE_DPAD_CENTER:
//                                                case KeyEvent.KEYCODE_ENTER:
//                                                    final Sender sender = new Sender();
//
//                                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//
//                                                        sender.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//                                                    } else {
//                                                        sender.execute();
//                                                    }
//
//                                                    return true;
//                                                default:
//                                                    break;
//                                            }
//                                        }
//                                    }
//                                }
//
//                        return false;
//
//                    }
//                });
            Receiver receiver = new Receiver();
            receiver.execute();
        } else {
         //   gameClientActivity.print("Kunde inte ansluta till " + DEFAULT_HOST);

        }
    }


private class Sender extends AsyncTask<Void, Void, Void> {

    private String sender_message;

    @Override
    protected Void doInBackground(Void... voids) {

//            //TODO: INLOGGNING
        //TODO: Kommunikation till servern nedan

//            sender_message = inputText.getText().toString();
//
//            try {
//                //Loggar in klienten och ger denne sitt användarnamn
//                if (sender_message.startsWith("LOGIN")) {
//                    user_name = sender_message.substring(6);
//                    print("Du har loggats in som " + "'" + user_name + "'");
//
//                    //Stänger kopplingen
//                } else if (sender_message.equals("SHUTDOWN")) {
//                    printWriter.println(sender_message);
//                    socket.close();
//                    bufferedReader.close();
//                    printWriter.close();
//                    print("Ingen förbindelse.");
//                    print("Chatten är avslutad.");
//                }
//                printWriter.println(sender_message);
//
//
//            } catch (IOException e) {
//                print("Anslutningen nere.");
//            }
        Log.d("Sender", "RunInBackground");
        return null;

    }

    //Metoden som formaterar användarens chattmeddelande lokalt
    @Override
    protected void onPostExecute(Void result) {

        Log.d("Sender", "onPostExecute");

        //TODO: Hantera POST sändningen
//            inputText.setText("");
//            if (user_name == "") {
//                print("Du: " + sender_message);
//            } else {
//                if (!sender_message.startsWith("LOGIN") && !sender_message.equals("SHUTDOWN")) {
//                    print(user_name + ": " + sender_message);
//                }
//            }
        super.onPostExecute(result);
    }
} //Sender Class

//Klassen som hanterar hämtningen av alla meddelande från andra klienter.

private class Receiver extends AsyncTask<Void, Void, Void> {

    private String receiver_message;

    @Override
    protected Void doInBackground(Void... voids) {
        while (true) {
            try {
                receiver_message = bufferedReader.readLine();

                if (receiver_message == null) {
                    break;
                }

                publishProgress(null);


            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                Thread.sleep(500); //Väntar en halv sekund innan nästa meddelande kommer för att inte överbelasta
            } catch (InterruptedException ie) {
            }
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
//        gameClientActivity.print(receiver_message);
        if(loginActivity != null) loginActivity.received_lines(receiver_message);
        if(gameClientActivity != null) gameClientActivity.received_lines(receiver_message);

        Log.d("Received_lines", receiver_message);
    }
} //Receiver Class
}//ServerHandler class