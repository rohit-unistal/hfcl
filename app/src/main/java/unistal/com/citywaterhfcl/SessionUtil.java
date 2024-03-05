package unistal.com.citywaterhfcl;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionUtil {
    private static final String USER_SESSION_PREF = "SessionPrefCityWaterSmartHFCL123";
    public static final String USER_ID = "userID";
    public static final String USER_TYPE_ID = "userTypeID";
    private static final String USER_NAME = "userName";
    private static final String DEVICE_ID = "deviceId";
    private static final String BP_NUMBER = "bpNumber";
    private static final String METER_NUMBER = "meterNumber";
    private static final String TOKEN = "token";
    private static final String SCHEMA = "schema";
    private static final String Nodes = "nodes";
    private static final String Soil = "soil";
    private static final String Alignment = "alignment";
    private static final String Fitting = "fitting";
    private static final String Contractor = "contractor";
    private static final String Zone = "zone";
    private static final String Network = "network";
    private static final String DMA = "dma";
    private static final String Pipe = "pipe";
    private static final String GA = "GA";
    private static final String lengthOfConnection = "lengthOfConnection";
    private static final String waterStorage = "waterStorage";
    public static SharedPreferences getUserSessionPreferences(Context context) {
        return context.getSharedPreferences(USER_SESSION_PREF, Context.MODE_PRIVATE);
    }

    public static void removeUserDetails(Context context) {
        final SharedPreferences prefs = getUserSessionPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
       /* editor.remove(USER_ID);
        editor.remove(USER_TYPE_ID);
        editor.remove(USER_NAME);
        editor.remove(BP_NUMBER);
        editor.remove(METER_NUMBER);*/
       editor.clear();
       editor.commit();
    }

    public static void saveUserId(String userId, Context context) {
        SharedPreferences preferences = getUserSessionPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(USER_ID, userId);
        editor.commit();
    }

    public static String getUserId(Context context) {
        SharedPreferences preferences = getUserSessionPreferences(context);
        return preferences.getString(USER_ID, "");
    }


    public static void saveGA(String userId, Context context) {
        SharedPreferences preferences = getUserSessionPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(GA, userId);
        editor.commit();
    }

    public static String getGA(Context context) {
        SharedPreferences preferences = getUserSessionPreferences(context);
        return preferences.getString(GA, "");
    }



    public static void saveUserTypeId(String userId, Context context) {
        SharedPreferences preferences = getUserSessionPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(USER_TYPE_ID, userId);
        editor.commit();
    }

    public static String getUserTypeId(Context context) {
        SharedPreferences preferences = getUserSessionPreferences(context);
        return preferences.getString(USER_TYPE_ID, "");
    }

    public static void saveUserName(String userName, Context context) {
        SharedPreferences preferences = getUserSessionPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(USER_NAME, userName);
        editor.commit();
    }

    public static String getUserName(Context context) {
        SharedPreferences preferences = getUserSessionPreferences(context);
        return preferences.getString(USER_NAME, "");
    }


    public static void saveDeviceId(String deviceId, Context context) {
        SharedPreferences preferences = getUserSessionPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(DEVICE_ID, deviceId);
        editor.commit();
    }

    public static String getDeviceId(Context context) {
        SharedPreferences preferences = getUserSessionPreferences(context);
        return preferences.getString(DEVICE_ID, "");
    }
    public static void saveBPNumber(String bpNumber, Context context)
    {
        SharedPreferences sharedPreferences = getUserSessionPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(BP_NUMBER,bpNumber);
        editor.commit();
    }
    public static void saveMeterNumber(String meterNumber, Context context)
    {
        SharedPreferences sharedPreferences = getUserSessionPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(METER_NUMBER,meterNumber);
        editor.commit();
    }
    public static String getBpNumber(Context context)
    {
        SharedPreferences preferences = getUserSessionPreferences(context);
        return preferences.getString(BP_NUMBER,"");
    }
    public static String getMeterNumber(Context context)
    {
        SharedPreferences preferences = getUserSessionPreferences(context);
        return preferences.getString(METER_NUMBER, "");

    }
    public static void saveToken(String token, Context context) {
        SharedPreferences preferences = getUserSessionPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(TOKEN, token);
        editor.commit();
    }

    public static String getToken(Context context) {
        SharedPreferences preferences = getUserSessionPreferences(context);
        return preferences.getString(TOKEN, "");
    }
    public static void saveSchema(String schema, Context context) {
        SharedPreferences preferences = getUserSessionPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(SCHEMA, schema);
        editor.commit();
    }

    public static String getSchema(Context context) {
        SharedPreferences preferences = getUserSessionPreferences(context);
        return preferences.getString(SCHEMA, "");
    }
    public static void saveNodes(String nodes, Context context) {
        SharedPreferences preferences = getUserSessionPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Nodes, nodes);
        editor.commit();
    }

    public static String getNodes(Context context) {
        SharedPreferences preferences = getUserSessionPreferences(context);
        return preferences.getString(Nodes, "");
    }
    public static void saveSoil(String soil, Context context) {
        SharedPreferences preferences = getUserSessionPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Soil, soil);
        editor.commit();
    }

    public static String getSoil(Context context) {
        SharedPreferences preferences = getUserSessionPreferences(context);
        return preferences.getString(Soil, "");
    }
    public static void saveAlignment(String alignment, Context context) {
        SharedPreferences preferences = getUserSessionPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Alignment, alignment);
        editor.commit();
    }

    public static String getAlignment(Context context) {
        SharedPreferences preferences = getUserSessionPreferences(context);
        return preferences.getString(Alignment, "");
    }
    public static void saveFitting(String fitting, Context context) {
        SharedPreferences preferences = getUserSessionPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Fitting, fitting);
        editor.commit();
    }

    public static String getFitting(Context context) {
        SharedPreferences preferences = getUserSessionPreferences(context);
        return preferences.getString(Fitting, "");
    }
    public static void saveContractor(String contractor, Context context) {
        SharedPreferences preferences = getUserSessionPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Contractor, contractor);
        editor.commit();
    }

    public static String getContractor(Context context) {
        SharedPreferences preferences = getUserSessionPreferences(context);
        return preferences.getString(Contractor, "");
    }
    public static void saveZone(String zone, Context context) {
        SharedPreferences preferences = getUserSessionPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Zone, zone);
        editor.commit();
    }

    public static String getZone(Context context) {
        SharedPreferences preferences = getUserSessionPreferences(context);
        return preferences.getString(Zone, "");
    }
    public static void saveNetwork(String network, Context context) {
        SharedPreferences preferences = getUserSessionPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Network, network);
        editor.commit();
    }

    public static String getNetwork(Context context) {
        SharedPreferences preferences = getUserSessionPreferences(context);
        return preferences.getString(Network, "");
    }
    public static void saveDMA(String dma, Context context) {
        SharedPreferences preferences = getUserSessionPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(DMA, dma);
        editor.commit();
    }

    public static String getDMA(Context context) {
        SharedPreferences preferences = getUserSessionPreferences(context);
        return preferences.getString(DMA, "");
    }
    public static void savePipeDetail(String pipe, Context context) {
        SharedPreferences preferences = getUserSessionPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Pipe, pipe);
        editor.commit();
    }

    public static String getPipeDetail(Context context) {
        SharedPreferences preferences = getUserSessionPreferences(context);
        return preferences.getString(Pipe, "");
    }
    public static void savelengthOfConnection(String lengthofconnection, Context context) {
        SharedPreferences preferences = getUserSessionPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(lengthOfConnection, lengthofconnection);
        editor.commit();
    }

    public static String getlengthOfConnection(Context context) {
        SharedPreferences preferences = getUserSessionPreferences(context);
        return preferences.getString(lengthOfConnection, "");
    }
    public static void savewaterStorage(String waterstorage, Context context) {
        SharedPreferences preferences = getUserSessionPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(waterStorage,waterstorage);
        editor.commit();
    }

    public static String getwaterStorage(Context context) {
        SharedPreferences preferences = getUserSessionPreferences(context);
        return preferences.getString(waterStorage, "");
    }

}
