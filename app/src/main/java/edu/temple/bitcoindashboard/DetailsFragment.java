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

    Fragment contentFragment;
    AddressFragment mAddressFragment;

    public DetailsFragment() {
        // Required empty public constructor
    }

    public DetailsFragment newInstance() {
        DetailsFragment f = new DetailsFragment();
        f.mAddressFragment = new AddressFragment();
        return f;
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
                contentFragment = new ExchangeRateFragment();
                break;
            case "priceChart":
                contentFragment = new PriceChartFragment();
                break;
            case "block":
                contentFragment = new BlockFragment();
                break;
            case "address":
                contentFragment = new AddressFragment();
                break;
            case "quit":
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(0);
            default:
                throw new Exception("Invalid item chosen to transition to!");
        }
        getFragmentManager().beginTransaction()
                .replace(R.id.fragment_content, contentFragment)
                .commit();
        getFragmentManager().executePendingTransactions();
    }


}
