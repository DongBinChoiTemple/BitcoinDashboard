package edu.temple.bitcoindashboard;

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
import android.view.LayoutInflater;
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

import java.util.ArrayList;

public class AddressFragment extends Fragment {


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
                    mAdapter.notifyDataSetChanged();
                }
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
            getActivity().findViewById(R.id.button_go).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    address = ((EditText) getActivity().findViewById(R.id.enter_address)).getText().toString();
                    mAddressService.getAddressInfo(serviceHandler, address);
                }
            });
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            connected = false;
        }
    };

    public AddressFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        storedAddresses = new ArrayList<>();
        if (savedInstanceState != null) {
            storedAddresses = savedInstanceState.getStringArrayList("storedAddresses");
        }
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_address, container, false);
        ListView listView = (ListView) v.findViewById(R.id.listview_addresses);
        // Create an ArrayAdapter using the string array and a default spinner layout
        mAdapter = new MyAdapter(storedAddresses);
        // Apply the adapter to the spinner
        // set up listview
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
            return addresses.size();
        }

    }


}
