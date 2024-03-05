package unistal.com.citywaterhfcl;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
//import android.support.v7.app.AppCompatActivity;
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

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

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

public class RemoveGap extends AppCompatActivity {
    @BindView(R.id.edit_date)
    EditText editDate;
    @BindView(R.id.spin_network)
    Spinner spinnerNetwork;
    @BindView(R.id.spin_zone)
    Spinner spinnerZone;
    @BindView(R.id.spin_dma)
    Spinner spinnerDMA;
    @BindView(R.id.spin_pipe_segment)
    Spinner spinpipesegment;
    @BindView(R.id.spingap)
    Spinner spinGap;
    @BindView(R.id.edit_remarks)
    EditText editRemarks;
    @BindView(R.id.btnfetchgap)
    Button buttonFetchGap;
    @BindView(R.id.btnSubmit)
    Button buttonSubmit;
    @BindView(R.id.imgback)
    ImageView imgBack;
    @BindView(R.id.imglogout)
    ImageView imgLogout;

    @BindView(R.id.titletv)
    TextView citytv;
    ProgressLoading progressLoading;
    Context context;
    ArrayList networkList,dmaList, zoneList,PipeNumberList,gapList,idList,remarksList;
    ArrayAdapter zoneAdapter,dmaAdapter,pipeAdapter,gapAdapter,networkAdapter;
    String zoneName,   dmaName,pipeName,gapName,idName,remarksName,networkName;
    DataBaseHelper dataBaseHelper;
    Boolean flag = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_gap);
        ButterKnife.bind(this);
        context = this;
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().hide();
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        citytv.setText("Remove Gap");
        imgLogout.setVisibility(View.INVISIBLE);
        init();
    }
    private void init(){

        dataBaseHelper = new DataBaseHelper(context);
        progressLoading = new ProgressLoading(context);
        networkList = new ArrayList();
        zoneList = new ArrayList();
        dmaList = new ArrayList();
        PipeNumberList = new ArrayList();
        gapList = new ArrayList();
        idList = new ArrayList();
        remarksList = new ArrayList();
        sheetNetwork();
      //  sheetZone();
        editDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRouteSurveyDate();
            }
        });
        getCurrentDate();
        buttonFetchGap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sheetGapData();
            }
        });
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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

            final String url = AppConstants.APP_BASE_URL+"allzone?schema="+SessionUtil.getSchema(context)+"&network_type="+networkName;
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
                    error.getMessage();
                    progressLoading.dismiss();
                    Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            // Add the request to the RequestQueue.
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

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



            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    public void sheetGapData(){
        if (DialogUtil.checkInternetConnection(this)) {
            // loadingDialog.onShow();
            if (!progressLoading.isShowing()) {
                progressLoading.onShow();
            }

            RequestQueue queue = Volley.newRequestQueue(this);

            final String url = AppConstants.APP_BASE_URL+"getAllGaps?schema="+SessionUtil.getSchema(context)+"&zone="+zoneName;//+"&dma="+dmaName;
            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("Response", response);

                            //loadingDialog.dismiss();
                            //  SessionUtil.saveNodes(response,context);
                            //  Toast.makeText(context, "Saved Nodes",Toast.LENGTH_SHORT).show();
                            dataBaseHelper.deleteAll("tbl_remove_gap_list_data");
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if(jsonObject.optString("data").trim().isEmpty()){
                                    Toast.makeText(getApplicationContext(), "No gap found", Toast.LENGTH_LONG).show();

                                }else{
                                    JSONArray pipes = new JSONArray( jsonObject.optString("data"));

                                    if(pipes.length()>0) {


                                        if (!progressLoading.isShowing()) {
                                            progressLoading.onShow();
                                        }


                                        for (int j = 0; j < pipes.length(); j++) {
                                            JSONObject jsonfitting = pipes.getJSONObject(j);
                                            ContentValues namelist = new ContentValues();
                                            namelist.put("dma", jsonfitting.getString("dma"));
                                            namelist.put("pipe_number", jsonfitting.optString("pipe_number"));
                                            namelist.put("gap_id", jsonfitting.optString("gap_id"));
                                            namelist.put("remarks", jsonfitting.optString("remarks"));
                                            namelist.put("id", jsonfitting.optString("id"));
                                            Log.e("gap_data", jsonfitting.toString());

                                            dataBaseHelper.insert("tbl_remove_gap_list_data", namelist);


                                            if (j == pipes.length() - 1) {
                                                // progressLoading.dismiss();

                                            }
                                        }
                                    }else{
                                        Toast.makeText(getApplicationContext(), "No gap found", Toast.LENGTH_LONG).show();

                                    }


                                }
                                // dataBaseHelper.close();
                            } catch (Exception ignored) {
                                Toast.makeText(getApplicationContext(), "Exception Gap " + ignored, Toast.LENGTH_LONG).show();
                            }
                            progressLoading.dismiss();
                            getDMA();
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

    }
    public void getDMA(){
        dmaList.clear();

        Cursor cur = dataBaseHelper.getAll("SELECT DISTINCT dma" +
                " FROM tbl_remove_gap_list_data ;");
        if (cur.getCount() > 0) {
            if (cur.moveToFirst()) {
                do {


                    dmaList.add(cur.getString(0));
                } while (cur.moveToNext());
            }
        } else {
            setPipeNumber();
            //  Toast.makeText(RemoveGap.this, "No dma found or Please update Intermediate Point details", Toast.LENGTH_SHORT).show();
        }

        dmaAdapter = new ArrayAdapter(context, R.layout.layoutspinner, dmaList); //selected item will look like a spinner set from XML
        dmaAdapter.setDropDownViewResource(R.layout.layoutspinner);
        spinnerDMA.setAdapter(dmaAdapter);
        dmaAdapter.notifyDataSetChanged();
        spinnerDMA.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                dmaName= dmaList.get(i).toString();
                setPipeNumber();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                setPipeNumber();
            }
        });



    }

    private void setPipeNumber(){

        PipeNumberList.clear();
        Cursor cur = dataBaseHelper.getAll("SELECT DISTINCT pipe_number" +
                " FROM tbl_remove_gap_list_data where  dma ='"+dmaName+ "';");
        if (cur.getCount() > 0) {
            if (cur.moveToFirst()) {
                do {


                    PipeNumberList.add(cur.getString(0));
                } while (cur.moveToNext());
            }
        }else{
            setGap();
        }
        pipeAdapter= new ArrayAdapter(context,  android.R.layout.simple_spinner_dropdown_item, PipeNumberList);


        spinpipesegment.setAdapter(pipeAdapter);

        pipeAdapter.notifyDataSetChanged();



        spinpipesegment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                pipeName = PipeNumberList.get(i).toString();
                setGap();
                //spinnerType.setText(PipeNumberDataList.get(i).getPipeData().getPipeType());
                //spinnerSubType.setText(PipeNumberDataList.get(i).getPipeData().getSubType());
                //spinnerDiameter.setText(PipeNumberDataList.get(i).getPipeData().getPipeDiameter());
                //   Toast.makeText(context,PipeNumberDataList.get(i).getPipeNumber()+ " Selected", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                setGap();
            }
        });

    }
    private void setGap(){

        gapList.clear();
        idList.clear();
        remarksList.clear();
        Cursor cur = dataBaseHelper.getAll("SELECT DISTINCT  gap_id,id,remarks" +
                " FROM tbl_remove_gap_list_data where dma ='"+dmaName+ "'and pipe_number ='"+pipeName+ "';");
        if (cur.getCount() > 0) {
            if (cur.moveToFirst()) {
                do {


                    gapList.add(cur.getString(0));
                    idList.add(cur.getString(1));
                    remarksList.add(cur.getString(2));
                } while (cur.moveToNext());
            }
        }
        gapAdapter= new ArrayAdapter(context,  android.R.layout.simple_spinner_dropdown_item, gapList);


        spinGap.setAdapter(gapAdapter);

        gapAdapter.notifyDataSetChanged();



        spinGap.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                gapName= gapList.get(i).toString();
                if (remarksList.get(i) == null ) {
                    editRemarks.setText("");

                }else{
                    editRemarks.setText(remarksList.get(i).toString());}
                idName = idList.get(i).toString();
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
    @OnClick(R.id.btnSubmit)
    public void sheetSubmit() {

        if (DialogUtil.checkInternetConnection(this)) {
            // loadingDialog.onShow();
            if (!progressLoading.isShowing()) {
                progressLoading.onShow();
            }
            if (flag) {
                flag = false;

                RequestQueue queue = Volley.newRequestQueue(this);

                final String url = AppConstants.APP_BASE_URL+"removeGaps";
                // Request a string response from the provided URL.
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new com.android.volley.Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                progressLoading.dismiss();
                                //  Toast.makeText(context, response, Toast.LENGTH_LONG).show();
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    String status = jsonObject.getString("success");
                                    String message = jsonObject.getString("data");

                                    if (message.contains("success")) {
                                        //Toast.makeText(context, "Successfully submitted", Toast.LENGTH_LONG).show();
                                        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                                        sheetGapData();



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
                        error.getMessage();
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


                        params.put("zone", zoneName + "");
                        params.put("dma", dmaName + "");

                        params.put("id", idName + "");
                        params.put("pipe_number", pipeName+ "");

                        params.put("gap_close_date", editDate.getText().toString() + "");

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