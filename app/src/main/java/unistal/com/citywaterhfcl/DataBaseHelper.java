package unistal.com.citywaterhfcl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

class DataBaseHelper extends SQLiteOpenHelper {

    private static String DataBaseName="SmartWaterUtilitiesHFCL123.db";
    private static final int DB_VERSION = 29;
    private static String TABLE_NAME="defaul_setting";
    private static final String COLUMN_ID = "COLUMN_ID";
    private static String Loop_Runing_Status="status";
    private static String Laying_Table="Laying_Table";
    private static String Design_Table="tbl_design_data";
    private static String Laying_Design_Table="tbl_laying_design_data";
    private static String Crossing_Table="Crossing_Table";
    private static String MRS_Table="MRS_Table";
    private static String Regulator_Table="Regulator_Table";
    private static String Valve_Table="Valve_Table";
    private static String TF_Table="TF_Table";
    private static String Marker_Table="Marker_Table";
    private static String NonControllable_Table="NonControllable_Table";
    private static String Excavation_Table="Excavation_Table";
    private static String Building_Table="Building_Table";
    private static String Restoration_Table="Restoration_Table";
    private static String FittingMaterial_Table="tbl_fitting_material";
    private static String Fitting_Table="Fitting_Table";
    private static String Crossing_Category_Table="Crossing_Category_Table";
    private static String Gap_Table="Gap_Table";
    private static String chfrom="chfrom";
    private static String chto="chto";
    private static String wbs="wbs";

    private static String excavation_date="excavation_date";
    private static String common_trench="common_trench";
    private static String old_project_area="old_project_area";
    private static String start_point="start_point";

    private static String end_point="end_point";
    private static String end_point_longitude="end_point_longitude";
    private static String end_point_latitude="end_point_latitude";
    private static String end_depth="end_depth";
    private static String length="length";
    private static String road_type="road_type";
    private static String diameter="diameter";

    private static String pipe_side="pipe_side";
    private static String road_owner="road_owner";

    private static String liasoning_done="liasoning_done";
    private static String hse_done="hse_done";
    private static String method="method";
    private static String casing_dia="casing_dia";
    private static String graph_photo="graph_photo";
    private static String installed_date="installed_date";
    private static String installation_date="installation_date";
    private static String latitude="latitude";
    private static String longitude="longitude";
    private static String photo="photo";
    private static String depth="depth";
    private static String location="location";
    private static String valve="valve";
    private static String valve_dia="valve_dia";
    private static String valve_sub_type="valve_sub_type";

    private static String fitting="fitting";
    private static String non_type="non_type";
    private static String remark="remark";
    private static String marker_make="marker_make";
    private static String marker_type="marker_type";

    private static String tf_property="tf_property";
    private static String regulator="regulator";
    private static String regulator_sub_type="regulator_sub_type";
    private static String mrs_number="mrs_number";
    private static String mrs_long="mrs_long";
    private static String mrs_lat="mrs_lat";
    private static String category="category";
    private static String by_pass_indicator="by_pass_indicator";
    private static String meter_type="meter_type";
    private static String station_name="station_name";
    private static String mrs_photo="mrs_photo";
    private static String start_x="start_x";
    private static String start_y="start_y";
    private static String end_x="end_x";
    private static String end_y="end_y";
    private static String excavation_qty="excavation_qty";
    private static String excavation_of="excavation_of";

    private static String restoration_date="restoration_date";
    private static String restoration_type="restoration_type";
    private static String restoration_of="restoration_of";
    private static String area="area";
    private static String volume="volume";


    private static String crossing_date="crossing_date";
    private static String gate_longitude="gate_longitude";
    private static String gate_latitude="gate_latitude";
    private static String building_name="building_name";
    private static String building_category="building_category";
    private static String no_of_flats="no_of_flats";
    private static String no_of_floor="no_of_floor";



    private static String entry_longitude="entry_longitude";
    private static String entry_latitude="entry_latitude";
    private static String entry_depth="entry_depth";

    private static String exit_longitude="exit_longitude";
    private static String exit_latitude="exit_latitude";
    private static String exit_depth="exit_depth";
    private static String casing_size="casing_size";
    private static String crossing_type="crossing_type";
    private static String crossing_feature="crossing_feature";
    private static String profile_photo="profile_photo";



    private static String laying_date="laying_date";
    private static String network_type="network_type";
    private static String zone="zone";
    private static String dma="dma";
    private static String pipe_number="pipe_number";
    private static String pipe_manual="pipe_manual";
    private static String start_node_manual="start_node_manual";
    private static String end_node_manual="end_node_manual";
    private static String start_node="start_node";
    private static String end_node="end_node";

    private static String fitting_id="fitting_id";
    private static String fitting_subtype_id="fitting_subtype_id";
    private static String start_node_id="start_node_id";
    private static String end_node_id="end_node_id";
    private static String pipe_type="pipe_type";
    private static String pipe_sub_type="pipe_sub_type";
    private static String pipe_sub_type_dia="pipe_sub_type_dia";
    private static String scope="scope";
    private static String laying_length="laying_length";

    private static String user_id="user_id";
    private static String alignment="alignment";
    private static String soil_type="soil_type";
    private static String contractor="contractor";
    private static String remarks="remarks";
    private static String start_longitude="start_longitude";
    private static String start_latitude="start_latitude";
    private static String end_longitude="end_longitude";
    private static String end_latitude="end_latitude";
    private static String start_trenching_width="start_trenching_width";
    private static String end_trenching_width="end_trenching_width";
    private static String start_trenching_depth="start_trenching_depth";
    private static String end_trenching_depth="end_trenching_depth";
    private static String intermediate_point="intermediate_point";
    private static String intermediate_long="intermediate_long";
    private static String intermediate_lat="intermediate_lat";
    private static String schema="schema";
    private static String backfilling="backfilling";
    private static String intermediate_depth="intermediate_depth";
    private static String intermediate_width="intermediate_width";
    private static String actual_gap="actual_gap";
    private static String actual_length="actual_length";
    private static String contractor_id="contractor_id";
    private static String gap_date="gap_date";
    private static String reason="reason";
    private static String crossing_class="crossing_class";
    private static String crossing_casing="crossing_casing";
    private static String owner_name="owner_name";
    private static String crossing_position="crossing_position";
    private static String pipeline_position="pipeline_position";
    private static String permission_authority="permission_authority";


    public DataBaseHelper(Context context) {
        super(context, DataBaseName, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String newRaw="CREATE TABLE IF NOT EXISTS "+Laying_Table
                +"("
                +COLUMN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                +laying_date+" VARCHAR(255), "
                +network_type+" VARCHAR(255), "
                +zone+" VARCHAR(255), "
                +dma+" VARCHAR(255), "

                +pipe_number+" VARCHAR(255),"
                +pipe_manual+" VARCHAR(255),"
                +start_node_manual+" VARCHAR(255),"
                +end_node_manual+" VARCHAR(255),"
                +start_node+" VARCHAR(255),"
                +end_node+" VARCHAR(255),"
                +pipe_type+" VARCHAR(255),"
                +pipe_sub_type+" VARCHAR(255),"
                +pipe_sub_type_dia+" VARCHAR(255),"
                +scope+" VARCHAR(255),"
                +laying_length+" VARCHAR(255),"
                +chfrom+" VARCHAR(255),"
                +chto+" VARCHAR(255),"
                +user_id+" VARCHAR(255),"
                +alignment+" VARCHAR(255),"
                +soil_type+" VARCHAR(255),"
                +contractor+" VARCHAR(255),"
                +remarks+" VARCHAR(255),"
                +start_longitude+" VARCHAR(255),"
                +start_latitude+" VARCHAR(255),"
                +end_longitude+" VARCHAR(255),"
                +end_latitude+" VARCHAR(255),"
                +start_trenching_width+" VARCHAR,"
                +end_trenching_width+" VARCHAR,"
                +start_trenching_depth+" VARCHAR(255),"
                +end_trenching_depth+" VARCHAR(255),"
                +intermediate_point+" VARCHAR(255),"
                +intermediate_long+" VARCHAR(255),"
                +intermediate_lat+" VARCHAR(255),"

                +schema+" VARCHAR(255),"
                +backfilling+" VARCHAR(255),"
                +intermediate_depth+" VARCHAR(255),"
                +intermediate_width+" VARCHAR(255),"
                +photo+" VARCHAR(255)"




                +")";


        String Crossing="CREATE TABLE IF NOT EXISTS "+Crossing_Table
                +"("
                +COLUMN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                +schema+" VARCHAR(255), "
                +user_id+" VARCHAR(255), "

                +start_longitude+" VARCHAR(255),"
                +start_latitude+" VARCHAR(255),"
                +end_longitude+" VARCHAR(255),"
                +end_latitude+" VARCHAR(255),"
                +crossing_class+" VARCHAR(255), "
                +crossing_type+" VARCHAR(255),"
                +crossing_casing+" VARCHAR(255),"
                +length+" VARCHAR(255),"
                +owner_name+" VARCHAR(255),"
                +crossing_position+" VARCHAR(255),"
                +pipeline_position+" VARCHAR(255),"
                +permission_authority+" VARCHAR(255),"
                +remarks+" VARCHAR(255),"
                +contractor_id+" VARCHAR(255),"
                +photo+" VARCHAR(255)"


                +")";
        String mrs="CREATE TABLE IF NOT EXISTS "+MRS_Table
                +"("
                +COLUMN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                +schema+" VARCHAR(255), "
                +wbs+" VARCHAR(255), "
                +installation_date+" VARCHAR(255), "
                +mrs_number+" VARCHAR(255),"
                +mrs_long+" VARCHAR(255),"
                +mrs_lat+" VARCHAR(255),"
                +category+" VARCHAR(255),"
                +by_pass_indicator+" VARCHAR(255),"
                +meter_type+" VARCHAR(255),"
                +station_name+" VARCHAR(255),"
                +mrs_photo+" VARCHAR(255),"
                +remark+" VARCHAR(255)"


                +")";
        String Regulator="CREATE TABLE IF NOT EXISTS "+Regulator_Table
                +"("
                +COLUMN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                +schema+" VARCHAR(255), "
                +wbs+" VARCHAR(255), "
                +installation_date+" VARCHAR(255), "
                +regulator+" VARCHAR(255),"
                +regulator_sub_type+" VARCHAR(255),"
                +longitude+" VARCHAR(255),"
                +latitude+" VARCHAR(255),"
                +location+" VARCHAR(255),"
                +photo+" VARCHAR(255),"
                +remarks+" VARCHAR(255)"



                +")";
        String Fitting="CREATE TABLE IF NOT EXISTS "+Fitting_Table
                +"("
                +COLUMN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                +schema+" VARCHAR(255), "

                +installed_date+" VARCHAR(255), "
                +network_type+" VARCHAR(255), "
                +zone+" VARCHAR(255), "
                +dma+" VARCHAR(255),"
                +pipe_number+" VARCHAR(255),"
                +start_node+" VARCHAR(255),"
                +end_node+" VARCHAR(255),"
                +fitting_id+" VARCHAR(255),"
                +fitting_subtype_id+" VARCHAR(255),"
                +start_longitude+" VARCHAR(255),"
                +start_latitude+" VARCHAR(255),"
                +photo+" VARCHAR(255),"
                +depth+" VARCHAR(255),"
                +remarks+" VARCHAR(255),"
                +contractor+" VARCHAR(255),"
                +user_id+" VARCHAR(255)"


                +")";
        String Gap="CREATE TABLE IF NOT EXISTS "+Gap_Table
                +"("
                +COLUMN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                +schema+" VARCHAR(255), "
                +gap_date+" VARCHAR(255), "
                +network_type+" VARCHAR(255), "
                +zone+" VARCHAR(255), "
                +dma+" VARCHAR(255),"
                +pipe_number+" VARCHAR(255),"
                +start_node+" VARCHAR(255),"
                +end_node+" VARCHAR(255),"
                +actual_gap+" VARCHAR(255),"
                +chfrom+" VARCHAR(255),"
                +chto+" VARCHAR(255),"
                +actual_length+" VARCHAR(255),"
                +contractor_id+" VARCHAR(255),"
                +start_longitude+" VARCHAR(255),"
                +start_latitude+" VARCHAR(255),"
                +photo+" VARCHAR(255),"
                +reason+" VARCHAR(255),"
                +remarks+" VARCHAR(255),"
                +user_id+" VARCHAR(255)"


                +")";
        String Valve="CREATE TABLE IF NOT EXISTS "+Valve_Table
                +"("
                +COLUMN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                +schema+" VARCHAR(255), "
                +wbs+" VARCHAR(255), "
                +installation_date+" VARCHAR(255), "
                +valve+" VARCHAR(255),"
                +valve_dia+" VARCHAR(255),"
                +valve_sub_type+" VARCHAR(255),"
                +latitude+" VARCHAR(255),"
                +longitude+" VARCHAR(255),"
                +photo+" VARCHAR(255),"
                +location+" VARCHAR(255)"


                +")";
        String TF="CREATE TABLE IF NOT EXISTS "+TF_Table
                +"("
                +COLUMN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                +schema+" VARCHAR(255), "
                +wbs+" VARCHAR(255), "
                +installation_date+" VARCHAR(255), "
                +tf_property+" VARCHAR(255),"
                +latitude+" VARCHAR(255),"
                +longitude+" VARCHAR(255),"
                +location+" VARCHAR(255)"

                +")";
        String Marker="CREATE TABLE IF NOT EXISTS "+Marker_Table
                +"("
                +COLUMN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                +schema+" VARCHAR(255), "
                +wbs+" VARCHAR(255), "
                +installation_date+" VARCHAR(255), "
                +marker_make+" VARCHAR(255),"
                +marker_type+" VARCHAR(255),"
                +latitude+" VARCHAR(255),"
                +longitude+" VARCHAR(255),"
                +photo+" VARCHAR(255),"
                +location+" VARCHAR(255),"
                +remark+" VARCHAR(255)"

                +")";
        String NonControllable="CREATE TABLE IF NOT EXISTS "+NonControllable_Table
                +"("
                +COLUMN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                +schema+" VARCHAR(255), "
                +wbs+" VARCHAR(255), "
                +installation_date+" VARCHAR(255), "
                +fitting+" VARCHAR(255),"
                +non_type+" VARCHAR(255),"

                +latitude+" VARCHAR(255),"
                +longitude+" VARCHAR(255),"
                +photo+" VARCHAR(255),"
                +location+" VARCHAR(255),"
                +remarks+" VARCHAR(255)"
                +")";

        String Excavation="CREATE TABLE IF NOT EXISTS "+Excavation_Table
                +"("
                +COLUMN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                +schema+" VARCHAR(255), "
                +wbs+" VARCHAR(255), "
                +excavation_date+" VARCHAR(255), "
                +excavation_qty+" VARCHAR(255),"
                +excavation_of+" VARCHAR(255),"
                +start_x+" VARCHAR(255),"
                +start_y+" VARCHAR(255),"
                +end_x+" VARCHAR(255),"
                +end_y+" VARCHAR(255)"


                +")";
        String Restoration="CREATE TABLE IF NOT EXISTS "+Restoration_Table
                +"("
                +COLUMN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                +schema+" VARCHAR(255), "
                +wbs+" VARCHAR(255), "
                +restoration_date+" VARCHAR(255), "
                +restoration_type+" VARCHAR(255),"
                +restoration_of+" VARCHAR(255),"
                +area+" VARCHAR(255),"
                +volume+" VARCHAR(255)"



                +")";

        String Building="CREATE TABLE IF NOT EXISTS "+Building_Table
                +"("
                +COLUMN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                +schema+" VARCHAR(255), "
                +wbs+" VARCHAR(255), "
                +crossing_date+" VARCHAR(255),"
                +building_category+" VARCHAR(255),"
                +building_name+" VARCHAR(255),"
                +no_of_flats+" VARCHAR(255),"
                +no_of_floor+" VARCHAR(255),"
                +gate_longitude+" VARCHAR(255),"
                +gate_latitude+" VARCHAR(255)"
                +")";
        String CREATE_DATABASE_LAYING_DESIGN_DATA = "create table "+Laying_Design_Table+ "("
                + "_id integer  primary key autoincrement , "
                + "zone," + "dma,"+"pipe_number,"+"start_node,"+"stop_node,"+"scope);";
        String CREATE_DATABASE_DESIGN_DATA = "create table tbl_design_data("
                + "_id integer  primary key autoincrement , "
                + "zone," + "dma,"+ "scheme_ext,"+"pipe_number,"+"start_node,"+"stop_node,"+"scope,"
                + "pipe_type,"  + "sub_type,"
                + "pipe_diameter);";
        String CREATE_Crossing_Category_Table = "create table Crossing_Category_Table("
                + "_id integer  primary key autoincrement , "
                + "class_name," + "class_id,"+ "type_name," + "type_id);";
        String CREATE_DATABASE_REMOVE_GAP_List_DATA = "create table tbl_remove_gap_list_data("
                + "_id integer  primary key autoincrement , "+"id,"
                +  "dma,"+"pipe_number,"+"gap_id,"
                + "remarks);";
        String CREATE_DATABASE_FittingMaterial = "create table tbl_fitting_material("
                + "_id integer  primary key autoincrement , "
                + "FittingID," + "FittingName,"
                + "SubFittingName,"
                + "MaterialID,"  + "MaterialDescription,"
                + "MaterialCode);";
        db.execSQL(CREATE_Crossing_Category_Table);
        db.execSQL(CREATE_DATABASE_REMOVE_GAP_List_DATA);
        db.execSQL(CREATE_DATABASE_FittingMaterial);
        db.execSQL(Fitting);
        db.execSQL(CREATE_DATABASE_DESIGN_DATA);
        db.execSQL(CREATE_DATABASE_LAYING_DESIGN_DATA);
        db.execSQL(newRaw);

        db.execSQL(Regulator);
        db.execSQL(Valve);
        db.execSQL(TF);
        db.execSQL(Marker);
        db.execSQL(NonControllable);
        db.execSQL(mrs);
        db.execSQL(Excavation);
        db.execSQL(Restoration);
        db.execSQL(Building);
        db.execSQL(Crossing);
        db.execSQL(Gap);




    }
    public Cursor getAll(String select) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery(select, null);
    }
    public boolean deleteAll(String DATABASE_TABLE) {
        SQLiteDatabase db =this.getWritableDatabase();
        return db.delete(DATABASE_TABLE, null, null) > 0;

    }
    public void insert(String DATABASE_TABLE, ContentValues initialValues) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(DATABASE_TABLE, null, initialValues);
    }
    public boolean newRowInsert(HashMap<String,String> hashMap) {


        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(laying_date, hashMap.get(laying_date));
        contentValues.put(network_type, hashMap.get(network_type));
        contentValues.put(zone, hashMap.get(zone));
        contentValues.put(dma, hashMap.get(dma));
        contentValues.put(pipe_number, hashMap.get(pipe_number));
        contentValues.put(pipe_manual, hashMap.get(pipe_manual));
        contentValues.put(start_node_manual, hashMap.get(start_node_manual));
        contentValues.put(end_node_manual, hashMap.get(end_node_manual));
        contentValues.put(start_node, hashMap.get(start_node));
        contentValues.put(end_node, hashMap.get(end_node));
        contentValues.put(pipe_type, hashMap.get(pipe_type));
        contentValues.put(pipe_sub_type, hashMap.get(pipe_sub_type));
        contentValues.put(pipe_sub_type_dia, hashMap.get(pipe_sub_type_dia));
        contentValues.put(scope, hashMap.get(scope));
        contentValues.put(chfrom, hashMap.get(chfrom));
        contentValues.put(chto, hashMap.get(chto));
        contentValues.put(laying_length, hashMap.get(laying_length));
        contentValues.put(user_id, hashMap.get(user_id));
        contentValues.put(alignment, hashMap.get(alignment));
        contentValues.put(soil_type, hashMap.get(soil_type));
        contentValues.put(contractor, hashMap.get(contractor));
        contentValues.put(remarks, hashMap.get(remarks));
        contentValues.put(start_longitude, hashMap.get(start_longitude));
        contentValues.put(start_latitude, hashMap.get(start_latitude));
        contentValues.put(end_longitude, hashMap.get(end_longitude));
        contentValues.put(end_latitude, hashMap.get(end_latitude));

        contentValues.put(start_trenching_width, hashMap.get(start_trenching_width));
        contentValues.put(end_trenching_width, hashMap.get(end_trenching_width));
        contentValues.put(start_trenching_depth, hashMap.get(start_trenching_depth));
        contentValues.put(end_trenching_depth, hashMap.get(end_trenching_depth));
        contentValues.put(intermediate_point, hashMap.get(intermediate_point));
        contentValues.put(intermediate_long, hashMap.get(intermediate_long));
        contentValues.put(intermediate_lat, hashMap.get(intermediate_lat));
        contentValues.put(schema, hashMap.get(schema));
        contentValues.put(backfilling, hashMap.get(backfilling));
        contentValues.put(intermediate_depth, hashMap.get(intermediate_depth));
        contentValues.put(intermediate_width, hashMap.get(intermediate_width));
        contentValues.put(photo, hashMap.get(photo));

        int i = (int) db.insert(Laying_Table, null, contentValues);
        if (i==-1){
            return false;
        }else
            return true;
    }

    public ArrayList<HashMap<String, String>> getnewRowData() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery( "select * from " +Laying_Table+" ORDER BY "+ COLUMN_ID+" ASC", null );
        ArrayList<HashMap<String ,String >> arrayList=new ArrayList<>();

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                HashMap<String,String> h=new HashMap<>();
                h.put("COLUMN_ID",cursor.getString(cursor.getColumnIndex(COLUMN_ID)));
                h.put(laying_date,cursor.getString(cursor.getColumnIndex(laying_date)));
                h.put(network_type,cursor.getString(cursor.getColumnIndex(network_type)));
                h.put(zone,cursor.getString(cursor.getColumnIndex(zone)));
                h.put(dma,cursor.getString(cursor.getColumnIndex(dma)));
                h.put(pipe_number,cursor.getString(cursor.getColumnIndex(pipe_number)));
                h.put(pipe_manual,cursor.getString(cursor.getColumnIndex(pipe_manual)));
                h.put(start_node_manual,cursor.getString(cursor.getColumnIndex(start_node_manual)));
                h.put(end_node_manual,cursor.getString(cursor.getColumnIndex(end_node_manual)));
                h.put(start_node,cursor.getString(cursor.getColumnIndex(start_node)));

                h.put(end_node,cursor.getString(cursor.getColumnIndex(end_node)));
                h.put(pipe_type,cursor.getString(cursor.getColumnIndex(pipe_type)));
                h.put(pipe_sub_type,cursor.getString(cursor.getColumnIndex(pipe_sub_type)));
                h.put(pipe_sub_type_dia,cursor.getString(cursor.getColumnIndex(pipe_sub_type_dia)));
                h.put(scope,cursor.getString(cursor.getColumnIndex(scope)));
                h.put(chfrom,cursor.getString(cursor.getColumnIndex(chfrom)));
                h.put(chto,cursor.getString(cursor.getColumnIndex(chto)));

                h.put(laying_length,cursor.getString(cursor.getColumnIndex(laying_length)));
                h.put(user_id,cursor.getString(cursor.getColumnIndex(user_id)));
                h.put(alignment,cursor.getString(cursor.getColumnIndex(alignment)));
                h.put(soil_type,cursor.getString(cursor.getColumnIndex(soil_type)));
                h.put(contractor,cursor.getString(cursor.getColumnIndex(contractor)));
                h.put(remarks,cursor.getString(cursor.getColumnIndex(remarks)));
                h.put(start_longitude,cursor.getString(cursor.getColumnIndex(start_longitude)));
                h.put(start_latitude,cursor.getString(cursor.getColumnIndex(start_latitude)));

                h.put(end_longitude,cursor.getString(cursor.getColumnIndex(end_longitude)));
                h.put(end_latitude,cursor.getString(cursor.getColumnIndex(end_latitude)));

                h.put(start_trenching_width,cursor.getString(cursor.getColumnIndex(start_trenching_width)));
                h.put(end_trenching_width,cursor.getString(cursor.getColumnIndex(end_trenching_width)));
                h.put(start_trenching_depth,cursor.getString(cursor.getColumnIndex(start_trenching_depth)));
                h.put(end_trenching_depth,cursor.getString(cursor.getColumnIndex(end_trenching_depth)));
                h.put(intermediate_point,cursor.getString(cursor.getColumnIndex(intermediate_point)));
                h.put(intermediate_long,cursor.getString(cursor.getColumnIndex(intermediate_long)));
                h.put(intermediate_lat,cursor.getString(cursor.getColumnIndex(intermediate_lat)));
                h.put(schema,cursor.getString(cursor.getColumnIndex(schema)));
                h.put(backfilling,cursor.getString(cursor.getColumnIndex(backfilling)));
                h.put(intermediate_depth,cursor.getString(cursor.getColumnIndex(intermediate_depth)));
                h.put(intermediate_width,cursor.getString(cursor.getColumnIndex(intermediate_width)));
                h.put(photo,cursor.getString(cursor.getColumnIndex(photo)));


                arrayList.add(h);
                cursor.moveToNext();
            }
        }
        cursor.close();
        return arrayList;
    }
    public boolean gapInsert(HashMap<String,String> hashMap) {


        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(schema, hashMap.get(schema));
        contentValues.put(user_id, hashMap.get(user_id));
        contentValues.put(gap_date, hashMap.get(gap_date));
        contentValues.put(network_type, hashMap.get(network_type));

        contentValues.put(zone, hashMap.get(zone));
        contentValues.put(dma, hashMap.get(dma));
        contentValues.put(pipe_number, hashMap.get(pipe_number));
        contentValues.put(start_node, hashMap.get(start_node));
        contentValues.put(end_node, hashMap.get(end_node));
        contentValues.put(start_longitude, hashMap.get(start_longitude));
        contentValues.put(start_latitude, hashMap.get(start_latitude));

        contentValues.put(reason, hashMap.get(reason));
        contentValues.put(remarks, hashMap.get(remarks));
        contentValues.put(contractor_id, hashMap.get(contractor_id));
        contentValues.put(actual_length, hashMap.get(actual_length));
        contentValues.put(actual_gap, hashMap.get(actual_gap));
        contentValues.put(chfrom, hashMap.get(chfrom));
        contentValues.put(chto, hashMap.get(chto));
        contentValues.put(photo, hashMap.get(photo));



        int i = (int) db.insert(Gap_Table, null, contentValues);
        if (i==-1){
            return false;
        }else
            return true;
    }

    public ArrayList<HashMap<String, String>> getGapData() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery( "select * from " +Gap_Table+" ORDER BY "+ COLUMN_ID+" ASC", null );
        ArrayList<HashMap<String ,String >> arrayList=new ArrayList<>();

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                HashMap<String,String> h=new HashMap<>();
                h.put("COLUMN_ID",cursor.getString(cursor.getColumnIndex(COLUMN_ID)));
                h.put(schema,cursor.getString(cursor.getColumnIndex(schema)));
                h.put(user_id,cursor.getString(cursor.getColumnIndex(user_id)));
                h.put(gap_date,cursor.getString(cursor.getColumnIndex(gap_date)));
                h.put(network_type,cursor.getString(cursor.getColumnIndex(network_type)));

                h.put(zone,cursor.getString(cursor.getColumnIndex(zone)));
                h.put(dma,cursor.getString(cursor.getColumnIndex(dma)));
                h.put(pipe_number,cursor.getString(cursor.getColumnIndex(pipe_number)));
                h.put(start_node,cursor.getString(cursor.getColumnIndex(start_node)));

                h.put(end_node,cursor.getString(cursor.getColumnIndex(end_node)));
                h.put(start_longitude,cursor.getString(cursor.getColumnIndex(start_longitude)));
                h.put(start_latitude,cursor.getString(cursor.getColumnIndex(start_latitude)));

                h.put(reason,cursor.getString(cursor.getColumnIndex(reason)));
                h.put(remarks,cursor.getString(cursor.getColumnIndex(remarks)));
                h.put(chfrom,cursor.getString(cursor.getColumnIndex(chfrom)));
                h.put(chto,cursor.getString(cursor.getColumnIndex(chto)));

                h.put(contractor_id,cursor.getString(cursor.getColumnIndex(contractor_id)));
                h.put(actual_length,cursor.getString(cursor.getColumnIndex(actual_length)));
                h.put(actual_gap,cursor.getString(cursor.getColumnIndex(actual_gap)));
                h.put(photo,cursor.getString(cursor.getColumnIndex(photo)));

                arrayList.add(h);
                cursor.moveToNext();
            }
        }
        cursor.close();
        return arrayList;
    }
    public boolean DeleteRow(String Id,String tableName){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res =  db.rawQuery( "select * from " +tableName, null );
        res.moveToFirst();
        int i=db.delete(tableName,  COLUMN_ID+" = ? ", new String[] { Id } );
        res.close();
        if (i>0){
            return true;
        }else
            return false;

    }
    /*
    *   +start_longitude+" VARCHAR(255),"
                +start_latitude+" VARCHAR(255),"
                +end_longitude+" VARCHAR(255),"
                +end_latitude+" VARCHAR(255),"
                +crossing_class+" VARCHAR(255), "
                +crossing_type+" VARCHAR(255),"
                +crossing_casing+" VARCHAR(255),"
                +length+" VARCHAR(255),"
                +owner_name+" VARCHAR(255),"
                +crossing_position+" VARCHAR(255),"
                +pipeline_position+" VARCHAR(255),"
                +permission_authority+" VARCHAR(255),"
                +remarks+" VARCHAR(255),"

                +photo+" VARCHAR(255)"*/
    public boolean newCrossingInsert(HashMap<String,String> hashMap) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(schema, hashMap.get(schema));
        contentValues.put(user_id, hashMap.get(user_id));
        contentValues.put(start_longitude, hashMap.get(start_longitude));
        contentValues.put(start_latitude, hashMap.get(start_latitude));
        contentValues.put(end_longitude, hashMap.get(end_longitude));
        contentValues.put(end_latitude, hashMap.get(end_latitude));
        contentValues.put(crossing_class, hashMap.get(crossing_class));
        contentValues.put(crossing_type, hashMap.get(crossing_type));
        contentValues.put(crossing_casing, hashMap.get(crossing_casing));
        contentValues.put(length, hashMap.get(length));

        contentValues.put(owner_name, hashMap.get(owner_name));
        contentValues.put(crossing_position, hashMap.get(crossing_position));
        contentValues.put(pipeline_position, hashMap.get(pipeline_position));
        contentValues.put(permission_authority, hashMap.get(permission_authority));
        contentValues.put(photo, hashMap.get(photo));
        contentValues.put(remarks, hashMap.get(remarks));
        contentValues.put(contractor_id, hashMap.get(contractor_id));



        int i = (int) db.insert(Crossing_Table, null, contentValues);
        if (i==-1){
            return false;
        }else
            return true;
    }

    public ArrayList<HashMap<String, String>> getnewCrossingData() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery( "select * from " +Crossing_Table+" ORDER BY "+ COLUMN_ID+" ASC", null );
        ArrayList<HashMap<String ,String >> arrayList=new ArrayList<>();

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                HashMap<String,String> h=new HashMap<>();
                h.put("COLUMN_ID",cursor.getString(cursor.getColumnIndex(COLUMN_ID)));
                h.put(schema,cursor.getString(cursor.getColumnIndex(schema)));
                h.put(user_id,cursor.getString(cursor.getColumnIndex(user_id)));
                h.put(start_longitude,cursor.getString(cursor.getColumnIndex(start_longitude)));
                h.put(start_latitude,cursor.getString(cursor.getColumnIndex(start_latitude)));
                h.put(end_longitude,cursor.getString(cursor.getColumnIndex(end_longitude)));

                h.put(end_latitude,cursor.getString(cursor.getColumnIndex(end_latitude)));
                h.put(crossing_class,cursor.getString(cursor.getColumnIndex(crossing_class)));
                h.put(crossing_type,cursor.getString(cursor.getColumnIndex(crossing_type)));
                h.put(crossing_casing,cursor.getString(cursor.getColumnIndex(crossing_casing)));

                h.put(length,cursor.getString(cursor.getColumnIndex(length)));
                h.put(owner_name,cursor.getString(cursor.getColumnIndex(owner_name)));
                h.put(crossing_position,cursor.getString(cursor.getColumnIndex(crossing_position)));
                h.put(pipeline_position,cursor.getString(cursor.getColumnIndex(pipeline_position)));
                h.put(permission_authority,cursor.getString(cursor.getColumnIndex(permission_authority)));
                h.put(photo,cursor.getString(cursor.getColumnIndex(photo)));
                h.put(remarks,cursor.getString(cursor.getColumnIndex(remarks)));
                h.put(contractor_id,cursor.getString(cursor.getColumnIndex(contractor_id)));

                arrayList.add(h);
                cursor.moveToNext();
            }
        }
        cursor.close();
        return arrayList;
    }

    public boolean newMRSInsert(HashMap<String,String> hashMap) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(schema, hashMap.get(schema));
        contentValues.put(wbs, hashMap.get(wbs));
        contentValues.put(installation_date, hashMap.get(installation_date));
        contentValues.put(mrs_number, hashMap.get(mrs_number));
        contentValues.put(mrs_long, hashMap.get(mrs_long));
        contentValues.put(mrs_lat, hashMap.get(mrs_lat));
        contentValues.put(category, hashMap.get(category));
        contentValues.put(by_pass_indicator, hashMap.get(by_pass_indicator));
        contentValues.put(meter_type, hashMap.get(meter_type));
        contentValues.put(station_name, hashMap.get(station_name));
        contentValues.put(mrs_photo, hashMap.get(mrs_photo));
        contentValues.put(remark, hashMap.get(remark));

        int i = (int) db.insert(MRS_Table, null, contentValues);
        if (i==-1){
            return false;
        }else
            return true;
    }

    public ArrayList<HashMap<String, String>> getMRSData() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery( "select * from " +MRS_Table+" ORDER BY "+ COLUMN_ID+" ASC", null );
        ArrayList<HashMap<String ,String >> arrayList=new ArrayList<>();

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                HashMap<String,String> h=new HashMap<>();
                h.put("COLUMN_ID",cursor.getString(cursor.getColumnIndex(COLUMN_ID)));
                h.put(schema,cursor.getString(cursor.getColumnIndex(schema)));
                h.put(wbs,cursor.getString(cursor.getColumnIndex(wbs)));
                h.put(installation_date,cursor.getString(cursor.getColumnIndex(installation_date)));
                h.put(mrs_number,cursor.getString(cursor.getColumnIndex(mrs_number)));
                h.put(mrs_long,cursor.getString(cursor.getColumnIndex(mrs_long)));

                h.put(mrs_lat,cursor.getString(cursor.getColumnIndex(mrs_lat)));
                h.put(category,cursor.getString(cursor.getColumnIndex(category)));
                h.put(by_pass_indicator,cursor.getString(cursor.getColumnIndex(by_pass_indicator)));
                h.put(meter_type,cursor.getString(cursor.getColumnIndex(meter_type)));
                h.put(station_name,cursor.getString(cursor.getColumnIndex(station_name)));
                h.put(mrs_photo,cursor.getString(cursor.getColumnIndex(mrs_photo)));
                h.put(remark,cursor.getString(cursor.getColumnIndex(remark)));


                arrayList.add(h);
                cursor.moveToNext();
            }
        }
        cursor.close();
        return arrayList;
    }
    public boolean regulatorInsert(HashMap<String,String> hashMap) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(schema, hashMap.get(schema));
        contentValues.put(wbs, hashMap.get(wbs));
        contentValues.put(installation_date, hashMap.get(installation_date));
        contentValues.put(regulator, hashMap.get(regulator));
        contentValues.put(regulator_sub_type, hashMap.get(regulator_sub_type));
        contentValues.put(latitude, hashMap.get(latitude));
        contentValues.put(longitude, hashMap.get(longitude));
        contentValues.put(location, hashMap.get(location));

        contentValues.put(photo, hashMap.get(photo));
        contentValues.put(remarks, hashMap.get(remarks));

        int i = (int) db.insert(Regulator_Table, null, contentValues);
        if (i==-1){
            return false;
        }else
            return true;
    }

    public ArrayList<HashMap<String, String>> getRegulatorData() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery( "select * from " +Regulator_Table+" ORDER BY "+ COLUMN_ID+" ASC", null );
        ArrayList<HashMap<String ,String >> arrayList=new ArrayList<>();

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                HashMap<String,String> h=new HashMap<>();
                h.put("COLUMN_ID",cursor.getString(cursor.getColumnIndex(COLUMN_ID)));
                h.put(schema,cursor.getString(cursor.getColumnIndex(schema)));
                h.put(wbs,cursor.getString(cursor.getColumnIndex(wbs)));
                h.put(installation_date,cursor.getString(cursor.getColumnIndex(installation_date)));
                h.put(regulator,cursor.getString(cursor.getColumnIndex(regulator)));
                h.put(regulator_sub_type,cursor.getString(cursor.getColumnIndex(regulator_sub_type)));

                h.put(latitude,cursor.getString(cursor.getColumnIndex(latitude)));
                h.put(longitude,cursor.getString(cursor.getColumnIndex(longitude)));
                h.put(location,cursor.getString(cursor.getColumnIndex(location)));
                h.put(photo,cursor.getString(cursor.getColumnIndex(photo)));

                h.put(remarks,cursor.getString(cursor.getColumnIndex(remarks)));


                arrayList.add(h);
                cursor.moveToNext();
            }
        }
        cursor.close();
        return arrayList;
    }

    public boolean fittingInsert(HashMap<String,String> hashMap) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(schema, hashMap.get(schema));

        contentValues.put(installed_date, hashMap.get(installed_date));
        contentValues.put(network_type, hashMap.get(network_type));

        contentValues.put(zone, hashMap.get(zone));
        contentValues.put(dma, hashMap.get(dma));
        contentValues.put(pipe_number, hashMap.get(pipe_number));
        contentValues.put(start_node, hashMap.get(start_node));
        contentValues.put(end_node, hashMap.get(end_node));
        contentValues.put(fitting_id, hashMap.get(fitting_id));
        contentValues.put(fitting_subtype_id, hashMap.get(fitting_subtype_id));
        contentValues.put(start_longitude, hashMap.get(start_longitude));
        contentValues.put(start_latitude, hashMap.get(start_latitude));
        contentValues.put(depth, hashMap.get(depth));
        contentValues.put(photo, hashMap.get(photo));
        contentValues.put(remarks, hashMap.get(remarks));
        contentValues.put(contractor, hashMap.get(contractor));
        contentValues.put(user_id, hashMap.get(user_id));
        int i = (int) db.insert(Fitting_Table, null, contentValues);
        if (i==-1){
            return false;
        }else
            return true;
    }

    public ArrayList<HashMap<String, String>> getFittingData() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery( "select * from " +Fitting_Table+" ORDER BY "+ COLUMN_ID+" ASC", null );
        ArrayList<HashMap<String ,String >> arrayList=new ArrayList<>();

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                HashMap<String,String> h=new HashMap<>();
                h.put("COLUMN_ID",cursor.getString(cursor.getColumnIndex(COLUMN_ID)));
                h.put(schema,cursor.getString(cursor.getColumnIndex(schema)));
                h.put(user_id,cursor.getString(cursor.getColumnIndex(user_id)));
                h.put(installed_date,cursor.getString(cursor.getColumnIndex(installed_date)));
                h.put(zone,cursor.getString(cursor.getColumnIndex(zone)));
                h.put(dma,cursor.getString(cursor.getColumnIndex(dma)));
                h.put(pipe_number,cursor.getString(cursor.getColumnIndex(pipe_number)));
                h.put(start_node,cursor.getString(cursor.getColumnIndex(start_node)));
                h.put(end_node,cursor.getString(cursor.getColumnIndex(end_node)));
                h.put(fitting_id,cursor.getString(cursor.getColumnIndex(fitting_id)));
                h.put(fitting_subtype_id,cursor.getString(cursor.getColumnIndex(fitting_subtype_id)));
                h.put(start_longitude,cursor.getString(cursor.getColumnIndex(start_longitude)));
                h.put(start_latitude,cursor.getString(cursor.getColumnIndex(start_latitude)));
                h.put(depth,cursor.getString(cursor.getColumnIndex(depth)));
                h.put(photo,cursor.getString(cursor.getColumnIndex(photo)));
                h.put(contractor,cursor.getString(cursor.getColumnIndex(contractor)));

                h.put(remarks,cursor.getString(cursor.getColumnIndex(remarks)));


                arrayList.add(h);
                cursor.moveToNext();
            }
        }
        cursor.close();
        return arrayList;
    }
    public boolean valveInsert(HashMap<String,String> hashMap) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(schema, hashMap.get(schema));
        contentValues.put(wbs, hashMap.get(wbs));
        contentValues.put(installation_date, hashMap.get(installation_date));
        contentValues.put(valve, hashMap.get(valve));
        contentValues.put(valve_dia, hashMap.get(valve_dia));
        contentValues.put(valve_sub_type, hashMap.get(valve_sub_type));
        contentValues.put(latitude, hashMap.get(latitude));
        contentValues.put(longitude, hashMap.get(longitude));
        contentValues.put(location, hashMap.get(location));

        contentValues.put(photo, hashMap.get(photo));
        contentValues.put(remarks, hashMap.get(remarks));

        int i = (int) db.insert(Valve_Table, null, contentValues);
        if (i==-1){
            return false;
        }else
            return true;
    }

    public ArrayList<HashMap<String, String>> getValveData() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery( "select * from " +Valve_Table+" ORDER BY "+ COLUMN_ID+" ASC", null );
        ArrayList<HashMap<String ,String >> arrayList=new ArrayList<>();

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                HashMap<String,String> h=new HashMap<>();
                h.put("COLUMN_ID",cursor.getString(cursor.getColumnIndex(COLUMN_ID)));
                h.put(schema,cursor.getString(cursor.getColumnIndex(schema)));
                h.put(wbs,cursor.getString(cursor.getColumnIndex(wbs)));
                h.put(installation_date,cursor.getString(cursor.getColumnIndex(installation_date)));
                h.put(valve,cursor.getString(cursor.getColumnIndex(valve)));
                h.put(valve_dia,cursor.getString(cursor.getColumnIndex(valve_dia)));
                h.put(valve_sub_type,cursor.getString(cursor.getColumnIndex(valve_sub_type)));

                h.put(latitude,cursor.getString(cursor.getColumnIndex(latitude)));
                h.put(longitude,cursor.getString(cursor.getColumnIndex(longitude)));
                h.put(location,cursor.getString(cursor.getColumnIndex(location)));
                h.put(photo,cursor.getString(cursor.getColumnIndex(photo)));

                h.put(remarks,cursor.getString(cursor.getColumnIndex(remarks)));


                arrayList.add(h);
                cursor.moveToNext();
            }
        }
        cursor.close();
        return arrayList;
    }
    public boolean markerInsert(HashMap<String,String> hashMap) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(schema, hashMap.get(schema));
        contentValues.put(wbs, hashMap.get(wbs));
        contentValues.put(installation_date, hashMap.get(installation_date));
        contentValues.put(marker_make, hashMap.get(marker_make));
        contentValues.put(marker_type, hashMap.get(marker_type));
        contentValues.put(latitude, hashMap.get(latitude));
        contentValues.put(longitude, hashMap.get(longitude));
        contentValues.put(location, hashMap.get(location));

        contentValues.put(photo, hashMap.get(photo));
        contentValues.put(remark, hashMap.get(remark));

        int i = (int) db.insert(Marker_Table, null, contentValues);
        if (i==-1){
            return false;
        }else
            return true;
    }

    public ArrayList<HashMap<String, String>> getMarkerData() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery( "select * from " +Marker_Table+" ORDER BY "+ COLUMN_ID+" ASC", null );
        ArrayList<HashMap<String ,String >> arrayList=new ArrayList<>();

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                HashMap<String,String> h=new HashMap<>();
                h.put("COLUMN_ID",cursor.getString(cursor.getColumnIndex(COLUMN_ID)));
                h.put(schema,cursor.getString(cursor.getColumnIndex(schema)));
                h.put(wbs,cursor.getString(cursor.getColumnIndex(wbs)));
                h.put(installation_date,cursor.getString(cursor.getColumnIndex(installation_date)));
                h.put(marker_make,cursor.getString(cursor.getColumnIndex(marker_make)));
                h.put(marker_type,cursor.getString(cursor.getColumnIndex(marker_type)));

                h.put(latitude,cursor.getString(cursor.getColumnIndex(latitude)));
                h.put(longitude,cursor.getString(cursor.getColumnIndex(longitude)));
                h.put(location,cursor.getString(cursor.getColumnIndex(location)));
                h.put(photo,cursor.getString(cursor.getColumnIndex(photo)));

                h.put(remark,cursor.getString(cursor.getColumnIndex(remark)));


                arrayList.add(h);
                cursor.moveToNext();
            }
        }
        cursor.close();
        return arrayList;
    }

    public boolean nonControllableInsert(HashMap<String,String> hashMap) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(schema, hashMap.get(schema));
        contentValues.put(wbs, hashMap.get(wbs));
        contentValues.put(installation_date, hashMap.get(installation_date));
        contentValues.put(fitting, hashMap.get(fitting));
        contentValues.put(non_type, hashMap.get(non_type));

        contentValues.put(latitude, hashMap.get(latitude));
        contentValues.put(longitude, hashMap.get(longitude));
        contentValues.put(location, hashMap.get(location));

        contentValues.put(photo, hashMap.get(photo));
        contentValues.put(remarks, hashMap.get(remarks));

        int i = (int) db.insert(NonControllable_Table, null, contentValues);
        if (i==-1){
            return false;
        }else
            return true;
    }

    public ArrayList<HashMap<String, String>> getNonControllableData() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery( "select * from " +NonControllable_Table+" ORDER BY "+ COLUMN_ID+" ASC", null );
        ArrayList<HashMap<String ,String >> arrayList=new ArrayList<>();

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                HashMap<String,String> h=new HashMap<>();
                h.put("COLUMN_ID",cursor.getString(cursor.getColumnIndex(COLUMN_ID)));
                h.put(schema,cursor.getString(cursor.getColumnIndex(schema)));
                h.put(wbs,cursor.getString(cursor.getColumnIndex(wbs)));
                h.put(installation_date,cursor.getString(cursor.getColumnIndex(installation_date)));
                h.put(network_type,cursor.getString(cursor.getColumnIndex(network_type)));

                h.put(fitting,cursor.getString(cursor.getColumnIndex(fitting)));
                h.put(non_type,cursor.getString(cursor.getColumnIndex(non_type)));

                h.put(latitude,cursor.getString(cursor.getColumnIndex(latitude)));
                h.put(longitude,cursor.getString(cursor.getColumnIndex(longitude)));
                h.put(location,cursor.getString(cursor.getColumnIndex(location)));
                h.put(photo,cursor.getString(cursor.getColumnIndex(photo)));

                h.put(remarks,cursor.getString(cursor.getColumnIndex(remarks)));


                arrayList.add(h);
                cursor.moveToNext();
            }
        }
        cursor.close();
        return arrayList;
    }
    public boolean tfInsert(HashMap<String,String> hashMap) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(schema, hashMap.get(schema));
        contentValues.put(wbs, hashMap.get(wbs));
        contentValues.put(installation_date, hashMap.get(installation_date));
        contentValues.put(tf_property, hashMap.get(tf_property));

        contentValues.put(latitude, hashMap.get(latitude));
        contentValues.put(longitude, hashMap.get(longitude));
        contentValues.put(location, hashMap.get(location));



        int i = (int) db.insert(TF_Table, null, contentValues);
        if (i==-1){
            return false;
        }else
            return true;
    }

    public ArrayList<HashMap<String, String>> getTFData() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery( "select * from " +TF_Table+" ORDER BY "+ COLUMN_ID+" ASC", null );
        ArrayList<HashMap<String ,String >> arrayList=new ArrayList<>();

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                HashMap<String,String> h=new HashMap<>();
                h.put("COLUMN_ID",cursor.getString(cursor.getColumnIndex(COLUMN_ID)));
                h.put(schema,cursor.getString(cursor.getColumnIndex(schema)));
                h.put(wbs,cursor.getString(cursor.getColumnIndex(wbs)));
                h.put(installation_date,cursor.getString(cursor.getColumnIndex(installation_date)));
                h.put(tf_property,cursor.getString(cursor.getColumnIndex(tf_property)));

                h.put(latitude,cursor.getString(cursor.getColumnIndex(latitude)));
                h.put(longitude,cursor.getString(cursor.getColumnIndex(longitude)));
                h.put(location,cursor.getString(cursor.getColumnIndex(location)));


                arrayList.add(h);
                cursor.moveToNext();
            }
        }
        cursor.close();
        return arrayList;
    }
    public boolean excavationInsert(HashMap<String,String> hashMap) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(schema, hashMap.get(schema));
        contentValues.put(wbs, hashMap.get(wbs));
        contentValues.put(excavation_date, hashMap.get(excavation_date));
        contentValues.put(excavation_qty, hashMap.get(excavation_qty));

        contentValues.put(excavation_of, hashMap.get(excavation_of));
        contentValues.put(start_x, hashMap.get(start_x));
        contentValues.put(start_y, hashMap.get(start_y));
        contentValues.put(end_x, hashMap.get(end_x));
        contentValues.put(end_y, hashMap.get(end_y));


        int i = (int) db.insert(Excavation_Table, null, contentValues);
        if (i==-1){
            return false;
        }else
            return true;
    }

    public ArrayList<HashMap<String, String>> getExcavationData() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery( "select * from " +Excavation_Table+" ORDER BY "+ COLUMN_ID+" ASC", null );
        ArrayList<HashMap<String ,String >> arrayList=new ArrayList<>();

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                HashMap<String,String> h=new HashMap<>();
                h.put("COLUMN_ID",cursor.getString(cursor.getColumnIndex(COLUMN_ID)));
                h.put(schema,cursor.getString(cursor.getColumnIndex(schema)));
                h.put(wbs,cursor.getString(cursor.getColumnIndex(wbs)));
                h.put(excavation_date,cursor.getString(cursor.getColumnIndex(excavation_date)));
                h.put(excavation_qty,cursor.getString(cursor.getColumnIndex(excavation_qty)));

                h.put(excavation_of,cursor.getString(cursor.getColumnIndex(excavation_of)));
                h.put(start_x,cursor.getString(cursor.getColumnIndex(start_x)));
                h.put(start_y,cursor.getString(cursor.getColumnIndex(start_y)));
                h.put(end_x,cursor.getString(cursor.getColumnIndex(end_x)));
                h.put(end_y,cursor.getString(cursor.getColumnIndex(end_y)));


                arrayList.add(h);
                cursor.moveToNext();
            }
        }
        cursor.close();
        return arrayList;
    }
    public boolean restorationInsert(HashMap<String,String> hashMap) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(schema, hashMap.get(schema));
        contentValues.put(wbs, hashMap.get(wbs));
        contentValues.put(restoration_date, hashMap.get(restoration_date));
        contentValues.put(restoration_type, hashMap.get(restoration_type));

        contentValues.put(restoration_of, hashMap.get(restoration_of));
        contentValues.put(area, hashMap.get(area));
        contentValues.put(volume, hashMap.get(volume));


        int i = (int) db.insert(Restoration_Table, null, contentValues);
        if (i==-1){
            return false;
        }else
            return true;
    }

    public ArrayList<HashMap<String, String>> getRestorationData() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery( "select * from " +Restoration_Table+" ORDER BY "+ COLUMN_ID+" ASC", null );
        ArrayList<HashMap<String ,String >> arrayList=new ArrayList<>();

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                HashMap<String,String> h=new HashMap<>();
                h.put("COLUMN_ID",cursor.getString(cursor.getColumnIndex(COLUMN_ID)));
                h.put(schema,cursor.getString(cursor.getColumnIndex(schema)));
                h.put(wbs,cursor.getString(cursor.getColumnIndex(wbs)));
                h.put(restoration_date,cursor.getString(cursor.getColumnIndex(restoration_date)));
                h.put(restoration_type,cursor.getString(cursor.getColumnIndex(restoration_type)));

                h.put(restoration_of,cursor.getString(cursor.getColumnIndex(restoration_of)));
                h.put(area,cursor.getString(cursor.getColumnIndex(area)));
                h.put(volume,cursor.getString(cursor.getColumnIndex(volume)));


                arrayList.add(h);
                cursor.moveToNext();
            }
        }
        cursor.close();
        return arrayList;
    }
    public boolean buildingInsert(HashMap<String,String> hashMap) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(schema, hashMap.get(schema));
        contentValues.put(wbs, hashMap.get(wbs));
        contentValues.put(crossing_date, hashMap.get(crossing_date));
        contentValues.put(building_category, hashMap.get(building_category));
        contentValues.put(building_name, hashMap.get(building_name));
        contentValues.put(no_of_flats, hashMap.get(no_of_flats));
        contentValues.put(no_of_floor, hashMap.get(no_of_floor));
        contentValues.put(gate_longitude, hashMap.get(gate_longitude));

        contentValues.put(gate_latitude, hashMap.get(gate_latitude));


        int i = (int) db.insert(Building_Table, null, contentValues);
        if (i==-1){
            return false;
        }else
            return true;
    }

    public ArrayList<HashMap<String, String>> getBuildingData() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery( "select * from " +Building_Table+" ORDER BY "+ COLUMN_ID+" ASC", null );
        ArrayList<HashMap<String ,String >> arrayList=new ArrayList<>();

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                HashMap<String,String> h=new HashMap<>();
                h.put("COLUMN_ID",cursor.getString(cursor.getColumnIndex(COLUMN_ID)));
                h.put(schema,cursor.getString(cursor.getColumnIndex(schema)));
                h.put(wbs,cursor.getString(cursor.getColumnIndex(wbs)));
                h.put(crossing_date,cursor.getString(cursor.getColumnIndex(crossing_date)));
                h.put(building_category,cursor.getString(cursor.getColumnIndex(building_category)));
                h.put(building_name,cursor.getString(cursor.getColumnIndex(building_name)));
                h.put(no_of_flats,cursor.getString(cursor.getColumnIndex(no_of_flats)));
                h.put(no_of_floor,cursor.getString(cursor.getColumnIndex(no_of_floor)));
                h.put(gate_longitude,cursor.getString(cursor.getColumnIndex(gate_longitude)));

                h.put(gate_latitude,cursor.getString(cursor.getColumnIndex(gate_latitude)));

                arrayList.add(h);
                cursor.moveToNext();
            }
        }
        cursor.close();
        return arrayList;
    }










    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " +Laying_Table);
        db.execSQL("DROP TABLE IF EXISTS " +Design_Table);
        db.execSQL("DROP TABLE IF EXISTS " +Laying_Design_Table);
        db.execSQL("DROP TABLE IF EXISTS " +MRS_Table);
        db.execSQL("DROP TABLE IF EXISTS " +Marker_Table);
        db.execSQL("DROP TABLE IF EXISTS " +Regulator_Table);
        db.execSQL("DROP TABLE IF EXISTS " +Valve_Table);
        db.execSQL("DROP TABLE IF EXISTS " +TF_Table);
        db.execSQL("DROP TABLE IF EXISTS " +NonControllable_Table);
        db.execSQL("DROP TABLE IF EXISTS " +Excavation_Table);
        db.execSQL("DROP TABLE IF EXISTS " +Restoration_Table);
        db.execSQL("DROP TABLE IF EXISTS " +Building_Table);
        db.execSQL("DROP TABLE IF EXISTS " +Crossing_Table);
        db.execSQL("DROP TABLE IF EXISTS " +FittingMaterial_Table);
        db.execSQL("DROP TABLE IF EXISTS " +Fitting_Table);
        db.execSQL("DROP TABLE IF EXISTS " +Gap_Table);
        db.execSQL("DROP TABLE IF EXISTS " +"tbl_remove_gap_list_data");
        db.execSQL("DROP TABLE IF EXISTS " +Crossing_Category_Table);
        onCreate(db);
    }
}
