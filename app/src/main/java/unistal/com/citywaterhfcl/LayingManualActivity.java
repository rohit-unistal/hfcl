package unistal.com.citywaterhfcl;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

public class LayingManualActivity extends AppCompatActivity {
    @BindView(R.id.edit_pipe_segment_other)
    EditText editPipeSegmentOther;
    @BindView(R.id.spin_end_node)
    EditText spinnerEndNode;
    @BindView(R.id.spin_start_node)
    EditText spinnerStartNode;
    @BindView(R.id.spin_network)
    Spinner spinnerNetwork;
    @BindView(R.id.spin_zone)
    Spinner spinnerZone;
    @BindView(R.id.spin_soil)
    Spinner spinnerSoil;
    @BindView(R.id.spin_alignment)
    Spinner spinAlignment;
    @BindView(R.id.spin_dma)
    Spinner spinnerDMA;
    @BindView(R.id.edit_scope)
    EditText editScope;

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
    @BindView(R.id.spin_contractor)
    Spinner spinContractor;
    @BindView(R.id.editnextDepth)
    EditText editNextDepth;

    @BindView(R.id.editnextWidth)
    EditText editNextWidth;
   /* @BindView(R.id.editnextLength)
    EditText editNextLength;
    @BindView(R.id.nextDiameter)
    TextView tvNextDiameter;
    @BindView(R.id.nextLength)
    TextView tvNextLength;*/


    @BindView(R.id.edit_length)
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
    Spinner spinnerSubType;
    @BindView(R.id.spin_type)
    Spinner spinnerType;
    @BindView(R.id.spin_diameter)
    Spinner spinnerDiameter;


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
    @BindView(R.id.imgback)
    ImageView imgBack;


    @BindView(R.id.accuracytv)
    TextView acctv;
    @BindView(R.id.spin_backfill)
    Spinner spinnerBackfill;
    int pipeTypeIndex,pipeSubTypeIndex;
    private NetworkStateReceiver networkStateReceiver;
    // GPSTracker gpsTracker;
    private static final int START_CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int END_CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 101;
    String backfillOption[] = {"Yes","No"};
    ArrayList<ItemModel> contractorList,soilNameList,alignmentNameList,fittingList;
    ArrayList<PipeNum> PipeNumberDataList;
    ArrayList<PipeModel> PipeTypeDataList;
    ArrayList arrayNextLatitude, arrayNextLongitude, arrayNextAltitude, arrayDepth, arrayIntermediateList, arrayNextWidthList;//, arrayNextLengthList;
    ArrayList<ItemModel> pipeTypeModelList, pipeSubTypeModelList, pipeDiameterModelList,  intermediateModelList, segmentModelList;//startNodeModelList, endNodeModelList,
    ArrayList<String> networkList, zoneList,dmaList,startNodeList, endNodeList,  zoneIDList,pipeList, pipeIDList,pipeTypeList, pipeSubTypeList, pipeDiameterList,  intermediateList, segmentList;//dmaList, dmaIDList
    ArrayAdapter backfillAdapter, zoneAdapter, networkAdapter,   startNodeAdapter, endNodeAdapter,  dmaAdapter, intermediateAdapter, startSegmentAdapter;
    ArrayAdapter<ItemModel> soilAdapter, alignmentAdapter,contractorAdapter,fittingAdapter;
    ArrayAdapter<PipeNum>pipeAdapter;

    ArrayAdapter<PipeModel>pipeTypeAdapter;ArrayAdapter<PipeModel.PipeSubType> pipeSubTypeAdapter;ArrayAdapter<PipeModel.PipeSubType.PipeDium>pipeDiameterAdapter;
    String pipeNumber="",soilID="",alignmentID= "",contractorID="",fittingID="",pipeID="", zone_id="",segmentID="", typeID="",subtypeID="", diameterID="",startNodeID="", endNodeID="",  intermediateID, imageStart, imageEnd, zoneName, dmaName,pipetype="",subtype="", diameter="";//
    ProgressLoading progressLoading;
    Context context;
    // private Intent serviceIntent;
    Boolean flag = true;
    String strAccuracy;
    private String strLantitude, strLongitude, strAltitude;
    DataBaseHelper dataBaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laying_manual);
        ButterKnife.bind(this);
        context = this;
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().hide();
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        citytv.setText("Laying");
        imgLogout.setVisibility(View.INVISIBLE);
        init();
    }
    private void init(){

        dataBaseHelper = new DataBaseHelper(context);
        progressLoading = new ProgressLoading(context);
        networkList = new ArrayList<>();
        zoneList = new ArrayList();
        dmaList = new ArrayList();
        PipeNumberDataList = new ArrayList<PipeNum>();
        PipeTypeDataList = new ArrayList<PipeModel>();
        contractorList = new ArrayList<>();
        soilNameList = new ArrayList<>();
        alignmentNameList = new ArrayList<>();
        fittingList = new ArrayList<>();
        arrayNextLatitude= new ArrayList<>();
        arrayNextLongitude= new ArrayList<>();
        arrayDepth= new ArrayList<>();
        arrayIntermediateList= new ArrayList<>();
        // arrayNextLengthList= new ArrayList<>();
        arrayNextWidthList= new ArrayList<>();
        startNodeList = new ArrayList<>();
        endNodeList = new ArrayList<>();
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        getNetwork();
        getZone();
        setSoilData();
        setAlignmentData();
        setContractorData();
        setFittingData();

        setPipeType();
        backfillAdapter = new ArrayAdapter(context,  R.layout.layoutspinner,backfillOption);
        spinnerBackfill.setAdapter(backfillAdapter);

        editDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRouteSurveyDate();
            }
        });
        getCurrentDate();
        acctv.setText("Accuracy = " + strAccuracy);
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(LayingManualActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
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
        if(!(editNextLatitude.getText().toString().trim().equals("")||editNextLongitude.getText().toString().trim().equals("")||editNextDepth.getText().toString().trim().equals("")||editNextWidth.getText().toString().trim().equals(""))) {
            //  arrayNextAltitude.add(editNextAltitude.getText().toString());
            arrayNextLatitude.add(editNextLatitude.getText().toString());
            arrayNextLongitude.add(editNextLongitude.getText().toString());
            arrayDepth.add(editNextDepth.getText().toString());
            arrayIntermediateList.add(fittingList.get(spinnerIntermediate.getSelectedItemPosition()).getId());
            //  arrayNextLengthList.add(editNextLength.getText().toString());
            arrayNextWidthList.add(editNextWidth.getText().toString());
            editNextAltitude.setText("");
            editNextLatitude.setText("");
            editNextLongitude.setText("");
            editNextDepth.setText("");
            //   editNextLength.setText("");
            editNextWidth.setText("");
        }else{
            Toast.makeText(context,"Enter all intermediate fields",Toast.LENGTH_SHORT).show();
        }
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
                " FROM tbl_design_data");
        if (cur.getCount() > 0) {
            if (cur.moveToFirst()) {
                do {


                    zoneList.add(cur.getString(0));
                } while (cur.moveToNext());
            }
        } else {
            Toast.makeText(LayingManualActivity.this, "No Zone found or Please update details", Toast.LENGTH_SHORT).show();
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
                " FROM tbl_design_data where zone = '"+zone_id+"';");
        if (cur.getCount() > 0) {
            if (cur.moveToFirst()) {
                do {


                    dmaList.add(cur.getString(0));
                } while (cur.moveToNext());
            }
        } else {
            Toast.makeText(LayingManualActivity.this, "No DMA found or Please update Intermediate Point details", Toast.LENGTH_SHORT).show();
        }

        dmaAdapter = new ArrayAdapter(context, R.layout.layoutspinner, dmaList); //selected item will look like a spinner set from XML
        dmaAdapter.setDropDownViewResource(R.layout.layoutspinner);
        spinnerDMA.setAdapter(dmaAdapter);
        dmaAdapter.notifyDataSetChanged();
        spinnerDMA.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                dmaName= dmaList.get(i);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



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
            for(int l = 0; l<PipeTypeDataList.size(); l++){
                Log.e("pipe Type List Name", PipeTypeDataList.get(l).getPipeType());
                Log.e("pipe Type Name"+pipetype+":", PipeTypeDataList.get(l).getPipeType());

                if(PipeTypeDataList.get(l).getPipeType().trim().equals(pipetype.trim())){
                    Log.e("pipe Type Name"+pipetype+":", PipeTypeDataList.get(l).getPipeType());
                    //  if(spinnerType.getItemAtPosition(l).toString().equals(pipetype)){


                    spinnerType.setSelection(l);
                    break;
                }
            }
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
                for(int j = 0; j<PipeTypeDataList.get(pipeTypeIndex).getPipeSubType().size(); j++){
                    if(PipeTypeDataList.get(pipeTypeIndex).getPipeSubType().get(j).getName().equals(subtype)){
                        spinnerSubType.setSelection(j);
                    }
                }
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
                for(int k = 0; k<PipeTypeDataList.get(pipeTypeIndex).getPipeSubType().get(pipeSubTypeIndex).getPipeDia().size(); k++){
                    if(PipeTypeDataList.get(pipeTypeIndex).getPipeSubType().get(pipeSubTypeIndex).getPipeDia().get(k).equals(diameter)){
                        spinnerDiameter.setSelection(k);
                    }
                }
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

    private void setSoilData(){
        String  soilData=  SessionUtil.getSoil(context);
        soilNameList.clear();
        try {
            JSONObject djob = new JSONObject(soilData);

            JSONArray darr = djob.optJSONArray("data");
            if (darr != null) {
                Log.e("soil", darr.toString());
                for (int i = 0; i < darr.length(); i++) {

                    soilNameList.add(new ItemModel(darr.getJSONObject(i).getString("id"),darr.getJSONObject(i).getString("name")));

                }
            }
            soilAdapter= new ArrayAdapter(context,  R.layout.layoutspinner, soilNameList);


            spinnerSoil.setAdapter(soilAdapter);

            soilAdapter.notifyDataSetChanged();


        } catch (JSONException ignored) {

        }
        spinnerSoil.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                soilID = soilNameList.get(i).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    private void setAlignmentData(){
        String  alignmentData=  SessionUtil.getAlignment(context);
        alignmentNameList.clear();
        try {
            JSONObject djob = new JSONObject(alignmentData);

            JSONArray darr = djob.optJSONArray("data");
            if (darr != null) {
                Log.e("alignment", darr.toString());
                for (int i = 0; i < darr.length(); i++) {

                    alignmentNameList.add(new ItemModel(darr.getJSONObject(i).getString("id"),darr.getJSONObject(i).getString("name")));

                }
            }
            alignmentAdapter= new ArrayAdapter(context,  R.layout.layoutspinner, alignmentNameList);


            spinAlignment.setAdapter(alignmentAdapter);

            alignmentAdapter.notifyDataSetChanged();


        } catch (JSONException ignored) {

        }
        spinAlignment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                alignmentID = alignmentNameList.get(i).getId();
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
    private void setFittingData(){
        final String  fittingData=  SessionUtil.getFitting(context);
        fittingList.clear();
        try {
            JSONObject djob = new JSONObject(fittingData);

            JSONArray darr = djob.optJSONArray("data");
            if (darr != null) {
                Log.e("fitting", darr.toString());
                for (int i = 0; i < darr.length(); i++) {

                    fittingList.add(new ItemModel(darr.getJSONObject(i).getString("id"),darr.getJSONObject(i).getString("fitting_name")));

                }
            }
            fittingAdapter= new ArrayAdapter(context,  R.layout.layoutspinner,  fittingList);


            spinnerIntermediate.setAdapter(fittingAdapter);

            fittingAdapter.notifyDataSetChanged();


        } catch (JSONException ignored) {

        }
        spinnerIntermediate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                fittingID = fittingList.get(i).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
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
        // serviceIntent = new Intent(LayingOfflineActivity.this, LocationService.class);
        //startService(serviceIntent);
        // LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter("app.location.via.service"));
    }

    @Override
    protected void onPause() {
        super.onPause();
        //  stopService(serviceIntent);
        //  LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Really Exit?")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        LayingManualActivity.super.onBackPressed();
                    }
                }).create().show();
    }

    private void startLocationListener() {

        long mLocTrackingInterval = 1000 * 1*15; // 15 sec
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
          /* if (DialogUtil.checkInternetConnection(this)) {
                // loadingDialog.onShow();

                if (flag) {
                    flag = false;
                    if (!progressLoading.isShowing()) {
                        progressLoading.onShow();
                    }
                    RequestQueue queue = Volley.newRequestQueue(this);

                    final String url = AppConstants.APP_BASE_URL+"insertLaying";
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
                                        String status=         jsonObject.getString("success");
                                        String data=         jsonObject.getString("data");

                                        if (data.contains("Success")) {
                                            //Toast.makeText(context, "Successfully submitted", Toast.LENGTH_LONG).show();
                                            Toast.makeText(context, data, Toast.LENGTH_LONG).show();
                                            arrayDepth.clear();
                                            arrayNextLongitude.clear();
                                            arrayNextLatitude.clear();
                                            arrayNextAltitude.clear();
                                            arrayNextWidthList.clear();
                                           // arrayNextLengthList.clear();
                                            arrayIntermediateList.clear();
                                            editRemarks.setText("");
                                           // editStartAltitude.setText("");
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
                                         //   spinnerType.setText("");
                                           // spinnerSubType.setText("");
                                          //  spinnerDiameter.setText("");
                                           // typeID="";subtypeID=""; diameterID="";startNodeID=""; endNodeID="";

                                            editStartWidth.setText("");
                                            editEndWidth.setText("");

                                            imgStartPhoto.setImageResource(android.R.color.transparent);
                                            imgEndPhoto.setImageResource(android.R.color.transparent);


                                        }else{
                                            Toast.makeText(context," status: "+status+" message: "+data,Toast.LENGTH_SHORT ).show();
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
                        protected Map<String, String> getParams() {*/
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("type", "laying");

            params.put("user_id", SessionUtil.getUserId(context));
            params.put("network_type", SessionUtil.getNetwork(context)+ "");
            params.put("zone", zone_id+ "");
            params.put("dma", dmaName+ "");
                 params.put("pipe_number",  editPipeSegmentOther.getText().toString().trim() + "");
                params.put("pipe_manual","1");

                params.put("start_node_manual","1");
                params.put("start_node", spinnerStartNode.getText().toString().trim() + "");

                params.put("end_node_manual","1");
                params.put("end_node", spinnerEndNode.getText().toString().trim() + "");



            params.put("end_node", spinnerEndNode.getText().toString().trim() + "");

            params.put("pipe_type", typeID+ "");
            params.put("pipe_sub_type", subtypeID + "");
            params.put("pipe_sub_type_dia", diameterID + "");
            params.put("scope", editScope.getText().toString() + "");
            params.put("laying_length", editLength.getText().toString() + "");
            params.put("start_latitude", editStartLatitude.getText().toString() + "");
            params.put("start_longitude", editStartLongitude.getText().toString() + "");
            params.put("start_trenching_width", editStartWidth.getText().toString() + "");
            params.put("start_trenching_depth", editStartDepth.getText().toString() + "");

            params.put("end_latitude", editEndLatitude.getText().toString() + "");
            params.put("end_longitude", editEndLongitude.getText().toString() + "");
            params.put("end_trenching_width", editEndWidth.getText().toString() + "");
            params.put("end_trenching_depth", editEndDepth.getText().toString() + "");
            params.put("intermediate_point", arrayIntermediateList.toString().replace("[","").replace("]","") + "");

            params.put("intermediate_lat", arrayNextLatitude.toString().replace("[","").replace("]","")  + "");
            params.put("intermediate_long", arrayNextLongitude.toString().replace("[","").replace("]","")  + "");
            params.put("intermediate_width", arrayNextWidthList.toString().replace("[","").replace("]","")  + "");
            params.put("intermediate_depth", arrayDepth.toString().replace("[","").replace("]","")  + "");
            params.put("schema", SessionUtil.getSchema(context) + "");


            params.put("soil_type", soilID + "");
            params.put("alignment", alignmentID+ "");

            params.put("contractor", contractorID+ "");
            params.put("backfilling", spinnerBackfill.getSelectedItem().toString() + "");

            params.put("remarks", editRemarks.getText().toString() + "");
            params.put("laying_date", editDate.getText().toString() + "");


            Log.e("Pppppppppppppppppparams", params.toString());
            boolean s = dataBaseHelper.newRowInsert(params);
            if (s) {

                clear();
                Toast.makeText(context, "Data Locally Saved", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(context, "Not Inserted", Toast.LENGTH_LONG).show();
            }
                           /* return params;
                        }
                    };

                    // Add the request to the RequestQueue.
                    stringRequest.setRetryPolicy(new DefaultRetryPolicy(600000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                    queue.add(stringRequest);
                }
            } else {
                DialogUtil.showNoConnectionDialog(this);
            }*/
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
        if(editStartDepth.getText().toString().trim().equals("")||editStartWidth.getText().toString().trim().equals(""))
        {
            valid = false;
            Toast.makeText(context,"Enter Start Depth and Width",Toast.LENGTH_SHORT).show();
        }
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
        if(editLength.getText().toString().trim().equals(""))
        {
            valid = false;
            Toast.makeText(context,"Enter Length",Toast.LENGTH_SHORT).show();
        }
        if(editRemarks.getText().toString().trim().equals(""))
        {
            valid = false;
            Toast.makeText(context,"Enter Remarks",Toast.LENGTH_SHORT).show();
        }



        return valid;
    }
    private void clear()
    {
        arrayDepth.clear();
        arrayNextLongitude.clear();
        arrayNextLatitude.clear();
        //   arrayNextAltitude.clear();
        arrayNextWidthList.clear();
        // arrayNextLengthList.clear();
        arrayIntermediateList.clear();
        editRemarks.setText("");
        editPipeSegmentOther.setText("");
        // editStartAltitude.setText("");
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
        editScope.setText("");
        //   spinnerType.setText("");
        // spinnerSubType.setText("");
        //  spinnerDiameter.setText("");
        // typeID="";subtypeID=""; diameterID="";startNodeID=""; endNodeID="";

        editStartWidth.setText("");
        editEndWidth.setText("");

    }




}