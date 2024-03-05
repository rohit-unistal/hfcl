package unistal.com.citywaterhfcl;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class LoginActivity extends AppCompatActivity {
    @BindView(R.id.enter_user_name)
    EditText userName;
    @BindView(R.id.enter_password)
    EditText passWord;
    @BindView(R.id.btn_log_in)
    Button btnLogIn;
    @BindView(R.id.imgback)
    ImageView imgBack;
    @BindView(R.id.imglogout)
    ImageView imgLogout;
    Map dbparams = new HashMap();
    private ProgressLoading progressLoading;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        ButterKnife.bind(this);
        imgLogout.setVisibility(View.GONE);
        imgBack.setVisibility(View.GONE);
        progressLoading = new ProgressLoading(this);
        context = this;
        HttpsTrustManager.allowAllSSL();
        init();
        //registerReceiver();
    }/**
         * This method is responsible to register receiver with NETWORK_CHANGE_ACTION.
         * */


        @Override
        protected void onDestroy()
        {
            super.onDestroy();
        }

        /**
         * This is internal BroadcastReceiver which get status from external receiver(NetworkChangeReceiver)
         * */
       /* InternalNetworkChangeReceiver internalNetworkChangeReceiver = new InternalNetworkChangeReceiver();
        class InternalNetworkChangeReceiver extends BroadcastReceiver
        {
            @Override
            public void onReceive(Context context, Intent intent) {


                    userName.setText(intent.getStringExtra("status"));
            }
        }*/
    private void init(){

        if (!SessionUtil.getUserId(getApplicationContext()).equals("")) {
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            finish();
        }


    }




    @OnClick(R.id.btn_log_in)
    public void login()
    {
        progressLoading.onShow();
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        String url = AppConstants.APP_Login_URL;
        Map<String, String> params = new HashMap<String, String>();
        params.put("email",userName.getText().toString().trim());
        params.put("password",passWord.getText().toString().trim());

        JSONObject objRegData = new JSONObject(params);
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, url, objRegData, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressLoading.dismiss();
                        Log.e("Response",response.toString());
                        try {
                            JSONObject jsonObject = new JSONObject(response.toString());
                            String token = jsonObject.optString("token");
                            SessionUtil.saveToken(token,context);
                            JSONObject user =  jsonObject.optJSONObject("user");
                            String id = user.optString("id");
                            SessionUtil.saveUserId(id,context);
                            String schema = user.optString("schema");
                            SessionUtil.saveSchema(schema,context);
                            String ga_id = user.optString("ga_id");
                            SessionUtil.saveGA(ga_id,context);
                            String messages = jsonObject.optString("messages");
                            if(jsonObject.optString("messages").contains("success")){
                                startActivity(new Intent(LoginActivity.this,HomeActivity.class));
                                Toast.makeText(getApplicationContext(),messages,Toast.LENGTH_LONG).show();
                                  /*  setWBS();
                                    setDiameter();
                                    setPipeSide();
                                    setMDPEMethods();
                                    setInterMediatePoint();
                                    setRoadOwner();setPointType();setRoadType();*/
                            }
                            // handle response data
                        } catch (JSONException e) {
                            e.printStackTrace();
                            //pDialog.show();
                        }

                    }
                }, new Response.ErrorListener() {


                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressLoading.dismiss();
                        if(error.toString().contains("AuthFailureError"))
                        {
                            Toast.makeText(context, "Please enter correct username and password", Toast.LENGTH_SHORT).show();

                        }else
                        if (error!=null)
                        {
                            Toast.makeText(context, ""+ error.toString(), Toast.LENGTH_SHORT).show();}
                        else{
                            Toast.makeText(context, "error", Toast.LENGTH_SHORT).show();}
                        //  pDialog.hide();
                    }
                })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json; charset=utf-8");

              //  params.put("Content-Type", "text/plain");
                return params;
            }
        };


        requestQueue.add(jsObjRequest);
    }
   /* public void logIn() {
       // if(DialogUtil.checkInternetConnection(this)) {
            progressLoading.onShow();
            RequestQueue queue = Volley.newRequestQueue(this);

            final String url = AppConstants.APP_Login_URL;
            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(LoginActivity.this, response, Toast.LENGTH_SHORT).show();
                            Log.e("Response ", response);

                            progressLoading.dismiss();

                            try {


                                    JSONObject jsonObject = new JSONObject(response);
                                    progressLoading.dismiss();
                                    if (jsonObject.getString("msg").contains("success"))
                                {
                                    JSONArray dataArray = jsonObject.getJSONArray("data");
                                    JSONObject dataObject = (JSONObject)dataArray.get(0);
                                    SessionUtil.saveUserId(dataObject.getString("id"), LoginActivity.this);
                                    SessionUtil.saveGA(dataObject.getString("ga_name"), LoginActivity.this);
                                    SessionUtil.saveUserName(dataObject.getString("username"), LoginActivity.this);
                                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                    startActivity(intent);
                                }else{
                                        Toast.makeText(LoginActivity.this, " " + jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();

                                    }

                            }
                            catch (JSONException e)
                            {

                            }




                                progressLoading.dismiss();


                        }


                    }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(LoginActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                    progressLoading.dismiss();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                   // params.put("type", "login");
                    params.put("username", userName.getText().toString());
                    params.put("password", passWord.getText().toString());
                    dbparams = params;
                    return params;
                }

            };

        stringRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 30000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 0;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
            // Add the request to the RequestQueue.
            queue.add(stringRequest);
        *//*}else{
            DialogUtil.showNoConnectionDialog(this);
        }*//*
    }*/


}