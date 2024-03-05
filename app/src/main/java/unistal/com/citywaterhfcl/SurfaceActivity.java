package unistal.com.citywaterhfcl;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.net.ConnectivityManager;
import android.provider.MediaStore;
/*import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;*/
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.location.config.LocationAccuracy;
import io.nlopez.smartlocation.location.config.LocationParams;

public class SurfaceActivity extends AppCompatActivity implements NetworkStateReceiver.NetworkStateReceiverListener{
    @BindView(R.id.spin_surface)
    Spinner spinnerSurface;
    @BindView(R.id.spin_restoration)
    Spinner spinnerRestoration;
    @BindView(R.id.btnEndNode)
    Button btnEndNode;

    @BindView(R.id.btnStartNode)
    Button btnStartNode;
    @BindView(R.id.edit_start_latitude)
    EditText editStartLatitude;

    @BindView(R.id.edit_start_longitude)
    EditText editStartLongitude;


    @BindView(R.id.edit_end_latitude)
    EditText editEndLatitude;

    @BindView(R.id.edit_end_Longitude)
    EditText editEndLongitude;
    @BindView(R.id.edit_end_width)
    EditText editEndWidth;

    @BindView(R.id.edit_end_depth)
    EditText editEndDepth;
    @BindView(R.id.edit_start_width)
    EditText editStartWidth;

    @BindView(R.id.edit_start_depth)
    EditText editStartDepth;
    @BindView(R.id.edit_remarks)
    EditText editRemarks;

    @BindView(R.id.titletv)
    TextView citytv;
    @BindView(R.id.netstatus)
    ImageView netStatus;

    @BindView(R.id.accuracytv)
    TextView acctv;
    @BindView(R.id.imgView)
    ImageView imgView;
    private NetworkStateReceiver networkStateReceiver;
    ProgressLoading progressLoading;
    Context context;
    String username ="basicAuth";
    String password = "ranchi@2021";
    String credentials =new String(username + ":" + password);
    String strAccuracy;
    private String strLantitude, strLongitude, strAltitude,dma_id="1";
    private Intent serviceIntent;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;

    ArrayList restorationList,  restorationIDList,surfaceList, surfaceIDList;
    ArrayAdapter surfaceAdapter,restorationAdapter;
    String surfaceID,restorationID,image;
    Boolean flag = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surface);
        ButterKnife.bind(this);
        getSupportActionBar().hide();
        citytv.setText("Surface");

        context = this;
        progressLoading = new ProgressLoading(this);
        init();
    }
    private  void init(){
        netConnectivity();
        surfaceIDList = new ArrayList();
        surfaceList = new ArrayList();
        restorationList = new ArrayList();
        restorationIDList = new ArrayList();
        sheetSurface();
        statusRestoration();
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(SurfaceActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            //  gpsTracker.getLocation();
            startLocationListener();
        }
        btnStartNode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // gpsTracker.getLocation();
                // if(Integer.parseInt(strAccuracy)<5) {

                editStartLongitude.setText(strLongitude + "");
                editStartLatitude.setText(strLantitude + "");
               /* }
                else{
                    Toast.makeText(context,"Wait for Accuracy", Toast.LENGTH_SHORT);
                }*/
            }
        });

        btnEndNode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // gpsTracker.getLocation();
                // if(Integer.parseInt(strAccuracy)<5) {

                editEndLongitude.setText(strLongitude + "");
                editEndLatitude.setText(strLantitude + "");
               /* }
                else{
                    Toast.makeText(context,"Wait for Accuracy", Toast.LENGTH_SHORT);
                }*/
            }
        });

    }


    public void sheetSurface() {
        if (DialogUtil.checkInternetConnection(this)) {
            // loadingDialog.onShow();
            if (!progressLoading.isShowing()) {
                progressLoading.onShow();
            }

            RequestQueue queue = Volley.newRequestQueue(this);

            final String url = AppConstants.APP_BASE_URL;
            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("Response", response);
                            surfaceList.clear();
                            surfaceIDList.clear();
                            try {
                                JSONArray arr = new JSONArray(response);
                                if (arr != null) {
                                    for (int i = 0; i < arr.length(); i++) {

                                        surfaceList.add(arr.getJSONObject(i).getString("name"));
                                        surfaceIDList.add(arr.getJSONObject(i).getString("id"));

                                        // AlignmentIDList.add(arr.getJSONObject(i).getString("AlignmentID"));
                                    }
                                }
                                surfaceAdapter = new ArrayAdapter(context, R.layout.layoutspinner, surfaceList);


                                spinnerSurface.setAdapter(surfaceAdapter);


                                //productModelList.addAll(pipeNo);
                                surfaceAdapter.notifyDataSetChanged();


                            } catch (JSONException ignored) {

                            }
                            //loadingDialog.dismiss();

                            progressLoading.dismiss();
                            // productModelList.addAll(pipeNo);
                        }
                    }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.getMessage();
                    progressLoading.dismiss();
                    Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();

                    String auth = "Basic "
                            + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                    headers.put("Authorization", auth);
                    return headers;
                }
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("type", "surface_type");

                    params.put("user_id", SessionUtil.getUserId(context));

                    //type: 'get_zone', for:'laying', user_id: 30
                    return params;
                }
            };

            // Add the request to the RequestQueue.
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            queue.add(stringRequest);
        } else {
            DialogUtil.showNoConnectionDialog(this);
        }


        spinnerSurface.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.e(" Selected Item position", "position " + i);
                //  bendID = bendModelList.get(i).getId();

                surfaceID = surfaceIDList.get(i).toString();
                // sheetStartNode();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    public void statusRestoration() {
        if (DialogUtil.checkInternetConnection(this)) {
            // loadingDialog.onShow();
            if (!progressLoading.isShowing()) {
                progressLoading.onShow();
            }

            RequestQueue queue = Volley.newRequestQueue(this);

            final String url = AppConstants.APP_BASE_URL;
            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("Response", response);
                            restorationList.clear();
                            restorationIDList.clear();
                            try {
                                JSONArray arr = new JSONArray(response);
                                if (arr != null) {
                                    for (int i = 0; i < arr.length(); i++) {

                                        restorationList.add(arr.getJSONObject(i).getString("name"));
                                        restorationIDList.add(arr.getJSONObject(i).getString("id"));

                                        // AlignmentIDList.add(arr.getJSONObject(i).getString("AlignmentID"));
                                    }
                                }
                                restorationAdapter = new ArrayAdapter(context, R.layout.layoutspinner, restorationList);


                                spinnerRestoration.setAdapter(restorationAdapter);


                                //productModelList.addAll(pipeNo);
                                restorationAdapter.notifyDataSetChanged();


                            } catch (JSONException ignored) {

                            }
                            //loadingDialog.dismiss();

                            progressLoading.dismiss();
                            // productModelList.addAll(pipeNo);
                        }
                    }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.getMessage();
                    progressLoading.dismiss();
                    Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();

                    String auth = "Basic "
                            + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                    headers.put("Authorization", auth);
                    return headers;
                }
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("type", "restoration_status");

                    params.put("user_id", SessionUtil.getUserId(context));

                    //type: 'get_zone', for:'laying', user_id: 30
                    return params;
                }
            };

            // Add the request to the RequestQueue.
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            queue.add(stringRequest);
        } else {
            DialogUtil.showNoConnectionDialog(this);
        }


        spinnerRestoration.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.e(" Selected Item position", "position " + i);
                //  bendID = bendModelList.get(i).getId();

                restorationID = restorationIDList.get(i).toString();
                // sheetStartNode();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    @OnClick(R.id.imgView)
    public void captureStartImage() {
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(SurfaceActivity.this,
                    new String[]{Manifest.permission.CAMERA},
                    100);

        } else {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            //  fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

            //  intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

            // start the image capture Intent
            startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
        }


    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if the result is capturing Image
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                imgView.setImageBitmap(photo);
                image = getStringImage(photo);
                // successfully captured the image
                // launching upload activity
                //launchUploadActivity(true);


            } else if (resultCode == RESULT_CANCELED) {

                // user cancelled Image capture
                Toast.makeText(context,
                        "User cancelled image capture", Toast.LENGTH_SHORT)
                        .show();

            } else {
                // failed to capture image
                Toast.makeText(context,
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
            }

        }
    }
    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    @OnClick(R.id.btnSubmit)
    public void sheetSubmit() {
        if(validate())
        {
            if (DialogUtil.checkInternetConnection(this)) {
                // loadingDialog.onShow();

                if (flag) {
                    flag = false;
                    if (!progressLoading.isShowing()) {
                        progressLoading.onShow();
                    }
                    RequestQueue queue = Volley.newRequestQueue(this);

                    final String url = AppConstants.APP_BASE_URL;
                    // Request a string response from the provided URL.
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                            new com.android.volley.Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Log.e("Response", response);
                                    progressLoading.dismiss();
                                    //  Toast.makeText(context, response, Toast.LENGTH_LONG).show();
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        String status=         jsonObject.getString("status");
                                        String message=         jsonObject.getString("message");

                                        if (message.contains("Success")) {
                                            //Toast.makeText(context, "Successfully submitted", Toast.LENGTH_LONG).show();
                                            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                                            editStartWidth.setText("");
                                            editStartDepth.setText("");
                                            editEndWidth.setText("");
                                            editEndDepth.setText("");

                                            editStartLatitude.setText("");
                                            editStartLongitude.setText("");
                                            editEndLatitude.setText("");
                                            editEndLongitude.setText("");
                                            editRemarks.setText("");

                                            //  spinnerStartNode.setText("");
                                            //  spinnerEndNode.setText("");
                                            // startNodeID=""; endNodeID="";
                                            image = "";


                                            imgView.setImageResource(android.R.color.transparent);

                                        }else{
                                            Toast.makeText(context," status: "+status+" message: "+message,Toast.LENGTH_SHORT ).show();
                                        }



                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    flag = true;
                                    // productModelList.addAll(pipeNo);
                                }
                            }, new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.getMessage();
                            flag = true;
                            progressLoading.dismiss();
                            Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }) {
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            Map<String, String> headers = new HashMap<>();

                            String username ="basicAuth";
                            String password = "ranchi@2021";
                            String credentials =new String(username + ":" + password);
                            String auth = "Basic "
                                    + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                            headers.put("Authorization", auth);
                            return headers;
                        }
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("type", "save_surface_details");

                            params.put("user_id", SessionUtil.getUserId(context));
                            params.put("latitude_start", editStartLatitude.getText().toString().trim()+ "");

                            params.put("longitude_start", editStartLongitude.getText().toString().trim()+ "");
                            params.put("latitude_end", editEndLatitude.getText().toString().trim() + "");
                            params.put("longitude_end", editEndLongitude.getText().toString().trim()+ "");
                            params.put("depth_trench_start", editStartDepth.getText().toString().trim() + "");

                            params.put("width_trench_start", editStartWidth.getText().toString() + "");
                            params.put("depth_trench_end", editEndDepth.getText().toString().trim() + "");

                            params.put("width_trench_end", editEndWidth.getText().toString() + "");

                            params.put("restoration_status", restorationID + "");
                            params.put("surface_type", surfaceID + "");

                          params.put("surface_photo", image+ "");
                            params.put("surface_remarks", editRemarks.getText().toString()+ "");


                            Log.e("Pppppppppppppppppparams", params.toString());
                            return params;
                        }
                    };

                    // Add the request to the RequestQueue.
                    stringRequest.setRetryPolicy(new DefaultRetryPolicy(600000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                    queue.add(stringRequest);
                }
            } else {
                DialogUtil.showNoConnectionDialog(this);
            }}


    }

    public boolean validate(){
        boolean valid = true;

        return valid;
    }






    @Override
    protected void onStart() {
        super.onStart();
        startLocationListener();
    }


    @Override
    protected void onStop() {
        super.onStop();
        SmartLocation.with(context).location().stop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startLocationListener();
        serviceIntent = new Intent(context, LocationService.class);
        startService(serviceIntent);
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter("app.location.via.service"));
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopService(serviceIntent);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
    }

    private final BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            // Get extra data included in the Intent
            // strLongitude = intent.getStringExtra("extra_latitude");
            // strLantitude = intent.getStringExtra("extra_longitude");
            // strAccuracy = intent.getStringExtra("accuracy");
            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "\nAccuracy = " + strAccuracy, Snackbar.LENGTH_INDEFINITE);
            snackbar.setActionTextColor(Color.RED);

            View snackbarView = snackbar.getView();
            snackbarView.setMinimumHeight(200);
            snackbarView.setBackgroundColor(Color.TRANSPARENT);
           // TextView textView = snackbarView.findViewById(android.support.design.R.id.snackbar_text);
           // textView.setTextColor(Color.BLUE);

            /// snackbar.show();

            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) snackbarView.getLayoutParams();
            params.gravity = Gravity.TOP;
            snackbarView.setLayoutParams(params);
            // snackbar.show();
        }
    };


    private void startLocationListener() {

        long mLocTrackingInterval = 1000 * 1*15; // 5 sec
        float trackingDistance = 0;

        // LocationAccuracy trackingAccuracy = LocationAccuracy.HIGH;
        LocationAccuracy trackingAccuracy = LocationAccuracy.HIGH;

        LocationParams.Builder builder = new LocationParams.Builder()
                .setAccuracy(trackingAccuracy)
                .setDistance(trackingDistance)
                .setInterval(mLocTrackingInterval);

        SmartLocation.with(this)
                .location()
                .continuous()
                .config(builder.build())
                .start(new OnLocationUpdatedListener() {
                    @Override
                    public void onLocationUpdated(Location location) {
                        // textView.setText("Accuracy(Estimated Position Error): "+location.getAccuracy()+"\n\nLat: "+location.getLatitude()+"\n\nLong: "+location.getLongitude());
                        strLongitude = location.getLongitude() + "";
                        strLantitude = location.getLatitude() + "";
                        strAccuracy = location.getAccuracy() + "";
                        strAltitude = location.getAltitude() + "";
                        acctv.setText("Accuracy = " + strAccuracy);
                    }
                });
    }


    public void netConnectivity()
    {
        networkStateReceiver = new NetworkStateReceiver();
        networkStateReceiver.addListener(this);
        this.registerReceiver(networkStateReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

    }

    public void onDestroy() {
        super.onDestroy();
        networkStateReceiver.removeListener(this);
    }

    @Override
    public void networkAvailable() {
        netStatus.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.icons8_cloud_50));
    }

    @Override
    public void networkUnavailable() {
        netStatus.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.icons8_cloud_cross_50));

    }

    @Override
    public void networkWifiAvailable() {

    }

    @Override
    public void networkWifiUnavailable() {

    }
}