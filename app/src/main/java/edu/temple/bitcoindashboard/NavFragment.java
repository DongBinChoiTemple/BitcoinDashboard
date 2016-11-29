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

    boolean twoPaneParent;

    public OnFragmentInteractionListener activity;

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
        String[] menuItemNames
                = {"viewCurrentExchangeRate", "getPriceChart", "viewBlockInfo", "viewBitcoinAddress"};
        Adapter a = new MyAdapter(menuItemNames);
        lv.setAdapter((ListAdapter)a);
        return v;
    }

    public class MyAdapter implements ListAdapter {
        String[] menu;
        public MyAdapter (String[] array){
            menu = array;
        }

        @Override
        public void registerDataSetObserver(DataSetObserver dataSetObserver) {

        }

        @Override
        public void unregisterDataSetObserver(DataSetObserver observer) {

        }

        @Override
        public View getView(int position, View oldView, ViewGroup parent) {

            TextView tv = new TextView((Context) activity);

            LinearLayout layout = new LinearLayout((Context) activity);

            layout.setOrientation(LinearLayout.VERTICAL);

            String[] menu = getResources().getStringArray(R.array.menu);
            tv.setText(menu[position]);
            final String item = menu[position];
            if (item != null){
                tv.setTextSize(30);
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
        public int getItemViewType(int i) {
            return 0;
        }

        @Override
        public int getViewTypeCount() {
            return 0;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public String getItem(int position){
            return menu[position];
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        public int getCount(){
            return menu.length;
        }

        @Override
        public boolean areAllItemsEnabled() {
            return false;
        }

        @Override
        public boolean isEnabled(int i) {
            return false;
        }
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(String item);
    }
}