package com.seizthat.seizethatparse;

import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.util.Config;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

import java.util.List;

/**
 * Created by Nathan on 7/20/2014.
 */
public class ConsumerLocalDealAdapter extends ParseQueryAdapter<ParseObject> {
    ParseGeoPoint currentUserLocation;
    Context context;

    public ConsumerLocalDealAdapter(final Context context){
        super(context, new ParseQueryAdapter.QueryFactory<ParseObject>(){
            public ParseQuery create() {
                ParseQuery query = ParseQuery.getQuery("Deal");
                //query.whereNear("location", getGPS(context));
                try {
                    query.find();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return query;
            }
        });
        this.context = context;
        currentUserLocation = getGPS(context);
    }

    @Override
    public View getItemView(ParseObject object, View v, ViewGroup parent){
        if(v == null){
            v = View.inflate(context, R.layout.deals_listing, null);
        } else {
            super.getItemView(object, v, parent);

            TextView dealTitle = (TextView) v.findViewById(R.id.dealListViewTitle);
            dealTitle.setText(object.getString("title"));

            TextView dealBusiness = (TextView) v.findViewById(R.id.businessName);
            dealBusiness.setText(((ParseUser) object.get("business")).toString());//.getString("title"));

            //TextView dealDistance = (TextView) v.findViewById(R.id.distance);
            //dealDistance.setText(String.valueOf(((ParseGeoPoint)((ParseUser) object.get("business")).get("location")).distanceInMilesTo(currentUserLocation)));

            TextView available = (TextView) v.findViewById(R.id.available);
            int quantity = object.getInt("quantity");
            int amountSeized = object.getInt("amountSeized");

            String percentAvailable;

            if(quantity>0){
                percentAvailable = String.valueOf((1 - amountSeized / quantity) * 100) + "% Left";
            } else {
                percentAvailable = "0% Left";
            }

            available.setText(percentAvailable);


        }
        return v;
    }

    public static ParseGeoPoint getGPS(Context context){
        LocationManager service = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean enabled = service
                .isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (!enabled) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            context.startActivity(intent);
        }

        LocationManager locationManager;
        Criteria criteria = new Criteria();
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        String provider = locationManager.getBestProvider(criteria, false);
        Location location = locationManager.getLastKnownLocation(provider);
        double lat = location.getLatitude();
        double lon = location.getLongitude();

        return new ParseGeoPoint(lat,lon);
    }
}
