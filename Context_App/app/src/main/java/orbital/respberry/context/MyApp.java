package orbital.respberry.context;

import android.app.Application;

import orbital.respberry.context.Model.User;

public class MyApp extends Application {
    private User appUser;

    public User getUser(){
        return appUser;
    }

    public void setUser(User normaluser){
        this.appUser = normaluser;
    }
}
