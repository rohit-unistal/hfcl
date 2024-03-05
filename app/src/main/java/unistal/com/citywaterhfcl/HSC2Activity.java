package unistal.com.citywaterhfcl;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.provider.MediaStore;
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

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

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

public class HSC2Activity extends AppCompatActivity {
 /*   @BindView(R.id.edit_date)
    EditText editDate;*/
    @BindView(R.id.accuracytv)
    TextView acctv;
    @BindView(R.id.imgback)
    ImageView imgBack;
    @BindView(R.id.imglogout)
    ImageView imgLogout;
    @BindView(R.id.titletv)
    TextView citytv;
  /*  @BindView(R.id.spin_contractor)
    Spinner spinContractor;*/
    @BindView(R.id.spin_start_node)
    Spinner spinnerStartNode;
    @BindView(R.id.spin_zone)
    Spinner spinnerZone;
    @BindView(R.id.spinLengthConn)
    Spinner spinnerLengthConnection;
    @BindView(R.id.spinWaterStorage)
    Spinner spinnerWaterStorage;
    @BindView(R.id.spin_network)
    Spinner spinnerNetwork;

    @BindView(R.id.spin_dma)
    Spinner spinnerDMA;
    @BindView(R.id.btnlocation)
    Button btnStartNode;
    @BindView(R.id.edlatitude)
    EditText editStartLatitude;

    @BindView(R.id.edlongitude)
    EditText editStartLongitude;
//    @BindView(R.id.edMobileNumber)
//    TextInputEditText edMobileNumber;
//    @BindView(R.id.edit_owner)
//    EditText edOwner;
    @BindView(R.id.edfamilysize)
    EditText edfamilysize;
    @BindView(R.id.edadharnum)
    TextInputEditText edAadharNumber;
    @BindView(R.id.edit_remarks)
    EditText editRemarks;
    @BindView(R.id.spin_end_node)
    Spinner spinnerEndNode;
    @BindView(R.id.edsupervisor)
    EditText edsupervisor;
//    @BindView(R.id.edcity)
//    EditText edcity;
//    @BindView(R.id.edit_page)
//    EditText edpage;
    @BindView(R.id.edHoldingNumber)
    EditText edholdingnum;
/*
    @BindView(R.id.edhouseno)
    EditText edhouseno;
*/

    @BindView(R.id.spinPCCCrossing)
    Spinner spinPCCCrossing;
    @BindView(R.id.imgcustomerphoto)
    ImageView imgcustomerphoto;
    @BindView(R.id.imghscformphoto)
    ImageView imghscformphoto;
    @BindView(R.id.imgholdnophoto)
    ImageView imgholdnophoto;
    /* @BindView(R.id.btn_savenode)
     Button ButtonSaveNode;*/
    String strAccuracy, startnodeName, endnodeName, networkName;
    ArrayList<ItemModel> contractorList, lengthOfConnectionList, waterStorageList;
    ArrayAdapter<ItemModel> contractorAdapter, lengthOfConnectionAdapter, waterStorageAdapter;
    private String strLantitude, strLongitude, strAltitude, zone_id = "", dmaName = "", contractorID = "", pipeID = "", typeID = "", subtypeID = "", diameterID = "", startNodeID = "", endNodeID = "", zoneName = "", waterStorageID = "", lengthofConnectionID = "";
    Context context;
    Intent serviceIntent;
    ArrayList<String> networkList,zoneList, dmaList, startnodeList, endnodeList;
    ArrayAdapter startnodeAdapter, endnodeAdapter, zoneAdapter, dmaAdapter, crossingAdapter,networkAdapter;

    String[] pccCrossOption = {"No", "Yes"};
    Boolean flag = true;
    private Bitmap customerBitmap, hscBitmap, holdingBitmap;
    ProgressLoading progressLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hsc2);
        ButterKnife.bind(this);
        getSupportActionBar().hide();
        citytv.setText("HSC");

        context = this;
        progressLoading = new ProgressLoading(this);
        imgLogout.setVisibility(View.INVISIBLE);
        init();
    }

    private void init() {
        networkList = new ArrayList<>();
        zoneList = new ArrayList();
        dmaList = new ArrayList();
        startnodeList = new ArrayList();
        endnodeList = new ArrayList();
        contractorList = new ArrayList<>();
        lengthOfConnectionList = new ArrayList<>();
        waterStorageList = new ArrayList<>();

        crossingAdapter = new ArrayAdapter(context, R.layout.layoutspinner, pccCrossOption);
        spinPCCCrossing.setAdapter(crossingAdapter);
       /* editDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRouteSurveyDate();
            }
        });
        getCurrentDate();*/
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(HSC2Activity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            //  gpsTracker.getLocation();
            startLocationListener();
        }
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        sheetNetwork();
     //   sheetZone();
     //   sheetContractorData();


        sheetlengthOfConnectionData();
        sheetwaterstorageData();

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
    }

   /* public void getCurrentDate() {
        Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        editDate.setText(mYear + "-" + (mMonth + 1) + "-" + mDay);
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
                        editDate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }*/

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
        serviceIntent = new Intent(HSC2Activity.this, LocationService.class);
        startService(serviceIntent);
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter("app.location.via.service"));
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopService(serviceIntent);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
    }

    private void startLocationListener() {

        long mLocTrackingInterval = 1000 * 1 * 15; // 5 sec
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
            //  TextView textView = snackbarView.findViewById(android.support.design.R.id.snackbar_text);
            //  textView.setTextColor(Color.BLUE);

            /// snackbar.show();

            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) snackbarView.getLayoutParams();
            params.gravity = Gravity.TOP;
            snackbarView.setLayoutParams(params);
            // snackbar.show();
        }
    };
    public void sheetNetwork() {
        if (DialogUtil.checkInternetConnection(this)) {
            // loadingDialog.onShow();
            if (!progressLoading.isShowing()) {
                progressLoading.onShow();
            }

            RequestQueue queue = Volley.newRequestQueue(this);

            final String url = AppConstants.APP_BASE_URL + "allnetworktype?schema=" + SessionUtil.getSchema(context);
            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("Response", response);
                            networkList.clear();
                            try {
                                JSONObject job = new JSONObject(response);

                                JSONArray arr = job.optJSONArray("data");
                                if (arr != null) {
                                    for (int i = 0; i < arr.length(); i++) {

                                        networkList.add(arr.getJSONObject(i).getString("network_type"));
                                        // AlignmentIDList.add(arr.getJSONObject(i).getString("AlignmentID"));
                                    }
                                }
                                networkAdapter = new ArrayAdapter(context, R.layout.layoutspinner,networkList);


                                spinnerNetwork.setAdapter(networkAdapter);


                                //productModelList.addAll(pipeNo);
                                networkAdapter.notifyDataSetChanged();


                            } catch (JSONException ignored) {

                            }
                            //loadingDialog.dismiss();

                            progressLoading.dismiss();
                            // productModelList.addAll(pipeNo);
                        }
                    }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error == null || error.networkResponse == null) {
                        DialogUtil.showMessageDialog(context, "Internet connection break");

                    } else if (error != null && !error.toString().isEmpty()) {
                        //  DialogUtil.showMessageDialog(context, " Unknown Error from server side" + error.networkResponse.statusCode);

                        Toast.makeText(context, "Error from server side" + error.getMessage(), Toast.LENGTH_SHORT).show();
                       /* if (error.networkResponse.statusCode > 200) {
                           // DialogUtil.showMessageDialog(context, "Error from server side" + error.networkResponse.statusCode);
                        } else {
                            error.getMessage();
                            DialogUtil.showMessageDialog(context, error.getMessage());

                        }
                    } else {
                        DialogUtil.showMessageDialog(context, "Unknown Error from server side");
                  */  }
                    progressLoading.dismiss();

                }
            });

            // Add the request to the RequestQueue.
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(600000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            queue.add(stringRequest);
        } else {
            // DialogUtil.showNoConnectionDialog(this);
        }


        spinnerNetwork.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.e(" Selected Item position", "position " + i);
                //  bendID = bendModelList.get(i).getId();
                networkName = spinnerNetwork.getSelectedItem().toString();
                sheetZone();

                //  sheetDMA();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void sheetZone() {
        if (DialogUtil.checkInternetConnection(this)) {
            // loadingDialog.onShow();
            if (!progressLoading.isShowing()) {
                progressLoading.onShow();
            }

            RequestQueue queue = Volley.newRequestQueue(this);

            final String url = AppConstants.APP_BASE_URL + "allzone?schema=" + SessionUtil.getSchema(context)+"&network_type="+networkName;
            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("Response", response);
                            zoneList.clear();
                            try {
                                JSONObject job = new JSONObject(response);

                                JSONArray arr = job.optJSONArray("data");
                                if (arr != null) {
                                    for (int i = 0; i < arr.length(); i++) {

                                        zoneList.add(arr.getJSONObject(i).getString("zone"));
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
                    if (error == null || error.networkResponse == null) {
                        DialogUtil.showMessageDialog(context, "Internet connection break");

                    } else if (error != null && !error.toString().isEmpty()) {
                        //  DialogUtil.showMessageDialog(context, " Unknown Error from server side" + error.networkResponse.statusCode);

                        Toast.makeText(context, "Error from server side" + error.getMessage(), Toast.LENGTH_SHORT).show();
                       /* if (error.networkResponse.statusCode > 200) {
                           // DialogUtil.showMessageDialog(context, "Error from server side" + error.networkResponse.statusCode);
                        } else {
                            error.getMessage();
                            DialogUtil.showMessageDialog(context, error.getMessage());

                        }
                    } else {
                        DialogUtil.showMessageDialog(context, "Unknown Error from server side");
                  */
                    }
                    progressLoading.dismiss();

                }
            });

            // Add the request to the RequestQueue.
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(600000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            queue.add(stringRequest);
        } else {
            // DialogUtil.showNoConnectionDialog(this);
        }


        spinnerZone.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.e(" Selected Item position", "position " + i);
                //  bendID = bendModelList.get(i).getId();
                zoneName = spinnerZone.getSelectedItem().toString();

                sheetDMA();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void sheetDMA() {
        if (DialogUtil.checkInternetConnection(this)) {
            // loadingDialog.onShow();
            if (!progressLoading.isShowing()) {
                progressLoading.onShow();
            }

            RequestQueue queue = Volley.newRequestQueue(this);

            final String url = AppConstants.APP_BASE_URL + "allDma?zone=" + zoneName + "&schema=" + SessionUtil.getSchema(context);
            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("Response", response);
                            dmaList.clear();
                            try {
                                JSONObject djob = new JSONObject(response);

                                JSONArray darr = djob.optJSONArray("data");
                                if (darr != null) {
                                    Log.e("dma", darr.toString());
                                    for (int i = 0; i < darr.length(); i++) {

                                        dmaList.add(darr.getJSONObject(i).getString("dma"));

                                    }
                                }
                                dmaAdapter = new ArrayAdapter(context, R.layout.layoutspinner, dmaList);


                                spinnerDMA.setAdapter(dmaAdapter);


                                //productModelList.addAll(pipeNo);
                                dmaAdapter.notifyDataSetChanged();


                            } catch (JSONException ignored) {

                            }
                            //loadingDialog.dismiss();

                            progressLoading.dismiss();
                            // productModelList.addAll(pipeNo);
                        }
                    }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error == null || error.networkResponse == null) {
                        DialogUtil.showMessageDialog(context, "Internet connection break");

                    } else if (error != null && !error.toString().isEmpty()) {
                        //  DialogUtil.showMessageDialog(context, " Unknown Error from server side" + error.networkResponse.statusCode);

                        Toast.makeText(context, "Error from server side" + error.getMessage(), Toast.LENGTH_SHORT).show();
                       /* if (error.networkResponse.statusCode > 200) {
                           // DialogUtil.showMessageDialog(context, "Error from server side" + error.networkResponse.statusCode);
                        } else {
                            error.getMessage();
                            DialogUtil.showMessageDialog(context, error.getMessage());

                        }
                    } else {
                        DialogUtil.showMessageDialog(context, "Unknown Error from server side");
                  */
                    }
                    progressLoading.dismiss();
                    // Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            // Add the request to the RequestQueue.
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(600000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            queue.add(stringRequest);
        } else {
            //  DialogUtil.showNoConnectionDialog(this);
        }


        spinnerDMA.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.e(" Selected Item position", "position " + i);
                //  bendID = bendModelList.get(i).getId();
                dmaName = spinnerDMA.getSelectedItem().toString();
                sheetStartNode();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void sheetStartNode() {
        if (DialogUtil.checkInternetConnection(this)) {
            // loadingDialog.onShow();
            if (!progressLoading.isShowing()) {
                progressLoading.onShow();
            }

            RequestQueue queue = Volley.newRequestQueue(this);
            //http://jwil-wm.smartutilitiesnet.com/api/getStartNodeZoneDma?schema=
            final String url = AppConstants.APP_BASE_URL + "getStartNodeZoneDma?schema=" + SessionUtil.getSchema(context) + "&zone=" + zoneName + "&dma=" + dmaName;
            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("Response", response);
                            startnodeList.clear();
                            try {
                                JSONObject djob = new JSONObject(response);

                                JSONArray darr = djob.optJSONArray("data");
                                if (darr != null) {
                                    Log.e("start_node", darr.toString());
                                    for (int i = 0; i < darr.length(); i++) {

                                        startnodeList.add(darr.getJSONObject(i).getString("start_node"));

                                    }
                                }
                                startnodeAdapter = new ArrayAdapter(context, R.layout.layoutspinner, startnodeList);


                                spinnerStartNode.setAdapter(startnodeAdapter);


                                //productModelList.addAll(pipeNo);
                                startnodeAdapter.notifyDataSetChanged();


                            } catch (JSONException ignored) {

                            }
                            //loadingDialog.dismiss();

                            progressLoading.dismiss();
                            // productModelList.addAll(pipeNo);
                        }
                    }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error == null || error.networkResponse == null) {
                        DialogUtil.showMessageDialog(context, "Internet connection break");

                    } else if (error != null && !error.toString().isEmpty()) {
                        //  DialogUtil.showMessageDialog(context, " Unknown Error from server side" + error.networkResponse.statusCode);

                        Toast.makeText(context, "Error from server side" + error.getMessage(), Toast.LENGTH_SHORT).show();
                       /* if (error.networkResponse.statusCode > 200) {
                           // DialogUtil.showMessageDialog(context, "Error from server side" + error.networkResponse.statusCode);
                        } else {
                            error.getMessage();
                            DialogUtil.showMessageDialog(context, error.getMessage());

                        }
                    } else {
                        DialogUtil.showMessageDialog(context, "Unknown Error from server side");
                  */
                    }
                    progressLoading.dismiss();
                    // Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            // Add the request to the RequestQueue.
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(600000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            queue.add(stringRequest);
        } else {
            //  DialogUtil.showNoConnectionDialog(this);
        }


        spinnerStartNode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.e(" Selected Item position", "position " + i);
                //  bendID = bendModelList.get(i).getId();
                startnodeName = spinnerStartNode.getSelectedItem().toString();
                sheetEndNode();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void sheetEndNode() {
        if (DialogUtil.checkInternetConnection(this)) {
            // loadingDialog.onShow();
            if (!progressLoading.isShowing()) {
                progressLoading.onShow();
            }

            RequestQueue queue = Volley.newRequestQueue(this);
            //http://jwil-wm.smartutilitiesnet.com/api/getStartNodeZoneDma?schema=
            final String url = AppConstants.APP_BASE_URL + "getEndNodebyZoneDMAStartnode?schema=" + SessionUtil.getSchema(context) + "&zone=" + zoneName + "&dma=" + dmaName + "&start_node=" + startnodeName;
            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("Response", response);
                            endnodeList.clear();
                            try {
                                JSONObject djob = new JSONObject(response);

                                JSONArray darr = djob.optJSONArray("data");
                                if (darr != null) {
                                    Log.e("stop_node", darr.toString());
                                    for (int i = 0; i < darr.length(); i++) {

                                        endnodeList.add(darr.getJSONObject(i).getString("stop_node"));

                                    }
                                }
                                endnodeAdapter = new ArrayAdapter(context, R.layout.layoutspinner, endnodeList);


                                spinnerEndNode.setAdapter(endnodeAdapter);


                                //productModelList.addAll(pipeNo);
                                endnodeAdapter.notifyDataSetChanged();


                            } catch (JSONException ignored) {

                            }
                            //loadingDialog.dismiss();

                            progressLoading.dismiss();
                            // productModelList.addAll(pipeNo);
                        }
                    }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error == null || error.networkResponse == null) {
                        DialogUtil.showMessageDialog(context, "Internet connection break");

                    } else if (error != null && !error.toString().isEmpty()) {
                        //  DialogUtil.showMessageDialog(context, " Unknown Error from server side" + error.networkResponse.statusCode);

                        Toast.makeText(context, "Error from server side" + error.getMessage(), Toast.LENGTH_SHORT).show();
                       /* if (error.networkResponse.statusCode > 200) {
                           // DialogUtil.showMessageDialog(context, "Error from server side" + error.networkResponse.statusCode);
                        } else {
                            error.getMessage();
                            DialogUtil.showMessageDialog(context, error.getMessage());

                        }
                    } else {
                        DialogUtil.showMessageDialog(context, "Unknown Error from server side");
                  */
                    }
                    progressLoading.dismiss();
                    // Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            // Add the request to the RequestQueue.
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(600000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            queue.add(stringRequest);
        } else {
            //  DialogUtil.showNoConnectionDialog(this);
        }


        spinnerEndNode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.e(" Selected Item position", "position " + i);
                //  bendID = bendModelList.get(i).getId();
                endnodeName = spinnerEndNode.getSelectedItem().toString();


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


   /* public void sheetContractorData() {
        if (DialogUtil.checkInternetConnection(this)) {
            // loadingDialog.onShow();
            if (!progressLoading.isShowing()) {
                progressLoading.onShow();
            }

            RequestQueue queue = Volley.newRequestQueue(this);

            String url = AppConstants.APP_BASE_URL + "allContractor?assigned_ga=" + SessionUtil.getGA(context);
            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("Response", response);

                            //loadingDialog.dismiss();
                            SessionUtil.saveContractor(response, context);
                            setContractorData();
                            // productModelList.addAll(pipeNo);
                        }
                    }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error == null || error.networkResponse == null) {
                        DialogUtil.showMessageDialog(context, "Internet connection break");

                    } else if (error != null && !error.toString().isEmpty()) {
                        //  DialogUtil.showMessageDialog(context, " Unknown Error from server side" + error.networkResponse.statusCode);

                        Toast.makeText(context, "Error from server side" + error.getMessage(), Toast.LENGTH_SHORT).show();
                       *//* if (error.networkResponse.statusCode > 200) {
                           // DialogUtil.showMessageDialog(context, "Error from server side" + error.networkResponse.statusCode);
                        } else {
                            error.getMessage();
                            DialogUtil.showMessageDialog(context, error.getMessage());

                        }
                    } else {
                        DialogUtil.showMessageDialog(context, "Unknown Error from server side");
                  *//*
                    }


                }
            });

            // Add the request to the RequestQueue.
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(600000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            queue.add(stringRequest);
        } else {
            DialogUtil.showNoConnectionDialog(this);
        }

    }
*/
  /*  private void setContractorData() {
        final String contractorData = SessionUtil.getContractor(context);
        contractorList.clear();
        contractorList.add(new ItemModel("0", "Select"));

        try {
            JSONObject djob = new JSONObject(contractorData);

            JSONArray darr = djob.optJSONArray("data");
            if (darr != null) {
                Log.e("contractor", darr.toString());
                for (int i = 0; i < darr.length(); i++) {

                    contractorList.add(new ItemModel(darr.getJSONObject(i).getString("id"), darr.getJSONObject(i).getString("company_name")));

                }
            }
            contractorAdapter = new ArrayAdapter(context, R.layout.layoutspinner, contractorList);


            spinContractor.setAdapter(contractorAdapter);

            contractorAdapter.notifyDataSetChanged();


        } catch (JSONException ignored) {

        }
        spinContractor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                contractorID = contractorList.get(i).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
*/
    public void sheetlengthOfConnectionData() {
        if (DialogUtil.checkInternetConnection(this)) {
            // loadingDialog.onShow();
            if (!progressLoading.isShowing()) {
                progressLoading.onShow();
            }

            RequestQueue queue = Volley.newRequestQueue(this);

            final String url = AppConstants.APP_BASE_URL + "lengthofconnection?schema=" + SessionUtil.getSchema(context);
            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("Response", response);
                            progressLoading.dismiss();
                            //loadingDialog.dismiss();
                            SessionUtil.savelengthOfConnection(response, context);
                            setlengthOfConnectionData();

                            // productModelList.addAll(pipeNo);
                        }
                    }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error == null || error.networkResponse == null) {
                        DialogUtil.showMessageDialog(context, "Internet connection break");

                    } else if (error != null && !error.toString().isEmpty()) {
                        //  DialogUtil.showMessageDialog(context, " Unknown Error from server side" + error.networkResponse.statusCode);

                        Toast.makeText(context, "Error from server side" + error.getMessage(), Toast.LENGTH_SHORT).show();
                       /* if (error.networkResponse.statusCode > 200) {
                           // DialogUtil.showMessageDialog(context, "Error from server side" + error.networkResponse.statusCode);
                        } else {
                            error.getMessage();
                            DialogUtil.showMessageDialog(context, error.getMessage());

                        }
                    } else {
                        DialogUtil.showMessageDialog(context, "Unknown Error from server side");
                  */
                    }

                    progressLoading.dismiss();
                    // Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            // Add the request to the RequestQueue.
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(600000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            queue.add(stringRequest);
        } else {
            DialogUtil.showNoConnectionDialog(this);
        }

    }

    public void sheetwaterstorageData() {
        if (DialogUtil.checkInternetConnection(this)) {
            // loadingDialog.onShow();
            if (!progressLoading.isShowing()) {
                progressLoading.onShow();
            }

            RequestQueue queue = Volley.newRequestQueue(this);

            final String url = AppConstants.APP_BASE_URL + "waterstorage?schema=" + SessionUtil.getSchema(context);
            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("Response", response);
                            progressLoading.dismiss();
                            //loadingDialog.dismiss();
                            SessionUtil.savewaterStorage(response, context);
                            setwaterStorageData();
                            // productModelList.addAll(pipeNo);
                        }
                    }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error == null || error.networkResponse == null) {
                        DialogUtil.showMessageDialog(context, "Internet connection break");

                    } else if (error != null && !error.toString().isEmpty()) {
                        //  DialogUtil.showMessageDialog(context, " Unknown Error from server side" + error.networkResponse.statusCode);

                        Toast.makeText(context, "Error from server side" + error.getMessage(), Toast.LENGTH_SHORT).show();
                       /* if (error.networkResponse.statusCode > 200) {
                           // DialogUtil.showMessageDialog(context, "Error from server side" + error.networkResponse.statusCode);
                        } else {
                            error.getMessage();
                            DialogUtil.showMessageDialog(context, error.getMessage());

                        }
                    } else {
                        DialogUtil.showMessageDialog(context, "Unknown Error from server side");
                  */
                    }

                    progressLoading.dismiss();
                    // Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            // Add the request to the RequestQueue.
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(600000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            queue.add(stringRequest);
        } else {
            DialogUtil.showNoConnectionDialog(this);
        }

    }

    private void setlengthOfConnectionData() {

        String lengthOfConnectionData = SessionUtil.getlengthOfConnection(context);
        Log.e("lengthOfConnection", lengthOfConnectionData);
        lengthOfConnectionList.clear();
        try {
            JSONObject djob = new JSONObject(lengthOfConnectionData);

            JSONArray darr = djob.optJSONArray("data");
            if (darr != null) {
                Log.e("lengthOfConnection", darr.toString());
                for (int i = 0; i < darr.length(); i++) {

                    lengthOfConnectionList.add(new ItemModel(darr.getJSONObject(i).getString("id"), darr.getJSONObject(i).getString("connection_name")));

                }
            }
            lengthOfConnectionAdapter = new ArrayAdapter(context, R.layout.layoutspinner, lengthOfConnectionList);


            spinnerLengthConnection.setAdapter(lengthOfConnectionAdapter);

            lengthOfConnectionAdapter.notifyDataSetChanged();


        } catch (JSONException ignored) {

        }
        spinnerLengthConnection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                lengthofConnectionID = lengthOfConnectionList.get(i).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setwaterStorageData() {
        String waterStorageData = SessionUtil.getwaterStorage(context);
        Log.e("waterStorage", waterStorageData);

        waterStorageList.clear();
        try {
            JSONObject djob = new JSONObject(waterStorageData);

            JSONArray darr = djob.optJSONArray("data");
            if (darr != null) {
                Log.e("waterStorage", darr.toString());
                for (int i = 0; i < darr.length(); i++) {

                    waterStorageList.add(new ItemModel(darr.getJSONObject(i).getString("id"), darr.getJSONObject(i).getString("storage_type")));

                }
            }
            waterStorageAdapter = new ArrayAdapter(context, R.layout.layoutspinner, waterStorageList);


            spinnerWaterStorage.setAdapter(waterStorageAdapter);

            waterStorageAdapter.notifyDataSetChanged();


        } catch (JSONException ignored) {

        }
        spinnerWaterStorage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                waterStorageID = waterStorageList.get(i).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @OnClick(R.id.imgcustomerphoto)
    public void captureCustomerPhoto() {

        if ((ActivityCompat.shouldShowRequestPermissionRationale(HSC2Activity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) && (ActivityCompat.shouldShowRequestPermissionRationale(HSC2Activity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE))) {

        } else {
            if (ContextCompat.checkSelfPermission(context,
                    Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(HSC2Activity.this,
                        new String[]{Manifest.permission.CAMERA},
                        100);

            } else {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                //  fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

                //  intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

                // start the image capture Intent
                startActivityForResult(intent, 101);
            }
        }


    }

    @OnClick(R.id.imghscformphoto)
    public void captureHSCForm() {

        if ((ActivityCompat.shouldShowRequestPermissionRationale(HSC2Activity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) && (ActivityCompat.shouldShowRequestPermissionRationale(HSC2Activity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE))) {

        } else {
            if (ContextCompat.checkSelfPermission(context,
                    Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(HSC2Activity.this,
                        new String[]{Manifest.permission.CAMERA},
                        100);

            } else {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                //  fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

                //  intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

                // start the image capture Intent
                startActivityForResult(intent, 102);
            }
        }


    }

    @OnClick(R.id.imgholdnophoto)
    public void captureHoldNo() {

        if ((ActivityCompat.shouldShowRequestPermissionRationale(HSC2Activity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) && (ActivityCompat.shouldShowRequestPermissionRationale(HSC2Activity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE))) {

        } else {
            if (ContextCompat.checkSelfPermission(context,
                    Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(HSC2Activity.this,
                        new String[]{Manifest.permission.CAMERA},
                        100);

            } else {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                //  fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

                //  intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

                // start the image capture Intent
                startActivityForResult(intent, 103);
            }
        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if the result is capturing Image
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101) {
            if (resultCode == RESULT_OK) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                customerBitmap = photo;

                imgcustomerphoto.setImageBitmap(photo);
                       /* Uri selectedImageUri = data.getData();
                         graphPhotoPath =getPath(selectedImageUri);*/
                //     imageView.setImageBitmap(BitmapFactory.decodeFile(graphPhotoPath));
                // Uri tempUri = getImageUri(getApplicationContext(), photo);

                // CALL THIS METHOD TO GET THE ACTUAL PATH
              /*  File finalFile = new File(getRealPathFromURI(tempUri));
                filePath = finalFile.getAbsolutePath();
                System.out.println(filePath);
                Log.e("filePath",filePath);
                Toast.makeText(context,filePath,Toast.LENGTH_SHORT).show();*/
            } else {
                // Log.e("filePath",filePath);
                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();

            }

        } else if (requestCode == 102) {
            if (resultCode == RESULT_OK) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                hscBitmap = photo;

                imghscformphoto.setImageBitmap(photo);
                       /* Uri selectedImageUri = data.getData();
                         graphPhotoPath =getPath(selectedImageUri);*/
                //     imageView.setImageBitmap(BitmapFactory.decodeFile(graphPhotoPath));
                // Uri tempUri = getImageUri(getApplicationContext(), photo);

                // CALL THIS METHOD TO GET THE ACTUAL PATH
              /*  File finalFile = new File(getRealPathFromURI(tempUri));
                filePath = finalFile.getAbsolutePath();
                System.out.println(filePath);
                Log.e("filePath",filePath);
                Toast.makeText(context,filePath,Toast.LENGTH_SHORT).show();*/
            } else {
                // Log.e("filePath",filePath);
                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();

            }

        } else if (requestCode == 103) {
            if (resultCode == RESULT_OK) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                holdingBitmap = photo;

                imgholdnophoto.setImageBitmap(photo);
                       /* Uri selectedImageUri = data.getData();
                         graphPhotoPath =getPath(selectedImageUri);*/
                //     imageView.setImageBitmap(BitmapFactory.decodeFile(graphPhotoPath));
                // Uri tempUri = getImageUri(getApplicationContext(), photo);

                // CALL THIS METHOD TO GET THE ACTUAL PATH
              /*  File finalFile = new File(getRealPathFromURI(tempUri));
                filePath = finalFile.getAbsolutePath();
                System.out.println(filePath);
                Log.e("filePath",filePath);
                Toast.makeText(context,filePath,Toast.LENGTH_SHORT).show();*/
            } else {
                // Log.e("filePath",filePath);
                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();

            }

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

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);

        return byteArrayOutputStream.toByteArray();
    }
    @OnClick(R.id.btnSubmit)
    public void uploadBitmap() {
        if (validate()){
            if (DialogUtil.checkInternetConnection(this)){
        if (!progressLoading.isShowing()) {
            progressLoading.onShow();
        }
        flag=false;
        final String url = AppConstants.APP_BASE_URL + "insertHSC";
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, url,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        progressLoading.dismiss();
                        flag =true;
                        Log.e("RRRR response", String.valueOf(response));
                        try {
                            JSONObject jsonObject = new JSONObject(new String(response.data));
                            String status = jsonObject.getString("success");
                            String message = jsonObject.getString("data");

                            if (message.contains("success")) {
                                Toast.makeText(context,  message, Toast.LENGTH_LONG).show();
                                clear();

                            }else{
                                Toast.makeText(context, "status = " + status + " messages = " + message, Toast.LENGTH_LONG).show();

                            }


                        } catch (JSONException e) {
                            Toast.makeText(context, e.getMessage() + " " + e.toString(), Toast.LENGTH_SHORT).show();
                            Log.e("Not GotError response", e.toString());
                            e.printStackTrace();
                        }
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressLoading.dismiss();
                        flag = true;
                        if(error==null ) {
                            if (error != null && error.networkResponse.data != null) {
                                try {
                                    String responseString = new String(error.networkResponse.data);
                                    JSONObject obj = new JSONObject(responseString);
                                    if (responseString.contains("success")) {
                                        // Toast.makeText(getApplicationContext(), obj.getString("data"), Toast.LENGTH_SHORT).show();
                                        Log.e("Not GotError response", "" + obj.getString("success"));


                                        Log.e("GotError", "" + obj.getString("data"));
                                        Toast.makeText(context, "status = " + obj.getString("success") + " messages = " + obj.getString("data"), Toast.LENGTH_LONG).show();
                                    }
                                    if (responseString.contains("success")) {
                                        // Toast.makeText(getApplicationContext(), obj.getString("data"), Toast.LENGTH_SHORT).show();
                                        Log.e("Not GotError response", "" + obj.getString("success"));


                                        Log.e("GotError", "" + obj.getString("data"));
                                        Toast.makeText(context, "status = " + obj.getString("success") + " messages = " + obj.getString("data"), Toast.LENGTH_LONG).show();
                                    }

                                } catch (JSONException e) {
                                    Toast.makeText(context, e.getMessage() + " " + e.toString(), Toast.LENGTH_SHORT).show();
                                    Log.e("Not GotError response", e.toString());
                                    e.printStackTrace();
                                }
                            }
                        }else{
                            Toast.makeText(context, "Error in connectivity or server side error-"+ error.toString(), Toast.LENGTH_SHORT).show();
                            Log.e("Not GotError response", error.toString());
                        }
                    }
                }) {


            @Override
            protected Map<String, VolleyMultipartRequest.DataPart> getByteData() {
                Map<String, VolleyMultipartRequest.DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();

                params.put("customer_file", new VolleyMultipartRequest.DataPart("customer" + imagename + ".png", getFileDataFromDrawable(customerBitmap)));
                params.put("hsc_form", new VolleyMultipartRequest.DataPart("hsc_" + imagename + ".png", getFileDataFromDrawable(hscBitmap)));

                params.put("holding_no_doc", new VolleyMultipartRequest.DataPart("holding_no_" + imagename + ".png", getFileDataFromDrawable(holdingBitmap)));




                return params;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("schema", SessionUtil.getSchema(context));
                params.put("user_id", SessionUtil.getUserId(context));
                params.put("network_type", networkName);
                params.put("zone", zoneName);
                params.put("dma", dmaName);

                params.put("pipe_number", "0");
                params.put("start_node", startnodeName);
                params.put("end_node", endnodeName);
                params.put("latitude", editStartLatitude.getText().toString());
                params.put("longitude", editStartLongitude.getText().toString());


               params.put("page_serial","0");
                params.put("remark", editRemarks.getText().toString());
                params.put("completion_date", "0000-00-00");

                params.put("water_storage", waterStorageID +"");
                params.put("road_grossing", spinPCCCrossing.getSelectedItem().toString());

                params.put("hsc_contractor_name", edsupervisor.getText().toString());
               params.put("contractor","0");
                params.put("length_connection", lengthofConnectionID+"");
              params.put("city_village", "");
               params.put("house_plot_no", "");
                params.put("adhaar_no", edAadharNumber.getText().toString());
            params.put("mobile_no", "0000000000");
                params.put("family_size", edfamilysize.getText().toString());
               params.put("owner_name","0");
                params.put("holding_number", edholdingnum.getText().toString());
                Log.e("Pppppppppppppppppparams", params.toString());
                return params;

            }


        };

        //adding the request to volley
        volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                1000000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(this).add(volleyMultipartRequest);}
    }}
    private void clear() {
        editRemarks.setText("");

        editStartLatitude.setText("");
        editStartLongitude.setText("");
        edholdingnum.setText("");edAadharNumber.setText("");
    //    edMobileNumber.setText("");edhouseno.setText("");
     //   edOwner.setText("");
        //  spinnerStartNode.setText("");
        //  spinnerEndNode.setText("");
        // startNodeID=""; endNodeID="";
        customerBitmap = null;hscBitmap=null;holdingBitmap=null;
        imgcustomerphoto.setImageResource(android.R.color.transparent);
        imgholdnophoto.setImageResource(android.R.color.transparent);
        imghscformphoto.setImageResource(android.R.color.transparent);
    }
    public boolean validate(){
        boolean valid = true;
       /* if(edMobileNumber.getText().toString().trim().equals("")||edMobileNumber.getText().toString().trim().length()<10)
        {
            valid = false;
            Toast.makeText(context,"Enter valid mobile number",Toast.LENGTH_SHORT).show();
        }else*/
        if(edAadharNumber.getText().toString().trim().equals("")||edAadharNumber.getText().toString().trim().length()<12)
        {
            valid = false;
            Toast.makeText(context,"Enter valid aadhar number",Toast.LENGTH_SHORT).show();
        }else
        if(editStartLatitude.getText().toString().trim().equals("")||editStartLongitude.getText().toString().trim().equals(""))
        {
            valid = false;
            Toast.makeText(context,"Enter Location Latitude and Longitude",Toast.LENGTH_SHORT).show();
        }else
        if(edholdingnum.getText().toString().trim().equals(""))
        {
            valid = false;
            Toast.makeText(context,"Enter holding number",Toast.LENGTH_SHORT).show();
        }/*else
            if(edOwner.getText().toString().trim().equals(""))
            {
                valid = false;
                Toast.makeText(context,"Enter owner name",Toast.LENGTH_SHORT).show();
            }
*/

        else
        if(editRemarks.getText().toString().trim().equals(""))
        {
            valid = false;
            Toast.makeText(context,"Enter Remarks",Toast.LENGTH_SHORT).show();
        }else
        if(customerBitmap==null)
        {
            valid = false;
            Toast.makeText(context,"Capture Customer Photo",Toast.LENGTH_SHORT).show();
        }else
        if(hscBitmap==null)
        {
            valid = false;
            Toast.makeText(context,"Capture hsc Photo",Toast.LENGTH_SHORT).show();
        }else
        if(holdingBitmap==null)
        {
            valid = false;
            Toast.makeText(context,"Capture holding Photo",Toast.LENGTH_SHORT).show();
        }
      /*  if(editEndDepth.getText().toString().trim().equals("")||editEndWidth.getText().toString().trim().equals(""))
        {
            valid = false;
            Toast.makeText(context,"Enter End Depth and Width",Toast.LENGTH_SHORT).show();
        }*/



        return valid;
    }
}

/*
* "page_serial_no" => $this->request->getPost("page_serial"),
                        "holding_number" => '',
                        "k_number" => '',
                        "zone" => $this->request->getPost("zone"),
                        "dma" => $this->request->getPost('dma'),
                                                                                          "pipe_number"=>$this->request->getPost("pipe_number"),
                        "start_node" => $this->request->getPost('start_node'),
                        "end_node" => $this->request->getPost('end_node'),
                        "owner_name" => $this->request->getPost("owner_name"),
                        "family_size" => $this->request->getPost("family_size"),
                        "mobile_no" => $this->request->getPost("mobile_no"),
                        "adhaar_no" =>$this->request->getPost("adhaar_no"),
                        "house_plot_no" => '',
                                                                                          "customer_photo" => $photo,
                        "city_village" => $this->request->getPost("city_village"),
                        "connection_length" => $this->request->getPost("length_connection"),
                        "completion_date" =>date("Y-m-d", strtotime($this->request->getPost("completion_date"))),
                        "meter_no" => 0,
                        "meter_init_reading" => 0,
                        "meter_make" => '',
                        "meter_size" => 0,
                        "water_storage" => $this->request->getPost("water_storage"),
                        "pcc_road_grossing" => $this->request->getPost("road_grossing"),
                        "loc_latitude" => $this->request->getPost("latitude"),
                        "loc_longitude" => $this->request->getPost("longitude"),
                        "contractor_id" => $this->request->getPost("contractor"),
                        "contract_name" => '',
                        "remark" => $this->request->getPost("remark"),
                        "status"=>'1',
                        "manual_entry" => '',
                              "source" => 'APP',
                              "user_id" => $this->request->getPost("user_id")
customer_file
hsc_form
holding_no_doc
*/

