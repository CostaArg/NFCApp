package costas.firebasedb;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {
    private String id;
    private String userName;
    private int rating;

    public User() {

    }

    public User(String id, String userName, int rating) {
        this.userName = userName;
        this.rating = rating;
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public int getRating() {
        return rating;
    }
}
