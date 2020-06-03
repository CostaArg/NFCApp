package costas.firebasedb;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Access {
    private String accessId;
    private String accessName;
    private String accessRank;

    public Access(){
        //this constructor is required
    }

    public Access(String accessId, String accessName, String accessRank) {
        this.accessId = accessId;
        this.accessName = accessName;
        this.accessRank = accessRank;
    }

    public String getAccessId() {
        return accessId;
    }

    public String getAccessName() {
        return accessName;
    }

    public String getAccessRank() {
        return accessRank;
    }
}