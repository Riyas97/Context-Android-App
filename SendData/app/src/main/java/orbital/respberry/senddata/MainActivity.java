package orbital.respberry.senddata;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {


    EditText e1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        e1 = (EditText)findViewById(R.id.editText);
        Thread mine = new Thread(new MyServerThread());
        mine.start();


    }

    class MyServerThread implements Runnable {

        Socket s;
        ServerSocket ss;
        InputStreamReader a;
        BufferedReader b;
        String message;
        Handler h = new Handler();


        @Override
        public void run() {

                try {
                    ss = new ServerSocket(6001);

                    while (true) {
                        s = ss.accept();
                        a = new InputStreamReader(s.getInputStream());
                        b = new BufferedReader(a);
                        message = b.readLine();
                        h.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }catch (IOException e) {
                    e.printStackTrace();
                }

        }
    }
    public void send(View v) {
        MessageSender messageSender = new MessageSender();
        messageSender.execute(e1.getText().toString());
    }
}
