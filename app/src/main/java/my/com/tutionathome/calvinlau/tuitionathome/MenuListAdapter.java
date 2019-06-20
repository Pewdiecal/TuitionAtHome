package my.com.tutionathome.calvinlau.tuitionathome;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MenuListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] menuitem;                        //put data var here
    private final Integer[] imgid2;

    public MenuListAdapter(Activity context, String[] menuitem, Integer[] imgid2) {         //data var here
        super(context, R.layout.activity_custom_list, menuitem);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.menuitem=menuitem;
        this.imgid2=imgid2;
    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.activity_menu_custom_list, null,true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.menuilist);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.imgm);         //register data ID here

        txtTitle.setText(menuitem[position]);
        imageView.setImageResource(imgid2[position]);
                                                                //populate data to View here
        return rowView;

    }
}
