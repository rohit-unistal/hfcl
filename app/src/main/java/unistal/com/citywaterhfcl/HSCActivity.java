package unistal.com.citywaterhfcl;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
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

public class HSCActivity extends AppCompatActivity {
    @BindView(R.id.edit_date)
    EditText editDate;
    @BindView(R.id.accuracytv)
    TextView acctv;
    @BindView(R.id.imgback)
    ImageView imgBack;
    @BindView(R.id.imglogout)
    ImageView imgLogout;
    @BindView(R.id.titletv)
    TextView citytv;
    @BindView(R.id.spin_contractor)
    Spinner spinContractor;
    @BindView(R.id.spin_start_node)
    EditText spinnerStartNode;
    @BindView(R.id.spin_zone)
    Spinner spinnerZone;
    @BindView(R.id.spinLengthConn)
    Spinner spinnerLengthConnection;
    @BindView(R.id.spinWaterStorage)
    Spinner spinnerWaterStorage;
    @BindView(R.id.spin_pipe_segment)
    Spinner spinpipesegment;

    @BindView(R.id.spin_subtype)
    Spinner spinnerSubType;
    @BindView(R.id.spin_type)
    Spinner spinnerType;
    @BindView(R.id.spin_diameter)
    Spinner spinnerDiameter;


    @BindView(R.id.spin_dma)
    Spinner spinnerDMA;
    @BindView(R.id.btnlocation)
    Button btnStartNode;
    @BindView(R.id.edlatitude)
    EditText editStartLatitude;

    @BindView(R.id.edlongitude)
    EditText editStartLongitude;
    @BindView(R.id.edMobileNumber)
    EditText edMobileNumber;
    @BindView(R.id.edit_owner)
    EditText edOwner;
    @BindView(R.id.edfamilysize)
    EditText edfamilysize;
    @BindView(R.id.edadharnum)
    EditText edAadharNumber;
    @BindView(R.id.edit_remarks)
    EditText editRemarks;
    @BindView(R.id.spin_end_node)
    EditText spinnerEndNode;
    @BindView(R.id.edsupervisor)
    EditText edsupervisor;
    @BindView(R.id.edcity)
    EditText edcity;
    @BindView(R.id.spinPCCCrossing)
    Spinner spinPCCCrossing;
    @BindView(R.id.btn_savenode)
    Button ButtonSaveNode;
    String strAccuracy;
    ArrayList<ItemModel> contractorList,lengthOfConnectionList,waterStorageList;
    ArrayAdapter<ItemModel> contractorAdapter,lengthOfConnectionAdapter,waterStorageAdapter;
    private String strLantitude, strLongitude, strAltitude,zone_id="",dmaName="",contractorID="",pipeID="", typeID="",subtypeID="", diameterID="", startNodeID="", endNodeID="",zoneName="",waterStorageID="",lengthofConnectionID="";
    Context context;
    Intent serviceIntent;
    ArrayList<PipeNum> PipeNumberDataList;
    ArrayList<PipeModel> PipeTypeDataList;
    ArrayList<String>  zoneList,dmaList;
    ArrayAdapter pipeAdapter,zoneAdapter,dmaAdapter,crossingAdapter;
    ArrayAdapter<PipeModel>pipeTypeAdapter;ArrayAdapter<PipeModel.PipeSubType> pipeSubTypeAdapter;ArrayAdapter<PipeModel.PipeSubType.PipeDium>pipeDiameterAdapter;
    int pipeTypeIndex,pipeSubTypeIndex;
    String[] pccCrossOption = {"No","Yes"};
    Boolean flag = true;
    ProgressLoading progressLoading;
    DataBaseHelper dataBaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hsc);
        ButterKnife.bind(this);
        getSupportActionBar().hide();
        citytv.setText("HSC");

        context = this;
        progressLoading = new ProgressLoading(this);
        imgLogout.setVisibility(View.INVISIBLE);
        init();
    }
    private void init()
    {
        dataBaseHelper = new DataBaseHelper(this);
        zoneList = new ArrayList();
        dmaList = new ArrayList();
        contractorList = new ArrayList<>();
        lengthOfConnectionList = new ArrayList<>();
        waterStorageList = new ArrayList<>();
        PipeNumberDataList = new ArrayList<PipeNum>();
        PipeTypeDataList = new ArrayList<PipeModel>();
        crossingAdapter = new ArrayAdapter(context,R.layout.layoutspinner,pccCrossOption);
        spinPCCCrossing.setAdapter(crossingAdapter);
        editDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRouteSurveyDate();
            }
        });
        getCurrentDate();
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        setContractorData();
        setlengthOfConnectionData();
        setwaterStorageData();
        setPipeType();

        ButtonSaveNode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                sheetLayingDesignData();
                sheetlengthOfConnectionData();
                sheetwaterstorageData();


            }
        });
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

    public void getCurrentDate() {
        Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        editDate.setText( mYear+ "-" + (mMonth + 1) + "-" +mDay );
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
                        editDate.setText( year + "-" + (monthOfYear + 1) + "-" +dayOfMonth);

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
        serviceIntent = new Intent(HSCActivity.this, LocationService.class);
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
            //  TextView textView = snackbarView.findViewById(android.support.design.R.id.snackbar_text);
            //  textView.setTextColor(Color.BLUE);

            /// snackbar.show();

            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) snackbarView.getLayoutParams();
            params.gravity = Gravity.TOP;
            snackbarView.setLayoutParams(params);
            // snackbar.show();
        }
    };
    public void sheetLayingDesignData(){


        if (DialogUtil.checkInternetConnection(this)) {
            // loadingDialog.onShow();
            if (!progressLoading.isShowing()) {
                progressLoading.onShow();
            }

            RequestQueue queue = Volley.newRequestQueue(this);

            final String url = AppConstants.APP_BASE_URL+"getallLayingbyZone?schema="+SessionUtil.getSchema(context)+"&zone="+SessionUtil.getZone(context);//+"&dma="+dmaName;
            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("Response", response);

                            //loadingDialog.dismiss();
                            //  SessionUtil.saveNodes(response,context);
                            //  Toast.makeText(context, "Saved Nodes",Toast.LENGTH_SHORT).show();
                            dataBaseHelper.deleteAll("tbl_laying_design_data");
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if(jsonObject.optString("data").contains("[{")){

                                    JSONArray pipes = new JSONArray( jsonObject.optString("data"));









                                    if(!progressLoading.isShowing())
                                    {
                                        progressLoading.onShow();}


                                    for (int j = 0; j < pipes.length();j++) {
                                        JSONObject jsonfitting = pipes.getJSONObject(j);
                                        ContentValues namelist = new ContentValues();
                                        namelist.put("zone", jsonfitting.getString("zone"));
                                        namelist.put("dma", jsonfitting.getString("dma"));
                                        namelist.put("pipe_number", jsonfitting.optString("pipe_number"));
                                        namelist.put("start_node", jsonfitting.optString("start_node"));
                                        namelist.put("stop_node", jsonfitting.optString("stop_node"));
                                        namelist.put("scope", jsonfitting.optString("scope"));
                                        Log.e("laying_pipe_data",jsonfitting.toString());

                                        dataBaseHelper.insert("tbl_laying_design_data", namelist);


                                        if(j==pipes.length()-1) {
                                            progressLoading.setMessage();
                                            progressLoading.dismiss();
                                            getZone();

                                        }
                                    }}

                                // dataBaseHelper.close();
                            } catch (JSONException jsonException){
                                DialogUtil.showMessageDialog(context,jsonException.toString());
                            }catch (Exception ignored) {
                                DialogUtil.showMessageDialog(context,ignored.toString());
                                Toast.makeText(getApplicationContext(), "Exception Zone" + ignored, Toast.LENGTH_LONG).show();
                            }
                            progressLoading.dismiss();
                            // productModelList.addAll(pipeNo);
                        }
                    }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error == null || error.networkResponse == null) {
                        DialogUtil.showMessageDialog(context,"Internet connection break");

                    }else
                    if(error != null && !error.toString().isEmpty() ){
                        DialogUtil.showMessageDialog(context," Unknown Error from server side" + error.networkResponse.statusCode );


                        if(error.networkResponse.statusCode>200){
                            DialogUtil.showMessageDialog(context,"Error from server side" + error.networkResponse.statusCode );
                        }else{
                            error.getMessage();
                            DialogUtil.showMessageDialog(context,error.getMessage());
                            Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();}
                    }else{
                        DialogUtil.showMessageDialog(context,"Unknown Error from server side");
                    }
                    progressLoading.dismiss();

                }
            }) ;

            // Add the request to the RequestQueue.
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(600000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            queue.add(stringRequest);
        } else {
            DialogUtil.showNoConnectionDialog(this);
        }


    }
    private void setContractorData(){
        final String  contractorData=  SessionUtil.getContractor(context);
        contractorList.clear();
        try {
            JSONObject djob = new JSONObject(contractorData);

            JSONArray darr = djob.optJSONArray("data");
            if (darr != null) {
                Log.e("contractor", darr.toString());
                for (int i = 0; i < darr.length(); i++) {

                    contractorList.add(new ItemModel(darr.getJSONObject(i).getString("id"),darr.getJSONObject(i).getString("company_name")));

                }
            }
            contractorAdapter= new ArrayAdapter(context,  R.layout.layoutspinner,  contractorList);


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
    public void getZone(){
        zoneList.clear();

        Cursor cur = dataBaseHelper.getAll("SELECT DISTINCT zone" +
                " FROM tbl_laying_design_data");
        if (cur.getCount() > 0) {
            if (cur.moveToFirst()) {
                do {


                    zoneList.add(cur.getString(0));
                } while (cur.moveToNext());
            }
        } else {
            Toast.makeText(HSCActivity.this, "No Zone found or Please update  details", Toast.LENGTH_SHORT).show();
        }

        zoneAdapter = new ArrayAdapter(context, R.layout.layoutspinner, zoneList); //selected item will look like a spinner set from XML
        zoneAdapter.setDropDownViewResource(R.layout.layoutspinner);
        spinnerZone.setAdapter(zoneAdapter);
        zoneAdapter.notifyDataSetChanged();
        spinnerZone.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                zone_id = zoneList.get(i);
                getDMA();


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



    }
    public void getDMA(){
        dmaList.clear();

        Cursor cur = dataBaseHelper.getAll("SELECT DISTINCT dma" +
                " FROM tbl_laying_design_data where zone = '"+zone_id+"';");
        if (cur.getCount() > 0) {
            if (cur.moveToFirst()) {
                do {


                    dmaList.add(cur.getString(0));
                } while (cur.moveToNext());
            }
        } else {
            Toast.makeText(HSCActivity.this, "No DMA found or Please update details", Toast.LENGTH_SHORT).show();
        }

        dmaAdapter = new ArrayAdapter(context, R.layout.layoutspinner, dmaList); //selected item will look like a spinner set from XML
        dmaAdapter.setDropDownViewResource(R.layout.layoutspinner);
        spinnerDMA.setAdapter(dmaAdapter);
        dmaAdapter.notifyDataSetChanged();
        spinnerDMA.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                dmaName= dmaList.get(i);
                setPipeNumber();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



    }

    private void setPipeNumber(){
        // String  pipeNumberData=  SessionUtil.getNodes(context);
        PipeNumberDataList.clear();
        Cursor cur = dataBaseHelper.getAll("SELECT DISTINCT pipe_number,start_node,stop_node,scope" +
                " FROM tbl_laying_design_data where zone = '"+zone_id+"'and dma ='"+dmaName+ "';");
        if (cur.getCount() > 0) {
            if (cur.moveToFirst()) {
                do {

                    PipeNumberDataList.add(new PipeNum(cur.getString(0),
                            new PipeNum.PipeData(cur.getString(1),
                                    cur.getString(2),
                                    cur.getString(3),
                                    "",
                                    "",
                                    "")));

                }while (cur.moveToNext());
            }}
        pipeAdapter= new ArrayAdapter(context,  R.layout.layoutspinner, PipeNumberDataList);


        spinpipesegment.setAdapter(pipeAdapter);

        pipeAdapter.notifyDataSetChanged();



        spinpipesegment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spinnerStartNode.setText(PipeNumberDataList.get(i).getPipeData().getStartNode());
                spinnerEndNode.setText(PipeNumberDataList.get(i).getPipeData().getStopNode());
                pipeID = PipeNumberDataList.get(i).getPipeNumber();
                //spinnerType.setText(PipeNumberDataList.get(i).getPipeData().getPipeType());
                //spinnerSubType.setText(PipeNumberDataList.get(i).getPipeData().getSubType());
                //spinnerDiameter.setText(PipeNumberDataList.get(i).getPipeData().getPipeDiameter());
                //   Toast.makeText(context,PipeNumberDataList.get(i).getPipeNumber()+ " Selected", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

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
    private void setlengthOfConnectionData(){

         String  lengthOfConnectionData=  SessionUtil.getlengthOfConnection(context);
        Log.e("lengthOfConnection", lengthOfConnectionData);
        lengthOfConnectionList.clear();
        try {
            JSONObject djob = new JSONObject(lengthOfConnectionData);

            JSONArray darr = djob.optJSONArray("data");
            if (darr != null) {
                Log.e("lengthOfConnection", darr.toString());
                for (int i = 0; i < darr.length(); i++) {

                    lengthOfConnectionList.add(new ItemModel(darr.getJSONObject(i).getString("id"),darr.getJSONObject(i).getString("connection_name")));

                }
            }
            lengthOfConnectionAdapter= new ArrayAdapter(context,  R.layout.layoutspinner,  lengthOfConnectionList);


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
    private void setwaterStorageData(){
         String  waterStorageData=  SessionUtil.getwaterStorage(context);
        Log.e("waterStorage", waterStorageData);

        waterStorageList.clear();
        try {
            JSONObject djob = new JSONObject(waterStorageData);

            JSONArray darr = djob.optJSONArray("data");
            if (darr != null) {
                Log.e("waterStorage", darr.toString());
                for (int i = 0; i < darr.length(); i++) {

                    waterStorageList.add(new ItemModel(darr.getJSONObject(i).getString("id"),darr.getJSONObject(i).getString("storage_type")));

                }
            }
            waterStorageAdapter= new ArrayAdapter(context,  R.layout.layoutspinner,  waterStorageList);


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
    @OnClick(R.id.btnSubmit)
    public void sheetSubmit() {
        if(validate())
        {
        if (DialogUtil.checkInternetConnection(this)) {
            // loadingDialog.onShow();
            if (!progressLoading.isShowing()) {
                progressLoading.onShow();
            }
            if (flag) {
                flag = false;

                RequestQueue queue = Volley.newRequestQueue(this);

                final String url = AppConstants.APP_BASE_URL+"insertHSC";
                // Request a string response from the provided URL.
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new com.android.volley.Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                progressLoading.dismiss();
                                Log.e("response", response);
                                //  Toast.makeText(context, response, Toast.LENGTH_LONG).show();
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    String status = jsonObject.getString("success");
                                    String message = jsonObject.getString("data");

                                    if (status.contains("success")) {
                                        //Toast.makeText(context, "Successfully submitted", Toast.LENGTH_LONG).show();
                                        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                                        clear();



                                    } else {
                                        Toast.makeText(context, " status: " + status + " message: " + message, Toast.LENGTH_SHORT).show();
                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                flag = true;
                                // productModelList.addAll(pipeNo);
                            }
                        },  new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error == null || error.networkResponse == null) {
                            DialogUtil.showMessageDialog(context,"Internet connection break");

                        }else
                        if(error != null && !error.toString().isEmpty() ){
                            DialogUtil.showMessageDialog(context," Unknown Error from server side" + error.networkResponse.statusCode );


                            if(error.networkResponse.statusCode>200){
                                DialogUtil.showMessageDialog(context,"Error from server side" + error.networkResponse.statusCode );
                            }else{
                                error.getMessage();
                                DialogUtil.showMessageDialog(context,error.getMessage());
                                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();}
                        }else{
                            DialogUtil.showMessageDialog(context,"Unknown Error from server side");
                        }
                        progressLoading.dismiss();

                        flag = true;
                        Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();

                        params.put("schema", SessionUtil.getSchema(context));
                        params.put("user_id", SessionUtil.getUserId(context));


                        params.put("zone", zone_id + "");
                        params.put("dma", dmaName + "");
                        params.put("pipe_number", pipeID+ "");
                        params.put("start_node", spinnerStartNode.getText().toString() + "");
                        params.put("end_node", spinnerEndNode.getText().toString() + "");
                        params.put("owner_name", edOwner.getText().toString() + "");
                        params.put("family_size", edfamilysize.getText().toString() + "");
                        params.put("mobile_no", edMobileNumber.getText().toString()+ "");
                        params.put("adhaar_no", edAadharNumber.getText().toString() + "");
                        params.put("city_village", edcity.getText().toString()+ "");
                        params.put("connection_length", lengthofConnectionID + "");
                        params.put("completion_date", editDate.getText().toString() + "");
                        params.put("water_storage", waterStorageID + "");
                        params.put("pcc_road_grossing",spinPCCCrossing.getSelectedItem().toString() + "");
                        params.put("loc_latitude", editStartLatitude.getText().toString() + "");
                        params.put("loc_longitude", editStartLongitude.getText().toString() + "");
                        params.put("contractor", contractorID + "");
                        params.put("contract_name", edsupervisor.getText().toString() + "");
                        params.put("remark", editRemarks.getText().toString() + "");

                        Log.e("params",params.toString());
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
        }
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
        if(edOwner.getText().toString().trim().equals(""))
        {
            valid = false;
            Toast.makeText(context,"Enter owner name",Toast.LENGTH_SHORT).show();
        }
        if(edcity.getText().toString().trim().equals(""))
        {
            valid = false;
            Toast.makeText(context,"Enter City or Village name",Toast.LENGTH_SHORT).show();
        }
        if(spinnerEndNode.getText().toString().trim().equals("")||spinnerEndNode.getText().toString().trim().equals("null"))
        {
            valid = false;
            Toast.makeText(context,"Enter End Node",Toast.LENGTH_SHORT).show();
        }
        if((edMobileNumber.getText().toString().trim().equals(""))||( edMobileNumber.getText().toString().trim().length()!=10))
        {
            valid = false;
            Toast.makeText(context,"Enter Valid Mobile No",Toast.LENGTH_SHORT).show();
        }
        if(edAadharNumber.getText().toString().trim().equals("")||( edAadharNumber.getText().toString().trim().length()!=12))
        {
            valid = false;
            Toast.makeText(context,"Enter Valid Aadhar Number",Toast.LENGTH_SHORT).show();
        }
        if(editRemarks.getText().toString().trim().equals(""))
        {
            valid = false;
            Toast.makeText(context,"Enter Remarks",Toast.LENGTH_SHORT).show();
        }



        return valid;
    }
    private void setPipeType(){
        String  pipeTypeData=  SessionUtil.getPipeDetail(context);

        PipeTypeDataList.clear();
        try {
            JSONObject djob = new JSONObject(pipeTypeData);

            JSONArray darr = djob.optJSONArray("data");
            if (darr != null) {
                Log.e("pipe Type", darr.toString());
                for (int i = 0; i < darr.length(); i++) {
                    JSONArray sarr = darr.getJSONObject(i).getJSONArray("pipe_sub_type");
                    ArrayList<PipeModel.PipeSubType> pipeSubTypeList = new ArrayList<>();
                    for (int j = 0; j< sarr.length(); j++) {
                        JSONArray karr = sarr.getJSONObject(j).getJSONArray("pipe_dia");
                        ArrayList<PipeModel.PipeSubType.PipeDium> pipeDiumList = new ArrayList<>();
                        for(int k = 0;k <karr.length();k++){
                            //  Toast.makeText(context,karr.getJSONObject(k).getString("name"), Toast.LENGTH_LONG).show();

                            pipeDiumList.add( new PipeModel.PipeSubType.PipeDium(karr.getJSONObject(k).getString("id"), karr.getJSONObject(k).getString("name")));
                        }
                        //  Toast.makeText(context,sarr.getJSONObject(j).getString("name"), Toast.LENGTH_LONG).show();
                        pipeSubTypeList.add( new PipeModel.PipeSubType(sarr.getJSONObject(j).getString("id"),sarr.getJSONObject(j).getString("name"),pipeDiumList));
                        //   pipeDiumList.clear();
                    }
                    PipeTypeDataList.add(new PipeModel(darr.getJSONObject(i).getString("type_id"),darr.getJSONObject(i).getString("pipe_type"),
                            pipeSubTypeList));
                    //  Toast.makeText(context,pipeSubTypeList.toString(), Toast.LENGTH_LONG).show();

                    // pipeSubTypeList.clear();



                }
            }
            pipeTypeAdapter= new ArrayAdapter(context,  R.layout.layoutspinner, PipeTypeDataList);
            Log.e("pipe Type Adapter", "Adapter");

            spinnerType.setAdapter(pipeTypeAdapter);
          /*  for(int l = 0; l<PipeTypeDataList.size(); l++){
                Log.e("pipe Type List Name", PipeTypeDataList.get(l).getPipeType());
                Log.e("pipe Type Name"+pipetype+":", PipeTypeDataList.get(l).getPipeType());

                if(PipeTypeDataList.get(l).getPipeType().trim().equals(pipetype.trim())){
                    Log.e("pipe Type Name"+pipetype+":", PipeTypeDataList.get(l).getPipeType());
                    //  if(spinnerType.getItemAtPosition(l).toString().equals(pipetype)){


                    spinnerType.setSelection(l);
                    break;
                }
            }*/
            pipeTypeAdapter.notifyDataSetChanged();


        } catch (JSONException ignored) {

        }
        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                pipeTypeIndex = i;
                typeID = PipeTypeDataList.get(i).getTypeId();
                pipeSubTypeAdapter = new ArrayAdapter(context,R.layout.layoutspinner, PipeTypeDataList.get(i).getPipeSubType());
                spinnerSubType.setAdapter(pipeSubTypeAdapter);
              /*  for(int j = 0; j<PipeTypeDataList.get(pipeTypeIndex).getPipeSubType().size(); j++){
                    if(PipeTypeDataList.get(pipeTypeIndex).getPipeSubType().get(j).getName().equals(subtype)){
                        spinnerSubType.setSelection(j);
                    }
                }*/
                pipeSubTypeAdapter.notifyDataSetChanged();
                //  Toast.makeText(context,PipeTypeDataList.get(i).getPipeSubType()+ " Selected", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinnerSubType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                pipeSubTypeIndex = i;
                subtypeID = PipeTypeDataList.get(pipeTypeIndex).getPipeSubType().get(i).getId();
                pipeDiameterAdapter = new ArrayAdapter(context,R.layout.layoutspinner, PipeTypeDataList.get(pipeTypeIndex).getPipeSubType().get(i).getPipeDia());
                spinnerDiameter.setAdapter(pipeDiameterAdapter);
             /*   for(int k = 0; k<PipeTypeDataList.get(pipeTypeIndex).getPipeSubType().get(pipeSubTypeIndex).getPipeDia().size(); k++){
                    if(PipeTypeDataList.get(pipeTypeIndex).getPipeSubType().get(pipeSubTypeIndex).getPipeDia().get(k).equals(diameter)){
                        spinnerDiameter.setSelection(k);
                    }
                }*/
                pipeDiameterAdapter.notifyDataSetChanged();

                //  Toast.makeText(context,PipeTypeDataList.get(pipeTypeIndex).getPipeSubType().get(i).getName()+ " Selected", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinnerDiameter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                diameterID = PipeTypeDataList.get(pipeTypeIndex).getPipeSubType().get(pipeSubTypeIndex).getPipeDia().get(i).getId();

                // Toast.makeText(context,PipeTypeDataList.get(pipeTypeIndex).getPipeSubType().get(pipeSubTypeIndex).getPipeDia()+ " Selected", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }



    private void clear()
    {

        editRemarks.setText("");
        edAadharNumber.setText("");
        edMobileNumber.setText("");
        edOwner.setText("");
        editStartLatitude.setText("");
        editStartLongitude.setText("");

    }
}