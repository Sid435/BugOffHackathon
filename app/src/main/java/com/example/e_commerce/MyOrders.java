package com.example.e_commerce.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.e_commerce.R;
import com.example.e_commerce.data.MyAdapter;

public class MyOrders extends AppCompatActivity {
    private RecyclerView recyclerViewShoppingCart;
    private Button button;

    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);

        MyAdapter myAdapter = new MyAdapter(this,ShoppingCartActivity.dataModels,MyAdapter.SHOPPING_CART_ACT);
        button = findViewById(R.id.backButton);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MyOrders.this, HomeScreenActivity.class));
            }
        });


        recyclerViewShoppingCart = findViewById(R.id.recyclerViewActivity);

        recyclerViewShoppingCart.setAdapter(myAdapter);
        recyclerViewShoppingCart.setHasFixedSize(true);
        recyclerViewShoppingCart.setLayoutManager(new LinearLayoutManager(MyOrders.this));


    }
}