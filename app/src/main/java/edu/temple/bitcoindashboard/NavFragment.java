package edu.temple.bitcoindashboard;

import android.content.Context;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class NavFragment extends Fragment {

    public OnFragmentInteractionListener activity;
    boolean twoPaneParent;

    String[] menu = {"exchangeRate", "priceChart", "block", "address", "quit"};
    public NavFragment() {
        // Required empty public constructor
    }

    public void onAttach(Context activity){
        super.onAttach(activity);
        this.activity = (OnFragmentInteractionListener) activity;
    }

    public void onDetach(){
        super.onDetach();
        this.activity = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_nav, container, false);
        ListView lv = (ListView) v.findViewById(R.id.listView);
        Adapter a = new MyAdapter(menu);
        lv.setAdapter((ListAdapter)a);
        return v;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(String item);
    }

    public class MyAdapter extends BaseAdapter {
        String[] menu;
        public MyAdapter (String[] array){
            menu = array;
        }

        @Override
        public void unregisterDataSetObserver(DataSetObserver observer) {
            super.unregisterDataSetObserver(observer);
        }

        @Override
        public View getView(int position, View oldView, ViewGroup parent) {

            TextView tv = new TextView((Context) activity);

            LinearLayout layout = new LinearLayout((Context) activity);

            layout.setOrientation(LinearLayout.VERTICAL);

            String[] menuItemNames = getResources().getStringArray(R.array.menu_main);
            tv.setText(menuItemNames[position]);
            final String item = getItem(position);
            if (item != null){
                tv.setTextSize(20);
                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        activity.onFragmentInteraction(item);
                    }
                });

                layout.addView(tv);
            }

            return layout;
        }

        @Override
        public String getItem(int position){
            return menu[position];
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        public int getCount(){
            return menu.length;
        }

    }
}