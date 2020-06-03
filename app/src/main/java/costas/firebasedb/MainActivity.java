package costas.firebasedb;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;

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
        final RelativeLayout layout = (RelativeLayout) findViewById(R.id.activity_main);
        listViewUsers = (ListView) findViewById(R.id.listViewUsers);

        userList = new ArrayList<>();

        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if (!isConnected) {
            Snackbar snackbar = Snackbar
                    .make(layout, "Your wifi is off", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Try again", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                        }
            });
            snackbar.show();
        }

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

        Button gotoNFC = (Button) findViewById(R.id.gotoNFC);
        gotoNFC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNFCActivity();
            }
        });



        listViewUsers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                User user = userList.get(i);
                showUpdateDeleteDialog(user.getUserId(), user.getUserName());
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
                } else {
                    if (!TextUtils.isEmpty(rank)) {
                        updateRank(userId, rank);
                        b.dismiss();
                    }
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
        //getting the specified user reference
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("Users").child(id);

        //updating user
        User user = new User(id, name, rank);
        dR.setValue(user);
        Toast.makeText(getApplicationContext(), "User Updated", Toast.LENGTH_LONG).show();
        return true;
    }

    private boolean updateRank(String id, String rank) {
        //getting the specified rank reference
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("Users").child(id).child("userRank");

        //updating rank
        dR.setValue(rank);
        Toast.makeText(getApplicationContext(), "Rank Updated", Toast.LENGTH_LONG).show();
        return true;
    }

    private boolean deleteUser(String id) {
        //getting the specified user reference
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("Users").child(id);

        //removing user
        dR.removeValue();

        //getting the db reference for the specified user
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

            Toast.makeText(this, "User added", Toast.LENGTH_LONG).show();

        }else{
            Toast.makeText(this, "Please enter name", Toast.LENGTH_LONG).show();
        }
    }

    public void openPKIActivity() {
        Intent intent = new Intent(this, PKIActivity.class);
        startActivity(intent);
    }

    public void openNFCActivity() {
        Intent intent = new Intent(this, NFCActivity.class);
        startActivity(intent);
    }

}
