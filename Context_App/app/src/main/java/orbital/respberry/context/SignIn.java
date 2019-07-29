package orbital.respberry.context;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.rengwuxian.materialedittext.MaterialEditText;


import orbital.respberry.context.Model.User;

public class SignIn extends AppCompatActivity {
    MaterialEditText editPhone, editPassword;
    Button btnSignin;
    User myuser;
    MyApp app;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        editPassword = (MaterialEditText) findViewById(R.id.editPassword);
        editPhone = (MaterialEditText) findViewById(R.id.editPhone);
        btnSignin = (Button) findViewById(R.id.btnSignIn);
        app = ((MyApp) this.getApplication());
        myuser = app.getUser();

        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog mDialog = new ProgressDialog(SignIn.this);
                mDialog.setMessage("Please Wait");
                mDialog.show();
                try {
                    mDialog.dismiss();
                    myuser.setUsername(editPhone.getText().toString());
                    myuser.setPassword(editPassword.getText().toString());
                    myuser.login();
                    while(!myuser.getCheck());
                    if (myuser.getStatus() == 0) {
                        Log.i("username", myuser.getUsername() + " id : " + myuser.getUserid());
                        SaveState.setUserName(SignIn.this,myuser.getUsername(),myuser.getUserid());
                        app.setUser(myuser);
                        Intent next = new Intent(SignIn.this, menu_aft_login.class);
                        next.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(next);
                        finish();
                    } else if (myuser.getStatus() == 1) {
                        //wrong user and password
                        Toast.makeText(SignIn.this, "Wrong Username or Password", Toast.LENGTH_SHORT).show();
                    } else if (myuser.getStatus() == 2) {
                        //missing parameters
                        Toast.makeText(SignIn.this, "Missing Username or Password", Toast.LENGTH_SHORT).show();
                    } else if (myuser.getStatus() == 3) {
                        //network error
                        Toast.makeText(SignIn.this, "Network Error", Toast.LENGTH_SHORT).show();
                    } else {
                        //error
                        Toast.makeText(SignIn.this, "Unexpected error", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

        });
    }

}

