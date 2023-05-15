package com.example.e_commerce.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
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
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.e_commerce.R;
import com.example.e_commerce.data.DataModel;
import com.example.e_commerce.data.MyAdapter;

import java.util.ArrayList;
import java.util.Objects;

public class ViewItem extends AppCompatActivity {
    ImageView backBtn_IVVI;
    private ImageView itemImageIVVI;
    private TextView itemNameTVVI;
    private TextView itemDescriptionTVVI;
    private TextView priceTVVI;
    private TextView noOfItemTVVI;
    Button cartButton,buy;
    public static ArrayList<DataModel> dataModels = new ArrayList<>();


    @SuppressLint({"SetTextI18n", "CheckResult"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_item);

        TextView dec = findViewById(R.id.decrementItemQty);
        TextView inc = findViewById(R.id.incrementItemQty);
        TextView nos = findViewById(R.id.noOfItemTVVI);

        dec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Integer.parseInt(nos.getText().toString())>1){
                    nos.setText(Integer.toString(Integer.parseInt(nos.getText().toString())-1));
                    Toast.makeText(ViewItem.this, "Item cannot be smaller than 1", Toast.LENGTH_SHORT).show();
                }


            }
        });
        inc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nos.setText(Integer.toString(Integer.parseInt(nos.getText().toString())+1));
            }
        });



        init();
        backBtn_IVVI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payusingupi("Name","airtel66.payu@icici","E-Commerce","1");
            }
        });

        String IMAGE2=getIntent().getStringExtra("IMAGE2");
        int IMAGE1=getIntent().getIntExtra("IMAGE1",0);
        String ITEM_NAME=getIntent().getStringExtra("ITEM_NAME");
        String ITEM_DESCRIPTION=getIntent().getStringExtra("ITEM_DESCRIPTION");
        double ITEM_PRICE=getIntent().getDoubleExtra("ITEM_PRICE",400.00);
        int ITEM_QTY=getIntent().getIntExtra("ITEM_QTY",1);

        DataModel data = new DataModel(IMAGE1,ITEM_NAME,ITEM_DESCRIPTION,ITEM_PRICE);

        cartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyAdapter.shoppingList.add(data);
                Toast.makeText(getApplicationContext(), "Item added to the cart", Toast.LENGTH_SHORT).show();
            }
        });

        if(Objects.equals(IMAGE2, "")){
            itemImageIVVI.setImageResource(IMAGE1);
        }else{
            Glide.with(ViewItem.this).load(IMAGE2).into(itemImageIVVI);
        }

        itemNameTVVI.setText(ITEM_NAME);
        itemDescriptionTVVI.setText(ITEM_DESCRIPTION);
        priceTVVI.setText("Rs: "+String.valueOf(ITEM_PRICE*ITEM_QTY));
        noOfItemTVVI.setText(String.valueOf(ITEM_QTY));


    }
    private void init(){
        backBtn_IVVI=findViewById(R.id.backBtn_IVVI);
        itemImageIVVI=findViewById(R.id.itemImageIVVI);
        itemNameTVVI=findViewById(R.id.itemNameTVVI);
        itemDescriptionTVVI=findViewById(R.id.itemDescriptionTVVI);
        priceTVVI=findViewById(R.id.priceTVVI);
        noOfItemTVVI=findViewById(R.id.noOfItemTVVI);
        cartButton=findViewById(R.id.cartButton);
        buy = findViewById(R.id.buy);



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
        if (isConnectionAvailable(ViewItem.this))
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
}