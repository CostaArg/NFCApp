package costas.firebasedb;

import android.content.Intent;
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
    public static final String ACCESS_NAME = "costas.firebasedb.accessname";
    public static final String ACCESS_ID = "costas.firebasedb.accessid";

    EditText editTextName;
    Spinner spinnerRank;
    Button buttonAddAccess;
    ListView listViewAccesses;

    //a list to store all the access from firebase database
    List<Access> accesses;

    //our database reference object
    DatabaseReference databaseAccesses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //getting the reference of accesses node
        databaseAccesses = FirebaseDatabase.getInstance().getReference("accesses");

        //getting views
        editTextName = (EditText) findViewById(R.id.editTextName);
        spinnerRank = (Spinner) findViewById(R.id.spinnerRanks);
        listViewAccesses = (ListView) findViewById(R.id.listViewAccesses);

        buttonAddAccess = (Button) findViewById(R.id.buttonAddAccess);

        //list to store accesses
        accesses = new ArrayList<>();


        //adding an onclicklistener to button
        buttonAddAccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //calling the method addAccess()
                //the method is defined below
                //this method is actually performing the write operation
                addAccess();
            }
        });

        //attaching listener to listview
        listViewAccesses.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //getting the selected access
                Access access = accesses.get(i);

                //creating an intent
                Intent intent = new Intent(getApplicationContext(), AccessActivity.class);

                //putting access name and id to intent
                intent.putExtra(ACCESS_ID, access.getAccessId());
                intent.putExtra(ACCESS_NAME, access.getAccessName());

                //starting the activity with intent
                startActivity(intent);
            }
        });

        listViewAccesses.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Access access = accesses.get(i);
                showUpdateDeleteDialog(access.getAccessId(), access.getAccessName());
                return true;
            }
        });


    }

    private void showUpdateDeleteDialog(final String accessId, String accessName) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.update_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText editTextName = (EditText) dialogView.findViewById(R.id.editTextName);
        final Spinner spinnerRank = (Spinner) dialogView.findViewById(R.id.spinnerRanks);
        final Button buttonUpdate = (Button) dialogView.findViewById(R.id.buttonUpdateAccess);
        final Button buttonDelete = (Button) dialogView.findViewById(R.id.buttonDeleteAccess);

        dialogBuilder.setTitle(accessName);
        final AlertDialog b = dialogBuilder.create();
        b.show();


        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editTextName.getText().toString().trim();
                String rank = spinnerRank.getSelectedItem().toString();
                if (!TextUtils.isEmpty(name)) {
                    updateAccess(accessId, name, rank);
                    b.dismiss();
                }
            }
        });


        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                deleteAccess(accessId);
                b.dismiss();
            }
        });
    }

    private boolean updateAccess(String id, String name, String rank) {
        //getting the specified access reference
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("accesses").child(id);

        //updating access
        Access access = new Access(id, name, rank);
        dR.setValue(access);
        Toast.makeText(getApplicationContext(), "Access Updated", Toast.LENGTH_LONG).show();
        return true;
    }

    private boolean deleteAccess(String id) {
        //getting the specified access reference
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("accesses").child(id);

        //removing access
        dR.removeValue();

        //getting the users reference for the specified access
        DatabaseReference drUsers = FirebaseDatabase.getInstance().getReference("users").child(id);

        //removing all users
        drUsers.removeValue();
        Toast.makeText(getApplicationContext(), "Access Deleted", Toast.LENGTH_LONG).show();

        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        //attaching value event listener
        databaseAccesses.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //clearing the previous access list
                accesses.clear();

                //iterating through all the nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting access
                    Access access = postSnapshot.getValue(Access.class);
                    //adding access to the list
                    accesses.add(access);
                }

                //creating adapter
                AccessList accessAdapter = new AccessList(MainActivity.this, accesses);
                //attaching adapter to the listview
                listViewAccesses.setAdapter(accessAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    /*
     * This method is saving a new access to the
     * Firebase Realtime Database
     * */
    private void addAccess() {
        //getting the values to save
        String name = editTextName.getText().toString().trim();
        String rank = spinnerRank.getSelectedItem().toString();

        //checking if the value is provided
        if (!TextUtils.isEmpty(name)) {

            //getting a unique id using push().getKey() method
            //it will create a unique id and we will use it as the Primary Key for our Access
            String id = databaseAccesses.push().getKey();

            //creating an Access Object
            Access access = new Access(id, name, rank);

            //Saving the Access
            databaseAccesses.child(id).setValue(access);

            //setting edittext to blank again
            editTextName.setText("");

            //displaying a success toast
            Toast.makeText(this, "Access added", Toast.LENGTH_LONG).show();
        } else {
            //if the value is not given displaying a toast
            Toast.makeText(this, "Please enter a name", Toast.LENGTH_LONG).show();
        }
    }
}
