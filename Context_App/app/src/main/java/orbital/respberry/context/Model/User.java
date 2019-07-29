package orbital.respberry.context.Model;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;


public class User{
    private String username;
    private String password;
    private int user_id;
    private int status;
    private boolean check;

    public User(){

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getUserid() {
        return user_id;
    }

    public void setUserid(int userid) {
        this.user_id = userid;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public int getStatus(){
        return status;
    }

    public boolean getCheck(){
        return check;
    }

    public void setCheck(boolean input){
        this.check = input;
    }

    //status
    // 0 -- okay
    // 1 -- wrong user and password combo
    // 2 -- missing fields
    // 3 -- network issue
    public void login() throws Exception{
        final String email1 = this.username;
        final String password1 = this.password;
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    setCheck(false);
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
                        status = 3;
                    } else {
                        in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    }
                    String inputLine;
                    StringBuffer response = new StringBuffer();
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();
                    status = Character.getNumericValue(response.charAt(10));

                    if (status == 0) {
                        user_id = Integer.parseInt(response.substring(51, response.length() - 1));
                    }
                    setCheck(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    //status
    // 0 -- okay
    // 1 -- user already exist
    // 2 -- missing fields
    // 3 -- network issue
    public void register() throws Exception{
        final String email1 = this.username;
        final String password1 = this.password;
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    setCheck(false);
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
                        status = 3;
                    } else {
                        in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    }
                    String inputLine;
                    StringBuffer response = new StringBuffer();
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();
                    status = Character.getNumericValue(response.charAt(10));
                    setCheck(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }
}
