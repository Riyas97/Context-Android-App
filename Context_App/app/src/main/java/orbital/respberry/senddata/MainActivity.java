package orbital.respberry.senddata;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import java.net.*;
import java.io.IOException;
import android.content.Intent;
import android.util.Log;



public class MainActivity extends AppCompatActivity {

    EditText e1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        e1 = (EditText)findViewById(R.id.editText);
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            handleSendText(intent);
        }
    }

    public void send(View v) {
        final String user_id = Integer.toString(user);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run(){
                try {
                    udpClient sender = new udpClient();
                    sender.sendMsg("12" + user_id);
                    String to_send = sender.recvMsg();
                    Log.i("asd",to_send);
                    to_send = sender.recvMsg();
                    Log.i("asd",to_send);
                    if(to_send.equals("send")) {
                        String string_to_send = "22" + user_id + ":explorer \"";
                        string_to_send = string_to_send.concat(e1.getText().toString()).concat("\"\0");
                        sender.sendMsg(string_to_send);
                        sender.recvMsg();
                    }
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    public void handleSendText(Intent intent){
        final String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        if(sharedText != null){
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run(){
                    try {
                        udpClient sender = new udpClient();
                        sender.sendMsg("12");
                        String to_send = sender.recvMsg();
                        Log.i("asd",to_send);
                        to_send = sender.recvMsg();
                        Log.i("asd",to_send);
                        if(to_send.equals("send")) {
                            String string_to_send = "22explorer \"";
                            string_to_send = string_to_send.concat(sharedText).concat("\"\0");
                            sender.sendMsg(string_to_send);
                            sender.recvMsg();
                        }
                    } catch (IOException e){
                        e.printStackTrace();
                    }
                }
            });
            thread.start();
        }
        setResult(RESULT_OK);
        finish();
    }

}