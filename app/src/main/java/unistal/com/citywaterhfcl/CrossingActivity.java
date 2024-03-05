package unistal.com.citywaterhfcl;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.net.ConnectivityManager;
//import android.support.design.widget.Snackbar;
/*import android.support.v4.app.ActivityCompat;
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

public class CrossingActivity extends AppCompatActivity implements NetworkStateReceiver.NetworkStateReceiverListener{
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
    @BindView(R.id.editlength)
    EditText editLength;

    @BindView(R.id.edit_permission_authority)
    EditText editPermissionAuthority;
    @BindView(R.id.editowner)
    EditText editOwner;
    @BindView(R.id.edit_remarks)
    EditText editRemarks;

    @BindView(R.id.titletv)
    TextView citytv;
    @BindView(R.id.netstatus)
    ImageView netStatus;

    @BindView(R.id.accuracytv)
    TextView acctv;

    @BindView(R.id.spin_crossing_class)
    Spinner spinnerCrossingClass;
    @BindView(R.id.spin_crossing_type)
    Spinner spinnerCrossingType;
    @BindView(R.id.spin_crossing_cassing)
    Spinner spinnerCrossingCassing;
    @BindView(R.id.spin_crossing_position)
    Spinner spinnerCrossingPosition;
    @BindView(R.id.spin_positionpipeline)
    Spinner spinnerPositionPipeline;




    private NetworkStateReceiver networkStateReceiver;
    ProgressLoading progressLoading;
    Context context;
    String username ="basicAuth";
    String password = "ranchi@2021";
    String credentials =new String(username + ":" + password);
    String strAccuracy;
    private String strLantitude, strLongitude, strAltitude,dma_id="1";
    private Intent serviceIntent;
    Boolean flag = true;
    ArrayList crossingclassList,  crossingclassIDList,crossingtypeList, crossingtypeIDList,crossingpositionList, crossingpositionIDList,crossingcassingList, crossingcassingIDList,positionPipelineList,positionPipelineIDList;
    ArrayAdapter crossingclassAdapter,crossingtypeAdapter,crossingpositionAdapter,crossingcassingAdapter,positionpipelineAdapter;
    String crossingclassID,crossingtypeID,crossingpositionID,crossingcassingID,positionpipeID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crossing);
        ButterKnife.bind(this);
        getSupportActionBar().hide();
        citytv.setText("Crossing");
        Boolean flag = true;
        context = this;
        progressLoading = new ProgressLoading(this);
        init();
    }
    private  void init(){
        netConnectivity();
        crossingclassIDList = new ArrayList();
        crossingclassList = new ArrayList();
        crossingtypeList = new ArrayList();
        crossingtypeIDList = new ArrayList();
        crossingpositionIDList = new ArrayList();
        crossingpositionList = new ArrayList();
        crossingcassingList = new ArrayList();
        crossingcassingIDList = new ArrayList();
        positionPipelineIDList = new ArrayList();
        positionPipelineList = new ArrayList();
        sheetCrossingClass();

        sheetCrossingCassing();
        sheetCrossingPosition();
        sheetPositionPipeline();
       // sheetSurface();
       // statusRestoration();
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(CrossingActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
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
    public void sheetCrossingClass() {
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
                            crossingclassList.clear();
                            crossingclassIDList.clear();
                            try {
                                JSONArray arr = new JSONArray(response);
                                if (arr != null) {
                                    for (int i = 0; i < arr.length(); i++) {

                                        crossingclassList.add(arr.getJSONObject(i).getString("name"));
                                        crossingclassIDList.add(arr.getJSONObject(i).getString("id"));

                                        // AlignmentIDList.add(arr.getJSONObject(i).getString("AlignmentID"));
                                    }
                                }
                                crossingclassAdapter = new ArrayAdapter(context, R.layout.layoutspinner, crossingclassList);


                                spinnerCrossingClass.setAdapter(crossingclassAdapter);


                                //productModelList.addAll(pipeNo);
                                crossingclassAdapter.notifyDataSetChanged();


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
                    params.put("type", "crossing_class");

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


        spinnerCrossingClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.e(" Selected Item position", "position " + i);
                //  bendID = bendModelList.get(i).getId();
                sheetCrossingType();
                crossingclassID = crossingclassIDList.get(i).toString();
                // sheetStartNode();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    public void sheetCrossingType() {
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
                            crossingtypeList.clear();
                            crossingtypeIDList.clear();
                            try {
                                JSONArray arr = new JSONArray(response);
                                if (arr != null) {
                                    for (int i = 0; i < arr.length(); i++) {

                                        crossingtypeList.add(arr.getJSONObject(i).getString("name"));
                                        crossingtypeIDList.add(arr.getJSONObject(i).getString("id"));

                                        // AlignmentIDList.add(arr.getJSONObject(i).getString("AlignmentID"));
                                    }
                                }
                                crossingtypeAdapter = new ArrayAdapter(context, R.layout.layoutspinner, crossingtypeList);


                                spinnerCrossingType.setAdapter(crossingtypeAdapter);


                                //productModelList.addAll(pipeNo);
                                crossingtypeAdapter.notifyDataSetChanged();


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
                    params.put("type", "crossing_type");

                    params.put("user_id", SessionUtil.getUserId(context));
                    params.put("class_id", crossingclassID);
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


        spinnerCrossingType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.e(" Selected Item position", "position " + i);
                //  bendID = bendModelList.get(i).getId();

                crossingtypeID = crossingtypeIDList.get(i).toString();
                // sheetStartNode();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    public void sheetCrossingCassing() {
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
                            crossingcassingList.clear();
                            crossingcassingIDList.clear();
                            try {
                                JSONArray arr = new JSONArray(response);
                                if (arr != null) {
                                    for (int i = 0; i < arr.length(); i++) {

                                        crossingcassingList.add(arr.getJSONObject(i).getString("name"));
                                        crossingcassingIDList.add(arr.getJSONObject(i).getString("id"));

                                        // AlignmentIDList.add(arr.getJSONObject(i).getString("AlignmentID"));
                                    }
                                }
                                crossingcassingAdapter = new ArrayAdapter(context, R.layout.layoutspinner, crossingcassingList);


                                spinnerCrossingCassing.setAdapter(crossingcassingAdapter);


                                //productModelList.addAll(pipeNo);
                                crossingcassingAdapter.notifyDataSetChanged();


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
                    params.put("type", "crossing_cassing");

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


        spinnerCrossingCassing.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.e(" Selected Item position", "position " + i);
                //  bendID = bendModelList.get(i).getId();

                crossingcassingID = crossingcassingIDList.get(i).toString();
                // sheetStartNode();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    public void sheetCrossingPosition() {
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
                            crossingpositionList.clear();
                            crossingpositionIDList.clear();
                            try {
                                JSONArray arr = new JSONArray(response);
                                if (arr != null) {
                                    for (int i = 0; i < arr.length(); i++) {

                                        crossingpositionList.add(arr.getJSONObject(i).getString("name"));
                                        crossingpositionIDList.add(arr.getJSONObject(i).getString("id"));

                                        // AlignmentIDList.add(arr.getJSONObject(i).getString("AlignmentID"));
                                    }
                                }
                                crossingpositionAdapter = new ArrayAdapter(context, R.layout.layoutspinner, crossingpositionList);


                                spinnerCrossingPosition.setAdapter(crossingpositionAdapter);


                                //productModelList.addAll(pipeNo);
                                crossingpositionAdapter.notifyDataSetChanged();


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
                    params.put("type", "crossing_position");

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


        spinnerCrossingPosition.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.e(" Selected Item position", "position " + i);
                //  bendID = bendModelList.get(i).getId();

                crossingpositionID = crossingpositionIDList.get(i).toString();
                // sheetStartNode();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    public void sheetPositionPipeline() {
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
                            positionPipelineList.clear();
                            positionPipelineIDList.clear();
                            try {
                                JSONArray arr = new JSONArray(response);
                                if (arr != null) {
                                    for (int i = 0; i < arr.length(); i++) {

                                        positionPipelineList.add(arr.getJSONObject(i).getString("name"));
                                        positionPipelineIDList.add(arr.getJSONObject(i).getString("id"));

                                        // AlignmentIDList.add(arr.getJSONObject(i).getString("AlignmentID"));
                                    }
                                }
                                positionpipelineAdapter = new ArrayAdapter(context, R.layout.layoutspinner, positionPipelineList);


                                spinnerPositionPipeline.setAdapter(positionpipelineAdapter);


                                //productModelList.addAll(pipeNo);
                                positionpipelineAdapter.notifyDataSetChanged();


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
                    params.put("type", "position_of_pipeline");

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


        spinnerPositionPipeline.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.e(" Selected Item position", "position " + i);
                //  bendID = bendModelList.get(i).getId();

                positionpipeID = positionPipelineIDList.get(i).toString();
                // sheetStartNode();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
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
                                            editLength.setText("");
                                            editPermissionAuthority.setText("");

                                            editStartLatitude.setText("");
                                            editStartLongitude.setText("");
                                            editEndLatitude.setText("");
                                            editEndLongitude.setText("");
                                            editOwner.setText("");
                                            editRemarks.setText("");


                                            //  spinnerStartNode.setText("");
                                            //  spinnerEndNode.setText("");
                                            // startNodeID=""; endNodeID="";
                                            //image = "";


                                           // imgView.setImageResource(android.R.color.transparent);

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
                            params.put("type", "save_crossing_details");

                            params.put("user_id", SessionUtil.getUserId(context));
                            params.put("latitude_start", editStartLatitude.getText().toString().trim()+ "");

                            params.put("longitude_start", editStartLongitude.getText().toString().trim()+ "");
                            params.put("latitude_end", editEndLatitude.getText().toString().trim() + "");
                            params.put("longitude_end", editEndLongitude.getText().toString().trim()+ "");
                            params.put("entered_length_crossing", editLength.getText().toString().trim() + "");

                            params.put("permission_authority", editPermissionAuthority.getText().toString() + "");
                            params.put("crossing_class", crossingclassID + "");
                            params.put("crossing_type", crossingtypeID + "");

                            params.put("crossing_position", crossingpositionID + "");
                            params.put("crossing_cassing", crossingcassingID + "");
                            params.put("position_of_pipeline", positionpipeID + "");
                            params.put("owner_crossing", editOwner.getText().toString().trim()+ "");
                            params.put("crossing_remarks", editRemarks.getText().toString()+ "");



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