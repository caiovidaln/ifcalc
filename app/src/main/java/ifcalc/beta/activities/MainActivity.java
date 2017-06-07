package ifcalc.beta.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import ifcalc.beta.R;
import ifcalc.beta.util.ImportDisciplinasTask;

public class MainActivity extends BaseActivity {

    private TextView textCalc, textDisc, textNote, textConf;
    private CardView btCalc, btDisc, btAnot, btConf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setConfigurationsLayout();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    private void setConfigurationsLayout() {
        setUpToolbar();
        setUpTextViews();
        setUpButtons();
    }

    private void setUpTextViews() {
        textCalc = (TextView)findViewById(R.id.textCalc);
        textDisc = (TextView)findViewById(R.id.textDisc);
        textNote = (TextView)findViewById(R.id.textAnota);
        textConf = (TextView)findViewById(R.id.textConf);

        textCalc.setTypeface(swisRoman);
        textCalc.setTextSize(16.f);

        textDisc.setTypeface(swisRoman);
        textDisc.setTextSize(16.f);

        textNote.setTypeface(swisRoman);
        textNote.setTextSize(16.f);

        textConf.setTypeface(swisRoman);
        textConf.setTextSize(16.f);
    }

    private void setUpButtons() {
        btCalc = (CardView) findViewById(R.id.btCalc);
        btDisc = (CardView) findViewById(R.id.btDisc);
        btAnot = (CardView) findViewById(R.id.btAnot);
        btConf = (CardView) findViewById(R.id.btConf);

        btCalc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                startActivityCalc();
            }
        });

        btDisc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                startActivityDisciplinas();
            }
        });

        btAnot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                startActivityAnotacoes();
            }
        });

        btConf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                startActivityConfiguracoes();
            }

        });
    }

    public void startActivityCalc(){
        startActivity(new Intent(this, CalculatorActivity.class));
    }

}
