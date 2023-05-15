package com.example.e_commerce.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.AnimationTypes;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.e_commerce.R;
import com.example.e_commerce.SupportActivity;
import com.example.e_commerce.data.DataModel;
import com.example.e_commerce.data.MyAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeScreenActivity extends AppCompatActivity {
    private RecyclerView recyclerViewActivity;


    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private SearchView searchViewHS;
    private ImageView homeScreenHamburgerIV;
    public static Activity activity;
    FirebaseFirestore db;
    ImageSlider ImageSlider;

    ArrayList<DataModel> itemList;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        init();


        itemList = new ArrayList<>();
        itemList.add(new DataModel(R.drawable.shoe_image,"Campus Shoe","Get your shoe",400.00));
        itemList.add(new DataModel(R.drawable.shoe_image,"Campus Shoe","Get your shoe",400.00));
        itemList.add(new DataModel(R.drawable.shoppingcart,"Shopping Cart","Get your cart",700.00));
        itemList.add(new DataModel(R.drawable.shoe_image,"Campus Shoe","Get your shoe",400.00));
        itemList.add(new DataModel(R.drawable.shoe_image,"Campus Shoe","Get your shoe",400.00));
        itemList.add(new DataModel(R.drawable.shoe_image,"Campus Shoe","Get your shoe",400.00));
        itemList.add(new DataModel(R.drawable.shoe_image,"Campus Shoe","Get your shoe",400.00));
        itemList.add(new DataModel(R.drawable.shoe_image,"Campus Shoe","Get your shoe",400.00));
        itemList.add(new DataModel(Uri.parse("https://www.bigbasket.com/media/uploads/p/l/20006339_1-maple-stainless-steel-e-kettle.jpg"),
                "Kettle","Stainless steel 1L",1000.00));
        itemList.add(new DataModel(Uri.parse("https://img.etimg.com/thumb/width-640,height-480,imgsize-66442,resizemode-1,msid-95250524/top-trending-products/electronics/tv-appliances/best-tv-in-india.jpg"),
                "Sony H1L","Best TV in India - Best LED, 4K HDR TVs In India",75000.00));
        insertFromFireStore();

        MyAdapter myAdapter = new MyAdapter(this,itemList);

        recyclerViewActivity.setAdapter(myAdapter);
        recyclerViewActivity.setHasFixedSize(true);
        recyclerViewActivity.setLayoutManager(new GridLayoutManager(this,2));

        View navigationHeader= navigationView.getHeaderView(0);
        TextView navHeaderName=navigationHeader.findViewById(R.id.navHeaderName);
        TextView navHeaderEmail=navigationHeader.findViewById(R.id.navHeaderEmail);

        navHeaderName.setText(getSharedPreferences("CREDENTIALS",MODE_PRIVATE).getString("NAME","Arijit Modak"));
        navHeaderEmail.setText(getSharedPreferences("CREDENTIALS",MODE_PRIVATE).getString("EMAIL","arijitmodak2003@gmail.com"));

        CircleImageView circleImageView2=navigationHeader.findViewById(R.id.circleImageView2);
        circleImageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(HomeScreenActivity.this,ProfileActivity.class);
                startActivity(intent);
            }
        });

        ImageSlider = findViewById(R.id.ImageSlider);

        List<SlideModel> SliderImages = new ArrayList<>();

        SlideModel s1 = new SlideModel("https://media.istockphoto.com/id/1176999930/vector/biggest-sale-banner-or-poster-design-with-40-80-discount-offer-and-geometric-elements.jpg?s=612x612&w=is&k=20&c=g7yTy3ztJthUl0P7c0oSKMWuAHa06NjqWlhA_czYhIM=", ScaleTypes.CENTER_INSIDE);
        SlideModel s2 = new SlideModel("https://d1csarkz8obe9u.cloudfront.net/posterpreviews/mega-fashion-sale-poster-%7C-festive-sale-design-template-5bb927bf61a7fc69477949f600d7c3a2_screen.jpg?ts=1606888511",ScaleTypes.CENTER_INSIDE);
        SlideModel s3 = new SlideModel(R.drawable.canva_blue_navy_summer_sale_promotional_poster_hg0gmsxegx8,ScaleTypes.CENTER_INSIDE);
        SlideModel s4 = new SlideModel("https://thumbs.dreamstime.com/b/special-day-shopping-day-sale-poster-flyer-design-last-month-year-online-sale-special-day-shopping-day-sale-poster-204764274.jpg",ScaleTypes.CENTER_INSIDE);


        SliderImages.add(s1);
        SliderImages.add(s2);
        SliderImages.add(s3);
        SliderImages.add(s4);

        ImageSlider.setImageList(SliderImages);
        ImageSlider.setSlideAnimation(AnimationTypes.DEPTH_SLIDE);
        ImageSlider.startSliding(2000);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.myOrders:{
                        startActivity(new Intent(HomeScreenActivity.this, com.example.e_commerce.activity.MyOrders.class));
                        break;
                    }

                    case R.id.shoppingCartMenu:{
                        startActivity(new Intent(HomeScreenActivity.this,ShoppingCartActivity.class));
                        break;
                    }
                    case R.id.support:{
                        startActivity(new Intent(HomeScreenActivity.this,SupportActivity.class));
                        break;
                    }
                }
                return true;
            }
        });



        searchViewHS.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<DataModel> filteredItemList=new ArrayList<>();
                for(int i=0;i<itemList.size();i++){
                    if(itemList.get(i).getItemName().toLowerCase().contains(newText.toLowerCase())){
                        filteredItemList.add(itemList.get(i));
                    }
                }
                MyAdapter filteredMyAdapter=new MyAdapter(HomeScreenActivity.this,filteredItemList);
                recyclerViewActivity.setAdapter(filteredMyAdapter);
                recyclerViewActivity.setHasFixedSize(true);
                recyclerViewActivity.setLayoutManager(new GridLayoutManager(HomeScreenActivity.this,2));
                return true;
            }
        });




    }
    private void init(){
        recyclerViewActivity=findViewById(R.id.recyclerViewActivity);

        drawerLayout=findViewById(R.id.drawerLayout);
        navigationView=findViewById(R.id.navigationView);
        searchViewHS=findViewById(R.id.searchViewHS);
        homeScreenHamburgerIV=findViewById(R.id.homeScreenHamburgerIV);
        activity=HomeScreenActivity.this;
        db=FirebaseFirestore.getInstance();

        homeScreenHamburgerIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(drawerLayout.isDrawerOpen(Gravity.LEFT)){
                    drawerLayout.close();
                }
                else{
                    drawerLayout.openDrawer(Gravity.LEFT);
                }
            }
        });
    }



    private void insertFromFireStore(){
        db.collection("products").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        //itemList.removeAll(itemList);
                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot d:list) {

                            Map<String, Object> map = d.getData();
                            assert map != null;
                            for (Map.Entry<String, Object> mapEntry : map.entrySet()) {
                                //for(Map<String,Object> mapEE:mapEntry.get){}
                                if(mapEntry.getValue() instanceof Map){
                                    Map<String, Object> nestedMap = (Map<String, Object>) mapEntry.getValue();
                                    String NAME="",IMAGE="",DESCRIPTION="";
                                    double PRICE=0.0;
                                    //String id=d.getId();
                                    for (Map.Entry<String, Object> nestedEntry : nestedMap.entrySet()) {
                                        if(Objects.equals(nestedEntry.getKey(), "NAME")){
                                            NAME = nestedEntry.getValue().toString();
                                            Log.d("XYZXX",nestedEntry.getValue().toString());
                                        }
                                        else if(Objects.equals(nestedEntry.getKey(), "IMAGE")){
                                            IMAGE = nestedEntry.getValue().toString();
                                        }
                                        else if(Objects.equals(nestedEntry.getKey(), "PRICE")){
                                            PRICE = Double.parseDouble(nestedEntry.getValue().toString());
                                        }
                                        else if(Objects.equals(nestedEntry.getKey(), "DESCRIPTION")){
                                            DESCRIPTION=nestedEntry.getValue().toString();
                                        }

                                    }
                                    itemList.add(new DataModel(Uri.parse(IMAGE),NAME,DESCRIPTION,PRICE));
                                }
                            }
                        }
                        Log.d("my_data", itemList.toString());
                        //startActivity(new Intent(MainActivity.this, QRScanner.class));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(HomeScreenActivity.this, "Not able to load the items", Toast.LENGTH_SHORT).show();
                    }
                });
    }



}