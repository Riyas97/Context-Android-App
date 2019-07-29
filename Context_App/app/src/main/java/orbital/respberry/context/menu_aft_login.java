package orbital.respberry.context;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import java.io.IOException;
import android.util.Log;

import orbital.respberry.context.Model.User;


public class menu_aft_login extends AppCompatActivity {
    User myuser;
    MyApp app;
    EditText e1;
    String sharedText = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_aft_login);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        int saved_user_id = SaveState.getUserId(menu_aft_login.this);
        String saved_user_name = SaveState.getUserName(menu_aft_login.this);
        app = ((MyApp) this.getApplication());
        if(saved_user_id == 0){
            myuser = app.getUser();
        } else {
            myuser = new User();
            myuser.setUserid(saved_user_id);
            myuser.setUsername(saved_user_name);
        }
        e1 = (EditText)findViewById(R.id.editText);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            sharedText = getIntent().getExtras().getString("sharedText");
        }
        if (sharedText.length() != 0){
            handleSendText(sharedText);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_aft_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if (id == R.id.action_signout){
            SaveState.clearData(menu_aft_login.this);
            SharedPreferences getPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
            SharedPreferences.Editor e = getPrefs.edit();
            e.putBoolean("firstStart", false);
            e.apply();
            app.setUser(new User());
            Intent main = new Intent(menu_aft_login.this, MainActivity.class);
            startActivity(main);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void send(View v) {
        final String user_id = Integer.toString(myuser.getUserid());
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

    public void handleSendText(final String sharedText){
        final String user_id = Integer.toString(myuser.getUserid());
        if(sharedText != null){
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