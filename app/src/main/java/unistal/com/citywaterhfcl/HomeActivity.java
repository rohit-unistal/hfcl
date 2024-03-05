package unistal.com.citywaterhfcl;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
/*
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
*/
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeActivity extends AppCompatActivity implements NetworkStateReceiver.NetworkStateReceiverListener {
    private RightSideMenuHome rightSideMenu;


    @BindView(R.id.btnwifi)
    ImageView WifiButton;
    @BindView(R.id.btnMobile)
    ImageView MobileButton;

    @BindView(R.id.imglogout)
    ImageView imgLogout;
    @BindView(R.id.imgback)
    ImageView imgBack;

    @BindView(R.id.titletv)
    TextView citytv;

    @BindView(R.id.txtAboveGround)
    TextView tvag;

   /* @BindView(R.id.tv1)
    TextView tv1;

    @BindView(R.id.img1)
    ImageView img1;*/
   @BindView(R.id.spin_network)
   Spinner spinnerNetwork;
    @BindView(R.id.spin_zone)
    Spinner spinnerZone;
    @BindView(R.id.spin_dma)
    Spinner spinnerDMA;
    @BindView(R.id.btn_savenode)
    Button ButtonSaveNode;
    private NetworkStateReceiver networkStateReceiver;

    ArrayList zoneList, dmaList,networkList;
    ProgressLoading progressLoading;
    ArrayAdapter zoneAdapter, dmaAdapter,networkAdapter;
    String zoneName, dmaName,networkName;
    Context context;
    DataBaseHelper dataBaseHelper;
    Boolean downloadPipe = true, downloadAlignment = true, downloadSoil = true, downloadFitting = true, downloadContractor = true, downloadDesign = true, downloadCrossing = true, downloadIntermediate = true;
    Boolean statusPipe = true, statusAlignment = true, statusSoil = true, statusFitting = true, statusContractor = true, statusDesign = true, statusCrossing = true, statusIntermediate = true,statusClickable = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        context = this;
        ButterKnife.bind(this);
        netConnectivity();
        //   img1.setImageResource(R.drawable.logo);
        progressLoading = new ProgressLoading(this);
        zoneList = new ArrayList();
        dmaList = new ArrayList();networkList = new ArrayList();
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().hide();
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //    getSupportActionBar().setDisplayUseLogoEnabled(false);
        imgBack.setVisibility(View.INVISIBLE);
        citytv.setText((SessionUtil.getSchema(context)));
        //   getSupportActionBar().setIcon(R.mipmap.ic_launcher);
        dataBaseHelper = new DataBaseHelper(context);
        ImageView imgLogout = (ImageView) findViewById(R.id.imgLogout);

        RelativeLayout relAboveGround = (RelativeLayout) findViewById(R.id.relAboveGround);
        RelativeLayout relFitting = (RelativeLayout) findViewById(R.id.relFitting);
        RelativeLayout relSurface = (RelativeLayout) findViewById(R.id.relSurface);
        RelativeLayout relCrossing = (RelativeLayout) findViewById(R.id.relCrossing);
        RelativeLayout relStartGap = (RelativeLayout) findViewById(R.id.relBelowGround);
        RelativeLayout relRecords = (RelativeLayout) findViewById(R.id.relRecords);
        RelativeLayout relHSC = (RelativeLayout) findViewById(R.id.relHSC);

        //  relStartGap.setVisibility(View.GONE);
        relHSC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, HSC2Activity.class));
            }
        });
        relRecords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, RecordListActivity.class));
            }
        });
        relStartGap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, CreateGapActivity.class));
            }
        });
        RelativeLayout relRemoveGap = (RelativeLayout) findViewById(R.id.relRemoveGap);
        relRemoveGap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, RemoveGap.class));
            }
        });
        //relRemoveGap.setVisibility(View.GONE);
        // relStartGap.setVisibility(View.GONE);
        relAboveGround.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, LayingOfflinephotoActivity.class));
              //  startActivity(new Intent(HomeActivity.this, LayingManualActivity.class));
            }
        });
        relFitting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, FittingActivity.class));
            }
        });
        relSurface.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, HindranceActivity.class));
            }
        });
        relCrossing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, Crossing2Activity.class));
            }
        });
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(HomeActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    100);
            File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), "MyLaying");

            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    Log.d("App", "failed to create directory");
                }
            }
        }
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(HomeActivity.this,
                    new String[]{Manifest.permission.CAMERA},
                    100);

        }
        sheetNetwork();
       // sheetZone();


        ButtonSaveNode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (statusClickable){
                if (DialogUtil.checkInternetConnection(context)) {
                    // loadingDialog.onShow();
                    if (zoneName != null && !zoneName.trim().isEmpty()) {
                        if (downloadPipe && downloadAlignment && downloadSoil && downloadFitting && downloadContractor && downloadDesign && downloadIntermediate && downloadCrossing) {
                            if (!progressLoading.isShowing()) {
                                progressLoading.onShow();
                            }
//                SessionUtil.saveDMA(spinnerDMA.getSelectedItem().toString(),context);

                            sheetPipeData();
                            sheetAlignmentData();
                            sheetSoilData();
                            sheetFittingData();
                            sheetContractorData();
                            sheetDesignData();
                            //  sheetLayingDesignData();
                            setInterMediatePoint();
                            sheetCrossingTypeData();
                            statusClickable = false;
                            new CountDownTimer(120000, 10) { //Set Timer for
                                public void onTick(long millisUntilFinished) {

                                }

                                @Override
                                public void onFinish() {
                                    statusClickable = true ;
                                }
                            }.start();
                        }else{
                            Toast.makeText(context,"Cant Sync - Click after some time", Toast.LENGTH_LONG).show();
                            DialogUtil.showMessageDialog(context, "Cant Sync - Click after some time");
                        }
                    } else {
                        DialogUtil.showMessageDialog(context, "Select Zone");
                    }
                } else {
                    DialogUtil.showNoConnectionDialog(context);
                }}else{
                    Toast.makeText(context,"Cant Sync - Click after some time", Toast.LENGTH_LONG).show();
                }
            }
        });

        // registerReceiver();
    }

    /**
     * This method is responsible to register receiver with NETWORK_CHANGE_ACTION.
     */

    @OnClick(R.id.imglogout)
    void txtMenu() {
        rightSideMenu();
    }

    private void rightSideMenu() {
        if (rightSideMenu != null) {
            rightSideMenu.dismiss();
        }
        rightSideMenu = new RightSideMenuHome();
        rightSideMenu.show(getFragmentManager(), "");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem item = menu.add("Log Out");
        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                logOut();
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void logOut() {
        SessionUtil.removeUserDetails(this);
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
        finish();
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


        spinnerZone.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.e(" Selected Item position", "position " + i);
                //  bendID = bendModelList.get(i).getId();
                zoneName = spinnerZone.getSelectedItem().toString();
                 //   ButtonSaveNode.setClickable(true);
                //  sheetDMA();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
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
              //  ButtonSaveNode.setClickable(true);
                //  sheetDMA();

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

                    }else if (error != null && !error.toString().isEmpty()) {
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
            //  DialogUtil.showNoConnectionDialog(this);
        }


        spinnerDMA.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.e(" Selected Item position", "position " + i);
                //  bendID = bendModelList.get(i).getId();
                dmaName = spinnerDMA.getSelectedItem().toString();


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    /*   public void sheetDesignData(){
           if (DialogUtil.checkInternetConnection(this)) {
               // loadingDialog.onShow();
               if (!progressLoading.isShowing()) {
                   progressLoading.onShow();
               }

               RequestQueue queue = Volley.newRequestQueue(this);

               final String url = AppConstants.APP_BASE_URL+"getallDesign?schema="+SessionUtil.getSchema(context)+"&zone="+zoneName+"&dma="+dmaName;
               // Request a string response from the provided URL.
               StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                       new com.android.volley.Response.Listener<String>() {
                           @Override
                           public void onResponse(String response) {
                               Log.e("Response", response);

                               //loadingDialog.dismiss();
                               SessionUtil.saveNodes(response,context);
                               Toast.makeText(context, "Saved Nodes",Toast.LENGTH_SHORT).show();
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
               }) ;

               // Add the request to the RequestQueue.
               stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

               queue.add(stringRequest);
           } else {
               DialogUtil.showNoConnectionDialog(this);
           }

       }*/
    public void sheetDesignData() {
        SessionUtil.saveNetwork(networkName, context);
        SessionUtil.saveZone(zoneName, context);
        if (DialogUtil.checkInternetConnection(this)) {
            // loadingDialog.onShow();
            if (!progressLoading.isShowing()) {
                progressLoading.onShow();

            }
                downloadDesign =false;
            statusDesign =false;
            RequestQueue queue = Volley.newRequestQueue(this);

            final String url = AppConstants.APP_BASE_URL + "getallDesign?schema=" + SessionUtil.getSchema(context) + "&zone=" + zoneName+"&network_type="+networkName;
            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("Response", response);

                            //loadingDialog.dismiss();
                            //  SessionUtil.saveNodes(response,context);
                            //  Toast.makeText(context, "Saved Nodes",Toast.LENGTH_SHORT).show();
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                JSONArray pipes = new JSONArray(jsonObject.optString("data"));


                                dataBaseHelper.deleteAll("tbl_design_data");


                             /*   if (!progressLoading.isShowing()) {
                                    progressLoading.onShow();
                                }*/


                                for (int j = 0; j < pipes.length(); j++) {
                                    JSONObject jsonfitting = pipes.getJSONObject(j);
                                    ContentValues namelist = new ContentValues();
                                    namelist.put("zone", jsonfitting.getString("zone"));
                                    namelist.put("dma", jsonfitting.getString("dma"));
                                    namelist.put("scheme_ext", jsonfitting.optString("scheme_ext"));
                                    namelist.put("pipe_number", jsonfitting.optString("pipe_number"));
                                    namelist.put("start_node", jsonfitting.optString("start_node"));
                                    namelist.put("stop_node", jsonfitting.optString("stop_node"));
                                    namelist.put("scope", jsonfitting.optString("scope"));
                                    namelist.put("pipe_type", jsonfitting.optString("pipe_type"));
                                    namelist.put("sub_type", jsonfitting.optString("sub_type"));
                                    namelist.put("pipe_diameter", jsonfitting.optString("pipe_diameter"));
                                    Log.e("pipe_data", jsonfitting.toString());

                                    dataBaseHelper.insert("tbl_design_data", namelist);


                                  /*  if (j == pipes.length() - 1) {
                                        progressLoading.setMessage();
                                        progressLoading.dismiss();
                                    }*/
                                }

                                // dataBaseHelper.close();
                            } catch (JSONException jsonException) {
                                DialogUtil.showMessageDialog(context, jsonException.toString());
                            } catch (Exception ignored) {
                                DialogUtil.showMessageDialog(context, ignored.toString());
                                Toast.makeText(getApplicationContext(), "Exception Zone" + ignored, Toast.LENGTH_LONG).show();
                            }
                            downloadDesign= true;
                            statusDesign =true;
                            if (downloadPipe && downloadAlignment && downloadSoil && downloadFitting && downloadContractor && downloadDesign && downloadIntermediate && downloadCrossing) {


                                if (statusPipe && statusAlignment && statusSoil && statusFitting && statusContractor && statusDesign && statusIntermediate && statusCrossing) {
                                    progressLoading.dismiss();
                                    Toast.makeText(context, "Values updated", Toast.LENGTH_LONG).show();
                                }


                            }
                            // productModelList.addAll(pipeNo);
                        }
                    }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error == null || error.networkResponse == null) {
                        DialogUtil.showMessageDialog(context, "Internet connection break");

                    }else if (error != null && !error.toString().isEmpty()) {
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
                    downloadDesign = true;
                    if (downloadPipe && downloadAlignment && downloadSoil && downloadFitting && downloadContractor && downloadDesign && downloadIntermediate && downloadCrossing) {

                        progressLoading.dismiss();
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

    public void sheetCrossingTypeData() {


        if (DialogUtil.checkInternetConnection(this)) {
            // loadingDialog.onShow();
            if (!progressLoading.isShowing()) {
                progressLoading.onShow();
            }
            downloadCrossing = false;
            statusCrossing = false;
            RequestQueue queue = Volley.newRequestQueue(this);

            final String url = AppConstants.APP_BASE_URL + "getcrossingtype?schema=" + SessionUtil.getSchema(context);//+"&dma="+dmaName;
            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("Response", response);

                            //loadingDialog.dismiss();
                            //  SessionUtil.saveNodes(response,context);
                            //  Toast.makeText(context, "Saved Nodes",Toast.LENGTH_SHORT).show();
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                JSONArray pipes = new JSONArray(jsonObject.optString("data"));


                                dataBaseHelper.deleteAll("Crossing_Category_Table");


                              /*  if (!progressLoading.isShowing()) {
                                    progressLoading.onShow();
                                }*/


                                for (int j = 0; j < pipes.length(); j++) {
                                    JSONObject jsonfitting = pipes.getJSONObject(j);
                                    ContentValues namelist = new ContentValues();
                                    namelist.put("class_name", jsonfitting.getString("class_name"));
                                    namelist.put("class_id", jsonfitting.getString("crossing_class_id"));
                                    namelist.put("type_name", jsonfitting.optString("crossing_type"));
                                    namelist.put("type_id", jsonfitting.optString("crossing_type_id"));
                                    Log.e("crossing category data", jsonfitting.toString());

                                    dataBaseHelper.insert("Crossing_Category_Table", namelist);


                                   /* if (j == pipes.length() - 1) {
                                        progressLoading.setMessage();
                                        progressLoading.dismiss();
                                    }*/
                                }

                                // dataBaseHelper.close();
                            } catch (JSONException jsonException) {
                                DialogUtil.showMessageDialog(context, jsonException.toString());
                            } catch (Exception ignored) {
                                DialogUtil.showMessageDialog(context, ignored.toString());
                                Toast.makeText(getApplicationContext(), "Exception Zone" + ignored, Toast.LENGTH_LONG).show();
                            }
                            downloadCrossing =true;
                            statusCrossing = true;
                            if (downloadPipe && downloadAlignment && downloadSoil && downloadFitting && downloadContractor && downloadDesign && downloadIntermediate && downloadCrossing) {


                                if (statusPipe && statusAlignment && statusSoil && statusFitting && statusContractor && statusDesign && statusIntermediate && statusCrossing) {
                                    progressLoading.dismiss();
                                    Toast.makeText(context, "Values updated", Toast.LENGTH_LONG).show();
                                }

                            }
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
                    if (downloadPipe && downloadAlignment && downloadSoil && downloadFitting && downloadContractor && downloadDesign && downloadIntermediate && downloadCrossing) {
                        downloadCrossing = true;

                        progressLoading.dismiss();}

                }
            });

            // Add the request to the RequestQueue.
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(600000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            queue.add(stringRequest);
        } else {
            DialogUtil.showNoConnectionDialog(this);
        }


    }


    private void setInterMediatePoint() {
        // if (DialogUtil.checkInternetConnection(this)) {

        if (!progressLoading.isShowing()) {
            progressLoading.onShow();
        }
        // loadingDialog.onShow();
           /* final ProgressDialog progressDialog = ProgressDialog.show(WeldingActivity.this, "",
                    "Loading. Please wait...", false);*/
           downloadIntermediate = false;
           statusIntermediate = false;
        RequestQueue queue = Volley.newRequestQueue(this);

        final String url = AppConstants.APP_BASE_URL + "getInterMediatePoint?schema=" + SessionUtil.getSchema(context);
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("response fitting", response);
                        progressLoading.dismiss();
                        // Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray joints = new JSONArray(jsonObject.optString("data"));


                            dataBaseHelper.deleteAll("tbl_fitting_material");
                            for (int i = 0; i < joints.length(); i++) {
                              /*  if (!progressLoading.isShowing()) {
                                    progressLoading.onShow();
                                }*/

                                JSONObject c = joints.getJSONObject(i);
                                JSONArray cJSONArray = c.getJSONArray("fitting_list");
                                if (cJSONArray.length() > 0) {
                                    for (int j = 0; j < cJSONArray.length(); j++) {
                                        JSONObject jsonfitting = cJSONArray.getJSONObject(j);
                                        ContentValues namelist = new ContentValues();
                                        namelist.put("FittingID", c.getString("id"));
                                        namelist.put("FittingName", c.getString("name"));
                                        namelist.put("SubFittingName", jsonfitting.optString("fitting_name"));
                                        namelist.put("MaterialID", jsonfitting.optString("fitting_material_id"));
                                        namelist.put("MaterialDescription", jsonfitting.optString("description"));
                                        namelist.put("MaterialCode", jsonfitting.optString("material_code"));
                                        Log.e("jsonfitting", jsonfitting.toString());

                                        dataBaseHelper.insert("tbl_fitting_material", namelist);
                                    }
                                } else {

                                    ContentValues namelist = new ContentValues();
                                    namelist.put("FittingID", c.optString("id"));
                                    namelist.put("FittingName", c.optString("name"));

                                    namelist.put("MaterialID", "");
                                    namelist.put("MaterialDescription", "");
                                    namelist.put("MaterialCode", "");

                                    dataBaseHelper.insert("tbl_fitting_material", namelist);
                                }

                              /*  if (i == joints.length() - 1) {
                                    progressLoading.dismiss();
                                }*/
                            }

                            // dataBaseHelper.close();
                        } catch (Exception ignored) {
                            Toast.makeText(getApplicationContext(), "Exception Fitting " + ignored, Toast.LENGTH_LONG).show();
                        }/*finally {

                                dataBaseHelper.close();
                            }*/


                        //  Toast.makeText(getApplicationContext(), "Welder Number", Toast.LENGTH_LONG).show();
                        // loadingDialog.dismiss();
                        downloadIntermediate = true;
                        statusIntermediate = true;
                        if (downloadPipe && downloadAlignment && downloadSoil && downloadFitting && downloadContractor && downloadDesign && downloadIntermediate && downloadCrossing) {


                            if (statusPipe && statusAlignment && statusSoil && statusFitting && statusContractor && statusDesign && statusIntermediate && statusCrossing) {
                                progressLoading.dismiss();
                                Toast.makeText(context, "Values updated", Toast.LENGTH_LONG).show();
                            }

                        }

                    }
                }, new Response.ErrorListener() {
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
                //  loadingDialog.dismiss();
                downloadIntermediate = true;
                if (downloadPipe && downloadAlignment && downloadSoil && downloadFitting && downloadContractor && downloadDesign && downloadIntermediate && downloadCrossing) {

                    progressLoading.dismiss();}
                //  Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                // Toast.makeText(context, "Some thing went wrong", Toast.LENGTH_SHORT).show();
            }
        });
        // Add the request to the RequestQueue.
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                600000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(stringRequest);
        /*} else {
            DialogUtil.showNoConnectionDialog(this);
        }*/

    }

    public void sheetSoilData() {
        if (DialogUtil.checkInternetConnection(this)) {
            // loadingDialog.onShow();
            if (!progressLoading.isShowing()) {
                progressLoading.onShow();
            }
            downloadSoil = false;
            statusSoil = false;
            RequestQueue queue = Volley.newRequestQueue(this);

            final String url = AppConstants.APP_BASE_URL + "allSoilType?schema=" + SessionUtil.getSchema(context);
            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("Response", response);

                            //loadingDialog.dismiss();
                            SessionUtil.saveSoil(response, context);
                            downloadSoil = true;
                            statusSoil = true;
                            if (downloadPipe && downloadAlignment && downloadSoil && downloadFitting && downloadContractor && downloadDesign && downloadIntermediate && downloadCrossing) {


                                if (statusPipe && statusAlignment && statusSoil && statusFitting && statusContractor && statusDesign && statusIntermediate && statusCrossing) {
                                    progressLoading.dismiss();
                                    Toast.makeText(context, "Values updated", Toast.LENGTH_LONG).show();
                                }


                            }
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
                    downloadSoil = true;
                    if (downloadPipe && downloadAlignment && downloadSoil && downloadFitting && downloadContractor && downloadDesign && downloadIntermediate && downloadCrossing) {

                        progressLoading.dismiss();}
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

    public void sheetAlignmentData() {
        if (DialogUtil.checkInternetConnection(this)) {
            // loadingDialog.onShow();
            if (!progressLoading.isShowing()) {
                progressLoading.onShow();
            }
            downloadAlignment = false;
            statusAlignment = false;
            RequestQueue queue = Volley.newRequestQueue(this);

            final String url = AppConstants.APP_BASE_URL + "allPipeAlign?schema=" + SessionUtil.getSchema(context);
            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("Response", response);

                            //loadingDialog.dismiss();
                            SessionUtil.saveAlignment(response, context);
                            downloadAlignment=true;
                            statusAlignment = true;
                            if (downloadPipe && downloadAlignment && downloadSoil && downloadFitting && downloadContractor && downloadDesign && downloadIntermediate && downloadCrossing) {


                                if (statusPipe && statusAlignment && statusSoil && statusFitting && statusContractor && statusDesign && statusIntermediate && statusCrossing) {
                                    progressLoading.dismiss();
                                    Toast.makeText(context, "Values updated", Toast.LENGTH_LONG).show();
                                }


                            }
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
                    downloadAlignment=true;
                    if (downloadPipe && downloadAlignment && downloadSoil && downloadFitting && downloadContractor && downloadDesign && downloadIntermediate && downloadCrossing) {

                        progressLoading.dismiss();
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

    public void sheetContractorData() {
        if (DialogUtil.checkInternetConnection(this)) {
            // loadingDialog.onShow();
            if (!progressLoading.isShowing()) {
                progressLoading.onShow();
            }
                downloadContractor = false;statusContractor =false;
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
                            downloadContractor = true;statusContractor = true;
                            if (downloadPipe && downloadAlignment && downloadSoil && downloadFitting && downloadContractor && downloadDesign && downloadIntermediate && downloadCrossing) {


                                if (statusPipe && statusAlignment && statusSoil && statusFitting && statusContractor && statusDesign && statusIntermediate && statusCrossing) {
                                    progressLoading.dismiss();
                                    Toast.makeText(context, "Values updated", Toast.LENGTH_LONG).show();
                                }


                            }
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
                    downloadContractor = true;
                    if (downloadPipe && downloadAlignment && downloadSoil && downloadFitting && downloadContractor && downloadDesign && downloadIntermediate && downloadCrossing) {

                        progressLoading.dismiss();


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

    public void sheetFittingData() {
        if (DialogUtil.checkInternetConnection(this)) {
            // loadingDialog.onShow();
            if (!progressLoading.isShowing()) {
                progressLoading.onShow();
            }
                downloadFitting =false;statusFitting = false;
            RequestQueue queue = Volley.newRequestQueue(this);

            final String url = AppConstants.APP_BASE_URL + "fittingmaster?schema=" + SessionUtil.getSchema(context);
            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("Response", response);

                            //loadingDialog.dismiss();
                            SessionUtil.saveFitting(response, context);
                            downloadFitting = true;statusFitting = true;
                            if (downloadPipe && downloadAlignment && downloadSoil && downloadFitting && downloadContractor && downloadDesign && downloadIntermediate && downloadCrossing) {


                                if (statusPipe && statusAlignment && statusSoil && statusFitting && statusContractor && statusDesign && statusIntermediate && statusCrossing) {
                                    progressLoading.dismiss();
                                    Toast.makeText(context, "Values updated", Toast.LENGTH_LONG).show();
                                }
                            }
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
                    downloadFitting = true;
                    if (downloadPipe && downloadAlignment && downloadSoil && downloadFitting && downloadContractor && downloadDesign && downloadIntermediate && downloadCrossing) {

                       progressLoading.dismiss();
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

    public void sheetPipeData() {
        if (DialogUtil.checkInternetConnection(this)) {
            // loadingDialog.onShow();
            if (!progressLoading.isShowing()) {
                progressLoading.onShow();
            }
            downloadPipe = false;
            statusPipe = false;
            RequestQueue queue = Volley.newRequestQueue(this);

            final String url = AppConstants.APP_BASE_URL + "allPipeTypesubTypeDia?schema=" + SessionUtil.getSchema(context);
            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("Response type", response);

                            //loadingDialog.dismiss();
                            SessionUtil.savePipeDetail(response, context);
                            downloadPipe = true;statusPipe = true;
                            if (downloadPipe && downloadAlignment && downloadSoil && downloadFitting && downloadContractor && downloadDesign && downloadIntermediate && downloadCrossing) {


                                if (statusPipe && statusAlignment && statusSoil && statusFitting && statusContractor && statusDesign && statusIntermediate && statusCrossing) {
                                    progressLoading.dismiss();
                                    Toast.makeText(context, "Values updated", Toast.LENGTH_LONG).show();
                                }

                            }
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
                    downloadPipe = true;
                    if (downloadPipe && downloadAlignment && downloadSoil && downloadFitting && downloadContractor && downloadDesign && downloadIntermediate && downloadCrossing) {

                        progressLoading.dismiss();


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

    @Override
    public void networkAvailable() {
        MobileButton.setBackgroundResource(R.drawable.mobile_green);
    }

    @Override
    public void networkUnavailable() {
        MobileButton.setBackgroundResource(R.drawable.mobile_red);
    }

    @Override
    public void networkWifiAvailable() {
        WifiButton.setBackgroundResource(R.drawable.wifi_green);
    }

    @Override
    public void networkWifiUnavailable() {
        WifiButton.setBackgroundResource(R.drawable.wifi_red);
    }


    public void netConnectivity() {
        networkStateReceiver = new NetworkStateReceiver();
        networkStateReceiver.addListener(this);
        this.registerReceiver(networkStateReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

    }

    @Override
    public void onDestroy() {
        try {
            networkStateReceiver.removeListener(this);
            if (networkStateReceiver != null) {
                this.unregisterReceiver(networkStateReceiver);
            }
        } catch (Exception e) {

        }

        super.onDestroy();
    }

}
