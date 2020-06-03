package costas.firebasedb;

import com.google.firebase.database.DataSnapshot;

public interface DBManager {
    void onSuccess(DataSnapshot dataSnapshot);
    void onStart();
    void onFailure();
}
