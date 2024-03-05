package unistal.com.citywaterhfcl;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

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
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
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

public class HindranceActivity extends AppCompatActivity {
    @BindView(R.id.accuracytv)
    TextView acctv;
     @BindView(R.id.imgback)
    ImageView imgBack;
    @BindView(R.id.edit_date_from)
    EditText editDate;
    @BindView(R.id.imglogout)
    ImageView imgLogout;
    @BindView(R.id.titletv)
    TextView citytv;
    @BindView(R.id.spin_network)
    Spinner spinnerNetwork;
    @BindView(R.id.spin_httype)
    Spinner spinnerhttype;
    @BindView(R.id.spin_dma)
    Spinner spinnerDMA;
    @BindView(R.id.spin_zone)
    Spinner spinnerZone;
    @BindView(R.id.edwork_impacted)
    EditText edwork_impacted;
    @BindView(R.id.eddescription)
    EditText eddescription;
    @BindView(R.id.imgcustomerphoto)
    ImageView imgcustomerphoto;

    Context context;
    ProgressLoading progressLoading;
    String strAccuracy;
    String dmaName, zoneName, networkName, httypeid, httypename;
    ArrayList<String> networkList, zoneList, dmaList, httypenameList,
            httypeidList;
    private String strLantitude="0.0", strLongitude="0.0", strAltitude="0.0";

    ArrayAdapter zoneAdapter, dmaAdapter, networkAdapter, htAdapter;
    private Bitmap customerBitmap;
    Boolean flag = true;
    Intent serviceIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hindrance);
        ButterKnife.bind(this);
        getSupportActionBar().hide();
        citytv.setText("Hindrance ");

        context = this;
        progressLoading = new ProgressLoading(this);
        imgLogout.setVisibility(View.INVISIBLE);
        init();
    }

    private void init() {
        networkList = new ArrayList<>();
        zoneList = new ArrayList();
        dmaList = new ArrayList();
        httypenameList = new ArrayList();
        httypeidList = new ArrayList();
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        sheetNetwork();
        sheetHindranceType();
        editDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRouteSurveyDate();
            }
        });
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(HindranceActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            //  gpsTracker.getLocation();
            startLocationListener();
        }
        getCurrentDate();
    }

    public void getCurrentDate() {
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
                                networkAdapter = new ArrayAdapter(context, R.layout.layoutspinner, networkList);


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
    @Override
    protected void onStop() {
        super.onStop();
        SmartLocation.with(context).location().stop();
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
    @Override
    protected void onResume() {
        super.onResume();
        startLocationListener();
        serviceIntent = new Intent(HindranceActivity.this, LocationService.class);
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
    public void sheetZone() {
        if (DialogUtil.checkInternetConnection(this)) {
            // loadingDialog.onShow();
            if (!progressLoading.isShowing()) {
                progressLoading.onShow();
            }

            RequestQueue queue = Volley.newRequestQueue(this);

            final String url = AppConstants.APP_BASE_URL + "allzone?schema=" + SessionUtil.getSchema(context) + "&network_type=" + networkName;
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


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void sheetHindranceType() {
        if (DialogUtil.checkInternetConnection(this)) {
            // loadingDialog.onShow();
            if (!progressLoading.isShowing()) {
                progressLoading.onShow();
            }

            RequestQueue queue = Volley.newRequestQueue(this);

            final String url = AppConstants.APP_BASE_URL + "gethindrance";
            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("Response", response);
                            httypenameList.clear();
                            httypeidList.clear();
                            try {
                                JSONObject job = new JSONObject(response);

                                JSONArray arr = job.optJSONArray("data");
                                if (arr != null) {
                                    for (int i = 0; i < arr.length(); i++) {

                                        httypenameList.add(arr.getJSONObject(i).getString("hindrance_type"));
                                        httypeidList.add(arr.getJSONObject(i).getString("id"));
                                    }
                                }
                                htAdapter = new ArrayAdapter(context, R.layout.layoutspinner, httypenameList);


                                spinnerhttype.setAdapter(htAdapter);


                                //productModelList.addAll(pipeNo);
                                htAdapter.notifyDataSetChanged();


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


        spinnerhttype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.e(" Selected Item position", "position " + i);
                //  bendID = bendModelList.get(i).getId();
                httypeid = httypeidList.get(i);


                //  sheetDMA();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @OnClick(R.id.imgcustomerphoto)
    public void captureCustomerPhoto() {

        if ((ActivityCompat.shouldShowRequestPermissionRationale(HindranceActivity.this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) && (ActivityCompat.shouldShowRequestPermissionRationale(HindranceActivity.this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE))) {

        } else {
            if (ContextCompat.checkSelfPermission(context,
                    android.Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(HindranceActivity.this,
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

        }


    }

    public boolean validate() {
        boolean valid = true;

        if (edwork_impacted.getText().toString().trim().equals("")) {
            valid = false;
            Toast.makeText(context, "Enter work_impacted", Toast.LENGTH_SHORT).show();
        }
        return valid;
    }

    private void clear() {
        edwork_impacted.setText("");
        eddescription.setText("");
        customerBitmap = null;
        imgcustomerphoto.setImageResource(android.R.color.transparent);
    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);

        return byteArrayOutputStream.toByteArray();
    }

    @OnClick(R.id.btnSubmit)
    public void uploadBitmap() {
        if (validate()) {
            if (DialogUtil.checkInternetConnection(this)) {
                if (!progressLoading.isShowing()) {
                    progressLoading.onShow();
                }
                flag = false;
                final String url = AppConstants.APP_BASE_URL + "insertHindrance";
                VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, url,
                        new Response.Listener<NetworkResponse>() {
                            @Override
                            public void onResponse(NetworkResponse response) {
                                progressLoading.dismiss();
                                flag = true;
                                Log.e("RRRR response", String.valueOf(response));
                                try {
                                    JSONObject jsonObject = new JSONObject(new String(response.data));
                                    String status = jsonObject.getString("success");
                                    String message = jsonObject.getString("data");

                                    if (message.contains("success")) {
                                        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                                        clear();

                                    } else {
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
                                if (error == null) {
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
                                } else {
                                    Toast.makeText(context, "Error in connectivity or server side error-" + error.toString(), Toast.LENGTH_SHORT).show();
                                    Log.e("Not GotError response", error.toString());
                                }
                            }
                        }) {


                    @Override
                    protected Map<String, DataPart> getByteData() {
                        Map<String, VolleyMultipartRequest.DataPart> params = new HashMap<>();
                        long imagename = System.currentTimeMillis();
                        if (customerBitmap != null) {
                            params.put("photo", new VolleyMultipartRequest.DataPart("hindrance" + imagename + ".png", getFileDataFromDrawable(customerBitmap)));
                        }


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

                        params.put("hindrance_type_id", httypeid);
                        params.put("work_impacted", edwork_impacted.getText().toString());
                        params.put("description", eddescription.getText().toString());

                        params.put("date_from", editDate.getText().toString());
                        params.put("date_to", "");
                        params.put("latitude", strLantitude);
                        params.put("longitude",strLongitude);


                        //params.put("date_to", "0000-00-00");
                        Log.e("Pppppppppppppppppparams", params.toString());
                        return params;

                    }


                };

                //adding the request to volley
                volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                        1000000,
                        0,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                Volley.newRequestQueue(this).add(volleyMultipartRequest);
            }
        }
    }
}