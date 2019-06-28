package orbital.respberry.senddata;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import java.net.*;
import java.io.IOException;
import android.content.Intent;


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
            if ("text/plain".equals(type)) {
                handleSendText(intent);
            }
        }

    }

    public void send(View v) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run(){
                try {
                    udpClient sender = new udpClient();
                    String string_to_send = "explorer ";
                    string_to_send = string_to_send.concat(e1.getText().toString()).concat("\0");
                    sender.sendMsg(string_to_send);
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
                        String string_to_send = "explorer ";
                        string_to_send = string_to_send.concat(sharedText).concat("\0");
                        sender.sendMsg(string_to_send);
                    } catch (IOException e){
                        e.printStackTrace();
                    }
                }
            });
            thread.start();
        }
    }

}