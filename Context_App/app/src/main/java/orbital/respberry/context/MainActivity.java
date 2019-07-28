package orbital.respberry.context;


import android.content.Intent;
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
                    finish();
                }
            });

            btnSignIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, SignIn.class);
                    intent.putExtra("sharedText", sharedText);
                    startActivity(intent);
                    finish();
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