package costas.firebasedb;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText editTextName;
    Button buttonAdd;
    Spinner spinnerRanks;


    DatabaseReference databaseUsers;

    ListView listViewUsers;

    List<User> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseUsers = FirebaseDatabase.getInstance().getReference("Users");

        editTextName = (EditText) findViewById(R.id.editTextName);
        buttonAdd = (Button) findViewById(R.id.buttonAddUser);
        spinnerRanks = (Spinner) findViewById(R.id.spinnerRanks);

        listViewUsers = (ListView) findViewById(R.id.listViewUsers);

        userList = new ArrayList<>();

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addUser();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                userList.clear();

                for(DataSnapshot userSnapshot: dataSnapshot.getChildren()){
                    User user = userSnapshot.getValue(User.class);

                    userList.add(user);
                }

                UserList adapter = new UserList(MainActivity.this, userList);
                listViewUsers.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

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
