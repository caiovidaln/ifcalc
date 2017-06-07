package ifcalc.beta.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import ifcalc.beta.R;

public class AjudaActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajuda);
        setUpToolbar(getString(R.string.title_activity_ajuda));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setUpViews();
    }

    private void setUpViews() {
        TextView textView1  = (TextView) findViewById(R.id.textView1);
        TextView textView2  = (TextView) findViewById(R.id.textView2);
        TextView textView3  = (TextView) findViewById(R.id.textView3);
        TextView textView4  = (TextView) findViewById(R.id.textView4);
        TextView textView5  = (TextView) findViewById(R.id.textView5);
        TextView textView6  = (TextView) findViewById(R.id.textView6);
        TextView textView7  = (TextView) findViewById(R.id.textView7);
        TextView textView8  = (TextView) findViewById(R.id.textView8);

        textView1.setTypeface(pfBeausans);
        textView2.setTypeface(pfBeausans);
        textView3.setTypeface(pfBeausans);
        textView4.setTypeface(pfBeausans);
        textView5.setTypeface(pfBeausans);
        textView6.setTypeface(pfBeausans);
        textView7.setTypeface(pfBeausans);
        textView8.setTypeface(pfBeausans);
    }

}
