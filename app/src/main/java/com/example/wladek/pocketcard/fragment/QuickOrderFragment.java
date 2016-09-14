package com.example.wladek.pocketcard.fragment;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wladek.pocketcard.BuyScreenActivity;
import com.example.wladek.pocketcard.R;
import com.example.wladek.pocketcard.helper.DatabaseHelper;
import com.example.wladek.pocketcard.pojo.ShopItem;

import java.util.ArrayList;

/**
 * Created by wladek on 7/13/16.
 */
public class QuickOrderFragment extends Fragment {

    View myView;
    DatabaseHelper databaseHelper;
    Typeface typeface;
    ListView listViewQuickOrder;
    CustomListAdaptor customListAdaptor;

    ArrayList<ShopItem> items = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseHelper = new DatabaseHelper(getActivity());
        loadItems();
    }

    private void loadItems() {
        items.clear();

        Cursor res = databaseHelper.getAllShopItems();

        if (res.getCount() > 0) {
            while (res.moveToNext()) {
                ShopItem shopItem = new ShopItem();
                shopItem.setName(res.getString(1));
                shopItem.setCode(res.getString(2));
                shopItem.setUnitPrice(res.getDouble(3));
                items.add(shopItem);
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final BuyScreenActivity activity = (BuyScreenActivity) getActivity();

        //Define typeface for formating text fields and list view
        typeface = Typeface.createFromAsset(activity.getAssets(), "fonts/regular_serif.ttf");

        myView = inflater.inflate(R.layout.fragment_quickorder, container, false);

        listViewQuickOrder = (ListView) myView.findViewById(R.id.listViewQuickOrder);

        customListAdaptor = new CustomListAdaptor(activity , items);

        listViewQuickOrder.setAdapter(customListAdaptor);

        return myView;
    }

    class CustomListAdaptor extends BaseAdapter {

        private ArrayList<ShopItem> shopItems = new ArrayList<>();
        Typeface type;
        Context context;
        ViewHolder viewHolder;
        private LayoutInflater layoutInflater;

        public CustomListAdaptor(Context context, ArrayList<ShopItem> shopItems) {

            layoutInflater = LayoutInflater.from(context);
            this.shopItems = shopItems;
            this.context = context;
            type = Typeface.createFromAsset(context.getAssets(), "fonts/regular_serif.ttf");

        }

        @Override
        public int getCount() {
            return shopItems.size();
        }

        @Override
        public Object getItem(int position) {
            return shopItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            final ShopItem shopItem = shopItems.get(position);

            if (convertView == null){
                convertView = layoutInflater.inflate(R.layout.quick_order_list_row , null);

                viewHolder = new ViewHolder();

                viewHolder.itemName = (TextView) convertView.findViewById(R.id.txtItemName);
                viewHolder.itemCost = (TextView) convertView.findViewById(R.id.txtItemCost);
                viewHolder.btnAddToCart = (ImageButton) convertView.findViewById(R.id.btnAddToCart);

                convertView.setTag(viewHolder);

            }else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.itemName.setText(shopItem.getName());
            viewHolder.itemCost.setText("Kshs. "+shopItem.getUnitPrice());

            viewHolder.btnAddToCart.setOnClickListener(new AddToCartClickListener("btnAddToCart", shopItem, context));


            return convertView;
        }
    }

    static class ViewHolder{
        TextView itemName;
        TextView itemCost;
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
