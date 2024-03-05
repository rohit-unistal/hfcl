package unistal.com.citywaterhfcl;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.provider.MediaStore;
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
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.location.config.LocationAccuracy;
import io.nlopez.smartlocation.location.config.LocationParams;

public class FittingActivity extends AppCompatActivity {
    @BindView(R.id.edit_date)
    EditText editDate;
    @BindView(R.id.spin_network)
    Spinner spinnerNetwork;
    @BindView(R.id.spin_start_node)
    EditText spinnerStartNode;
    @BindView(R.id.spin_zone)
    Spinner spinnerZone;
    @BindView(R.id.spin_pipe_segment)
    Spinner spinpipesegment;

    @BindView(R.id.spin_dma)
    Spinner spinnerDMA;

    @BindView(R.id.spin_end_node)
    EditText spinnerEndNode;
    @BindView(R.id.spin_fitting_type)
    Spinner spinnerfitting;
    @BindView(R.id.spin_sub_fitting_type)
    Spinner spinnersubfitting;

    @BindView(R.id.titletv)
    TextView citytv;
    @BindView(R.id.netstatus)
    ImageView netStatus;
    @BindView(R.id.imgView)
    ImageView imgView;
    @BindView(R.id.btnStartNode)
    Button btnStartNode;
    @BindView(R.id.edit_latitude)
    EditText editStartLatitude;

    @BindView(R.id.edit_longitude)
    EditText editStartLongitude;
    @BindView(R.id.edit_remarks)
    EditText editRemarks;
    @BindView(R.id.spin_contractor)
    Spinner spinContractor;
   /* @BindView(R.id.edit_fitting_serial)
    EditText editFittingSerial;*/
    @BindView(R.id.edit_depth)
    EditText editDepth;
    @BindView(R.id.accuracytv)
    TextView acctv;
    @BindView(R.id.imgback)
    ImageView imgBack;
    @BindView(R.id.imglogout)
    ImageView imgLogout;
    @BindView(R.id.btn_savenode)
    Button ButtonSaveNode;
    ProgressLoading progressLoading;
    Context context;
    Boolean flag = true;
    String strAccuracy;
    private String strLantitude, strLongitude, strAltitude,zone_id="";
    private Intent serviceIntent;
    private Bitmap bitmap;
    private String filePath="";
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;

    ArrayList fittingNameList,fittingIdList,subfittingNameList,subfittingIdList;
    ArrayList<ItemModel> contractorList;
    ArrayList<PipeNum> PipeNumberDataList;
    ArrayList<String> networkList,  zoneList,dmaList;

    ArrayAdapter<ItemModel> contractorAdapter;
    ArrayAdapter pipeAdapter,zoneAdapter,fittingAdapter,subfittingAdapter,dmaAdapter,networkAdapter;
    String image,dmaName ,pipeNumber="",contractorID="",pipeID="", startNodeID="", endNodeID="",zoneName="",fittingID="",subfittingID="";
    DataBaseHelper dataBaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fitting);
        ButterKnife.bind(this);
        getSupportActionBar().hide();
        citytv.setText("Fitting");

        context = this;
        progressLoading = new ProgressLoading(this);
        imgLogout.setVisibility(View.INVISIBLE);
        init();
    }
    public void init(){
        dataBaseHelper = new DataBaseHelper(context);
        networkList = new ArrayList<>();
        zoneList = new ArrayList();
        dmaList = new ArrayList();
        PipeNumberDataList = new ArrayList<PipeNum>();
       // zoneList=new ArrayList();
        contractorList = new ArrayList<>();
        fittingNameList = new ArrayList();
        fittingIdList = new ArrayList();
        subfittingNameList = new ArrayList();
        subfittingIdList = new ArrayList();
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(FittingActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            //  gpsTracker.getLocation();
            startLocationListener();
        }
        editDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRouteSurveyDate();
            }
        });
        getCurrentDate();
        getNetwork();
        getZone();
        sheetfitting();
        setContractorData();
        ButtonSaveNode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                sheetLayingDesignData();



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
    @OnClick(R.id.btnSubmit)
    public void sheetSubmit() {
        if(validate())
        {
          HashMap<String, String> params = new HashMap<String, String>();

            params.put("schema", SessionUtil.getSchema(context));
            params.put("user_id", SessionUtil.getUserId(context));
            params.put("network_type", SessionUtil.getNetwork(context)+ "");
            params.put("zone", zone_id + "");
            params.put("dma", dmaName + "");
            params.put("fitting_id", fittingID+ "");
            params.put("fitting_subtype_id", subfittingID + "");
            params.put("start_node", spinnerStartNode.getText().toString());
            params.put("end_node", spinnerEndNode.getText().toString());

            params.put("start_latitude", editStartLatitude.getText().toString() + "");
            params.put("start_longitude", editStartLongitude.getText().toString() + "");

            params.put("pipe_number", pipeID + "");

            params.put("contractor", contractorID+ "");
            params.put("remarks", editRemarks.getText().toString() + "");
            params.put("installed_date", editDate.getText().toString() + "");
            params.put("photo", filePath+ "");
            params.put("depth", editDepth.getText().toString()+ "");


            Log.e("Pppppppppppppppppparams", params.toString());
            boolean s = dataBaseHelper.fittingInsert(params);
            if (s) {

                clear();
                Toast.makeText(context, "Data Locally Saved", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(context, "Not Inserted", Toast.LENGTH_LONG).show();
            }
           }


    }
    /*
     if (DialogUtil.checkInternetConnection(this)) {
                // loadingDialog.onShow();

                if (flag) {
                    flag = false;
                    if (!progressLoading.isShowing()) {
                        progressLoading.onShow();
                    }


                    final String url = AppConstants.APP_BASE_URL+"insertfitting";
                    // Request a string response from the provided URL.
                    VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST,url,
                            new Response.Listener<NetworkResponse>() {
                                @Override
                                public void onResponse(NetworkResponse response) {
                                    Log.e("Response",new String(response.data));
                                    progressLoading.dismiss();
                                    //  Toast.makeText(context, response, Toast.LENGTH_LONG).show();
                                    try {
                                        JSONObject jsonObject = new JSONObject(new String(response.data));
                                        String status=         jsonObject.getString("success");
                                        String message=         jsonObject.getString("data");

                                        if (message.contains("success")) {
                                            //Toast.makeText(context, "Successfully submitted", Toast.LENGTH_LONG).show();
                                            Toast.makeText(context, message, Toast.LENGTH_LONG).show();

                                            clear();



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
                    }){


                        @Override
                        protected Map<String, DataPart> getByteData() {
                            Map<String, DataPart> params = new HashMap<>();
                            long imagename = System.currentTimeMillis();
                            params.put("photo", new DataPart(imagename + ".png", getFileDataFromDrawable(bitmap)));
                            return params;
                        }

                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<String, String>();

                            params.put("schema", SessionUtil.getSchema(context));
                            params.put("user_id", SessionUtil.getUserId(context));
                            params.put("zone", zone_id + "");
                            params.put("dma", dmaName + "");
                            params.put("fitting_id", fittingID+ "");
                            params.put("fitting_subtype_id", subfittingID + "");
                            params.put("start_node_id", startNodeID + "");
                            params.put("end_node_id", endNodeID + "");

                            params.put("start_latitude", editStartLatitude.getText().toString() + "");
                            params.put("start_longitude", editStartLongitude.getText().toString() + "");

                            params.put("pipe_number", pipeID + "");
                            params.put("start_node", spinnerStartNode.getText().toString());
                            params.put("end_node", spinnerEndNode.getText().toString());


                            params.put("remarks", editRemarks.getText().toString() + "");
                            params.put("installed_date", editDate.getText().toString() + "");
                             params.put("photo", filePath+ "");
                            params.put("depth", editDepth.getText().toString()+ "");


                            Log.e("Pppppppppppppppppparams", params.toString());
                            return params;
                        }
                    };

                    //adding the request to volley
                    volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                            600000,
                            1,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    Volley.newRequestQueue(this).add(volleyMultipartRequest);
                }

            } else {
                DialogUtil.showNoConnectionDialog(this);
            }*/
    private void clear() {
        editRemarks.setText("");
        filePath= "";
        editStartLatitude.setText("");
        editStartLongitude.setText("");
        editDepth.setText("");
        //  spinnerStartNode.setText("");
        //  spinnerEndNode.setText("");
        // startNodeID=""; endNodeID="";
        image = "";
        imgView.setImageResource(android.R.color.transparent);

    }
    public void getNetwork(){
        networkList.clear();
        networkList.add(SessionUtil.getNetwork(context));

        networkAdapter = new ArrayAdapter(context, R.layout.layoutspinner, networkList); //selected item will look like a spinner set from XML
        networkAdapter.setDropDownViewResource(R.layout.layoutspinner);
        spinnerNetwork.setAdapter(networkAdapter);
        networkAdapter.notifyDataSetChanged();
        spinnerNetwork.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

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
            Toast.makeText(FittingActivity.this, "No Zone found or Please update  details", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(FittingActivity.this, "No DMA found or Please update details", Toast.LENGTH_SHORT).show();
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
    public void sheetfitting()
    {
        fittingIdList.clear();
        fittingNameList.clear();

        Cursor cur = dataBaseHelper.getAll("SELECT DISTINCT FittingID,FittingName" +
                " FROM tbl_fitting_material");
        if (cur.getCount() > 0) {
            if (cur.moveToFirst()) {
                do {


                    fittingIdList.add(cur.getString(0));fittingNameList.add(cur.getString(1));
                } while (cur.moveToNext());
            }
        } else {
            Toast.makeText(FittingActivity.this, "No Fitting point found or Please update Intermediate Point details", Toast.LENGTH_SHORT).show();
        }

        fittingAdapter = new ArrayAdapter(context, R.layout.layoutspinner, fittingNameList); //selected item will look like a spinner set from XML
        fittingAdapter.setDropDownViewResource(R.layout.layoutspinner);
        spinnerfitting.setAdapter(fittingAdapter);
        fittingAdapter.notifyDataSetChanged();
        spinnerfitting.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                fittingID = fittingIdList.get(i).toString();
                getMaterial();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }

    public void getMaterial()
    {
        subfittingIdList.clear();
        subfittingNameList.clear();
        subfittingID="";

        Cursor cur = dataBaseHelper.getAll("SELECT DISTINCT MaterialID,SubFittingName,MaterialDescription,MaterialCode" +
                " FROM tbl_fitting_material WHERE FittingID ="+"'" + fittingID +"';");
        if (cur.getCount() > 0) {
            if (cur.moveToFirst()) {
                do {


                    subfittingIdList.add(cur.getString(0));subfittingNameList.add(cur.getString(1));
                } while (cur.moveToNext());
            }
        } else {
            Toast.makeText(FittingActivity.this, "No Material found or Please update Material details", Toast.LENGTH_SHORT).show();
        }

        subfittingAdapter = new ArrayAdapter(context, R.layout.layoutspinner, subfittingNameList); //selected item will look like a spinner set from XML
        subfittingAdapter .setDropDownViewResource(R.layout.layoutspinner);
        spinnersubfitting.setAdapter(subfittingAdapter );
        subfittingAdapter.notifyDataSetChanged();
        spinnersubfitting.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                subfittingID = subfittingIdList.get(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


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
        if(editDepth.getText().toString().trim().equals(""))
        {
            valid = false;
            Toast.makeText(context,"Enter Depth",Toast.LENGTH_SHORT).show();
        }
        if(editRemarks.getText().toString().trim().equals(""))
        {
            valid = false;
            Toast.makeText(context,"Enter Remarks",Toast.LENGTH_SHORT).show();
        }

      /*  if(editEndDepth.getText().toString().trim().equals("")||editEndWidth.getText().toString().trim().equals(""))
        {
            valid = false;
            Toast.makeText(context,"Enter End Depth and Width",Toast.LENGTH_SHORT).show();
        }*/



        return valid;
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
        serviceIntent = new Intent(FittingActivity.this, LocationService.class);
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
          //  TextView textView = snackbarView.findViewById(android.support.design.R.id.snackbar_text);
          //  textView.setTextColor(Color.BLUE);

            /// snackbar.show();

            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) snackbarView.getLayoutParams();
            params.gravity = Gravity.TOP;
            snackbarView.setLayoutParams(params);
            // snackbar.show();
        }
    };
    @OnClick(R.id.imgView)
    public void captureStartImage() {

            if ((ActivityCompat.shouldShowRequestPermissionRationale(FittingActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) && (ActivityCompat.shouldShowRequestPermissionRationale(FittingActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE))) {

            } else {
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(FittingActivity.this,
                    new String[]{Manifest.permission.CAMERA},
                    100);

        } else {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            //  fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

            //  intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

            // start the image capture Intent
            startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
        }}


    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if the result is capturing Image
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                bitmap = photo;

                imgView.setImageBitmap(photo);
                       /* Uri selectedImageUri = data.getData();
                         graphPhotoPath =getPath(selectedImageUri);*/
                //     imageView.setImageBitmap(BitmapFactory.decodeFile(graphPhotoPath));
                Uri tempUri = getImageUri(getApplicationContext(), photo);

                // CALL THIS METHOD TO GET THE ACTUAL PATH
                File finalFile = new File(getRealPathFromURI(tempUri));
                filePath = finalFile.getAbsolutePath();
                System.out.println(filePath);
                Log.e("filePath",filePath);
                Toast.makeText(context,filePath,Toast.LENGTH_SHORT).show();
            }else{
                Log.e("filePath",filePath);
                Toast.makeText(context,"Something went wrong",Toast.LENGTH_SHORT).show();

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
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        Bitmap OutImage = Bitmap.createScaledBitmap(inImage, 1000, 1000,true);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), OutImage, "Laying_Paytora"+ Calendar.getInstance().getTime(), null);
        return Uri.parse(path);
    }
    public String getRealPathFromURI(Uri contentUri)
    {
        try
        {
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor cursor = managedQuery(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        catch (Exception e)
        {
            return contentUri.getPath();
        }
    }
    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }
    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }
    public void sheetLayingDesignData(){


        if (DialogUtil.checkInternetConnection(this)) {
            // loadingDialog.onShow();
            if (!progressLoading.isShowing()) {
                progressLoading.onShow();
            }

            RequestQueue queue = Volley.newRequestQueue(this);

            final String url = AppConstants.APP_BASE_URL+"getallLayingbyZone?schema="+SessionUtil.getSchema(context)+"&zone="+SessionUtil.getZone(context)+"&network_type="+SessionUtil.getNetwork(context);
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

}