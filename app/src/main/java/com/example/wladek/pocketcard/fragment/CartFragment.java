package com.example.wladek.pocketcard.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.wladek.pocketcard.BuyScreenActivity;
import com.example.wladek.pocketcard.R;
import com.example.wladek.pocketcard.helper.DatabaseHelper;
import com.example.wladek.pocketcard.net.CheckoutRequest;
import com.example.wladek.pocketcard.pojo.ShopItem;
import com.example.wladek.pocketcard.pojo.StudentData;
import com.example.wladek.pocketcard.util.ConnectivityReceiver;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by wladek on 7/13/16.
 */
public class CartFragment extends Fragment {
    View myView;
    ArrayList<ShopItem> cartList = new ArrayList<ShopItem>();
    int totalCartItemCount = 0;
    Double totalCartValue = new Double(0);
    ListView lvl;
    final String[] qtyValues = {"1", "2", "3" ,"4", "5", "6", "7", "8", "9", "10"};
    TextView txtItemText;
    TextView txtItemCount;
    TextView txtTotalAmount;
    Button btnCheckOut;
    ImageButton btnRefreshCart;
    TextView txtCartEmpty;
    DatabaseHelper databaseHelper;

    BuyScreenActivity buyScreenActivity;
    AlertDialog.Builder builder;
    AlertDialog dialog;

    SweetAlertDialog sweetAlertDialog;

    boolean checkedOut;

    ConnectivityReceiver receiver;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseHelper = new DatabaseHelper(getActivity());
        receiver = new ConnectivityReceiver(getContext() , getActivity());
        getCartData();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        myView = inflater.inflate(R.layout.fragment_mycart, container, false);
        totalCartItemCount = cartList.size();
        totalCartValue = new Double(0);

        for (int i = 0; i < cartList.size(); i++) {
            totalCartValue = (totalCartValue + cartList.get(i).getTotalCartValue());
        }

        buyScreenActivity = (BuyScreenActivity) getActivity();

        Typeface typeface = Typeface.createFromAsset(buyScreenActivity.getAssets(), "fonts/regular_serif.ttf");

        txtItemText = (TextView) myView.findViewById(R.id.txtItemText);
        txtItemCount = (TextView) myView.findViewById(R.id.txtItemCount);
        txtTotalAmount = (TextView) myView.findViewById(R.id.txtTotalAmount);
        btnCheckOut = (Button) myView.findViewById(R.id.btnCheckOut);
        btnRefreshCart = (ImageButton) myView.findViewById(R.id.btnRefreshCart);
        lvl = (ListView) myView.findViewById(R.id.listCartView);
        txtCartEmpty = (TextView) myView.findViewById(R.id.txtCartEmpty);


        if (totalCartItemCount == 0){
            txtItemText.setVisibility(myView.INVISIBLE);
            txtTotalAmount.setVisibility(myView.INVISIBLE);
            btnCheckOut.setVisibility(myView.INVISIBLE);
            lvl.setVisibility(myView.INVISIBLE);
            txtItemCount.setVisibility(myView.INVISIBLE);
            btnRefreshCart.setVisibility(myView.VISIBLE);
            txtCartEmpty.setVisibility(myView.VISIBLE);
        }else {
            txtItemText.setVisibility(myView.VISIBLE);
            txtTotalAmount.setVisibility(myView.VISIBLE);
            btnCheckOut.setVisibility(myView.VISIBLE);
            lvl.setVisibility(myView.VISIBLE);
            txtItemCount.setVisibility(myView.VISIBLE);
            btnRefreshCart.setVisibility(myView.VISIBLE);
            txtCartEmpty.setVisibility(myView.INVISIBLE);
        }

        txtItemCount.setText(""+totalCartItemCount);
        txtTotalAmount.setText("Ksh."+totalCartValue);

        txtItemText.setTypeface(typeface);
        txtCartEmpty.setTypeface(typeface);
        txtTotalAmount.setTypeface(typeface);
        btnCheckOut.setTypeface(typeface);

        lvl.setAdapter(new CustomListOne(this.getActivity(), cartList));
        btnCheckOut.setOnClickListener(new MyCheckOutClickListener("btnCheckOut"));
        btnRefreshCart.setOnClickListener(new MyPersonalClickListener("btnRefreshCart"));

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
            type = Typeface.createFromAsset(context.getAssets(), "fonts/regular_serif.ttf");

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
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ShopItem tmpItem =
                    cartList.get(position);

            if (convertView == null){
                convertView = layoutInflater.inflate(R.layout.listone_custom , null);

                viewHolder = new ViewHolder();

                viewHolder.btnRmFromCart = (Button) convertView.findViewById(R.id.imgBtnRmFromCart);
                viewHolder.spnQty = (Spinner) convertView.findViewById(R.id.spnQty);
                viewHolder.txtCartItemName = (TextView) convertView.findViewById(R.id.txtCartItemName);
                viewHolder.txtCartItemValue = (TextView) convertView.findViewById(R.id.txtCartItemValue);

                convertView.setTag(viewHolder);
            }else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.txtCartItemName.setText(tmpItem.getName());
            viewHolder.txtCartItemValue.setText("Ksh." + tmpItem.getTotalCartValue());

            viewHolder.txtCartItemName.setTypeface(type);
            viewHolder.txtCartItemValue.setTypeface(type);

            ArrayAdapter<String> aa = new ArrayAdapter<String>(context , R.layout.spinner_item , qtyValues);
            aa.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

            viewHolder.spnQty.setAdapter(aa);
            viewHolder.spnQty.setSelection(tmpItem.getCartQuantity() - 1);

            viewHolder.btnRmFromCart.setOnClickListener(new MyPersonalClickListener("button_delete", tmpItem));

            viewHolder.spnQty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                    /**
                     * If user changes the quantity, change it in the db and refresh cart list
                     */

                    if ((parent.getSelectedItemPosition() + 1) != tmpItem.getCartQuantity()) {

                        updateCart(parent.getSelectedItemPosition() + 1, tmpItem);

                        getCartData();

                        notifyDataSetChanged();

                        //Refresh Data outside the List View
                        View parentView = (View) view.getParent().getParent().getParent().getParent();
                        TextView txtItemCount = (TextView) parentView.findViewById(R.id.txtItemCount);
                        TextView txtTotalAmount = (TextView) parentView.findViewById(R.id.txtTotalAmount);

                        totalCartItemCount = cartList.size();
                        totalCartValue = new Double(0);

                        for (int i = 0; i < cartList.size(); i++) {
                            totalCartValue = (totalCartValue + cartList.get(i).getTotalCartValue());
                        }

                        txtItemCount.setText(""+totalCartItemCount);
                        txtTotalAmount.setText("Ksh." + totalCartValue);

                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            return convertView;
        }
    }

    static class ViewHolder{
        Button btnRmFromCart;
        Spinner spnQty;
        TextView txtCartItemName;
        TextView txtCartItemValue;
    }

    public class MyCheckOutClickListener implements View.OnClickListener{

        String buttonName;

        public MyCheckOutClickListener(String btnCheckOut) {
            this.buttonName = btnCheckOut;
        }

        @Override
        public void onClick(View v) {

            if(buttonName.equals("btnCheckOut")){

                final LayoutInflater inflater = getActivity().getLayoutInflater();
                final View view = inflater.inflate(R.layout.student_pin_layout, null);

                if (!receiver.hasNetworkConnection()){
                    Snackbar.make(view, "Connect to internet and try again.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }else {

                    final EditText pinText = (EditText) view.findViewById(R.id.inputPin);

                    builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("ENTER PIN");
                    builder.setView(view);
                    builder.setCancelable(false);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            String pin = pinText.getText().toString();

                            if (pin.length() == 4) {
                                checkout(pin);
                            } else {
                                Toast.makeText(getContext(), "Pin too short, please try again.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    builder.setNegativeButton("CANCEL", null);

                    dialog = builder.create();
                    dialog.show();
                }
            }
        }
    }

    private void checkout(String pin) {

        Map<String, String> checkoutParams = new HashMap<String, String>();

        getCartData();

        StudentData studentData = getStudentData();

        JSONArray itemsArray = new JSONArray(cartList);

        checkoutParams.put("pin" , pin);
        checkoutParams.put("cardNumber", studentData.getCardNumber());
        checkoutParams.put("items", itemsArray.toString());



        sweetAlertDialog = new SweetAlertDialog(getActivity() , SweetAlertDialog.PROGRESS_TYPE);
        sweetAlertDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        sweetAlertDialog.setTitleText("Please wait ...");
        sweetAlertDialog.setCancelable(true);
        sweetAlertDialog.show();

        Response.Listener<JSONObject> loginResponseListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    checkedOut = response.getBoolean("checkOut");

                    String serverResp = response.getString("checkOutResponse");

                    if (checkedOut){

                        sweetAlertDialog.dismiss();

                    }else {
                        sweetAlertDialog.dismiss();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        CheckoutRequest checkoutRequest = new CheckoutRequest(new JSONObject(checkoutParams) , loginResponseListener);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(checkoutRequest);

    }

    public class MyPersonalClickListener implements View.OnClickListener{
        String buttonName;
        ShopItem shopItem;
        int tmpQty;
        int tmpValue;

        public MyPersonalClickListener(String buttonName, ShopItem shopItem) {
            this.buttonName = buttonName;
            this.shopItem = shopItem;
        }

        public MyPersonalClickListener(String buttonName) {
            this.buttonName = buttonName;
        }

        @Override
        public void onClick(View v) {

            if (buttonName.equals("button_delete")){

                removeFromCart(shopItem);

                Toast.makeText(getActivity() , "REMOVED" , Toast.LENGTH_SHORT).show();

                getCartData();

                View lView = (View) v.getParent().getParent();

                ((ListView) lView).setAdapter(new CustomListOne(getActivity(), cartList));

                TextView txtItemText = (TextView) myView.findViewById(R.id.txtItemText);
                TextView txtItemCount = (TextView) myView.findViewById(R.id.txtItemCount);
                TextView txtTotalAmount = (TextView) myView.findViewById(R.id.txtTotalAmount);
                Button btnCheckOut = (Button) myView.findViewById(R.id.btnCheckOut);
                ImageButton btnRefreshCart = (ImageButton) myView.findViewById(R.id.btnRefreshCart);
                ListView lvl = (ListView) myView.findViewById(R.id.listCartView);
                TextView txtCartEmpty = (TextView) myView.findViewById(R.id.txtCartEmpty);

                totalCartItemCount = cartList.size();
                totalCartValue = new Double(0);

                for (int i = 0; i < cartList.size(); i++) {
                    totalCartValue = (totalCartValue + cartList.get(i).getTotalCartValue());
                }

                txtItemCount.setText(""+totalCartItemCount);
                txtTotalAmount.setText("Ksh."+totalCartValue);

                if (totalCartItemCount == 0){
                    txtItemText.setVisibility(myView.INVISIBLE);
                    txtTotalAmount.setVisibility(myView.INVISIBLE);
                    btnCheckOut.setVisibility(myView.INVISIBLE);
                    btnRefreshCart.setVisibility(myView.VISIBLE);
                    lvl.setVisibility(myView.INVISIBLE);
                    txtItemCount.setVisibility(myView.INVISIBLE);
                    txtCartEmpty.setVisibility(myView.VISIBLE);
                }else {
                    txtItemText.setVisibility(myView.VISIBLE);
                    txtTotalAmount.setVisibility(myView.VISIBLE);
                    btnCheckOut.setVisibility(myView.VISIBLE);
                    btnRefreshCart.setVisibility(myView.VISIBLE);
                    lvl.setVisibility(myView.VISIBLE);
                    txtItemCount.setVisibility(myView.VISIBLE);
                    txtCartEmpty.setVisibility(myView.INVISIBLE);
                }

                lvl.setAdapter(new CustomListOne(getActivity(), cartList));

            }else if (buttonName.equals("btnRefreshCart")){

                getCartData();

                Toast.makeText(getActivity() , " Refreshing .... " , Toast.LENGTH_LONG).show();

                TextView txtItemText = (TextView) myView.findViewById(R.id.txtItemText);
                TextView txtItemCount = (TextView) myView.findViewById(R.id.txtItemCount);
                TextView txtTotalAmount = (TextView) myView.findViewById(R.id.txtTotalAmount);
                Button btnCheckOut = (Button) myView.findViewById(R.id.btnCheckOut);
                ImageButton btnRefreshCart = (ImageButton) myView.findViewById(R.id.btnRefreshCart);
                ListView lvl = (ListView) myView.findViewById(R.id.listCartView);
                TextView txtCartEmpty = (TextView) myView.findViewById(R.id.txtCartEmpty);

                totalCartItemCount = cartList.size();
                totalCartValue = new Double(0);

                for (int i = 0; i < cartList.size(); i++) {
                    totalCartValue = (totalCartValue + cartList.get(i).getTotalCartValue());
                }

                txtItemCount.setText(""+totalCartItemCount);
                txtTotalAmount.setText("Ksh."+totalCartValue);

                if (totalCartItemCount == 0){
                    txtItemText.setVisibility(myView.INVISIBLE);
                    txtTotalAmount.setVisibility(myView.INVISIBLE);
                    btnCheckOut.setVisibility(myView.INVISIBLE);
                    btnRefreshCart.setVisibility(myView.VISIBLE);
                    lvl.setVisibility(myView.INVISIBLE);
                    txtItemCount.setVisibility(myView.INVISIBLE);
                    txtCartEmpty.setVisibility(myView.VISIBLE);
                }else {
                    txtItemText.setVisibility(myView.VISIBLE);
                    txtTotalAmount.setVisibility(myView.VISIBLE);
                    btnCheckOut.setVisibility(myView.VISIBLE);
                    btnRefreshCart.setVisibility(myView.VISIBLE);
                    lvl.setVisibility(myView.VISIBLE);
                    txtItemCount.setVisibility(myView.VISIBLE);
                    txtCartEmpty.setVisibility(myView.INVISIBLE);
                }

                lvl.setAdapter(new CustomListOne(getActivity(), cartList));
            }

        }
    }

    private void getCartData() {

        Log.e("LIST SIZE" , "+++ GETTING CART DATA +++");

        cartList.clear();
        cartList.addAll(databaseHelper.getCartItems());

        Log.e("LIST SIZE" , "+++ "+cartList.size());
    }

    private StudentData getStudentData() {
        return databaseHelper.getStudentData();
    }

    private void updateCart(int quantity , ShopItem item){
        Log.e("CART UPDATE", "+++ UPDATING CART DATA +++");

        int result = databaseHelper.updateCart(quantity , item);

        Log.e("CART UPDATE" , "+++ RESULT ==== "+result);
    }

    public void removeFromCart(ShopItem shopItem){
        Log.e("CART REMOVE", "+++ REMOVING +++");

        databaseHelper.removeFromCart(shopItem);

        Log.e("CART REMOVE" , "+++ RESULT ==== ");
    }
}
