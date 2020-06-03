package costas.firebasedb;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PKIActivity extends AppCompatActivity {

    String pki;

    EditText pkiInput;

    Button pkiSubmit;

    Button pkiAuthenticate;

    DatabaseReference databaseUsers;

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
                pki = pkiInput.getText().toString();

                showToast(pki);

                String message = getString(R.string.currentPki);
                message += pki;

                TextView textView = (TextView) findViewById(R.id.pkiDisplay);
                textView.setText(message);
            }
        });

        pkiAuthenticate = (Button) findViewById(R.id.pkiAuthenticate);
        pkiAuthenticate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authUser();
        });

    }

        }

    private void showToast(String pki) {
        Toast.makeText(PKIActivity.this, pki, Toast.LENGTH_SHORT).show();
    }
    }

    private void authUser(String pki, DataSnapshot dataSnapshot) {

        for(DataSnapshot userSnapshot: dataSnapshot.getChildren()){
            String pkiDb = String.valueOf(userSnapshot.child("userName").getValue());

            if(pkiDb.compareTo(pki)==0) {
                Toast.makeText(this, "User found", Toast.LENGTH_LONG).show();
            }else
            {
                Toast.makeText(this, "User not found", Toast.LENGTH_LONG).show();
    }

}
