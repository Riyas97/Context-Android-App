import java.io.*;
import java.net.*;

public class User{
    //login method return user_id. user_id should be anything above 1
    //if method return 0. it means that there is an error somewhere.
    public int login(String email, String password) throws Exception {
        String data =
        "email=" + URLEncoder.encode(email,"UTF-8")
        + "&password=" + URLEncoder.encode(password, "UTF-8");
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
        if (responseCode >= 400){
            in = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            return 1;
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
        int user_id;
        if(status == 0){
            user_id = Integer.parseInt(response.substring(51,response.length()-1));
        } else {
            user_id = 0;
        }
        return user_id;
    }

    //register method returns the status of the register. It should return 0 if successful.
    //Else it would return anything >0.
    public int register(String email, String password) throws Exception{
        String data =
        "email=" + URLEncoder.encode(email,"UTF-8")
        + "&password=" + URLEncoder.encode(password, "UTF-8");
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
            return 3;
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
        return status;
    }

    public static void main(String[] args) throws Exception{
        User new_user = new User();
        new_user.register("riyas", "riyas123");
    }

}