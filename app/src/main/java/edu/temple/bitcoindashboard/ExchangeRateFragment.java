package edu.temple.bitcoindashboard;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class ExchangeRateFragment extends Fragment {
    public ExchangeRateFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ExchangeRateFragment newInstance(String param1, String param2) {
        ExchangeRateFragment fragment = new ExchangeRateFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_exchange_rate, container, false);
    }

    public void onStart(){
        super.onStart();
        ExchangeRateTask task = new ExchangeRateTask();
        task.execute("https://blockchain.info/ticker");
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private class ExchangeRateTask extends AsyncTask<String, Void, Double> {
        protected Double doInBackground(String... strings){
            try {
                URL url = new URL(strings[0]);
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(
                                url.openStream()));
                String nextLine;
                StringBuilder sb = new StringBuilder();
                while ((nextLine = reader.readLine()) != null) {
                    sb.append(nextLine);
                }
                String response = sb.toString();
                Log.v("Downloaded data", response);
                JSONObject exRateObject = new JSONObject(response);
                return exRateObject.getJSONObject("USD").getDouble("last");
            } catch (Exception e) {
                e.printStackTrace();
                return 0.0;
            }
        }
        protected void onPostExecute(Double result){
            ((TextView)getActivity().findViewById(R.id.display_exrate)).setText(String.valueOf(result));
        }
    }

}
