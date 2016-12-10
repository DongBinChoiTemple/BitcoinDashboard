package edu.temple.bitcoindashboard;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;


public class PriceChartFragment extends Fragment {


    boolean connected;
    String chartType;
    PriceChartService mPriceChartService;
    Handler serviceHandler;
    ServiceConnection myConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            PriceChartService.TestBinder binder = (PriceChartService.TestBinder) service;
            mPriceChartService = binder.getService();
            mPriceChartService.getChart(serviceHandler, chartType);
            connected = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            connected = false;
        }
    };

    public PriceChartFragment() {
        // Required empty public constructor
    }

    public static PriceChartFragment newInstance() {
        PriceChartFragment fragment = new PriceChartFragment();
        fragment.chartType = "1d";
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_price_chart, container, false);
        ((RadioButton) v.findViewById(R.id.radio_1d)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chartType = "1d";
                mPriceChartService.changeTimeInterval(chartType);
            }
        });
        ((RadioButton) v.findViewById(R.id.radio_5d)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chartType = "5d";
                mPriceChartService.changeTimeInterval(chartType);
            }
        });
        serviceHandler = new Handler(new Handler.Callback() {
            public boolean handleMessage(Message msg) {
                Bitmap chart = (Bitmap) msg.obj;
                ImageView iv = (ImageView) v.findViewById(R.id.imageView);
                iv.setImageBitmap(chart);
                return false;
            }
        });
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        Intent serviceIntent = new Intent(getActivity(), PriceChartService.class);
        getActivity().bindService(serviceIntent, myConnection, Context.BIND_AUTO_CREATE);
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
    public void onStop() {
        super.onStop();
        getActivity().unbindService(myConnection);
    }
}
