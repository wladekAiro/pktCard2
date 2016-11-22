package com.example.wladek.pocketcard;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.wladek.pocketcard.helper.DatabaseHelper;
import com.example.wladek.pocketcard.pojo.ShopItem;

import java.util.ArrayList;

public class ViewItemsActivity extends AppCompatActivity {

    ListView lstItemsView;
    CustomListAdaptor customListAdaptor;

    DatabaseHelper databaseHelper;

    ArrayList<ShopItem> items = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_items);
        databaseHelper = new DatabaseHelper(this);

        loadShopItems();

        lstItemsView = (ListView) findViewById(R.id.lstItemsView);

        customListAdaptor = new CustomListAdaptor(ViewItemsActivity.this , items);

        lstItemsView.setAdapter(customListAdaptor);


    }

    private void loadShopItems() {
        items.clear();
        items.addAll(databaseHelper.getAllShopItems());
//        Cursor res = databaseHelper.getAllShopItems();
//
//        if (res.getCount() > 0) {
//            while (res.moveToNext()) {
//                ShopItem shopItem = new ShopItem();
//                shopItem.setName(res.getString(1));
//                shopItem.setCode(res.getString(2));
//                shopItem.setUnitPrice(res.getDouble(3));
//                items.add(shopItem);
//            }
//        }
    }

    class CustomListAdaptor extends BaseAdapter{

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
                convertView = layoutInflater.inflate(R.layout.view_items_list_view_row , null);

                viewHolder = new ViewHolder();

                viewHolder.itemName = (TextView) convertView.findViewById(R.id.txtItemName);
                viewHolder.itemCost = (TextView) convertView.findViewById(R.id.txtItemCost);

                convertView.setTag(viewHolder);

            }else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.itemName.setText(shopItem.getName());
            viewHolder.itemCost.setText("Kshs. "+shopItem.getUnitPrice());


            return convertView;
        }
    }

    static class ViewHolder{
        TextView itemName;
        TextView itemCost;
    }
}
