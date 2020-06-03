package costas.firebasedb;

public class User {

    String userId;
    String userName;
    String userRank;

    public User() {

    }

    public User(String userId, String userName, String userRank) {
        this.userId = userId;
        this.userName = userName;
        this.userRank = userRank;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserRank() {
        return userRank;
    }
}
