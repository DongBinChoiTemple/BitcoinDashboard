package edu.temple.bitcoindashboard;


import android.app.FragmentManager;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailsFragment extends Fragment {

    public DetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_details, container, false);
    }

    public void transition(String item) throws Exception {
        switch (item) {
            case "exchangeRate":
                getFragmentManager().beginTransaction()
                        .replace(R.id.fragment_content, new ExchangeRateFragment()).commit();
                getFragmentManager().executePendingTransactions();
                break;
            case "priceChart":
                getFragmentManager().beginTransaction()
                        .replace(R.id.fragment_content, new PriceChartFragment()).commit();
                getFragmentManager().executePendingTransactions();
                break;
            case "block":
                getFragmentManager().beginTransaction()
                        .replace(R.id.fragment_content, new BlockFragment()).commit();
                getFragmentManager().executePendingTransactions();
                break;
            case "address":
                getFragmentManager().beginTransaction()
                        .replace(R.id.fragment_content, new AddressFragment()).commit();
                getFragmentManager().executePendingTransactions();
                break;
            default:
                throw new Exception("Invalid item chosen to transition to!");
        }
    }

}
