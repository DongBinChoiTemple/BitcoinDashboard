package edu.temple.bitcoindashboard;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity
        implements NavFragment.OnFragmentInteractionListener {
    String[] menu = {"exchangeRate", "priceChart", "block", "address"};
    String item;
    boolean twoPanes;
    NavFragment navFragment;
    DetailsFragment detailsFragment;

    @Override
    protected void onSaveInstanceState (Bundle outState){
        outState.putString("item", item);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navFragment = new NavFragment();

        //  Determine if only one or two panes are visible
        twoPanes = (findViewById(R.id.fragment_details) != null);

        detailsFragment = new DetailsFragment();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_nav, navFragment);
        fragmentTransaction.commit();
        fragmentManager.executePendingTransactions();
        if (savedInstanceState != null) {
            item = savedInstanceState.getString("item");
            if (twoPanes) {
                getFragmentManager().beginTransaction()
                        .add(R.id.fragment_details, detailsFragment)
                        .commit();
            } else {
                getFragmentManager().executePendingTransactions();
            }
        }
        else {
            //  Load palette fragment by default
            if (twoPanes) {
                getFragmentManager().beginTransaction()
                        .add(R.id.fragment_details, detailsFragment)
                        .commit();
                getFragmentManager().executePendingTransactions();
            }
        }
    }

    protected void onResume(){
        getFragmentManager().executePendingTransactions();
        if (item != null) {
            if (twoPanes)
                detailsFragment.transition(item);
        }
        super.onResume();
    }

    @Override
    public void onFragmentInteraction(String item) {
        doTransition(item);
    }

    private void doTransition(String item){
        this.item = item;
        if (twoPanes) { // if displaying two panes (large or landscape)
            detailsFragment.transition(item);
        } else { // if displaying only one pane
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_nav, detailsFragment)
                    .addToBackStack(null)
                    .commit();
            getFragmentManager().executePendingTransactions();
            detailsFragment.transition(item);
        }
    }
}