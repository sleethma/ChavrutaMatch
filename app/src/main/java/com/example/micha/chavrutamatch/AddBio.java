package com.example.micha.chavrutamatch;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import butterknife.BindView;

/**
 * Created by micha on 8/26/2017.
 */

public class AddBio extends AppCompatActivity {
//TODO: Add input validation using: https://www.androidhive.info/2015/09/android-material-design-floating-labels-for-edittext/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_bio);

        Intent intent = getIntent();
        String teaName = intent.getStringExtra(OrderActivity.EXTRA_TEA_NAME);
        int price = intent.getIntExtra(OrderActivity.EXTRA_TOTAL_PRICE, 0);
        String size = intent.getStringExtra(OrderActivity.EXTRA_SIZE);
        String milkType = intent.getStringExtra(OrderActivity.EXTRA_MILK_TYPE);
        String sugarType = intent.getStringExtra(OrderActivity.EXTRA_SUGAR_TYPE);
        int quantity = intent.getIntExtra(OrderActivity.EXTRA_QUANTITY, 0);

        displayOrderSummary(teaName, price, size, milkType, sugarType, quantity);
    }
    }

    public void skipBio(View view){
        Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
    }
}
