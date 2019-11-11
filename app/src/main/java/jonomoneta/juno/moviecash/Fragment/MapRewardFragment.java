package jonomoneta.juno.moviecash.Fragment;


import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Objects;

import jonomoneta.juno.moviecash.Adapter.LocationListAdapter;
import jonomoneta.juno.moviecash.Adapter.LocationTypeSpinnerAdapter;
import jonomoneta.juno.moviecash.CustomTextView;
import jonomoneta.juno.moviecash.CustomTextViewBold;
import jonomoneta.juno.moviecash.Model.Request.SaveLocationItemJson;
import jonomoneta.juno.moviecash.Model.Response.CommonResponse;
import jonomoneta.juno.moviecash.Model.Response.GetGoogleSearchLocationResponse;
import jonomoneta.juno.moviecash.Model.Response.GetSearchLocationsTypesResponse;
import jonomoneta.juno.moviecash.Model.Response.PlaceResponse;
import jonomoneta.juno.moviecash.PreferenceSettings;
import jonomoneta.juno.moviecash.R;
import jonomoneta.juno.moviecash.Retrofit.RetroInterface;
import jonomoneta.juno.moviecash.Retrofit.RetrofitAdapter;
import jonomoneta.juno.moviecash.Utils.Utility;
import jonomoneta.juno.moviecash.services.GPSTracker;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static jonomoneta.juno.moviecash.Utils.Utility.map_based_reward;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapRewardFragment extends Fragment {

    MapView mMapView;
    private GoogleMap mMap;
    public static double curLat, curLng;
    LatLng curLocation;
    GPSTracker gpsTracker;
    RecyclerView locationListRV;
    ArrayList<GetGoogleSearchLocationResponse.ResponseData> placeList;
    private LinearLayout slidingLL;
    BottomSheetBehavior bottomSheetBehavior;

    ImageView openBTN;
    ArrayList<Marker> markerList;
    AppCompatSpinner typesSPNR;
    ArrayList<GetSearchLocationsTypesResponse.ResponseData> typeList;
    ProgressDialog dialog;
    ArrayList<SaveLocationItemJson> placeListJSON;
    ArrayList<String> typesListJSON;
    int uid;
    LocationListAdapter locationListAdapter;

    public MapRewardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);

        uid = new PreferenceSettings(getActivity()).getUserDetails().getResponseData().getID();

        gpsTracker = new GPSTracker(getActivity());
        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Loading...");
        dialog.setCancelable(false);

        mMapView = rootView.findViewById(R.id.mapView);
        locationListRV = rootView.findViewById(R.id.locationListRV);
        slidingLL = rootView.findViewById(R.id.slidingLL);
        openBTN = rootView.findViewById(R.id.openBTN);
        typesSPNR = rootView.findViewById(R.id.typesSPNR);

        bottomSheetBehavior = BottomSheetBehavior.from(slidingLL);

        openBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });

        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {
                if (i == BottomSheetBehavior.STATE_COLLAPSED) {
                    openBTN.setImageResource(R.drawable.ic_up);
                } else if (i == BottomSheetBehavior.STATE_EXPANDED) {
                    openBTN.setImageResource(R.drawable.ic_down_white);
                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {
//                bg.setVisibility(View.VISIBLE);
//                bg.setAlpha(v);
            }
        });

        locationListRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        locationListRV.setItemAnimator(new DefaultItemAnimator());

        mMapView.onCreate(savedInstanceState);

        mMapView.onResume();

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;

                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setCompassEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
                mMap.getUiSettings().setMapToolbarEnabled(false);
                mMap.clear();


//                if (gpsTracker.getLatitude() == 0 || gpsTracker.getLongitude() == 0) {
//                    if (getMyLocation() != null) {
//                        curLocation = new LatLng(getMyLocation().getLatitude(), getMyLocation().getLongitude());
//                    }
//                } else {
//                    curLocation = new LatLng(gpsTracker.getLatitude(),
//                            gpsTracker.getLongitude());
//                }

                if (mMapView != null &&
                        mMapView.findViewById(Integer.parseInt("1")) != null) {
                    // Get the button view
                    View locationButton = ((View) mMapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
                    // and next place it, on bottom right (as Google Maps app)
                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)
                            locationButton.getLayoutParams();
                    // position on right bottom
                    layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
                    layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
                    layoutParams.setMargins(0, 0, 30, 30);
                }

                if (getMyLocation() != null) {
                    typesSPNR.setVisibility(View.VISIBLE);
                    curLocation = new LatLng(getMyLocation().getLatitude(), getMyLocation().getLongitude());
//                    curLocation = new LatLng(33.7613041 ,-84.3825804);

                    curLat = curLocation.latitude;
                    curLng = curLocation.longitude;
                    Log.e("current location", curLat + "-----" + curLng);

                    CameraPosition cameraPosition = new CameraPosition.Builder().target(curLocation).zoom(16).build();
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition
                            (cameraPosition));
                } else {
                    gpsTracker = new GPSTracker(getActivity());
                    if (gpsTracker.getLatitude() != 0 && gpsTracker.getLongitude() != 0) {
                        typesSPNR.setVisibility(View.VISIBLE);
                        curLocation = new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude());
//                        curLocation = new LatLng(33.7613041 ,-84.3825804);

                        curLat = curLocation.latitude;
                        curLng = curLocation.longitude;
                        Log.e("current location", curLat + "-----" + curLng);

                        CameraPosition cameraPosition = new CameraPosition.Builder().target(curLocation).zoom(16).build();
                        mMap.animateCamera(CameraUpdateFactory.newCameraPosition
                                (cameraPosition));
                    } else {
                        typesSPNR.setVisibility(View.GONE);
                        buildAlertMessageNoGps();
                    }
                }
                gettypeAPI();

            }
        });

        typesSPNR.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    mMap.clear();
                    slidingLL.setVisibility(View.GONE);
                    getLocationFromDB_API(String.valueOf(curLat), String.valueOf(curLng),
                            typeList.get(position).getValue(), true);
                } else {
                    mMap.clear();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return rootView;
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS),
                                999);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 999) {
            if (getMyLocation() != null) {
                typesSPNR.setVisibility(View.VISIBLE);
                curLocation = new LatLng(getMyLocation().getLatitude(), getMyLocation().getLongitude());

                curLat = curLocation.latitude;
                curLng = curLocation.longitude;
                Log.e("current location", curLat + "-----" + curLng);

                CameraPosition cameraPosition = new CameraPosition.Builder().target(curLocation).zoom(16).build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition
                        (cameraPosition));
            } else {
                gpsTracker = new GPSTracker(getActivity());
                if (gpsTracker.getLatitude() != 0 && gpsTracker.getLongitude() != 0) {
                    typesSPNR.setVisibility(View.VISIBLE);
                    curLocation = new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude());

                    curLat = curLocation.latitude;
                    curLng = curLocation.longitude;
                    Log.e("current location", curLat + "-----" + curLng);

                    CameraPosition cameraPosition = new CameraPosition.Builder().target(curLocation).zoom(16).build();
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition
                            (cameraPosition));
                } else {
                    typesSPNR.setVisibility(View.GONE);
                    buildAlertMessageNoGps();
                }
            }
        }
    }

    private Location getMyLocation() {
        // Get location from GPS if it's available
        Location myLocation = null;
        LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    getActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    Activity#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
                return null;
            }
        }
        myLocation = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        // Location wasn't found, check the next most accurate place for the current location
        if (myLocation == null) {
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_COARSE);
            // Finds a provider that matches the criteria
            String provider = lm.getBestProvider(criteria, true);
            // Use the provider to get the last known location
            myLocation = lm.getLastKnownLocation(provider);
        }


        return myLocation;
    }

    public void showPlaceDetailDialog(Marker marker) {
        final Dialog dialog;
        dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.custom_earn_dialog);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.flag_transparent));

        ImageView iconIV = dialog.findViewById(R.id.iconIV);
        final CustomTextViewBold nameTV = dialog.findViewById(R.id.nameTV),
                earnBTN = dialog.findViewById(R.id.earnBTN),
                okBTN = dialog.findViewById(R.id.okBTN);
        final CustomTextView distanceTV = dialog.findViewById(R.id.distanceTV),
                addressTV = dialog.findViewById(R.id.addressTV),
                remainTimeTV = dialog.findViewById(R.id.remainTimeTV);
        final LinearLayout mainLL = dialog.findViewById(R.id.mainLL),
                errorLL = dialog.findViewById(R.id.errorLL);

        for (int i = 0; i < markerList.size(); i++) {
            if (markerList.get(i).getPosition().latitude == marker.getPosition().latitude &&
                    markerList.get(i).getPosition().longitude == marker.getPosition().longitude) {
                if (placeList.get(i).getIcon() != null) {
                    Picasso.get().load(placeList.get(i).getIcon()).error(R.drawable.marker_large)
                            .into(iconIV);
                }
                nameTV.setText(placeList.get(i).getName());
                distanceTV.setText(placeList.get(i).getDistance() + " km away");
                addressTV.setText(placeList.get(i).getVicinity());
                if (placeList.get(i).getLastUsedInSecond() > 0) {
                    earnBTN.setText("Already Earned");
                    earnBTN.setEnabled(false);
                    earnBTN.setBackgroundResource(R.drawable.grey_bottom_corner);
                    remainTimeTV.setVisibility(View.VISIBLE);
                    int remainTime = 60 - placeList.get(i).getLastUsedInMinuite();
                    remainTimeTV.setText("(Come again after " + remainTime + " mins.)");
                }

                final int j = i;
                earnBTN.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (Utility.isOnline(getActivity())) {
                            saveRewardAPI(j, dialog, mainLL, errorLL);
                        } else {
                            Utility.noInternetError(getActivity());
                        }
                    }
                });

                break;
            }
        }

        okBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    public void saveRewardAPI(final int pos, final Dialog detailDialog,
                              final LinearLayout mainLL, final LinearLayout errorLL) {
        dialog.show();
        RetroInterface retroInterface = RetrofitAdapter.retrofit.create(RetroInterface.class);
        Call<CommonResponse> savereward = retroInterface.saveRewardLocation(uid, placeList.get(pos).getPlace_id(),
                map_based_reward);
        savereward.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                dialog.dismiss();
                if (response.body().getResponseCode().equalsIgnoreCase("SUCCESS")) {
                    detailDialog.dismiss();
                    placeList.get(pos).setLastUsedInSecond(1);
                    locationListAdapter.notifyDataSetChanged();


                    int height = 64;
                    int width = 64;
                    BitmapDrawable bitmapdraw;
                    bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.marker_large_disable);
                    Bitmap b = bitmapdraw.getBitmap();
                    Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
                    markerList.get(pos).setIcon(BitmapDescriptorFactory.fromBitmap(smallMarker));

                    Toast.makeText(getActivity(), "Congratulations! You have earned $ " + map_based_reward + " JM.", Toast.LENGTH_LONG).show();
                    Utility.getProfileDetails();
                } else {
                    if (response.body().getResponseID() == -1) {
                        mainLL.setVisibility(View.GONE);
                        errorLL.setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(getActivity(), response.body().getResponseMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void gettypeAPI() {
        dialog.show();

        RetroInterface retroInterface = RetrofitAdapter.retrofit.create(RetroInterface.class);
        Call<GetSearchLocationsTypesResponse> gettype = retroInterface.getSearchLocationsTypes();
        gettype.enqueue(new Callback<GetSearchLocationsTypesResponse>() {
            @Override
            public void onResponse(Call<GetSearchLocationsTypesResponse> call, Response<GetSearchLocationsTypesResponse> response) {
                dialog.dismiss();
                if (response.body().getResponseCode().equalsIgnoreCase("SUCCESS")) {
                    typeList = new ArrayList<>();
                    typeList = response.body().getResponseDataArrayList();
                    typeList.add(0, new GetSearchLocationsTypesResponse.ResponseData(0, "Select Type", ""));
                    LocationTypeSpinnerAdapter locationTypeSpinnerAdapter = new LocationTypeSpinnerAdapter(getActivity(),
                            typeList);
                    typesSPNR.setAdapter(locationTypeSpinnerAdapter);
                    locationTypeSpinnerAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getActivity(), response.body().getResponseMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetSearchLocationsTypesResponse> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getLocationFromDB_API(final String latitude, final String longitude, final String type,
                                       final boolean isFirstTime) {
        if (!dialog.isShowing()) {
            dialog.show();
        }
        RetroInterface retroInterface = RetrofitAdapter.retrofit.create(RetroInterface.class);
        Call<GetGoogleSearchLocationResponse> getplace = retroInterface.getGoogleSearchLocation(uid, type, latitude, longitude);
        getplace.enqueue(new Callback<GetGoogleSearchLocationResponse>() {
            @Override
            public void onResponse(Call<GetGoogleSearchLocationResponse> call, Response<GetGoogleSearchLocationResponse> response) {

                try {
                    if (response.body().getResponseCode().equalsIgnoreCase("SUCCESS")) {
                        placeList = new ArrayList<>();
                        markerList = new ArrayList<>();

                        if (response.body().getResponseData() != null &&
                                response.body().getResponseData().size() > 0) {

                            slidingLL.setVisibility(View.VISIBLE);

                            for (int i = 0; i < response.body().getResponseData().size(); i++) {
                                GetGoogleSearchLocationResponse.ResponseData data = response.body().getResponseData().get(i);


                                Location curLoc = new Location("Current");
                                curLoc.setLatitude(curLocation.latitude);
                                curLoc.setLongitude(curLocation.longitude);

                                Location placeLoc = new Location("Place");
                                placeLoc.setLatitude(Double.parseDouble(data.getLatitude()));
                                placeLoc.setLongitude(Double.parseDouble(data.getLongitude()));

                                double distance = (curLoc.distanceTo(placeLoc)) * 0.000621371;

                                data.setDistance(new DecimalFormat("##.##").format(distance * 1.60934));


                                int height = 64;
                                int width = 64;
                                BitmapDrawable bitmapdraw;
                                if (data.getLastUsedInSecond() > 0) {
                                    bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.marker_large_disable);
                                } else {
                                    bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.marker_large);
                                }
                                Bitmap b = bitmapdraw.getBitmap();
                                Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);

                                // Setting the position for the marker
                                MarkerOptions curMarkerOption = new MarkerOptions();
                                curMarkerOption.icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                                        .position(new LatLng(Double.parseDouble(data.getLatitude()),
                                                Double.parseDouble(data.getLongitude())));
                                Marker marker = mMap.addMarker(curMarkerOption);

                                markerList.add(marker);

                                placeList.add(data);
                            }
                            LatLngBounds.Builder builder = new LatLngBounds.Builder();
                            for (Marker marker : markerList) {
                                builder.include(marker.getPosition());
                            }
                            LatLngBounds bounds = builder.build();
                            int padding = 35; // offset from edges of the map in pixels
                            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                            mMap.animateCamera(cu);

                            locationListAdapter = new LocationListAdapter(getActivity(), placeList,
                                    markerList, mMap, MapRewardFragment.this);
                            locationListRV.setAdapter(locationListAdapter);
                            locationListAdapter.notifyDataSetChanged();
                            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                            openBTN.setImageResource(R.drawable.ic_down_white);

                            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                                @Override
                                public boolean onMarkerClick(Marker marker) {
                                    showPlaceDetailDialog(marker);
                                    return false;
                                }
                            });

                            if (placeList.size() <= 15) {
                                if (isFirstTime) {
                                    getNearByPlaceAPI(latitude, longitude, type, false);
                                } else {
                                    dialog.dismiss();
                                }
                            } else {
                                dialog.dismiss();
                            }

                        } else {
                            if (isFirstTime) {
                                getNearByPlaceAPI(latitude, longitude, type, true);
                            } else {
                                Toast.makeText(getActivity(), "No place found, Please try again after some times.", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        }
                    } else {
                        dialog.dismiss();
                        Toast.makeText(getActivity(), response.body().getResponseMessage(), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    Log.e("exc", e.getMessage());
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<GetGoogleSearchLocationResponse> call, Throwable t) {
                dialog.dismiss();
                Log.e("exc", t.getMessage());
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getNearByPlaceAPI(final String latitude, final String longitude, final String type, final boolean isEmpty) {

        if (!dialog.isShowing()) {
            dialog.show();
        }

        RetroInterface retroInterface = RetrofitAdapter.retrofitPlace.create(RetroInterface.class);
        Call<PlaceResponse> call = retroInterface.getPlaces(latitude + "," + longitude, "300",
                "", type, getResources().getString(R.string.place_api_key));
        call.enqueue(new Callback<PlaceResponse>() {
            @Override
            public void onResponse(Call<PlaceResponse> call, Response<PlaceResponse> response) {
                try {
                    placeListJSON = new ArrayList<>();
                    typesListJSON = new ArrayList<>();

                    if (response.body().getResultsArrayList() != null &&
                            response.body().getResultsArrayList().size() > 0) {

                        for (int i = 0; i < response.body().getResultsArrayList().size(); i++) {
                            PlaceResponse.results data = response.body().getResultsArrayList().get(i);

                            typesListJSON = data.getTypesArrayList();
                            Gson gson = new Gson();
                            String types = gson.toJson(typesListJSON);
                            placeListJSON.add(new SaveLocationItemJson(data.getPlace_id(), data.getName(),
                                    String.valueOf(data.getGeometry().getLocation().getLat()),
                                    String.valueOf(data.getGeometry().getLocation().getLng()),
                                    types, data.getVicinity(), data.getIcon()));
                        }

                        Gson gson = new Gson();
                        String placesJSON = gson.toJson(placeListJSON);
                        savePlaceToDatabaseAPI(type, placesJSON, latitude, longitude);

                    } else {
                        dialog.dismiss();
                        if (isEmpty) {
                            placeList.clear();
                            markerList.clear();
                            mMap.clear();
                            LocationListAdapter locationListAdapter = new LocationListAdapter(getActivity(), placeList,
                                    markerList, mMap, MapRewardFragment.this);
                            locationListRV.setAdapter(locationListAdapter);
                            locationListAdapter.notifyDataSetChanged();
                            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                            Toast.makeText(getActivity(), "No place found, Please try again after some times.", Toast.LENGTH_SHORT).show();
                        }
                    }

                } catch (Exception e) {
                    Log.e("exception", e.getMessage());
                    dialog.dismiss();
                }

            }

            @Override
            public void onFailure(Call<PlaceResponse> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void savePlaceToDatabaseAPI(final String type, String searchLocJSON, final String lat, final String lng) {
        if (!dialog.isShowing()) {
            dialog.show();
        }
        RetroInterface retroInterface = RetrofitAdapter.retrofit.create(RetroInterface.class);
        Call<CommonResponse> savePlaces = retroInterface.saveGoogleSearchLocation(type, searchLocJSON);
        savePlaces.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                if (response.body().getResponseCode().equalsIgnoreCase("SUCCESS")) {
                    getLocationFromDB_API(lat, lng, type, false);
                } else {
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                dialog.dismiss();
            }
        });
    }

//    private Location getMyLocation() {
//        // Get location from GPS if it's available
//        LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
//                    getActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                // TODO: Consider calling
//                //    Activity#requestPermissions
//                // here to request the missing permissions, and then overriding
//                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                //                                          int[] grantResults)
//                // to handle the case where the user grants the permission. See the documentation
//                // for Activity#requestPermissions for more details.
//                return null;
//            }
//        }
//        Location myLocation = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//
//        // Location wasn't found, check the next most accurate place for the current location
//        if (myLocation == null) {
//            Criteria criteria = new Criteria();
//            criteria.setAccuracy(Criteria.ACCURACY_COARSE);
//            // Finds a provider that matches the criteria
//            String provider = lm.getBestProvider(criteria, true);
//            // Use the provider to get the last known location
//            myLocation = lm.getLastKnownLocation(provider);
//        }
//
//        return myLocation;
//    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

}
