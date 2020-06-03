package costas.firebasedb;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PKIActivity extends AppCompatActivity {

    String pkiKey;

    EditText pkiInput;

    Button pkiSubmit;

    Button pkiAuthenticate;

    DatabaseReference databaseUsers;

    public void saveInfo(){

        SharedPreferences sharedPref = getSharedPreferences("pkiKey", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("pkiInput", pkiInput.getText().toString());
        editor.apply();

    }

    public String getData(){
        SharedPreferences sharedPref = getSharedPreferences("pkiKey", Context.MODE_PRIVATE);
        return sharedPref.getString("pkiInput", "");

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pki);

        databaseUsers = FirebaseDatabase.getInstance().getReference("Users");

        pkiInput = (EditText) findViewById(R.id.pkiInput);

        pkiSubmit = (Button) findViewById(R.id.pkiSubmit);

        pkiSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveInfo();

                String pkiKey = getData();
                String message = getString(R.string.currentPki);
                message += pkiKey;
                TextView pkitextView = (TextView) findViewById(R.id.pkiDisplay);
                pkitextView.setText(message);

                showPkiToast(pkiKey);
            }
        });

        String pkiKey = getData();
        String message = getString(R.string.currentPki);
        message += pkiKey;
        TextView pkitextView = (TextView) findViewById(R.id.pkiDisplay);
        pkitextView.setText(message);

        pkiAuthenticate = (Button) findViewById(R.id.pkiAuthenticate);
        pkiAuthenticate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pkiKey = getData();
                if (!TextUtils.isEmpty(pkiKey)) {
                    authUser(pkiKey);
                } else {
                    Toast.makeText(PKIActivity.this, "You haven't entered a key", Toast.LENGTH_LONG).show();
                }
            }
        });

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String key = bundle.getString("key");
            if (key != null) {
                authUser(key);
            }
        }

    }

    private void showPkiToast(String pkiKey) {
        Toast.makeText(PKIActivity.this, pkiKey, Toast.LENGTH_SHORT).show();
    }

    public void authUser(final String pkiKey) {
        final Context c = this;

        readData(databaseUsers, new DBManager() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                boolean userFound = false;
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String pkiDb = String.valueOf(userSnapshot.child("userId").getValue());
                    if (pkiDb.compareTo(pkiKey) == 0) {
                        userFound = true;
                        break;
                    }
                }

                if (userFound) {
                    Toast.makeText(c, "Key found", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(c, "Key not found", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onStart() {
                return;
            }

            @Override
            public void onFailure() {
                return;
            }
        });
    }

    public void readData(DatabaseReference ref, final DBManager listener) {
        listener.onStart();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listener.onSuccess(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onFailure();
            }
        });

    }
}
