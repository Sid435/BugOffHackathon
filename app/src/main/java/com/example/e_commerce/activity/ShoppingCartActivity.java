package com.example.e_commerce.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.e_commerce.R;
import com.example.e_commerce.data.DataModel;
import com.example.e_commerce.data.MyAdapter;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ShoppingCartActivity extends AppCompatActivity {
    private RecyclerView recyclerViewShoppingCart;
    private LinearLayout totalAmountLL;
    private TextView noOfItemsInCartTV;
    private ImageView backBtnSCTIV;
    private Button Proceed;
    private TextView subTotal, bagTotal, shipping;
    public static ArrayList<DataModel> dataModels = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);
        init();
        backBtnSCTIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        MyAdapter myAdapter = new MyAdapter(this,MyAdapter.shoppingList,MyAdapter.SHOPPING_CART_ACT);
        if(MyAdapter.shoppingList.size()==0){
            totalAmountLL.setVisibility(View.VISIBLE);
        }
        String noOfItemsInCart=MyAdapter.shoppingList.size() + " items are in the cart";
        noOfItemsInCartTV.setText(noOfItemsInCart);
        recyclerViewShoppingCart.setAdapter(myAdapter);
        recyclerViewShoppingCart.setHasFixedSize(true);
        recyclerViewShoppingCart.setLayoutManager(new LinearLayoutManager(ShoppingCartActivity.this));

        long a = 0;
        for (int i = 0; i < MyAdapter.itemList.size(); i++) {
            a += MyAdapter.itemList.get(i).getSingleItemPrice()*MyAdapter.itemList.get(i).getNoOfItem();
        }
        subTotal.setText("Rs. " +String.valueOf(a));
        bagTotal.setText("Rs. " + String.valueOf(a + Integer.parseInt(shipping.getText().toString().split(" ")[1])));

        Proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MyAdapter.shoppingList.size()!=0){
                    payusingupi("Name","airtel66.payu@icici","E-Commerce","1");
                }
                else{
                    Toast.makeText(ShoppingCartActivity.this, "ADD SOME ITEMS", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
    private void init(){
        recyclerViewShoppingCart=findViewById(R.id.recyclerViewShoppingCart);
        totalAmountLL=findViewById(R.id.totalAmountLL);
        noOfItemsInCartTV=findViewById(R.id.noOfItemsInCartTV);
        backBtnSCTIV=findViewById(R.id.backBtnSCTIV);
        Proceed = findViewById(R.id.proceed);
        subTotal = findViewById(R.id.idTVSubTotalAmt);
        bagTotal = findViewById(R.id.idBagTotal);
        shipping = findViewById(R.id.idShipping);

    }
    void payusingupi(String name, String upiId, String note, String amount)
    {
        Log.e("main","name" + name + "--up--" + upiId + "--" + note + "--" + amount);
        Uri uri = Uri.parse("upi://pay").buildUpon()
                .appendQueryParameter("pa",upiId)
                .appendQueryParameter("pn",name)
                .appendQueryParameter("tn", note)
                .appendQueryParameter("am", amount)
//                .appendEncodedPath("cu","INR")
                .build();
        Intent upiPayIntent = new Intent(Intent.ACTION_VIEW);
        upiPayIntent.setData(uri);

        Intent chooser = Intent.createChooser(upiPayIntent,"Pay With");

        if (null!= chooser.resolveActivity(getPackageManager()))
        {
            startActivityForResult(chooser,0);
        }
        else
        {
            Toast.makeText(this, "No UPI App Installed", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("main","response " + resultCode);

        switch (requestCode)
        {
            case 0:
                if ((RESULT_OK == resultCode) || (resultCode == 11))
                {
                    if (data!=null)
                    {
                        String text = data.getStringExtra("response");
                        Log.e("UPI","onActivityResult: " + text);
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add(text);
                        upiPaymentDataOperation(dataList);
                    }
                    else
                    {
                        Log.e("UPI","onActivityResult: " + "Return Data Is null");
                        ArrayList<String> dataList= new ArrayList<>();
                        dataList.add("nothing");
                        upiPaymentDataOperation(dataList);
                    }
                }
                else
                {
                    Log.e("UPI","onActivityResult: " + "Return Data is null");
                    ArrayList<String> dataList = new ArrayList<>();
                    dataList.add("nothing");
                    upiPaymentDataOperation(dataList);
                }
                break;
        }
    }

    private void upiPaymentDataOperation(ArrayList<String> data)
    {
        if (isConnectionAvailable(ShoppingCartActivity.this))
        {
            String str = data.get(0);
            Log.d("UPIPAY","upiPaymentDataOperation: " + str);
            String paymentCancel = "";
            if (str==null)
            {
                str = "discard";
            }
            String status = "";
            String approvalRefNo = "";
            String response[] = str.split("&");
            for (int i=0;i<response.length;i++)
            {
                String equalStr[] = response[i].split("=");
                if (equalStr.length>=2)
                {
                    if (equalStr[0].toLowerCase().equals("Status".toLowerCase()))
                    {
                        status = equalStr[1].toLowerCase();
                    }
                    else if (equalStr[0].toLowerCase().equals("ApprovalRefNo".toLowerCase()) || equalStr[0].toLowerCase().equals("txnRef".toLowerCase()))
                    {
                        approvalRefNo = equalStr[1];
                    }
                }
                else
                {
                    paymentCancel = "Payment cancelled by user";
                }
            }
            if (status.equals("success"))
            {
                dataModels.addAll(MyAdapter.itemList);
                Toast.makeText(this, "Transaction Successful", Toast.LENGTH_SHORT).show();
                Log.d("UPI","responseStr: " + approvalRefNo);
            }
            else if ("Payment cancelled by user".equals(paymentCancel))
            {
                Toast.makeText(this, "Payment cancelled by user", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(this, "Transaction failed. Please try again", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            Toast.makeText(this, "Internet connection is not available. Please check and try again", Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean isConnectionAvailable(Context context)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager!=null)
        {
            NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
            if (netInfo!=null && netInfo.isConnected() && netInfo.isConnectedOrConnecting() && netInfo.isAvailable())
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}