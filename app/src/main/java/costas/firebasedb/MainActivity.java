package costas.firebasedb;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    EditText editTextName;
    Button buttonAdd;
    Spinner spinnerRanks;


    DatabaseReference databaseUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseUsers = FirebaseDatabase.getInstance().getReference("Users");

        editTextName = (EditText) findViewById(R.id.editTextName);
        buttonAdd = (Button) findViewById(R.id.buttonAddUser);
        spinnerRanks = (Spinner) findViewById(R.id.spinnerRanks);

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addUser();
            }
        });
    }

    private void addUser(){
        String name = editTextName.getText().toString().trim();
        String rank = spinnerRanks.getSelectedItem().toString();

        if(!TextUtils.isEmpty(name)){

          String id =  databaseUsers.push().getKey();

          User user = new User(id, name, rank);

          databaseUsers.child(id).setValue(user);

          Toast.makeText(this, "Users added", Toast.LENGTH_LONG).show();

        }else{
            Toast.makeText(this, "Please enter name", Toast.LENGTH_LONG).show();
        }
    }
}
