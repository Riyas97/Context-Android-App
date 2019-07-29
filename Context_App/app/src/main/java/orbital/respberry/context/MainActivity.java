package orbital.respberry.context;


import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import orbital.respberry.context.Model.User;

public class MainActivity extends AppCompatActivity {
    Button btnSignUp, btnSignIn;
    TextView txtSlogan;
    String sharedText = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                SharedPreferences getPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                boolean checkFirstTime = getPrefs.getBoolean("firstStart", true);

                if (checkFirstTime) {
                    final Intent i = new Intent(MainActivity.this, Onboarding.class);
                    runOnUiThread(new Runnable() {
                        @Override public void run() {
                            startActivity(i);
                        }
                    });

                    SharedPreferences.Editor e = getPrefs.edit();

                    e.putBoolean("firstStart", false);

                    e.apply();
                }
            }
        });

        t.start();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent sharedintent = getIntent();
        String action = sharedintent.getAction();
        String type = sharedintent.getType();
        if (Intent.ACTION_SEND.equals(action) && type != null) {
            sharedText = sharedintent.getStringExtra(Intent.EXTRA_TEXT);
        }



        if (SaveState.getUserId(MainActivity.this) == 0) {
            btnSignIn = (Button) findViewById(R.id.btnSignIn);
            btnSignUp = (Button) findViewById(R.id.btnSignUp);
            txtSlogan = (TextView) findViewById(R.id.txt_slogan);
            ((MyApp) this.getApplication()).setUser(new User());
            btnSignUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent signup = new Intent(MainActivity.this, SignUp.class);
                    signup.putExtra("sharedText", sharedText);
                    startActivity(signup);
                }
            });

            btnSignIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, SignIn.class);
                    intent.putExtra("sharedText", sharedText);
                    startActivity(intent);
                }
            });
        } else {
            Intent intent = new Intent(MainActivity.this, menu_aft_login.class);
            intent.putExtra("sharedText", sharedText);
            startActivity(intent);
            finish();
        }
    }
}