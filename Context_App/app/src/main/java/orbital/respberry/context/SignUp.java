package orbital.respberry.context;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.rengwuxian.materialedittext.MaterialEditText;

import orbital.respberry.context.Model.User;

public class SignUp extends AppCompatActivity {
    MaterialEditText editPhone, editPassword;
    Button btnSignUp;
    User myuser;
    MyApp app;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        app = ((MyApp) this.getApplication());
        myuser = app.getUser();
        editPhone = findViewById(R.id.editPhone);
        editPassword = findViewById(R.id.editPassword);
        btnSignUp = findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog mDialog = new ProgressDialog(SignUp.this);
                mDialog.setMessage("Please Wait");
                mDialog.show();

                try {
                    mDialog.dismiss();
                    boolean okaylogin = false;
                    myuser.setUsername(editPhone.getText().toString());
                    myuser.setPassword(editPassword.getText().toString());
                    myuser.register();
                    while(!myuser.getCheck());
                    if (myuser.getStatus() == 0) {
                        okaylogin = true;
                    } else if (myuser.getStatus() == 1) {
                        //wrong user and password
                        Toast.makeText(SignUp.this, "User already exist", Toast.LENGTH_SHORT).show();
                    } else if (myuser.getStatus() == 2) {
                        //missing parameters
                        Toast.makeText(SignUp.this, "Missing Username or Password", Toast.LENGTH_SHORT).show();
                    } else if (myuser.getStatus() == 3) {
                        //network error
                        Toast.makeText(SignUp.this, "Network Error", Toast.LENGTH_SHORT).show();
                    } else {
                        //error
                        Toast.makeText(SignUp.this, "Unexpected error", Toast.LENGTH_SHORT).show();
                    }
                    if(okaylogin){
                        myuser.login();
                    } else {
                        return;
                    }
                    while(!myuser.getCheck());
                    if (myuser.getStatus() == 0) {
                        SaveState.setUserName(SignUp.this,myuser.getUsername(),myuser.getUserid());
                        app.setUser(myuser);
                        Intent next = new Intent(SignUp.this, menu_aft_login.class);
                        startActivity(next);
                        finish();
                    } else if (myuser.getStatus() == 1) {
                        //wrong user and password
                        Toast.makeText(SignUp.this, "Wrong Username or Password", Toast.LENGTH_SHORT).show();
                    } else if (myuser.getStatus() == 2) {
                        //missing parameters
                        Toast.makeText(SignUp.this, "Missing Username or Password", Toast.LENGTH_SHORT).show();
                    } else if (myuser.getStatus() == 3) {
                        //network error
                        Toast.makeText(SignUp.this, "Network Error", Toast.LENGTH_SHORT).show();
                    } else {
                        //error
                        Toast.makeText(SignUp.this, "Unexpected error", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }
}