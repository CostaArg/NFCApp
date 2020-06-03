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

                //pki = pkiInput.getText().toString();

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
                    Toast.makeText(PKIActivity.this, "You haven't entered a Public Key", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void showPkiToast(String pkiKey) {
        Toast.makeText(PKIActivity.this, pkiKey, Toast.LENGTH_SHORT).show();
    }

    private void authUser(final String pkiKey) {

        final Context c = this;

        databaseUsers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String userFound;

                userFound = "reset";

                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String pkiDb = String.valueOf(userSnapshot.child("userName").getValue());

                    if (pkiDb.compareTo(pkiKey) == 0) {
                        userFound = "yes";
                    }

                }

                if (userFound.equals("yes")) {
                    Toast.makeText(c, "User found", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(c, "User not found", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }


}
