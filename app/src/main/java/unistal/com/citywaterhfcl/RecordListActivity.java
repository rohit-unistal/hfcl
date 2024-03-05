package unistal.com.citywaterhfcl;

import android.content.Intent;
import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class RecordListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_record_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().hide();
        ImageView imgBack = findViewById(R.id.imgback);
        ImageView imgLogout = findViewById(R.id.imglogout);
        LinearLayout lllay = findViewById(R.id.rellaying);
        LinearLayout llvalve = findViewById(R.id.relFitting);
        LinearLayout llgap = findViewById(R.id.relGap);
       /* LinearLayout llmarker = findViewById(R.id.relmarker);
        LinearLayout llmrs = findViewById(R.id.relMRS);
        LinearLayout llregulator = findViewById(R.id.relregulator);
        LinearLayout lltf = findViewById(R.id.reltf);
        LinearLayout llnoncontrollable = findViewById(R.id.relnoncontrollable);
        LinearLayout llexcavation = findViewById(R.id.relExcavation);
        LinearLayout llrestoration = findViewById(R.id.relRestoration);
        LinearLayout llbuilding = findViewById(R.id.relBuilding);*/
        LinearLayout llcrossing = findViewById(R.id.relCrossing);
        imgLogout.setVisibility(View.INVISIBLE);
        lllay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RecordListActivity.this,RecordSync2Activity.class));
            }
        });
        llvalve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RecordListActivity.this,RecordFittingActivity.class));
            }
        });
        llgap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RecordListActivity.this,RecordGapActivity.class));
            }
        });
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
       /* llmarker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RecordListActivity.this,MarkerRecordActivity.class));
            }
        });
        llmrs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RecordListActivity.this,MRSRecordActivity.class));
            }
        });
        llregulator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RecordListActivity.this,RegulatorRecordActivity.class));
            }
        });
        lltf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RecordListActivity.this,TFRecordActivity.class));
            }
        });
        llnoncontrollable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RecordListActivity.this,NonControllableRecordActivity.class));
            }
        });
        llexcavation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RecordListActivity.this,ExcavationRecordActivity.class));
            }
        });
        llrestoration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RecordListActivity.this,RestoreRecordActivity.class));
            }
        });
        llbuilding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RecordListActivity.this,LandbaseRecordActivity.class));
            }
        });*/
        llcrossing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RecordListActivity.this,RecordCrossingActivity.class));
            }
        });
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}