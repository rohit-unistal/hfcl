package unistal.com.citywaterhfcl;

import android.Manifest;
import android.app.DatePickerDialog;
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
/*
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
*/
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
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

import java.io.ByteArrayOutputStream;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.location.config.LocationAccuracy;
import io.nlopez.smartlocation.location.config.LocationParams;

public class StartGapActivity extends AppCompatActivity implements NetworkStateReceiver.NetworkStateReceiverListener{
    @BindView(R.id.titletv)
    TextView citytv;

    @BindView(R.id.accuracytv)
    TextView acctv;
    @BindView(R.id.spin_zone)
    Spinner spinnerZone;
    @BindView(R.id.spin_pipe_segment)
    Spinner spinpipesegment;
    @BindView(R.id.spin_reason)
    Spinner spinReason;
    @BindView(R.id.edit_start_node)
    EditText editStartNode;

    @BindView(R.id.edit_end_node)
    EditText editEndNode;
    /*@BindView(R.id.edit_pipe_segment)
    EditText editpipesegment;*/


    @BindView(R.id.btnStartNode)
    Button btnStartNode;
    @BindView(R.id.btnEndNode)
    Button btnEndNode;


    @BindView(R.id.edit_start_latitude)
    EditText editStartLatitude;

    @BindView(R.id.edit_start_longitude)
    EditText editStartLongitude;


    @BindView(R.id.edit_end_latitude)
    EditText editEndLatitude;

    @BindView(R.id.editendlongitude)
    EditText editEndLongitude;


    @BindView(R.id.edit_length)
    EditText editLength;

    @BindView(R.id.edit_date)
    EditText editDate;
    @BindView(R.id.edit_remarks)
    EditText editRemarks;
    @BindView(R.id.imgView)
    ImageView imageViewPhoto;
    @BindView(R.id.netstatus)
    ImageView netStatus;
    private NetworkStateReceiver networkStateReceiver;




    Context context;
    ProgressLoading progressLoading;

    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private Intent serviceIntent;
    ArrayList segmentList, zoneList,  zoneIDList,pipeList, pipeIDList;
    ArrayList<ItemModel> segmentModelList;
    Boolean flag = true;
    ArrayAdapter zoneAdapter,pipeAdapter,reasonAdapter;
    String zoneName,zone_id,pipeNumber,pipeID;
    String strAccuracy,imagePhoto;
    private String strLantitude, strLongitude;
    String reasonArray[] = {"obstruction","fitting","crossing","other"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_gap);
        ButterKnife.bind(this);
        getSupportActionBar().hide();
        citytv.setText("Create Gap");

        context = this;
        progressLoading = new ProgressLoading(this);
        init();

    }
    public void init()
    {
        netConnectivity();
        segmentList = new ArrayList();
        segmentModelList = new ArrayList<>();
        zoneList = new ArrayList(); zoneIDList = new ArrayList();
        pipeIDList = new ArrayList();pipeList = new ArrayList();
        sheetZone();
        reasonAdapter = new ArrayAdapter(context, R.layout.layoutspinner, reasonArray);


        spinReason.setAdapter(reasonAdapter);


        //productModelList.addAll(pipeNo);

        editDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRouteSurveyDate();
            }
        });
        acctv.setText("Accuracy = " + strAccuracy);
        getCurrentDate();
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(StartGapActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            //  gpsTracker.getLocation();
            startLocationListener();
        }
        btnStartNode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // gpsTracker.getLocation();
              //   if(Integer.parseInt(strAccuracy)<2) {

                editStartLongitude.setText(strLongitude + "");
                editStartLatitude.setText(strLantitude + "");
                /*}
                else{
                    Toast.makeText(context,"Wait for Accuracy", Toast.LENGTH_SHORT);
                }*/
            }
        });

        btnEndNode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // gpsTracker.getLocation();
              //   if(Integer.parseInt(strAccuracy)<2) {

                editEndLongitude.setText(strLongitude + "");
                editEndLatitude.setText(strLantitude + "");
               /*}
                else{
                    Toast.makeText(context,"Wait for Accuracy", Toast.LENGTH_SHORT);
                }*/
            }
        });
    }
    @OnClick(R.id.imgView)
    public void captureStartImage() {
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(StartGapActivity.this,
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
    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if the result is capturing Image
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                imageViewPhoto.setImageBitmap(photo);
                imagePhoto = getStringImage(photo);
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
    public void getCurrentDate() {
        Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        editDate.setText(mDay + "/" + (mMonth + 1) + "/" + mYear);
    }

    private void setRouteSurveyDate() {
        // Get Current Date
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        editDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
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
        serviceIntent = new Intent(StartGapActivity.this, LocationService.class);
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
           /* TextView textView = snackbarView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.BLUE);*/

            /// snackbar.show();

            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) snackbarView.getLayoutParams();
            params.gravity = Gravity.TOP;
            snackbarView.setLayoutParams(params);
            // snackbar.show();
        }
    };

   /* @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Really Exit?")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        StartGapActivity.super.onBackPressed();
                    }
                }).create().show();
    }*/

    private void startLocationListener() {

        long mLocTrackingInterval = 1000*1*15; // 5 sec
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

                        acctv.setText("Accuracy = " + strAccuracy);
                    }
                });
    }
    @OnClick(R.id.btnSubmit)
    public void sheetSubmit() {

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
                                Toast.makeText(context, response, Toast.LENGTH_LONG).show();
                                progressLoading.dismiss();
                                if (response.contains("Success")) {
                                    Toast.makeText(context, "Successfully submitted", Toast.LENGTH_LONG).show();

                                    editRemarks.setText("");
                                    editStartLatitude.setText("");
                                    editStartLongitude.setText("");
                                    editEndLatitude.setText("");
                                    editEndLongitude.setText("");
                                    editLength.setText("");
                                    editStartNode.setText("");
                                    editEndNode.setText("");
                                    imagePhoto = "";


                                    imageViewPhoto.setImageResource(android.R.color.transparent);


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
                        params.put("type", "create_gap");

                        params.put("user_id", SessionUtil.getUserId(context));

                        params.put("zone_id", zone_id + "");
                       params.put("reason", spinReason.getSelectedItem().toString() + "");

                        params.put("start_node_lat", editStartLatitude.getText().toString() + "");
                        params.put("start_node_lon", editStartLongitude.getText().toString() + "");
                        params.put("end_node_lat", editEndLatitude.getText().toString() + "");
                        params.put("end_node_lon", editEndLongitude.getText().toString() + "");
                        params.put("length", editLength.getText().toString() + "");
                        params.put("pipe_number", pipeNumber + "");
                        params.put("remarks", editRemarks.getText().toString() + "");
                        params.put("date", editDate.getText().toString() + "");
                       params.put("photo", imagePhoto + "");

                        Log.e("Pppppppppppppppppparams", params.toString());
                        return params;
                    }
                };

                // Add the request to the RequestQueue.
                stringRequest.setRetryPolicy(new DefaultRetryPolicy(600000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                queue.add(stringRequest);
            }
        } else {
            DialogUtil.showNoConnectionDialog(this);
        }


    }
    public void sheetZone() {
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
                            zoneList.clear();
                            zoneIDList.clear();
                            try {
                                JSONArray arr = new JSONArray(response);
                                if (arr != null) {
                                    for (int i = 0; i < arr.length(); i++) {

                                        zoneList.add(arr.getJSONObject(i).getString("name"));
                                        zoneIDList.add(arr.getJSONObject(i).getString("id"));
                                        // AlignmentIDList.add(arr.getJSONObject(i).getString("AlignmentID"));
                                    }
                                }
                                zoneAdapter = new ArrayAdapter(context, R.layout.layoutspinner, zoneList);


                                spinnerZone.setAdapter(zoneAdapter);


                                //productModelList.addAll(pipeNo);
                                zoneAdapter.notifyDataSetChanged();


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
                    params.put("type", "get_zone");
                    params.put("for", "laying");
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


        spinnerZone.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.e(" Selected Item position", "position " + i);
                //  bendID = bendModelList.get(i).getId();
                zoneName = spinnerZone.getSelectedItem().toString();
                zone_id = zoneIDList.get(i).toString();

                sheetPipeNumber();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    public void sheetPipeNumber() {
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
                            pipeList.clear();
                            pipeIDList.clear();
                            try {
                                JSONArray arr = new JSONArray(response);
                                if (arr != null) {
                                    for (int i = 0; i < arr.length(); i++) {

                                        pipeList.add(arr.getJSONObject(i).getString("name"));
                                        pipeIDList.add(arr.getJSONObject(i).getString("id"));

                                        // AlignmentIDList.add(arr.getJSONObject(i).getString("AlignmentID"));
                                    }
                                }
                                pipeAdapter = new ArrayAdapter(context, R.layout.layoutspinner, pipeList);


                                spinpipesegment.setAdapter(pipeAdapter);


                                //productModelList.addAll(pipeNo);
                                pipeAdapter.notifyDataSetChanged();


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
                    params.put("type", "get_pipe_number");
                    params.put("for", "laying");
                    params.put("zone_id", zone_id);
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


        spinpipesegment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.e(" Selected Item position", "position " + i);
                //  bendID = bendModelList.get(i).getId();
                pipeNumber = spinpipesegment.getSelectedItem().toString();
                pipeID = pipeIDList.get(i).toString();


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

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