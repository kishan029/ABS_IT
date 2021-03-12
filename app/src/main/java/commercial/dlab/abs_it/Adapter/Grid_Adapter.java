package commercial.dlab.abs_it.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import commercial.dlab.abs_it.R;

public class Grid_Adapter extends BaseAdapter {

    private final Context context;
    private final String[] user_name;
    private final String[] user_location;
    public final int[] user_pic;
    private LayoutInflater layoutInflater;

    public Grid_Adapter(Context context, String[] user_name,String[] user_location, int[] user_pic) {
        this.context = context;
        this.user_name = user_name;
        this.user_pic=user_pic;
        this.user_location=user_location;
    }

    @Override
    public int getCount() {
        return user_name.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int random= (int) Math.round(Math.random()*3);
        if(layoutInflater==null){
            layoutInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if(convertView==null) {
            convertView = layoutInflater.inflate(R.layout.grid_view, null);
        }
        TextView username=convertView.findViewById(R.id.username);
        TextView userlocation=convertView.findViewById(R.id.user_location);
        ImageView imageView =convertView.findViewById(R.id.user_pic);
        username.setText(user_name[position]);
        userlocation.setText(user_location[position]);
        imageView.setImageResource(user_pic[random]);
        return convertView;
    }
}
