package ifcalc.beta.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import ifcalc.beta.R;

public class PoliticaPrivacidadeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_politica_privacidade);
        setUpToolbar(getString(R.string.title_activity_politica_privacidade));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setUpViews();
    }

    private void setUpViews() {
        TextView textView1  = (TextView) findViewById(R.id.textView1);
        TextView textView2  = (TextView) findViewById(R.id.textView2);
        TextView textView3  = (TextView) findViewById(R.id.textView3);
        TextView textView4  = (TextView) findViewById(R.id.textView4);

        textView1.setTypeface(pfBeausans);
        textView2.setTypeface(pfBeausans);
        textView3.setTypeface(pfBeausans);
        textView4.setTypeface(pfBeausans);
    }

}
