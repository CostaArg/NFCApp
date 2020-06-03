package costas.firebasedb;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AccessActivity extends AppCompatActivity {

    Button buttonAddUser;
    EditText editTextUserName;
    SeekBar seekBarRating;
    TextView textViewRating, textViewAccess;
    ListView listViewUsers;

    DatabaseReference databaseUsers;

    List<User> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_access);

        Intent intent = getIntent();

        /*
         * this line is important
         * this time we are not getting the reference of a direct node
         * but inside the node user we are creating a new child with the access id
         * and inside that node we will store all the users with unique ids
         * */
        databaseUsers = FirebaseDatabase.getInstance().getReference("users").child(intent.getStringExtra(MainActivity.ACCESS_ID));

        buttonAddUser = (Button) findViewById(R.id.buttonAddUser);
        editTextUserName = (EditText) findViewById(R.id.editTextName);
        seekBarRating = (SeekBar) findViewById(R.id.seekBarRating);
        textViewRating = (TextView) findViewById(R.id.textViewRating);
        textViewAccess = (TextView) findViewById(R.id.textViewAccess);
        listViewUsers = (ListView) findViewById(R.id.listViewUsers);

        users = new ArrayList<>();

        textViewAccess.setText(intent.getStringExtra(MainActivity.ACCESS_NAME));

        seekBarRating.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                textViewRating.setText(String.valueOf(i));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        buttonAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveUser();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                users.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    User user = postSnapshot.getValue(User.class);
                    users.add(user);
                }
                UserList userListAdapter = new UserList(AccessActivity.this, users);
                listViewUsers.setAdapter(userListAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void saveUser() {
        String userName = editTextUserName.getText().toString().trim();
        int rating = seekBarRating.getProgress();
        if (!TextUtils.isEmpty(userName)) {
            String id  = databaseUsers.push().getKey();
            User user = new User(id, userName, rating);
            databaseUsers.child(id).setValue(user);
            Toast.makeText(this, "User saved", Toast.LENGTH_LONG).show();
            editTextUserName.setText("");
        } else {
            Toast.makeText(this, "Please enter user name", Toast.LENGTH_LONG).show();
        }
    }
}
