package com.seizthat.seizethatparse;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.seizthat.seizethatparse.R;

public class DealActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deal);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.deal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void publishDeal(View view){
        EditText dealTitle = (EditText) findViewById(R.id.dealTitle);
        EditText quantity = (EditText) findViewById(R.id.dealQuantity);

        ParseUser user = ParseUser.getCurrentUser();

        ParseObject deal = new ParseObject("Deal");
        deal.put("title", dealTitle.getText().toString());
        deal.put("quantity", (int) Double.parseDouble(quantity.getText().toString()));
        deal.put("amountSeized", 0);
        deal.put("business", user);
        deal.put("location", user.get("location"));
        deal.put("businessName", user.getString("title"));

        deal.saveInBackground();

        ParseRelation<ParseObject> relation = user.getRelation("deals");
        relation.add(deal);
        user.saveInBackground();

        finish();
    }
}
