package edu.temple.bitcoindashboard;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class BlockFragment extends Fragment {

    boolean connected;
    BlockService mBlockService;
    int blockNo;
    final Handler serviceHandler = new Handler(new Handler.Callback() {
        public boolean handleMessage(Message msg) {
            try {
                JSONObject blockObject = new JSONObject((String) msg.obj);
                blockNo = blockObject.getJSONObject("data").getInt("nb");
                ((TextView) getActivity().findViewById(R.id.display_nb))
                        .setText(getString(R.string.block_number) + ": "
                                + blockObject.getJSONObject("data").getString("nb"));
                ((TextView) getActivity().findViewById(R.id.display_hash))
                        .setText(getString(R.string.hash) + ": "
                                + blockObject.getJSONObject("data").getString("hash"));
                ((TextView) getActivity().findViewById(R.id.display_time_utc))
                        .setText(getString(R.string.timestamp) + ": "
                                + blockObject.getJSONObject("data").getString("time_utc"));
                ((TextView) getActivity().findViewById(R.id.display_prev_block_nb))
                        .setText(getString(R.string.prev_block) + ": "
                                + blockObject.getJSONObject("data").getString("prev_block_nb"));
                ((TextView) getActivity().findViewById(R.id.display_next_block_nb))
                        .setText(getString(R.string.next_block) + ": "
                                + blockObject.getJSONObject("data").getString("next_block_nb"));
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
            BlockService.TestBinder binder = (BlockService.TestBinder) service;
            mBlockService = binder.getService();
            connected = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            connected = false;
        }
    };

    public BlockFragment() {
        // Required empty public constructor
    }

    public static BlockFragment newInstance() {
        BlockFragment fragment = new BlockFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.appbar_block, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView =
                (SearchView) MenuItemCompat.getActionView(searchItem);

    }

    @Override
    public void onStart() {
        super.onStart();
        Intent serviceIntent = new Intent(getActivity(), BlockService.class);
        getActivity().bindService(serviceIntent, myConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().unbindService(myConnection);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_previous:
                mBlockService.goToPrevBlock(serviceHandler, blockNo);
                return true;
            case R.id.action_next:
                mBlockService.goToNextBlock(serviceHandler, blockNo);
                return true;
            case R.id.action_search:
                blockNo = Integer.parseInt
                        (((EditText) getActivity().findViewById(R.id.enter_block_no))
                                .getText().toString());
                mBlockService.getBlockInfo(serviceHandler, blockNo);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_block, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
