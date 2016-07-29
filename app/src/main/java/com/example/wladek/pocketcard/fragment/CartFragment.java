package com.example.wladek.pocketcard.fragment;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.wladek.pocketcard.BuyScreenActivity;
import com.example.wladek.pocketcard.R;
import com.example.wladek.pocketcard.helper.DatabaseHelper;
import com.example.wladek.pocketcard.pojo.ShopItem;

import java.util.ArrayList;

/**
 * Created by wladek on 7/13/16.
 */
public class CartFragment extends Fragment {
    View myView;
    ArrayList<ShopItem> cartList = new ArrayList<ShopItem>();
    int count = 0;
    int totalCartItemCount = 0;
    Double totalCartValue = new Double(0);
    ListView lvl;
    final String[] qtyValues = {"1", "2", "4", "5", "6", "7", "8", "9", "10"};
    TextView txtItemText;
    TextView txtItemCount;
    TextView txtTotalAmount;
    Button btnCheckOut;
    TextView txtCartEmpty;
    DatabaseHelper databaseHelper;

    BuyScreenActivity buyScreenActivity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseHelper = new DatabaseHelper(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        myView = inflater.inflate(R.layout.fragment_mycart, container, false);
        getCartData();
        totalCartItemCount = cartList.size();
        totalCartValue = new Double(0);

        for (int i = 0; i < cartList.size(); i++) {
            totalCartValue = (totalCartValue + cartList.get(i).getTotalCartValue());
        }

        buyScreenActivity = (BuyScreenActivity) getActivity();

        Typeface typeface = Typeface.createFromAsset(buyScreenActivity.getAssets(), "fonts/AsimovNar.otf");

        txtItemText = (TextView) myView.findViewById(R.id.txtItemText);
        txtItemCount = (TextView) myView.findViewById(R.id.txtItemCount);
        txtTotalAmount = (TextView) myView.findViewById(R.id.txtTotalAmount);
        btnCheckOut = (Button) myView.findViewById(R.id.btnCheckOut);
        lvl = (ListView) myView.findViewById(R.id.listCartView);
        txtCartEmpty = (TextView) myView.findViewById(R.id.txtCartEmpty);


        if (totalCartItemCount == 0){
            txtItemText.setVisibility(myView.INVISIBLE);
            txtTotalAmount.setVisibility(myView.INVISIBLE);
            btnCheckOut.setVisibility(myView.INVISIBLE);
            lvl.setVisibility(myView.INVISIBLE);
            txtCartEmpty.setVisibility(myView.VISIBLE);
        }else {
            txtItemText.setVisibility(myView.VISIBLE);
            txtTotalAmount.setVisibility(myView.VISIBLE);
            btnCheckOut.setVisibility(myView.VISIBLE);
            lvl.setVisibility(myView.VISIBLE);
            txtCartEmpty.setVisibility(myView.INVISIBLE);
        }

        txtItemCount.setText(""+totalCartItemCount);
        txtTotalAmount.setText("Ksh."+totalCartValue);

        txtItemText.setTypeface(typeface);
        txtCartEmpty.setTypeface(typeface);
        txtTotalAmount.setTypeface(typeface);
        btnCheckOut.setTypeface(typeface);

        lvl.setAdapter(new CustomListOne(this.getActivity() , cartList));
        btnCheckOut.setOnClickListener(new MyCheckOutClickListener("btnCheckOut"));

        return myView;
    }

    public class CustomListOne extends BaseAdapter{

        private LayoutInflater layoutInflater;
        ViewHolder viewHolder;
        private ArrayList<ShopItem> cartList = new ArrayList<ShopItem>();
        int cartCounter;
        Typeface type;
        Context context;

        public CustomListOne(Context context, ArrayList<ShopItem> cartList) {
            layoutInflater = LayoutInflater.from(context);
            this.cartList = cartList;
            this.cartCounter = cartList.size();
            this.context = context;
            type = Typeface.createFromAsset(context.getAssets(), "fonts/AsimovNar.otf");

        }

        @Override
        public int getCount() {
            return cartCounter;
        }

        @Override
        public Object getItem(int position) {
            return cartList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ShopItem tmpIem =
                    cartList.get(position);

            if (convertView == null){
                convertView = layoutInflater.inflate(R.layout.listone_custom , null);

                viewHolder = new ViewHolder();

                viewHolder.btnRmFromCart = (ImageButton) convertView.findViewById(R.id.imgBtnRmFromCart);
                viewHolder.spnQty = (Spinner) convertView.findViewById(R.id.spnQty);
                viewHolder.txtCartItemName = (TextView) convertView.findViewById(R.id.txtCartItemName);
                viewHolder.txtCartItemValue = (TextView) convertView.findViewById(R.id.txtCartItemValue);

                convertView.setTag(viewHolder);
            }else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.txtCartItemName.setText(tmpIem.getName());
            viewHolder.txtCartItemValue.setText(""+tmpIem.getTotalCartValue());

            viewHolder.txtCartItemName.setTypeface(type);
            viewHolder.txtCartItemValue.setTypeface(type);

            return null;
        }
    }

    static class ViewHolder{
        ImageButton btnRmFromCart;
        Spinner spnQty;
        TextView txtCartItemName;
        TextView txtCartItemValue;
    }

    public class MyCheckOutClickListener implements View.OnClickListener{

        public MyCheckOutClickListener(String btnCheckOut) {

        }

        @Override
        public void onClick(View v) {

        }
    }

    private void getCartData() {
        cartList.clear();
        cartList.addAll(databaseHelper.getCartItems());

        Log.e("LIST SIZE" , "+++ "+cartList.size());
    }
}
