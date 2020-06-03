//package costas.firebasedb;
//
//public class PKICompare{
//
//        DatabaseReference databaseUsers;
//
//        ListView listViewUsers;
//
//        List<User> userList;
//
//@Override
//protected void onStart(){
//        super.onStart();
//
//        databaseUsers.addValueEventListener(new ValueEventListener(){
//@Override
//public void onDataChange(DataSnapshot dataSnapshot){
//
//        userList.clear();
//
//        for(DataSnapshot userSnapshot:dataSnapshot.getChildren()){
//        User user=userSnapshot.getValue(User.class);
//
//        userList.add(user);
//        }
//
//        UserList adapter=new UserList(MainActivity.this,userList);
//        listViewUsers.setAdapter(adapter);
//        }
//
//@Override
//public void onCancelled(DatabaseError databaseError){
//
//        }
//        });
//        }
//        }