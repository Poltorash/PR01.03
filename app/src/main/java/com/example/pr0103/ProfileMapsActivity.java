package com.example.pr0103;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.FirebaseDatabase;

import static androidx.core.content.ContextCompat.startActivity;
import static com.example.pr0103.Transform.getRoundedMapBitmap;
import static com.example.pr0103.UserStaticInfo.ActiveUser;
import static com.example.pr0103.UserStaticInfo.POSITION;
import static com.example.pr0103.UserStaticInfo.POSITION_LATITUDE;
import static com.example.pr0103.UserStaticInfo.POSITION_LONGITUDE;
import static com.example.pr0103.UserStaticInfo.USERS_PROFILE_INFO;
import static com.example.pr0103.UserStaticInfo.photos;
import static com.example.pr0103.UserStaticInfo.profileId;

public class ProfileMapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    Location lastLocation;
    FirebaseDatabase database;
    private TextView LatitudeTextView, LongitudeTextView, NameTextView;

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_maps);
        new UserStaticInfo(this);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Init();
        NameTextView.setText(ActiveUser.getName());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        try{setMapLocationChange();}
        catch (Exception e) {}
    }

    LayoutInflater inflater;
    GridView PhotosGridView;

    public class PhotoGridAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return photos.size();
        }

        @Override
        public Bitmap getItem(int position) {
            return photos.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            convertView = inflater.inflate(R.layout.item_photo, parent, false);
            final ImageView imageView = convertView.findViewById(R.id.image);
            imageView.setImageBitmap(getItem(position));
            convertView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(ProfileMapsActivity.this, PhotoActivity.class);
                    intent.putExtra(POSITION, position);
                    startActivity(intent);
                }
            });
            return convertView;
        }
    }

    private void Init() {
        PhotosGridView = findViewById(R.id.PhotosGridView);
        inflater = LayoutInflater.from(this);
        PhotosGridView.setAdapter(new PhotoGridAdapter());
        LatitudeTextView = findViewById(R.id.LatitudeTextView);
        LongitudeTextView = findViewById(R.id.LongitudeTextView);
        NameTextView = findViewById(R.id.NameTextView);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(ProfileMapsActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 123);


        }
        else {
            setMapLocationChange();
        }
    }

    PolylineOptions rectOptions = new PolylineOptions();
    Polyline polygon;

    private void setMapLocationChange(){
        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                if(location!=null){
                    mMap.clear();
                    if(database==null)
                        database = FirebaseDatabase.getInstance();

                    String Lat = String.valueOf(location.getLatitude());
                    String Lon = String.valueOf(location.getLongitude());

                    database.getReference(USERS_PROFILE_INFO).child(profileId).child(POSITION_LATITUDE).setValue(Lat);
                    database.getReference(USERS_PROFILE_INFO).child(profileId).child(POSITION_LONGITUDE).setValue(Lon);
                    LatitudeTextView.setText(Lat);
                    LongitudeTextView.setText(Lon);

                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

                    if(lastLocation!=null){
                        if(polygon!=null)
                            polygon.remove();
                        rectOptions.add(latLng);
                        polygon = mMap.addPolyline(rectOptions);
                    }
                    else
                        rectOptions.add(latLng);
                    lastLocation = location;

                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.www);
                    mMap.addMarker(new MarkerOptions().position(latLng)
                            .icon(BitmapDescriptorFactory.fromBitmap(getRoundedMapBitmap(bitmap))));
                }
            }
        });
    }
}
