package orbital.respberry.eatz.Model;

public class User {
    private String username;
    private int userid;

    public User() {

    }

    public User(String username, int userid) {
        this.username = username;
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }
}
