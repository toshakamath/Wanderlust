package com.example.wanderlust.ui.maps;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.wanderlust.Doa.BlogObject;
import com.example.wanderlust.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MapsFragment extends Fragment implements OnMapReadyCallback {

    private static final String TAG = "MapFragment";

    private MapsViewModel mapsViewModel;
    private MapView mapView;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private FirebaseFirestore db;
    private static final float DEFAULT_ZOOM = 11f;
//    private Marker marker;

    private ArrayList<BlogObject> allBlogs;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mapsViewModel = ViewModelProviders.of(this).get(MapsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_maps, container, false);
        mapView = (MapView) root.findViewById(R.id.mapDisplay);

        db = FirebaseFirestore.getInstance();

        allBlogs = new ArrayList<>();

        initMap();

        return root;
    }

    private void initMap() {
        mapView.onCreate(null);
        mapView.onResume();
        mapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.i(TAG, "onMapReady : called");

        mMap = googleMap;

        if(mMap != null) {
            mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                @Override
                public View getInfoWindow(Marker marker) {
                    return null;
                }

                @Override
                public View getInfoContents(Marker marker) {
                    View view = getLayoutInflater().inflate(R.layout.custom_info_window, null);

                    TextView blogTitleWindow = (TextView) view.findViewById(R.id.blogTitleWindow);
                    TextView blogLocationWindow = (TextView) view.findViewById(R.id.blogLocationWindow);
                    ImageView imgWindow = (ImageView) view.findViewById(R.id.imgWindow);

                    blogTitleWindow.setText(marker.getTitle());

                    String[] splitArr = marker.getSnippet().split(",");
                    blogLocationWindow.setText(splitArr[0]);
                    Log.i(TAG, "splitArr[1] ----> " + splitArr[1]);
                    if(splitArr[1] != "currentLocation"){
                        Picasso.with(getContext()).load(splitArr[1]).into(imgWindow);
                    } else{
                        Picasso.with(getContext()).load(R.drawable.baseline_my_location_black_18dp).into(imgWindow);
                    }

                    return view;
                }
            });
        }

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        MapsInitializer.initialize(getContext());

        getCurrentUserLocation();
    }

    private void getCurrentUserLocation() {
        Log.i(TAG, "getCurrentUserLocation : called");

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        try{
            final Task location = mFusedLocationProviderClient.getLastLocation();
            location.addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if(task.isSuccessful()){
                        Log.i(TAG, "onComplete: called. Found location");
                        Location currentLocation = (Location) task.getResult();

                        mMap.clear();
                        mMap.getUiSettings().setZoomControlsEnabled(true);
                        mMap.setMyLocationEnabled(true);
                        LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                        mMap.addMarker(new MarkerOptions().position(latLng).title("Current User Location").snippet("Your Location" + "," + "currentLocation"));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM));
                        searchNearbyBlogs(latLng.latitude, latLng.longitude);
                    }
                    else {
                        Log.i(TAG, "onComplete: called. Current location is null");
                    }
                }
            });

        } catch (Exception e){
            Log.e(TAG, "getDeviceLocation: Exception: " + e.getMessage() );
        }
    }

    public ArrayList<BlogObject> shortListLocations(double latitude, double longitude, ArrayList<BlogObject> blogList) {
        Location targetLocation = new Location("");
        targetLocation.setLatitude(latitude);
        targetLocation.setLongitude(longitude);
        ArrayList<BlogObject> nearbyBlogs = new ArrayList<>();
        for(int i = 0; i < blogList.size(); i++) {
            double lat = blogList.get(i).getBlogLat();
            double lng = blogList.get(i).getBlogLong();
            float[] results = new float[1];
            Location.distanceBetween(latitude, longitude, lat, lng, results);
            float result = 0;
            if(results != null){
                result = results[0];
            }
            double distance = (double)result;
//            Log.i(TAG, "distance: " + String.valueOf(distance));
            if(distance < 16093.4) {
                nearbyBlogs.add(blogList.get(i));
            }
        }
        Log.i(TAG, "nearbyBlogs.size() ----> " + nearbyBlogs.size());
        return nearbyBlogs;
    }

    private void displayBlogsOnMap(ArrayList<BlogObject> nearestBlogs) {
        for(int i = 0; i < nearestBlogs.size(); i++) {
            Log.i(TAG, nearestBlogs.get(i).getBlogTitle());
            BlogObject blog = nearestBlogs.get(i);
            MarkerOptions options=new MarkerOptions().position(new LatLng(blog.getBlogLat(), blog.getBlogLong()));
            options.title(blog.getBlogTitle());
            options.icon(BitmapDescriptorFactory.fromResource(R.drawable.dig10k_penguin));
            options.snippet(blog.getBlogLocation() + "," + blog.getblogPicUrl());
            mMap.addMarker(options);
        }
    }

    private void searchNearbyBlogs(final double latitude, final double longitude) {
        db.collection("blog_table").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    long blogLikes = (long) documentSnapshot.get("blogLikes");
                    BlogObject blog = new BlogObject((String) documentSnapshot.get("blogId"), (String) documentSnapshot.get("userId"), (String) documentSnapshot.get("blogTitle"),
                            (String)documentSnapshot.get("blogLocation"), (String)documentSnapshot.get("blogText"), (Double) documentSnapshot.get("blogLat"),
                            (Double) documentSnapshot.get("blogLong"), (int) blogLikes, (ArrayList<String>) documentSnapshot.get("blogReviews"),
                            (String) documentSnapshot.get("blogPicUrl"));

                    allBlogs.add(blog);
                }
                ArrayList<BlogObject> nearestBlogs = shortListLocations(latitude, longitude, allBlogs);
                displayBlogsOnMap(nearestBlogs);
            }
        }). addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i(TAG, "Could not get data from database");
                Toast.makeText(getContext(), "No near by blogs", Toast.LENGTH_LONG).show();
            }
        });
    }

//    @Override
//    public void onStop() {
//        super.onStop();
//
//        marker.remove();
//    }
}