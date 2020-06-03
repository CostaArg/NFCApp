package costas.firebasedb;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class AccessList extends ArrayAdapter<Access> {
    private Activity context;
    List<Access> accesses;

    public AccessList(Activity context, List<Access> accesses) {
        super(context, R.layout.layout_access_list, accesses);
        this.context = context;
        this.accesses = accesses;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.layout_access_list, null, true);

        TextView textViewName = (TextView) listViewItem.findViewById(R.id.textViewName);
        TextView textViewRank = (TextView) listViewItem.findViewById(R.id.textViewRank);

        Access access = accesses.get(position);
        textViewName.setText(access.getAccessName());
        textViewRank.setText(access.getAccessRank());

        return listViewItem;
    }
}