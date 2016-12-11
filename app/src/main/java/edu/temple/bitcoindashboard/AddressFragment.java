package edu.temple.bitcoindashboard;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class AddressFragment extends Fragment {

    public static final String FILE_NAME = "MyFile";
    ArrayList<String> storedAddresses;
    boolean connected;
    AddressService mAddressService;
    String address;
    BaseAdapter mAdapter;
    final Handler serviceHandler = new Handler(new Handler.Callback() {
        public boolean handleMessage(Message msg) {
            try {
                JSONObject blockObject = new JSONObject((String) msg.obj);
                String address = blockObject.getJSONObject("data").getString("address");
                if (!storedAddresses.contains(address)) {
                    storedAddresses.add(address);
                    if (storedAddresses.size() > 100){
                        storedAddresses.remove(0);
                    }
                }
                mAdapter.notifyDataSetChanged();
                ((TextView) getActivity().findViewById(R.id.display_address))
                        .setText(getString(R.string.address) + ": " + address);
                ((TextView) getActivity().findViewById(R.id.display_balance))
                        .setText(getString(R.string.balance) + ": "
                                + blockObject.getJSONObject("data").getString("balance"));
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            return false;
        }
    });
    ServiceConnection myConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            AddressService.TestBinder binder = (AddressService.TestBinder) service;
            mAddressService = binder.getService();
            connected = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            connected = false;
        }
    };

    public AddressFragment() {
        // Required empty public constructor
    }

    public static AddressFragment newInstance(String param1, String param2) {
        AddressFragment fragment = new AddressFragment();
        return fragment;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList("storedAddresses", storedAddresses);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.appbar_address, menu);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        storedAddresses = new ArrayList<>();
        if (savedInstanceState != null) {
            storedAddresses = savedInstanceState.getStringArrayList("storedAddresses");
        }
        FileInputStream fis;
        try {
            fis = getActivity().openFileInput(FILE_NAME);
            ObjectInputStream ois = new ObjectInputStream(fis);
            storedAddresses = (ArrayList<String>) ois.readObject();
            Log.v("AddressFragment", "Loaded storedAddresses from file");
            Log.v("AddressFragment", "storedAddresses.size() = " + storedAddresses.size());
            for (String address : storedAddresses){
                Log.v("AddressFragment", address);
            }
            ois.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_address, container, false);
        ListView listView = (ListView) v.findViewById(R.id.listview_addresses);
        mAdapter = new MyAdapter(storedAddresses);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> AdapterView, View view, int i, long l) {
                mAddressService.getAddressInfo(serviceHandler, storedAddresses.get(i));
            }
        });
        listView.setAdapter(mAdapter);
        return v;
    }

    @Override
    public void onAttach(Context context) {

        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onStart() {
        super.onStart();
        Intent serviceIntent = new Intent(getActivity(), AddressService.class);
        getActivity().bindService(serviceIntent, myConnection, Context.BIND_AUTO_CREATE);
    }

    public void onPause(){
        super.onPause();
        try {
            FileOutputStream fos = getActivity().openFileOutput(FILE_NAME, 0);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(storedAddresses);
            oos.close();
            File file = new File(getActivity().getFilesDir(), FILE_NAME);
            Log.v("AddressFragment", "Saved storedAddresses to file");
            file.deleteOnExit();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().unbindService(myConnection);
    }

    public class MyAdapter extends BaseAdapter {
        ArrayList<String> addresses;

        public MyAdapter(ArrayList<String> addresses) {
            this.addresses = addresses;
        }

        @Override
        public void unregisterDataSetObserver(DataSetObserver observer) {
            super.unregisterDataSetObserver(observer);
        }

        @Override
        public View getView(int position, View oldView, ViewGroup parent) {

            TextView tv = new TextView((Context) getActivity());

            LinearLayout layout = new LinearLayout((Context) getActivity());

            layout.setOrientation(LinearLayout.VERTICAL);

            tv.setText(getItem(position));
            final String address = getItem(position);
            if (address != null) {
                tv.setTextSize(15);
                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mAddressService.getAddressInfo(serviceHandler, address);
                    }
                });

                layout.addView(tv);
            }

            return layout;
        }

        @Override
        public String getItem(int position) {
            return addresses.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        public int getCount() {
            return addresses.size() < 100 ? addresses.size() : 100;
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                address = ((EditText) getActivity().findViewById(R.id.enter_address))
                        .getText().toString();
                mAddressService.getAddressInfo(serviceHandler, address);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
