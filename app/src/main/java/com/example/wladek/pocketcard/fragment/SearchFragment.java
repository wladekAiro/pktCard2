package com.example.wladek.pocketcard.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.wladek.pocketcard.BuyScreenActivity;
import com.example.wladek.pocketcard.R;
import com.example.wladek.pocketcard.helper.DatabaseHelper;
import com.example.wladek.pocketcard.pojo.ShopItem;

import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Created by wladek on 7/13/16.
 */
public class SearchFragment extends Fragment {

    Logger logger = Logger.getLogger("SEARCH FRAGMENT");

    TextView productName;
    Button btnAddToCart;

    View myFragmentView;
    SearchView search;
    Typeface typeface;
    ListView searchResults;
    String found = "N";
    DatabaseHelper databaseHelper;


    //Array list to hold data pulled from the server
    ArrayList<ShopItem> itemResults;

    //Array to hold filtered resultd after search
    ArrayList<ShopItem> filteredResults = new ArrayList<ShopItem>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseHelper = new DatabaseHelper(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Get content of the buy activity

        final BuyScreenActivity activity = (BuyScreenActivity) getActivity();

        //Define typeface for formating text fields and list view
        typeface = Typeface.createFromAsset(activity.getAssets(), "fonts/regular_serif.ttf");

        myFragmentView = inflater.inflate(R.layout.fragment_search, container, false);
        search = (SearchView) myFragmentView.findViewById(R.id.searchViewItem);

        search.setQueryHint("Start typing to search ...");


        searchResults = (ListView) myFragmentView.findViewById(R.id.listViewItem);


        //this part is to handle the situation when user enters any search criteria
        //How should the application behave.

        search.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {

            }
        });

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if (newText.length() > 1) {
                    searchResults.setVisibility(View.VISIBLE);
                    MyAssyncTask m = (MyAssyncTask) new MyAssyncTask().execute(newText);
                } else {
                    searchResults.setVisibility(View.INVISIBLE);
                }

                return false;
            }
        });

        return myFragmentView;
    }

    public ArrayList<ShopItem> getItemResults() {
        return itemResults;
    }

    public void setItemResults(ArrayList<ShopItem> itemResults) {
        this.itemResults = itemResults;
    }

    //In this assync task, we are fetching data from server for the search string entered by the user

    public class MyAssyncTask extends AsyncTask<String, Void, String> {
        String url = new String();
        String textSearch;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Searching...");
            progressDialog.getWindow().setGravity(Gravity.CENTER);
            progressDialog.show();

        }

        @Override
        protected String doInBackground(String... strings) {
            String returnResult = getProductList(itemResults);
            this.textSearch = strings[0];
            return returnResult;
        }

        private String getProductList(ArrayList<ShopItem> shopItems) {

            if (shopItems.size() > 0) {

                boolean matchFound = false;
                for (int j = 0; j < shopItems.size(); j++) {
                    if (itemResults.get(j).getCode().equals(shopItems.get(j).getCode())) {
                        matchFound = true;
                    }

                    if (!matchFound) {
                        itemResults.add(shopItems.get(j));
                    }
                }
            }
            return "OK";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //Calling this method to to filter the search results fromt item list and add them to filtered item list
            filterItemArray(textSearch);
            searchResults.setAdapter(new SearchResultsAdapter(getContext(), filteredResults));
            progressDialog.dismiss();
        }

        private void filterItemArray(String textSearch) {
            String pCode;

            filteredResults.clear();

            for (int i = 0; i < itemResults.size(); i++) {
                pCode = itemResults.get(i).getCode().toLowerCase();

                if (pCode.contains(textSearch.toLowerCase()) ||
                        itemResults.get(i).getName().toLowerCase().contains(textSearch.toLowerCase())) {
                    filteredResults.add(itemResults.get(i));
                }
            }
        }
    }

    public class SearchResultsAdapter extends BaseAdapter {

        private LayoutInflater layoutInflater;
        private ArrayList<ShopItem> shopItems = new ArrayList<ShopItem>();
        int count;
        Typeface typeface;
        Context context;

        public SearchResultsAdapter(Context context, ArrayList<ShopItem> shopItems) {
            layoutInflater = LayoutInflater.from(context);
            this.shopItems = shopItems;
            this.count = shopItems.size();
            this.context = context;
            typeface = Typeface.createFromAsset(context.getAssets(), "fonts/regular_serif.ttf");
        }

        @Override
        public int getCount() {
            return count;
        }

        @Override
        public Object getItem(int i) {
            return shopItems.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            ViewHolder viewHolder;
            ShopItem shopItem = shopItems.get(i);

            Log.d(" ++ CURRENT PRODUCT ++", "Name :" + shopItem.getName() + "CODE :" + shopItem.getCode());

            if (view == null) {
                view = layoutInflater.inflate(R.layout.listtwo_searchresults, null);
                viewHolder = new ViewHolder();
                viewHolder.productName = (TextView) view.findViewById(R.id.txtItemName);
                viewHolder.btnAddToCart = (ImageButton) view.findViewById(R.id.btnAddToCart);

                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }

            viewHolder.productName.setText(shopItem.getName());
            viewHolder.productName.setTypeface(typeface);

            viewHolder.btnAddToCart.setOnClickListener(new AddToCartClickListener("btnAddToCart", shopItem, context));

            return view;
        }
    }

    static class ViewHolder {
        TextView productName;
        ImageButton btnAddToCart;
    }

    class AddToCartClickListener implements View.OnClickListener {
        String btn_name;
        ShopItem shopItem;
        Context context;

        public AddToCartClickListener(String btn_name, ShopItem shopItem, Context context) {
            this.btn_name = btn_name;
            this.shopItem = shopItem;
            this.context = context;

        }

        //Here we are adding the item to the cart
        //If the item already exists in the cart, increase the quantity by one, else add the item to cart
        //If the quantity of the item has reached 10, then do nothing.
        @Override
        public void onClick(View view) {

            logger.config("CLICKED");

            try {

                if (shopItem != null) {

                    databaseHelper.insertIntoCart(shopItem);

                    Toast.makeText(context, shopItem.getName() + " added to cart",
                            Toast.LENGTH_SHORT).show();
                }

            }catch (NullPointerException e){
                e.printStackTrace();
            }

        }
    }
}

