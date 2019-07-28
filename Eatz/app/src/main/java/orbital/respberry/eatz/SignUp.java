package orbital.respberry.eatz;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import orbital.respberry.eatz.Model.User;

public class SignUp extends AppCompatActivity {

    int user_id;
    MaterialEditText editPhone, editPassword;
    Button btnSignUp;
    boolean check = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        editPhone = findViewById(R.id.editPhone);
        editPassword = findViewById(R.id.editPhone);
        btnSignUp = findViewById(R.id.btnSignUp);


        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog mDialog = new ProgressDialog(SignUp.this);
                mDialog.setMessage("Please Wait");
                mDialog.show();

                try {
                    mDialog.dismiss();
                    Register(editPhone.getText().toString(), editPassword.getText().toString());
                    while(!check);
                    check = false;
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        });
    }

    public void Register(String email, String password) throws Exception{
        final String email1 = email;
        final String password1 = password;
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                String data = "email=" + URLEncoder.encode(email1,"UTF-8")
                                + "&password=" + URLEncoder.encode(password1, "UTF-8");
                BufferedReader in;
                URL obj = new URL("http://118.189.187.18/register.php");
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                con.setRequestMethod("POST");
                con.setDoOutput(true);
                DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                wr.writeBytes(data);
                wr.flush();
                wr.close();
                int responseCode = con.getResponseCode();
                if (responseCode >= 400){
                    in = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                    Toast.makeText(SignUp.this, "Signup Failed!", Toast.LENGTH_SHORT).show();
                    user_id = 3;
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
                check = true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }
}
