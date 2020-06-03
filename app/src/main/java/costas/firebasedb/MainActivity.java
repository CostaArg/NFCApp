package costas.firebasedb;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
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

        Button gotoPKI = (Button) findViewById(R.id.gotoPKI);
        gotoPKI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPKIActivity();
            }
        });

        listViewUsers.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                User user = userList.get(i);
                showUpdateDeleteDialog(user.getUserId(), user.getUserName());
                return true;
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

    private void showUpdateDeleteDialog(final String userId, String userName) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.update_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText editTextName = (EditText) dialogView.findViewById(R.id.editTextName);
        final Spinner spinnerRank = (Spinner) dialogView.findViewById(R.id.spinnerRanks);
        final Button buttonUpdate = (Button) dialogView.findViewById(R.id.buttonUpdateUser);
        final Button buttonDelete = (Button) dialogView.findViewById(R.id.buttonDeleteUser);

        dialogBuilder.setTitle(userName);
        final AlertDialog b = dialogBuilder.create();
        b.show();


        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editTextName.getText().toString().trim();
                String rank = spinnerRank.getSelectedItem().toString();
                if (!TextUtils.isEmpty(name)) {
                    updateUser(userId, name, rank);
                    b.dismiss();
                }
            }
        });


        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                deleteUser(userId);
                b.dismiss();
            }
        });
    }

    private boolean updateUser(String id, String name, String rank) {
        //getting the specified access reference
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("Users").child(id);

        //updating access
        User user = new User(id, name, rank);
        dR.setValue(user);
        Toast.makeText(getApplicationContext(), "User Updated", Toast.LENGTH_LONG).show();
        return true;
    }

    private boolean deleteUser(String id) {
        //getting the specified access reference
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("Users").child(id);

        //removing access
        dR.removeValue();

        //getting the users reference for the specified access
        DatabaseReference drUsers = FirebaseDatabase.getInstance().getReference("users").child(id);

        //removing all users
        drUsers.removeValue();
        Toast.makeText(getApplicationContext(), "User Deleted", Toast.LENGTH_LONG).show();

        return true;
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

    public void openPKIActivity() {
        Intent intent = new Intent(this, PKIActivity.class);
        startActivity(intent);
    }
}
