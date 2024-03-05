package unistal.com.citywaterhfcl;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.net.ConnectivityManager;
import android.provider.MediaStore;
//import android.support.design.widget.Snackbar;
/*import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;*/
import android.os.Bundle;
//import androidx.cardview.widget.CardView;
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
import org.json.JSONObject;

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

public class Laying3Activity extends AppCompatActivity implements NetworkStateReceiver.NetworkStateReceiverListener{
    @BindView(R.id.spin_start_node)
    EditText spinnerStartNode;
    @BindView(R.id.spin_zone)
    Spinner spinnerZone;
    @BindView(R.id.spin_pipe_segment)
    Spinner spinpipesegment;
    @BindView(R.id.spin_soil)
    Spinner spinnerSoil;
    @BindView(R.id.spin_alignment)
    Spinner spinAlignment;
   /* @BindView(R.id.spin_dma)
    Spinner spinnerDMA;*/

    @BindView(R.id.spin_end_node)
    EditText spinnerEndNode;


    @BindView(R.id.btnEndNode)
    Button btnEndNode;
    @BindView(R.id.btnNextNode)
    Button btnNextNode;
    @BindView(R.id.btnStartNode)
    Button btnStartNode;
    @BindView(R.id.editStartLatitude)
    EditText editStartLatitude;

    @BindView(R.id.edit_start_longitude)
    EditText editStartLongitude;
    @BindView(R.id.edit_start_altitude)
    EditText editStartAltitude;

    @BindView(R.id.edit_end_latitude)
    EditText editEndLatitude;

    @BindView(R.id.edit_end_Longitude)
    EditText editEndLongitude;
    @BindView(R.id.edit_end_altitude)
    EditText editEndAltitude;

    @BindView(R.id.edit_next_latitude)
    EditText editNextLatitude;

    @BindView(R.id.edit_next_Longitude)
    EditText editNextLongitude;
    @BindView(R.id.edit_next_altitude)
    EditText editNextAltitude;

    @BindView(R.id.edit_date)
    EditText editDate;
    @BindView(R.id.edit_remarks)
    EditText editRemarks;
    @BindView(R.id.edit_contractor)
    EditText editContractor;
    @BindView(R.id.editnextDepth)
    EditText editNextDepth;

    @BindView(R.id.editnextDiameter)
    EditText editNextDiameter;
    @BindView(R.id.editnextLength)
    EditText editNextLength;
    @BindView(R.id.nextDiameter)
    TextView tvNextDiameter;
    @BindView(R.id.nextLength)
    TextView tvNextLength;


    @BindView(R.id.editlength)
    EditText editLength;


    @BindView(R.id.edit_start_depth)
    EditText editStartDepth;
    @BindView(R.id.editendDepth)
    EditText editEndDepth;


    @BindView(R.id.edit_start_width)
    EditText editStartWidth;
    @BindView(R.id.edit_end_width)
    EditText editEndWidth;

    @BindView(R.id.spin_start_options)
    Spinner spinnerStartOptions;

   /* @BindView(R.id.cvform2)
    CardView cvform2;*/


    @BindView(R.id.spin_subtype)
    EditText spinnerSubType;
    @BindView(R.id.spin_type)
    EditText spinnerType;
    @BindView(R.id.spin_diameter)
    EditText spinnerDiameter;


    @BindView(R.id.spin_intermediate)
    Spinner spinnerIntermediate;

    @BindView(R.id.imgStartPhoto)
    ImageView imgStartPhoto;

    @BindView(R.id.imgEndPhoto)
    ImageView imgEndPhoto;

    @BindView(R.id.imglogout)
    ImageView imgLogout;

    @BindView(R.id.titletv)
    TextView citytv;
    @BindView(R.id.netstatus)
    ImageView netStatus;

    @BindView(R.id.accuracytv)
    TextView acctv;
    private NetworkStateReceiver networkStateReceiver;
    // GPSTracker gpsTracker;
    private static final int START_CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int END_CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 101;
    ArrayList soilNameList,alignmentNameList;
    ArrayList arrayNextLatitude, arrayNextLongitude, arrayNextAltitude, arrayDepth, arrayIntermediateList, arrayNextDiameterList, arrayNextLengthList;
    ArrayList<ItemModel> pipeTypeModelList, pipeSubTypeModelList, pipeDiameterModelList,  intermediateModelList, segmentModelList;//startNodeModelList, endNodeModelList,
    ArrayList  zoneList,  zoneIDList,pipeList, pipeIDList,pipeTypeList, pipeSubTypeList, pipeDiameterList,  intermediateList, segmentList;//startNodeList, endNodeList,dmaList, dmaIDList
    ArrayAdapter pipeAdapter,zoneAdapter, soilAdapter, alignmentAdapter, pipeTypeAdapter, pipeSubTypeAdapter, pipeDiameterAdapter, startNodeAdapter, endNodeAdapter,  dmaAdapter, intermediateAdapter, startSegmentAdapter;
    String pipeNumber="",pipeID="", zone_id="",segmentID="", typeID="",subtypeID="", diameterID="",startNodeID="", endNodeID="",  intermediateID, imageStart, imageEnd, zoneName, dmaName;//
    ProgressLoading progressLoading;
    Context context;
    private Intent serviceIntent;
    Boolean flag = true;
    String strAccuracy;
    private String strLantitude, strLongitude, strAltitude,dma_id="1";
    String username ="basicAuth";
    String password = "ranchi@2021";
    String credentials =new String(username + ":" + password);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laying3);

        ButterKnife.bind(this);
        getSupportActionBar().hide();
        citytv.setText("LAYING");

        context = this;
        progressLoading = new ProgressLoading(this);
        init();
    }

    public void init() {
        // gpsTracker = new GPSTracker(context);
        netConnectivity();

        segmentList = new ArrayList();
        segmentModelList = new ArrayList<>();
        zoneList = new ArrayList(); zoneIDList = new ArrayList();
        pipeList = new ArrayList();
        alignmentNameList = new ArrayList();
        soilNameList = new ArrayList();


        pipeIDList = new ArrayList();
       /* dmaList = new ArrayList();

        dmaIDList = new ArrayList();*/
        arrayNextDiameterList = new ArrayList();
        arrayNextLengthList = new ArrayList();
        pipeTypeModelList = new ArrayList<>();
        pipeTypeList = new ArrayList();
        pipeSubTypeModelList = new ArrayList<>();
        pipeSubTypeList = new ArrayList();
        pipeDiameterModelList = new ArrayList<>();
        pipeDiameterList = new ArrayList();
      /*  startNodeModelList = new ArrayList<>();
        startNodeList = new ArrayList();
        endNodeModelList = new ArrayList<>();
        endNodeList = new ArrayList();*/
        arrayNextLatitude = new ArrayList();
        arrayNextLongitude = new ArrayList();
        arrayNextAltitude = new ArrayList();
        arrayDepth = new ArrayList();
        arrayIntermediateList = new ArrayList();
        intermediateList = new ArrayList();
        intermediateModelList = new ArrayList<>();

        acctv.setText("Accuracy = " + strAccuracy);
        editDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRouteSurveyDate();
            }
        });
        getCurrentDate();
        // sheetSegmentType();

        sheetIntermediate();
        sheetZone();
        sheetSoil();
        sheetAlignment();
        // sheetDMA();
        //sheetType();
        // sheetEndNode();
        // gpsTracker.getLocation();
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Laying3Activity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            //  gpsTracker.getLocation();
            startLocationListener();
        }
        btnStartNode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // gpsTracker.getLocation();
                // if(Integer.parseInt(strAccuracy)<5) {
                editStartAltitude.setText(strAltitude + "");
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
                editEndAltitude.setText(strAltitude + "");
                editEndLongitude.setText(strLongitude + "");
                editEndLatitude.setText(strLantitude + "");
               /* }
                else{
                    Toast.makeText(context,"Wait for Accuracy", Toast.LENGTH_SHORT);
                }*/
            }
        });

        btnNextNode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // gpsTracker.getLocation();
                // if(Integer.parseInt(strAccuracy)<5) {
                editNextAltitude.setText(strAltitude + "");
                editNextLongitude.setText(strLongitude + "");
                editNextLatitude.setText(strLantitude + "");
            /*}
                else{
                Toast.makeText(context,"Wait for Accuracy", Toast.LENGTH_SHORT);
            }*/
            }
        });


    }

    @OnClick(R.id.btnAdd)
    public void onAdd() {
        if(!(editNextLatitude.getText().toString().trim().equals("")||editStartLongitude.getText().toString().trim().equals("")||editNextDepth.getText().toString().trim().equals(""))) {
            arrayNextAltitude.add(editNextAltitude.getText().toString());
            arrayNextLatitude.add(editNextLatitude.getText().toString());
            arrayNextLongitude.add(editNextLongitude.getText().toString());
            arrayDepth.add(editNextDepth.getText().toString());
            arrayIntermediateList.add(intermediateModelList.get(spinnerIntermediate.getSelectedItemPosition()).getId());
            arrayNextLengthList.add(editNextLength.getText().toString());
            arrayNextDiameterList.add(editNextDiameter.getText().toString());
            editNextAltitude.setText("");
            editNextLatitude.setText("");
            editNextLongitude.setText("");
            editNextDepth.setText("");
            editNextLength.setText("");
            editNextDiameter.setText("");
        }else{
            Toast.makeText(context,"Enter all intermediate fields",Toast.LENGTH_SHORT).show();
        }
    }

   /* public void sheetSegmentType() {
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
                            segmentList.clear();
                            segmentModelList.clear();
                            try {
                                JSONArray arr = new JSONArray(response);
                                if (arr != null) {
                                    for (int i = 0; i < arr.length(); i++) {
                                        // bendNo.add(new ItemModel(arr.getJSONObject(i).getString("id"), arr.getJSONObject(i).getString("name")));
                                        segmentModelList.add(new ItemModel(arr.getJSONObject(i).getString("id"), arr.getJSONObject(i).getString("name")));

                                        segmentList.add(arr.getJSONObject(i).getString("name"));

                                        // AlignmentIDList.add(arr.getJSONObject(i).getString("AlignmentID"));
                                    }
                                }
                                startSegmentAdapter = new ArrayAdapter(context, R.layout.layoutspinner, segmentList);


                                spinnerStartOptions.setAdapter(startSegmentAdapter);


                                //productModelList.addAll(pipeNo);
                                startSegmentAdapter.notifyDataSetChanged();


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
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("type", "get_start_options");

                    params.put("user_id", SessionUtil.getUserId(context));

                    //   type: 'get_from_node', for:'laying', zone:'J01', dma:'A' , user_id: 30

                    return params;
                }
            };

            // Add the request to the RequestQueue.
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            queue.add(stringRequest);
        } else {
            DialogUtil.showNoConnectionDialog(this);
        }


        spinnerStartOptions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.e(" Selected Item position", "position " + i);
                //  bendID = bendModelList.get(i).getId();
                // segmentID = segmentModelList.get(i).getId();
                //  sheetEndNode();

                *//*sheetZone();
                sheetType();*//*

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }*/

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
                spinnerStartNode.setText("");
                spinnerEndNode.setText("");
                spinnerType.setText("");
                spinnerSubType.setText("");
                spinnerDiameter.setText("");
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
                // sheetStartNode();
                sheetPipeDetail();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }







    public void sheetPipeDetail() {
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
                            progressLoading.dismiss();
                           spinnerStartNode.setText("");
                           spinnerEndNode.setText("");
                           spinnerType.setText("");
                           spinnerSubType.setText("");
                           spinnerDiameter.setText("");
                            try {
                                JSONArray arr = new JSONArray(response);
                                if (arr != null) {
                                    for (int i = 0; i < arr.length(); i++) {
                                        spinnerStartNode.setText(arr.getJSONObject(i).optString("from_node"));
                                        spinnerEndNode.setText(arr.getJSONObject(i).optString("to_node"));
                                        spinnerType.setText(arr.getJSONObject(i).optString("pipe_type"));
                                        spinnerSubType.setText(arr.getJSONObject(i).optString("pipe_subtype"));
                                        spinnerDiameter.setText(arr.getJSONObject(i).optString("pipe_diameter"));
                                        startNodeID = arr.getJSONObject(i).optString("start_node_id")+"";
                                        endNodeID = arr.getJSONObject(i).optString("end_node_id")+"";
                                        typeID = arr.getJSONObject(i).optString("type_id");
                                        subtypeID = arr.getJSONObject(i).optString("subtype_id");
                                        diameterID = arr.getJSONObject(i).optString("diameter_id");

                                        //  pipeList.add(arr.getJSONObject(i).getString("name"));
                                      //  pipeIDList.add(arr.getJSONObject(i).getString("id"));

                                        // AlignmentIDList.add(arr.getJSONObject(i).getString("AlignmentID"));
                                    }
                                }



                            } catch (JSONException ignored) {

                            }
                            //loadingDialog.dismiss();


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
                    params.put("type", "get_pipe_details");
                    params.put("for", "laying");
                    params.put("zone_id", zone_id);
                    //params.put("pipe_number", pipeNumber);
                    params.put("pipe_id", pipeID);
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



    }
   /*public void sheetDMA() {
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
                            dmaList.clear();
                            try {
                                JSONArray arr = new JSONArray(response);
                                if (arr != null) {
                                    for (int i = 0; i < arr.length(); i++) {

                                        dmaList.add(arr.getJSONObject(i).getString("name"));
                                        dmaIDList.add(arr.getJSONObject(i).getString("id"));

                                        // AlignmentIDList.add(arr.getJSONObject(i).getString("AlignmentID"));
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
                    error.getMessage();
                    progressLoading.dismiss();
                    Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("type", "get_dma");
                    params.put("for", "laying");
                   // params.put("zone", zoneName);
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


        spinnerDMA.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.e(" Selected Item position", "position " + i);
                //  bendID = bendModelList.get(i).getId();
                dmaName = spinnerDMA.getSelectedItem().toString();
                dma_id = dmaIDList.get(i).toString();
               // sheetStartNode();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }*/


   /* public void sheetStartNode() {
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
                            startNodeList.clear();
                            startNodeModelList.clear();
                            sheetEndNode();
                            try {
                                JSONArray arr = new JSONArray(response);
                                if (arr != null) {
                                    for (int i = 0; i < arr.length(); i++) {
                                        // bendNo.add(new ItemModel(arr.getJSONObject(i).getString("id"), arr.getJSONObject(i).getString("name")));
                                        startNodeModelList.add(new ItemModel(arr.getJSONObject(i).getString("id"), arr.getJSONObject(i).getString("name")));

                                        startNodeList.add(arr.getJSONObject(i).getString("name"));

                                        // AlignmentIDList.add(arr.getJSONObject(i).getString("AlignmentID"));
                                    }
                                }
                                startNodeAdapter = new ArrayAdapter(context, R.layout.layoutspinner, startNodeList);


                                spinnerStartNode.setAdapter(startNodeAdapter);


                                //productModelList.addAll(pipeNo);
                                startNodeAdapter.notifyDataSetChanged();


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
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("type", "get_from_node");
                    params.put("for", "laying");
                    params.put("zone", zoneName);
                    params.put("dma", dmaName);
                    params.put("start_option", segmentID);

                    params.put("user_id", SessionUtil.getUserId(context));

                    //   type: 'get_from_node', for:'laying', zone:'J01', dma:'A' , user_id: 30

                    return params;
                }
            };

            // Add the request to the RequestQueue.
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            queue.add(stringRequest);
        } else {
            DialogUtil.showNoConnectionDialog(this);
        }


        spinnerStartNode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.e(" Selected Item position", "position " + i);
                //  bendID = bendModelList.get(i).getId();
                startNodeID = startNodeModelList.get(i).getId();
                sheetEndNode();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //  bendID = bendModelList.get(0).getId();
                startNodeID = startNodeModelList.get(0).getId();
                sheetEndNode();

            }
        });
    }*/


   /* public void sheetEndNode() {
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
                            endNodeModelList.clear();
                            endNodeList.clear();
                            Log.e("Response", response);
                            try {
                                JSONArray arr = new JSONArray(response);
                                if (arr != null) {
                                    for (int i = 0; i < arr.length(); i++) {
                                        // bendNo.add(new ItemModel(arr.getJSONObject(i).getString("id"), arr.getJSONObject(i).getString("name")));
                                        endNodeModelList.add(new ItemModel(arr.getJSONObject(i).getString("id"), arr.getJSONObject(i).getString("name")));

                                        endNodeList.add(arr.getJSONObject(i).getString("name"));

                                        // AlignmentIDList.add(arr.getJSONObject(i).getString("AlignmentID"));
                                    }
                                }
                                endNodeAdapter = new ArrayAdapter(context, R.layout.layoutspinner, endNodeList);


                                spinnerEndNode.setAdapter(endNodeAdapter);


                                //productModelList.addAll(pipeNo);
                                endNodeAdapter.notifyDataSetChanged();


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
                    // Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("type", "get_to_node");
                    params.put("from_node_id", startNodeID);
                    params.put("for", "laying");
                    params.put("start_option", segmentID);
                    params.put("user_id", SessionUtil.getUserId(context));

                    //type: 'get_to_node', from_node_id:683 ,for:'laying', user_id: 30
                    return params;
                }
            };

            // Add the request to the RequestQueue.
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            queue.add(stringRequest);
        } else {
            DialogUtil.showNoConnectionDialog(this);
        }


        spinnerEndNode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.e(" Selected Item position", "position " + i);
                //  bendID = bendModelList.get(i).getId();
                endNodeID = endNodeModelList.get(i).getId();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //  bendID = bendModelList.get(0).getId();
                endNodeID = endNodeModelList.get(0).getId();

            }
        });
    }*/


   /* public void sheetType() {
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
                            pipeTypeList.clear();
                            pipeTypeModelList.clear();
                            try {
                                JSONArray arr = new JSONArray(response);
                                if (arr != null) {
                                    for (int i = 0; i < arr.length(); i++) {
                                        // bendNo.add(new ItemModel(arr.getJSONObject(i).getString("id"), arr.getJSONObject(i).getString("name")));
                                        pipeTypeModelList.add(new ItemModel(arr.getJSONObject(i).getString("id"), arr.getJSONObject(i).getString("name")));

                                        pipeTypeList.add(arr.getJSONObject(i).getString("name"));

                                        // AlignmentIDList.add(arr.getJSONObject(i).getString("AlignmentID"));
                                    }
                                }
                                pipeTypeAdapter = new ArrayAdapter(context, R.layout.layoutspinner, pipeTypeList);


                                spinnerType.setAdapter(pipeTypeAdapter);


                                //productModelList.addAll(pipeNo);
                                pipeTypeAdapter.notifyDataSetChanged();


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
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("type", "pipe_type");

                    params.put("user_id", SessionUtil.getUserId(context));


                    return params;
                }
            };

            // Add the request to the RequestQueue.
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            queue.add(stringRequest);
        } else {
            DialogUtil.showNoConnectionDialog(this);
        }


        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.e(" Selected Item position", "position " + i);
                //  bendID = bendModelList.get(i).getId();
                typeID = pipeTypeModelList.get(i).getId();
                sheetSubType();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //  bendID = bendModelList.get(0).getId();
                typeID = pipeTypeModelList.get(0).getId();
                sheetSubType();
            }
        });
    }*/

    public void sheetIntermediate() {
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
                            try {
                                JSONArray arr = new JSONArray(response);
                                if (arr != null) {
                                    for (int i = 0; i < arr.length(); i++) {
                                        // bendNo.add(new ItemModel(arr.getJSONObject(i).getString("id"), arr.getJSONObject(i).getString("name")));
                                        intermediateModelList.add(new ItemModel(arr.getJSONObject(i).getString("id"), arr.getJSONObject(i).getString("name")));

                                        intermediateList.add(arr.getJSONObject(i).getString("name"));

                                        // AlignmentIDList.add(arr.getJSONObject(i).getString("AlignmentID"));
                                    }
                                }
                                intermediateAdapter = new ArrayAdapter(context, R.layout.layoutspinner, intermediateList);


                                spinnerIntermediate.setAdapter(intermediateAdapter);


                                //productModelList.addAll(pipeNo);
                                intermediateAdapter.notifyDataSetChanged();


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
                    params.put("type", "get_intermediate_list");

                    params.put("user_id", SessionUtil.getUserId(context));


                    return params;
                }
            };

            // Add the request to the RequestQueue.
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            queue.add(stringRequest);
        } else {
            DialogUtil.showNoConnectionDialog(this);
        }


        spinnerIntermediate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.e(" Selected Item position", "position " + i);
                //  bendID = bendModelList.get(i).getId();
                intermediateID = intermediateModelList.get(i).getId();
                if (i == 9) {
                    editNextDiameter.setVisibility(View.VISIBLE);
                    editNextLength.setVisibility(View.VISIBLE);
                    tvNextDiameter.setVisibility(View.VISIBLE);
                    tvNextLength.setVisibility(View.VISIBLE);
                } else {
                    editNextDiameter.setVisibility(View.GONE);
                    editNextLength.setVisibility(View.GONE);
                    tvNextLength.setVisibility(View.GONE);
                    tvNextDiameter.setVisibility(View.GONE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

  /*  public void sheetSubType() {
        if (DialogUtil.checkInternetConnection(this)) {
            // loadingDialog.onShow();
            if (!progressLoading.isShowing()) {
                progressLoading.onShow();
            }
            pipeSubTypeModelList.clear();
            pipeSubTypeList.clear();
            RequestQueue queue = Volley.newRequestQueue(this);

            final String url = AppConstants.APP_BASE_URL;
            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("Response", response);


                            try {
                                JSONArray arr = new JSONArray(response);
                                if (arr != null) {
                                    for (int i = 0; i < arr.length(); i++) {
                                        // bendNo.add(new ItemModel(arr.getJSONObject(i).getString("id"), arr.getJSONObject(i).getString("name")));
                                        pipeSubTypeModelList.add(new ItemModel(arr.getJSONObject(i).getString("id"), arr.getJSONObject(i).getString("name")));

                                        pipeSubTypeList.add(arr.getJSONObject(i).getString("name"));

                                        // AlignmentIDList.add(arr.getJSONObject(i).getString("AlignmentID"));
                                    }
                                }
                                pipeSubTypeAdapter = new ArrayAdapter(context, R.layout.layoutspinner, pipeSubTypeList);


                                spinnerSubType.setAdapter(pipeSubTypeAdapter);


                                //productModelList.addAll(pipeNo);
                                pipeSubTypeAdapter.notifyDataSetChanged();


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
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("type", "pipe_subtype");

                    params.put("user_id", SessionUtil.getUserId(context));
                    params.put("type_id", typeID);


                    return params;
                }
            };

            // Add the request to the RequestQueue.
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            queue.add(stringRequest);
        } else {
            DialogUtil.showNoConnectionDialog(this);
        }


        spinnerSubType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.e(" Selected Item position", "position " + i);
                subtypeID = pipeSubTypeModelList.get(i).getId();
                sheetDiameter();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                subtypeID = pipeSubTypeModelList.get(0).getId();
                sheetDiameter();
            }
        });
    }
*/
   /* public void sheetDiameter() {
        if (DialogUtil.checkInternetConnection(this)) {
            // loadingDialog.onShow();
            if (!progressLoading.isShowing()) {
                progressLoading.onShow();
            }
            pipeDiameterList.clear();
            pipeDiameterModelList.clear();
            RequestQueue queue = Volley.newRequestQueue(this);

            final String url = AppConstants.APP_BASE_URL;
            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("Response", response);


                            try {
                                JSONArray arr = new JSONArray(response);
                                if (arr != null) {
                                    for (int i = 0; i < arr.length(); i++) {
                                        // bendNo.add(new ItemModel(arr.getJSONObject(i).getString("id"), arr.getJSONObject(i).getString("name")));
                                        pipeDiameterModelList.add(new ItemModel(arr.getJSONObject(i).getString("id"), arr.getJSONObject(i).getString("name")));

                                        pipeDiameterList.add(arr.getJSONObject(i).getString("name"));

                                        // AlignmentIDList.add(arr.getJSONObject(i).getString("AlignmentID"));
                                    }
                                }
                                pipeDiameterAdapter = new ArrayAdapter(context, R.layout.layoutspinner, pipeDiameterList);


                                spinnerDiameter.setAdapter(pipeDiameterAdapter);


                                //productModelList.addAll(pipeNo);
                                pipeDiameterAdapter.notifyDataSetChanged();


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
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("type", "pipe_diameter");

                    params.put("user_id", SessionUtil.getUserId(context));
                    params.put("type_id", typeID);
                    params.put("subtype_id", subtypeID);


                    return params;
                }
            };

            // Add the request to the RequestQueue.
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            queue.add(stringRequest);
        } else {
            DialogUtil.showNoConnectionDialog(this);
        }


        spinnerDiameter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.e("11111111Diameter Id", "position " + i + " " + pipeDiameterModelList.get(i).getId());
                diameterID = pipeDiameterModelList.get(i).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                diameterID = pipeDiameterModelList.get(0).getId();
            }
        });
    }*/

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
                                    arrayDepth.clear();
                                    arrayNextLongitude.clear();
                                    arrayNextLatitude.clear();
                                    arrayNextAltitude.clear();
                                    arrayNextDiameterList.clear();
                                    arrayNextLengthList.clear();
                                    arrayIntermediateList.clear();
                                    editRemarks.setText("");
                                    editStartAltitude.setText("");
                                    editStartDepth.setText("");
                                    editStartLatitude.setText("");
                                    editStartLongitude.setText("");
                                    editEndAltitude.setText("");
                                    editEndDepth.setText("");
                                    editEndLatitude.setText("");
                                    editEndLongitude.setText("");
                                    editLength.setText("");
                                    spinnerStartNode.setText("");
                                    spinnerEndNode.setText("");
                                    spinnerType.setText("");
                                    spinnerSubType.setText("");
                                    spinnerDiameter.setText("");
                                    typeID="";subtypeID=""; diameterID="";startNodeID=""; endNodeID="";
                                    imageStart = "";
                                    imageEnd ="";
                                    editStartWidth.setText("");
                                    editEndWidth.setText("");
                                    editContractor.setText("");

                                    imgStartPhoto.setImageResource(android.R.color.transparent);
                                    imgEndPhoto.setImageResource(android.R.color.transparent);


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


                        String auth = "Basic "
                                + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                        headers.put("Authorization", auth);
                        return headers;
                    }
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("type", "laying");

                        params.put("user_id", SessionUtil.getUserId(context));
                        params.put("zone_id", zone_id + "");
                        params.put("dma_id", dma_id + "");
                        params.put("type_id", typeID+ "");
                        params.put("subtype_id", subtypeID + "");
                        params.put("start_node_id", startNodeID + "");
                        params.put("end_node_id", endNodeID + "");
                        params.put("diameter_id", diameterID + "");
                        params.put("start_node_lat", editStartLatitude.getText().toString() + "");
                        params.put("start_node_lon", editStartLongitude.getText().toString() + "");
                        params.put("start_node_ht", editStartAltitude.getText().toString() + "");
                        params.put("end_node_lat", editEndLatitude.getText().toString() + "");
                        params.put("end_node_lon", editEndLongitude.getText().toString() + "");
                        params.put("end_node_ht", editEndAltitude.getText().toString() + "");
                        params.put("next_point_lat", arrayNextLatitude.toString() + "");
                        params.put("next_point_lon", arrayNextLongitude.toString() + "");
                        params.put("next_point_ht", arrayNextAltitude.toString() + "");
                        params.put("next_point_depth", arrayDepth.toString() + "");
                        params.put("tee_reducer_diameter", arrayNextDiameterList.toString() + "");
                        params.put("tee_reducer_length", arrayNextLengthList.toString() + "");
                        params.put("start_node_depth", editStartDepth.getText().toString() + "");
                        params.put("end_node_depth", editEndDepth.getText().toString() + "");
                      //  params.put("length", editLength.getText().toString() + "");
                       // params.put("pipe_number", pipeNumber + "");
                        params.put("pipe_id", pipeID + "");
                        params.put("soil_type", spinnerSoil.getSelectedItem().toString() + "");
                        params.put("alignment", spinAlignment.getSelectedItem().toString() + "");

                        params.put("start_option", segmentID + "");
                      //  params.put("contractor", editContractor.getText().toString() + "");

                        params.put("remarks", editRemarks.getText().toString() + "");
                        params.put("laying_date", editDate.getText().toString() + "");
                        //type: 'laying', user_id: 31,  type_id:1, subtype_id:1, start_node_id:1, diameter_id:1, end_node_id:1, length: 10, start_node_lat:16.576554, start_node_lon:71.56645, start_node_depth:17, start_node_ht:12, end_node_lat:28.12345, end_node_lon:77.12345, end_node_ht:15, end_node_depth:11, next_point_lat: '[28.135788,28.14686,28.15798,28.17955]', next_point_lon: '[77.1457567,77.1648754,77.175645,77.1954645]', next_point_ht: '[10,12,45,32]', next_point_depth: '[10,12,42,12]', remarks:'test', laying_date:'11/09/2019',
                        // start_trenching_width: 10, end_trenching_width: 2, intermediate_ids: '[1,1,3,4]', start_photo: 'base64', end_photo: 'base64'
                        params.put("start_trenching_width", editStartWidth.getText().toString() + "");
                        params.put("end_trenching_width", editEndWidth.getText().toString() + "");
                        params.put("intermediate_ids", arrayIntermediateList.toString() + "");
                        params.put("start_photo", imageStart + "");
                        params.put("end_photo", imageEnd + "");

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
            if(spinnerStartNode.getText().toString().trim().equals("")||spinnerStartNode.getText().toString().trim().equals("null"))
            {
                valid = false;
                Toast.makeText(context,"Enter Start Node",Toast.LENGTH_SHORT).show();
            }
        if(editStartLatitude.getText().toString().trim().equals("")||editStartLongitude.getText().toString().trim().equals(""))
        {
            valid = false;
            Toast.makeText(context,"Enter Start Location Latitude and Longitude",Toast.LENGTH_SHORT).show();
        }
       /* if(editStartDepth.getText().toString().trim().equals("")||editStartWidth.getText().toString().trim().equals(""))
        {
            valid = false;
            Toast.makeText(context,"Enter Start Depth and Width",Toast.LENGTH_SHORT).show();
        }*/
            if(spinnerEndNode.getText().toString().trim().equals("")||spinnerEndNode.getText().toString().trim().equals("null"))
            {
                valid = false;
                Toast.makeText(context,"Enter End Node",Toast.LENGTH_SHORT).show();
            }
        if(editEndLatitude.getText().toString().trim().equals("")||editEndLongitude.getText().toString().trim().equals(""))
        {
            valid = false;
            Toast.makeText(context,"Enter End  Location Latitude and Longitude",Toast.LENGTH_SHORT).show();
        }
      /*  if(editEndDepth.getText().toString().trim().equals("")||editEndWidth.getText().toString().trim().equals(""))
        {
            valid = false;
            Toast.makeText(context,"Enter End Depth and Width",Toast.LENGTH_SHORT).show();
        }*/



        return valid;
    }

    @OnClick(R.id.imgStartPhoto)
    public void captureStartImage() {
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Laying3Activity.this,
                    new String[]{Manifest.permission.CAMERA},
                    100);

        } else {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            //  fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

            //  intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

            // start the image capture Intent
            startActivityForResult(intent, START_CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
        }


    }

    @OnClick(R.id.imgEndPhoto)
    public void captureEndImage() {
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Laying3Activity.this,
                    new String[]{Manifest.permission.CAMERA},
                    100);

        } else {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            //  fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

            //  intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

            // start the image capture Intent
            startActivityForResult(intent, END_CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
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
        if (requestCode == START_CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                imgStartPhoto.setImageBitmap(photo);
                imageStart = getStringImage(photo);
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
        if (requestCode == END_CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                imgEndPhoto.setImageBitmap(photo);
                imageEnd = getStringImage(photo);
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
    public void sheetSoil() {
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
                            Log.e("Response aaaaaa", response);
                            soilNameList.clear();

                            try {
                                JSONArray arr = new JSONArray(response);
                                if (arr != null) {
                                    for (int i = 0; i < arr.length(); i++) {

                                        soilNameList.add(arr.getJSONObject(i).getString("name"));
                                         }
                                }
                                soilAdapter = new ArrayAdapter(context, R.layout.layoutspinner, soilNameList);


                                spinnerSoil.setAdapter(soilAdapter);


                                //productModelList.addAll(pipeNo);
                                soilAdapter.notifyDataSetChanged();


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
            })  {
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
                    params.put("type", "get_soil_type");

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
    }
    public void sheetAlignment() {
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
                            alignmentNameList.clear();

                            try {
                                JSONArray arr = new JSONArray(response);
                                if (arr != null) {
                                    for (int i = 0; i < arr.length(); i++) {

                                        alignmentNameList.add(arr.getJSONObject(i).getString("name"));
                                    }
                                }
                                alignmentAdapter = new ArrayAdapter(context, R.layout.layoutspinner, alignmentNameList);


                                spinAlignment.setAdapter(alignmentAdapter);


                                //productModelList.addAll(pipeNo);
                                alignmentAdapter.notifyDataSetChanged();


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
                    params.put("type", "get_alignment");

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
        serviceIntent = new Intent(Laying3Activity.this, LocationService.class);
        startService(serviceIntent);
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter("app.location.via.service"));
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopService(serviceIntent);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Really Exit?")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        Laying3Activity.super.onBackPressed();
                    }
                }).create().show();
    }

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
         //   TextView textView = snackbarView.findViewById(android.support.design.R.id.snackbar_text);
         //   textView.setTextColor(Color.BLUE);

            /// snackbar.show();

            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) snackbarView.getLayoutParams();
            params.gravity = Gravity.TOP;
            snackbarView.setLayoutParams(params);
            // snackbar.show();
        }
    };

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
