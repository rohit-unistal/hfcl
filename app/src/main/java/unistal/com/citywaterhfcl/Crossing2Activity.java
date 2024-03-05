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
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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

public class Crossing2Activity extends AppCompatActivity {
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
    @BindView(R.id.imglogout)
    ImageView imgLogout;

    @BindView(R.id.titletv)
    TextView citytv;
    @BindView(R.id.imgback)
    ImageView imgBack;
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
    @BindView(R.id.imgView)
    ImageView imgView;
    @BindView(R.id.spin_contractor)
    Spinner spinContractor;
    String strAccuracy;
    private String strLantitude, strLongitude, strAltitude,dma_id="1",crossingClassID,crossingTypeID;
    Intent serviceIntent;
    Context context;
    DataBaseHelper dataBaseHelper;
    Boolean flag = true;
    private String filePath="";
    private Bitmap bitmap;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    ArrayList<ItemModel> contractorList;
    ArrayList crossingClassIDList,crossingClassNameList,crossingTypeIDList,crossingTypeNameList;
    ArrayAdapter crossingTypeAdapter, crossingClassAdapter,crossingpositionAdapter,crossingcassingAdapter,positionpipelineAdapter,contractorAdapter;
   String [] crossingposition = {"OH","AG","BG"}; String [] crossingcasing = {"Yes","No"}; String [] pipelineposition = {"Below","Above","Parallel"};
    ProgressLoading progressLoading;
    String contractorID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crossing2);
        ButterKnife.bind(this);
        getSupportActionBar().hide();
        citytv.setText("Crossing");
        Boolean flag = true;
        context = this;
        imgLogout.setVisibility(View.INVISIBLE);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        init();
    }
    private void init(){
        progressLoading = new ProgressLoading(context);
        crossingClassIDList = new ArrayList();
        crossingClassNameList = new ArrayList();
        crossingTypeIDList = new ArrayList();
        crossingTypeNameList = new ArrayList();
        contractorList = new ArrayList<>();
        dataBaseHelper = new DataBaseHelper(context);
        sheetCrossingClass();
        setContractorData();
        crossingpositionAdapter = new ArrayAdapter(context, R.layout.simple_spinner_item, crossingposition); //selected item will look like a spinner set from XML
        crossingcassingAdapter = new ArrayAdapter(context, R.layout.simple_spinner_item, crossingcasing); //selected item will look like a spinner set from XML
        positionpipelineAdapter = new ArrayAdapter(context, R.layout.simple_spinner_item, pipelineposition); //selected item will look like a spinner set from XML
        spinnerCrossingPosition.setAdapter(crossingpositionAdapter);
        spinnerCrossingCassing.setAdapter(crossingcassingAdapter);
        spinnerPositionPipeline.setAdapter(positionpipelineAdapter);
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Crossing2Activity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
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
    public void sheetCrossingClass()
    {
        crossingClassIDList.clear();
        crossingClassNameList.clear();

        Cursor cur = dataBaseHelper.getAll("SELECT DISTINCT class_id,class_name" +
                " FROM Crossing_Category_Table");
        if (cur.getCount() > 0) {
            if (cur.moveToFirst()) {
                do {


                    crossingClassIDList.add(cur.getString(0));crossingClassNameList.add(cur.getString(1));
                } while (cur.moveToNext());
            }
        } else {
            Toast.makeText(Crossing2Activity.this, "No Crossing Type found or Please update Crossing  details", Toast.LENGTH_SHORT).show();
        }

        crossingClassAdapter = new ArrayAdapter(context, R.layout.simple_spinner_item, crossingClassNameList); //selected item will look like a spinner set from XML
        crossingClassAdapter.setDropDownViewResource(R.layout.simple_spinner_item);
        spinnerCrossingClass.setAdapter(crossingClassAdapter);
        crossingClassAdapter.notifyDataSetChanged();
        spinnerCrossingClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                crossingClassID = crossingClassIDList.get(i).toString();
                getCrossingType();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


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
    public void getCrossingType()
    {
        crossingTypeIDList.clear();
        crossingTypeNameList.clear();
        crossingTypeID="";

        Cursor cur = dataBaseHelper.getAll("SELECT DISTINCT type_id,type_name" +
                " FROM Crossing_Category_Table WHERE class_id ="+"'" + crossingClassID +"';");
        if (cur.getCount() > 0) {
            if (cur.moveToFirst()) {
                do {


                    crossingTypeIDList.add(cur.getString(0));crossingTypeNameList.add(cur.getString(1));
                } while (cur.moveToNext());
            }
        } else {
            Toast.makeText(Crossing2Activity.this, "No Material found or Please update Material details", Toast.LENGTH_SHORT).show();
        }

        crossingTypeAdapter = new ArrayAdapter(context, R.layout.simple_spinner_item, crossingTypeNameList); //selected item will look like a spinner set from XML
        crossingTypeAdapter .setDropDownViewResource(R.layout.simple_spinner_item);
        spinnerCrossingType.setAdapter(crossingTypeAdapter );
        crossingTypeAdapter.notifyDataSetChanged();
        spinnerCrossingType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                crossingTypeID = crossingTypeIDList.get(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }
    public boolean validate(){
        boolean valid = true;
        if(editRemarks.getText().toString().trim().equals(""))
        {
            valid = false;
            Toast.makeText(context,"Enter Remarks",Toast.LENGTH_SHORT).show();
        }
        if(editLength.getText().toString().trim().equals(""))
        {
            valid = false;
            Toast.makeText(context,"Enter Length",Toast.LENGTH_SHORT).show();
        }
        if(editOwner.getText().toString().trim().equals(""))
        {
            valid = false;
            Toast.makeText(context,"Enter Owner",Toast.LENGTH_SHORT).show();
        }
        if(editPermissionAuthority.getText().toString().trim().equals(""))
        {
            valid = false;
            Toast.makeText(context,"Enter Permission Authority",Toast.LENGTH_SHORT).show();
        }
        return valid;
    }
    @OnClick(R.id.btnSubmit)
    public void sheetSubmit() {
        if(validate())
        {
            HashMap<String, String> params = new HashMap<String, String>();

            params.put("schema", SessionUtil.getSchema(context));
            params.put("user_id", SessionUtil.getUserId(context));

            params.put("start_latitude", editStartLatitude.getText().toString() + "");
            params.put("start_longitude", editStartLongitude.getText().toString() + "");
            params.put("end_latitude", editEndLatitude.getText().toString() + "");
            params.put("end_longitude", editEndLongitude.getText().toString() + "");

            params.put("crossing_class", crossingClassID + "");
            params.put("crossing_type", crossingTypeID + "");
            params.put("crossing_casing", spinnerCrossingCassing.getSelectedItem().toString()+ "");
            params.put("length", editLength.getText().toString() + "");
            params.put("owner_name", editOwner.getText().toString() + "");
            params.put("crossing_position", spinnerCrossingPosition.getSelectedItem().toString() + "");

            params.put("pipeline_position", spinnerPositionPipeline.getSelectedItem().toString() + "");
            params.put("permission_authority", editPermissionAuthority.getText().toString());

            params.put("remarks", editRemarks.getText().toString() + "");
            params.put("photo", filePath+ "");
            params.put("contractor_id", contractorID+ "");

            Log.e("Pppppppppppppppppparams", params.toString());
            boolean s = dataBaseHelper.newCrossingInsert(params);
            if (s) {

                clear();
                Toast.makeText(context, "Data Locally Saved", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(context, "Not Inserted", Toast.LENGTH_LONG).show();
            }
        }


    }
       /* if (DialogUtil.checkInternetConnection(this)) {
            // loadingDialog.onShow();

            if (flag) {
                flag = false;
                if (!progressLoading.isShowing()) {
                    progressLoading.onShow();
                }


                final String url = AppConstants.APP_BASE_URL+"insertcrossing";
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

                        params.put("start_latitude", editStartLatitude.getText().toString() + "");
                        params.put("start_longitude", editStartLongitude.getText().toString() + "");
                        params.put("end_latitude", editEndLatitude.getText().toString() + "");
                        params.put("end_longitude", editEndLongitude.getText().toString() + "");

                        params.put("crossing_class", crossingClassID + "");
                        params.put("crossing_type", crossingTypeID + "");
                        params.put("crossing_casing", spinnerCrossingCassing.getSelectedItem().toString()+ "");
                        params.put("length", editLength.getText().toString() + "");
                        params.put("owner_name", editOwner.getText().toString() + "");
                        params.put("crossing_position", spinnerCrossingPosition.getSelectedItem().toString() + "");

                        params.put("pipeline_position", spinnerPositionPipeline.getSelectedItem().toString() + "");
                        params.put("permission_authority", editPermissionAuthority.getText().toString());

                        params.put("remarks", editRemarks.getText().toString() + "");
                      //  params.put("photo", filePath+ "");

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
        }


    }*/
    private void clear() {
        editRemarks.setText("");
        filePath= "";
        editStartLatitude.setText("");
        editStartLongitude.setText("");
        editEndLatitude.setText("");
        editEndLongitude.setText("");
        editLength.setText("");
        editOwner.setText("");
        editPermissionAuthority.setText("");
        //  spinnerStartNode.setText("");
        //  spinnerEndNode.setText("");
        // startNodeID=""; endNodeID="";

        imgView.setImageResource(android.R.color.transparent);

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
    @OnClick(R.id.imgView)
    public void captureStartImage() {

        if ((ActivityCompat.shouldShowRequestPermissionRationale(Crossing2Activity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) && (ActivityCompat.shouldShowRequestPermissionRationale(Crossing2Activity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE))) {

        } else {
            if (ContextCompat.checkSelfPermission(context,
                    Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(Crossing2Activity.this,
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
}