package costas.firebasedb;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class UserList extends ArrayAdapter<User>{

    private Activity context;
    private List<User> userList;

    public  UserList(Activity context, List<User> userList){
        super(context, R.layout.list_layout, userList);
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.list_layout, null, true);

        TextView textViewName = (TextView) listViewItem.findViewById(R.id.textViewName);
        TextView textViewRank = (TextView) listViewItem.findViewById(R.id.textViewRank);

        User user = userList.get(position);

        textViewName.setText(user.getUserName());
        textViewRank.setText(user.getUserRank());

        return listViewItem;
    }
}
