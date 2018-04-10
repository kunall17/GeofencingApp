package com.example.sameer.geofencingapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class DealsActivity extends AppCompatActivity {

    TextView textView;
    ListView listView;

    ArrayList<Coupon> arrayList=new ArrayList<Coupon>();
    MyCustomAdapter myCustomAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deals);

        String id = getIntent().getStringExtra("ID");
        textView = findViewById(R.id.tvID);
        SpannableString content = new SpannableString("" + id);
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        textView.setText(content);

        listView = findViewById(R.id.listView);
        if (id.equalsIgnoreCase("Academic Block 5")) {
            arrayList.add(new Coupon("Coupon 1 ", "kunalRocks", "get 100% off"));
            arrayList.add(new Coupon("Coupon 2 ", "DISC50", "get 50% off"));
            arrayList.add(new Coupon("Coupon 3 ", "GETOFF", "get 10% off on order above 1.50 $"));
            arrayList.add(new Coupon("Coupon 4 ", "GOAWAY", "get 1$ cashback"));
            arrayList.add(new Coupon("Coupon 5 ", "GETGPA", "get extra cgpa"));
        } else if (id.equalsIgnoreCase("Mc Donald's")) {
            arrayList.add(new Coupon("Coupon 1 ", "kunalRocks", "get 100% off"));
            arrayList.add(new Coupon("Coupon 2 ", "DISC50", "get 50% off"));
            arrayList.add(new Coupon("Coupon 3 ", "GETOFF", "get 10% off on order above 1.50 $"));
            arrayList.add(new Coupon("Coupon 4 ", "GOAWAY", "get 1$ cashback"));
            arrayList.add(new Coupon("Coupon 5 ", "GETGPA", "get extra cgpa"));
        } else if (id.equalsIgnoreCase("Kentucky Fried Chicken")) {
            arrayList.add(new Coupon("Coupon 1 ", "kunalRocks", "get 100% off"));
            arrayList.add(new Coupon("Coupon 2 ", "DISC50", "get 50% off"));
            arrayList.add(new Coupon("Coupon 3 ", "GETOFF", "get 10% off on order above 1.50 $"));
            arrayList.add(new Coupon("Coupon 4 ", "GOAWAY", "get 1$ cashback"));
        }
        else {
            Toast.makeText(getApplicationContext(),"Wrong ID",Toast.LENGTH_LONG).show();
        }
        myCustomAdapter= new MyCustomAdapter(arrayList);
        listView.setAdapter(myCustomAdapter);
    }
     class MyCustomAdapter extends BaseAdapter {
        public  ArrayList<Coupon>  listnewsDataAdpater ;

        public MyCustomAdapter(ArrayList<Coupon>  listnewsDataAdpater) {
            this.listnewsDataAdpater=listnewsDataAdpater;
        }


        @Override
        public int getCount() {
            return listnewsDataAdpater.size();
        }

        @Override
        public String getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            LayoutInflater mInflater = getLayoutInflater();
            View myView = mInflater.inflate(R.layout.coupon_layout, null);

            final Coupon s = listnewsDataAdpater.get(position);

            TextView name = (TextView) myView.findViewById(R.id.tvName);
            name.setText(s.name);
            TextView coupon = (TextView) myView.findViewById(R.id.tvCoupon);
            coupon.setText(s.coupon);
            TextView description = (TextView) myView.findViewById(R.id.tvDescription);
            description.setText(s.description);

            return myView;

        }

    }
}
