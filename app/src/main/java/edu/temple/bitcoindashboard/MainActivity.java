package edu.temple.bitcoindashboard;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.File;

public class MainActivity extends AppCompatActivity
        implements NavFragment.OnFragmentInteractionListener {
    String[] menu = {"exchangeRate", "priceChart", "block", "address", "quit"};
    boolean twoPanes;
    NavFragment navFragment;
    DetailsFragment detailsFragment;
    public static final String FILE_NAME = "MyFile";

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //  Determine if only one or two panes are visible
        twoPanes = (findViewById(R.id.fragment_details) != null);
        navFragment = new NavFragment();
        detailsFragment = new DetailsFragment();
        getFragmentManager().beginTransaction().replace(R.id.fragment_nav, navFragment).commit();
        getFragmentManager().executePendingTransactions();
    }

    protected void onResume(){
        super.onResume();
        twoPanes = (findViewById(R.id.fragment_details) != null);
        if (twoPanes) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.fragment_details, detailsFragment)
                    .commit();
            getFragmentManager().executePendingTransactions();
            doTransition(detailsFragment, menu[0]);
        }
    }

    @Override
    public void onFragmentInteraction(String item) {
        doTransition(detailsFragment, item);
    }

    private void doTransition(DetailsFragment detailsFragment, String item) {
        if (!twoPanes) { // if displaying only one pane
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_nav, detailsFragment)
                    .addToBackStack(null)
                    .commit();
            getFragmentManager().executePendingTransactions();
        }
        try {
            detailsFragment.transition(item);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void onDestroy(){
        super.onDestroy();
    }
}