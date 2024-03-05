package unistal.com.citywaterhfcl;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecordCrossingActivity extends AppCompatActivity implements NetworkStateReceiver.NetworkStateReceiverListener{
    @BindView(R.id.lv)
    ListView lv;
    @BindView(R.id.btnwifi)
    ImageView WifiButton;
    @BindView(R.id.btnMobile)
    ImageView MobileButton;
    @BindView(R.id.btnsyncall)
    Button btnsyncall;


    private NetworkStateReceiver networkStateReceiver;
    Context context;
    DataBaseHelper dataBaseHelper ;
    ArrayList<HashMap<String, String>> recordsArrayList;
    // ArrayAdapter lvAdapter;
    CustomListAdapter lvAdapter;
    Bitmap bitmap;
    ProgressLoading progressLoading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_crossing);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ButterKnife.bind(this);
        context = this;
        netConnectivity();
        init();
    }
    private void init(){
        progressLoading = new ProgressLoading(context);
        recordsArrayList = new ArrayList();
        dataBaseHelper = new DataBaseHelper(context);
        recordsArrayList = dataBaseHelper.getnewCrossingData();
        lvAdapter = new CustomListAdapter(context,recordsArrayList);
        lvAdapter.notifyDataSetChanged();
        lv.setAdapter(lvAdapter);
       /* lvAdapter = new ArrayAdapter(context,R.layout.simple_spinner_item,recordsArrayList);
        lvAdapter.notifyDataSetChanged();
        lv.setAdapter(lvAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                uploadBitmap(recordsArrayList.get(i));
            }
        });*/

       /* lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                uploadBitmap(recordsArrayList.get(i));
            }
        });*/
        btnsyncall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                for(int i =0 ; i <recordsArrayList.size();i++)
                {
                    if(isOnline()){
                        uploadBitmap(recordsArrayList.get(i));}
                    else{
                        Toast.makeText(context,"Internet disconnected",Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
            }
        });



    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);

        return byteArrayOutputStream.toByteArray();
    }
    private void uploadBitmap(final HashMap<String, String> hashMap) {
        if(!progressLoading.isShowing()) {
            progressLoading.onShow();
        }
        // bitmap = getResizedBitmap(BitmapFactory.decodeFile(hashMap.get("graph_photo")),4*1024*1024);
        if(hashMap.get("photo")!=null&&hashMap.get("photo")!="null" &&(!hashMap.get("photo").trim().isEmpty()))
        {
            bitmap = BitmapFactory.decodeFile(hashMap.get("photo"));}else{
            bitmap = null;
        }
        final String columnid = hashMap.get("COLUMN_ID");
        final String url = AppConstants.APP_BASE_URL+"insertcrossing";
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST,url,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        progressLoading.dismiss();

                        try {
                            JSONObject jsonObject = new JSONObject(new String(response.data));
                            String status=         jsonObject.getString("success");
                            String message=         jsonObject.getString("data");

                            if (message.contains("success")) {

                                Toast.makeText(context, message, Toast.LENGTH_LONG).show();

                                boolean d = dataBaseHelper.DeleteRow(columnid, "Crossing_Table");

                                //    Toast.makeText(getApplicationContext(), "Record is deleted " + d, Toast.LENGTH_SHORT).show();

                                if (d) {
                                    //     Toast.makeText(getApplicationContext(), d + "", Toast.LENGTH_SHORT).show();
                                    recordsArrayList.clear();
                                    recordsArrayList = dataBaseHelper.getnewCrossingData();
                                    // lvAdapter = new ArrayAdapter(context,R.layout.simple_spinner_item,recordsArrayList);
                                    lvAdapter = new CustomListAdapter(context, recordsArrayList);
                                    lvAdapter.notifyDataSetChanged();
                                    lv.setAdapter(lvAdapter);
                                } else {
                                    Toast.makeText(getApplicationContext(), d+"", Toast.LENGTH_SHORT).show();

                                }

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
                        Toast.makeText(context, error.networkResponse.statusCode+ error.getMessage()+"", Toast.LENGTH_SHORT).show();

                        if(error!=null){
                            try {
                                String responseString =  new String(error.networkResponse.data);
                                JSONObject obj = new JSONObject(responseString);
                                if(responseString.contains("success")) {
                                    // Toast.makeText(getApplicationContext(), obj.getString("data"), Toast.LENGTH_SHORT).show();
                                    Log.e("Not GotError response", "" + obj.getString("success"));


                                    Log.e("GotError", "" + obj.getString("data"));
                                    Toast.makeText(context, "status = " + obj.getString("success") + " messages = " + obj.getString("data"), Toast.LENGTH_LONG).show();
                                }
                                if(responseString.contains("success")) {
                                    // Toast.makeText(getApplicationContext(), obj.getString("data"), Toast.LENGTH_SHORT).show();
                                    Log.e("Not GotError response", "" + obj.getString("success"));


                                    Log.e("GotError", "" + obj.getString("data"));
                                    Toast.makeText(context, "status = " + obj.getString("success") + " messages = " + obj.getString("data"), Toast.LENGTH_LONG).show();
                                }

                            } catch (JSONException e) {
                                Toast.makeText(context,e.getMessage() +" "+ e.toString(),Toast.LENGTH_SHORT).show();
                                Log.e("Not GotError response",e.toString());
                                e.printStackTrace();
                            }
                        }}
                }) {


            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                if(hashMap.get("photo")!=null&&hashMap.get("photo")!="null" &&(!hashMap.get("photo").trim().isEmpty()))
                {
                    params.put("photo", new DataPart(imagename + ".png", getFileDataFromDrawable(bitmap)));
                    Log.e("GotError photo", hashMap.get("photo"));
                }



                return params;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("schema", hashMap.get("schema"));
                params.put("user_id", hashMap.get("user_id"));
                params.put("start_latitude", hashMap.get("start_latitude"));
                params.put("start_longitude", hashMap.get("start_longitude"));
                params.put("end_latitude", hashMap.get("end_latitude"));
                params.put("end_longitude", hashMap.get("end_longitude"));

                params.put("crossing_class", hashMap.get("crossing_class"));
                params.put("crossing_type", hashMap.get("crossing_type"));
                params.put("crossing_casing", hashMap.get("crossing_casing"));
                params.put("length", hashMap.get("length"));
                params.put("owner_name", hashMap.get("owner_name"));
                params.put("crossing_position", hashMap.get("crossing_position"));

                params.put("pipeline_position", hashMap.get("pipeline_position"));
                params.put("permission_authority", hashMap.get("permission_authority"));

                params.put("remarks",  hashMap.get("remarks"));
                params.put("contractor_id",  hashMap.get("contractor_id"));
                Log.e("Pppppppppppppppppparams", params.toString());
                return params;

            }


        };

        //adding the request to volley
        volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                1000000,
                1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(this).add(volleyMultipartRequest);
    }

    @Override
    public void networkAvailable() {
        MobileButton.setBackgroundResource( R.drawable.mobile_green);
    }

    @Override
    public void networkUnavailable() {
        MobileButton.setBackgroundResource( R.drawable.mobile_red);
    }
    @Override
    public void networkWifiAvailable() {
        WifiButton.setBackgroundResource( R.drawable.wifi_green);
    }

    @Override
    public void networkWifiUnavailable() {
        WifiButton.setBackgroundResource( R.drawable.wifi_red);
    }

    /* public static Bitmap getResizedBitmap(Bitmap reduced_bitmap, int maxSize) {
     *//* int width = image.getWidth();
        int height = image.getHeight();



        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 0) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        Bitmap reduced_bitmap = Bitmap.createScaledBitmap(image, width, height, true);*//*
        if(reduced_bitmap.getAllocationByteCount() > (maxSize)) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            // Must compress the Image to reduce image size to make upload easy
            reduced_bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
            return getResizedBitmap(reduced_bitmap, maxSize);
        } else {
            return reduced_bitmap;
        }
    }*/
    public void netConnectivity()
    {
        networkStateReceiver = new NetworkStateReceiver();
        networkStateReceiver.addListener(this);
        this.registerReceiver(networkStateReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

    }
    @Override
    public void onDestroy() {
        try {
            networkStateReceiver.removeListener(this);
            if(networkStateReceiver!=null){
                this.unregisterReceiver(networkStateReceiver);}
        }catch (Exception e){

        }

        super.onDestroy();
    }

    public class CustomListAdapter extends BaseAdapter {
        private Context context; //context
        private ArrayList<HashMap<String, String>> items; //data source of the list adapter

        //public constructor
        public CustomListAdapter(Context context, ArrayList<HashMap<String, String>> items) {
            this.context = context;
            this.items = items;
        }

        @Override
        public int getCount() {
            return items.size(); //returns total of items in the list
        }

        @Override
        public Object getItem(int position) {
            return items.get(position); //returns list item at the specified position
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // inflate the layout for each list row
            if (convertView == null) {
                convertView = LayoutInflater.from(context).
                        inflate(R.layout.layout_list_view_row_items, parent, false);
            }

            // get current item to be displayed
            final HashMap<String, String> currentItem = (HashMap<String, String>) getItem(position);

            // get the TextView for item name and item description
            TextView textViewItemName = (TextView)
                    convertView.findViewById(R.id.tvschema);
            TextView textViewItemDescription = (TextView)
                    convertView.findViewById(R.id.tvRemarks);
            ImageView imgAsync = (ImageView)convertView.findViewById(R.id.imageASync);
            //sets the text for item name and item description from the current item object
            textViewItemName.setText(currentItem.get("installed_date"));
            textViewItemDescription.setText(currentItem.get("remarks"));
            imgAsync.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context,"Record is press for uploading",Toast.LENGTH_SHORT).show();
                    if(isOnline()){
                        uploadBitmap(items.get(position));}
                    else{
                        Toast.makeText(context,"Internet disconnected",Toast.LENGTH_SHORT).show();
                    }
                    recordsArrayList.clear();
                    recordsArrayList = dataBaseHelper.getValveData();
                    lvAdapter = new CustomListAdapter(context,recordsArrayList);
                    lvAdapter.notifyDataSetChanged();
                    lv.setAdapter(lvAdapter);
                }
            });

            // returns the view for the current row
            return convertView;
        }
    }

    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }


}