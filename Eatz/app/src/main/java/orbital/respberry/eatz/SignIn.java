package orbital.respberry.eatz;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.common.internal.service.Common;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.io.*;
import java.net.*;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import orbital.respberry.eatz.Common.common;
import orbital.respberry.eatz.Model.User;

public class SignIn extends AppCompatActivity {
    int user_id;
    MaterialEditText editPhone, editPassword;
    Button btnSignin;
    boolean check = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        editPassword = (MaterialEditText) findViewById(R.id.editPassword);
        editPhone = (MaterialEditText) findViewById(R.id.editPhone);
        btnSignin = (Button) findViewById(R.id.btnSignIn);


        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog mDialog = new ProgressDialog(SignIn.this);
                mDialog.setMessage("Please Wait");
                mDialog.show();

                try {
                    mDialog.dismiss();
                    login(editPhone.getText().toString(), editPassword.getText().toString());
                    while(!check);
                    check = false;
                    if (user_id != 0) {
                        Intent next = new Intent(SignIn.this, MenuPage.class);
                        User user = new User(editPhone.getText().toString(), user_id);
                        common.currentUser = user;
                        startActivity(next);
                        finish();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

        });
    }


    private void login(String email, String password) throws Exception{
        final String email1 = email;
        final String password1 = password;
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String data = "email=" + URLEncoder.encode(email1, "UTF-8")
                            + "&password=" + URLEncoder.encode(password1, "UTF-8");
                    BufferedReader in;
                    URL obj = new URL("http://118.189.187.18/login.php");
                    HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                    con.setRequestMethod("POST");
                    con.setDoOutput(true);
                    DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                    wr.writeBytes(data);
                    wr.flush();
                    wr.close();
                    int responseCode = con.getResponseCode();
                    if (responseCode >= 400) {
                        in = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                        user_id = 1;
                    } else {
                        in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    }
                    String inputLine;
                    StringBuffer response = new StringBuffer();
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();
                    int status = Character.getNumericValue(response.charAt(10));

                    if (status == 0) {
                        user_id = Integer.parseInt(response.substring(51, response.length() - 1));
                    } else if (status == 1) {
                        user_id = 0;
                        Toast.makeText(SignIn.this, "Wrong Username / Password", Toast.LENGTH_SHORT).show();
                    } else if (status == 2) {
                        Toast.makeText(SignIn.this, "Wrong", Toast.LENGTH_SHORT).show();
                    }
                    check = true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }
}

