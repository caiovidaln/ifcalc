package ifcalc.beta.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import ifcalc.beta.R;
import ifcalc.beta.util.Util;

public class SplashActivity extends BaseActivity {

    private TextView txtCarregando;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();

        txtCarregando = (TextView) findViewById(R.id.carregando);
        txtCarregando.setTypeface(pfBeausans);

        SharedPreferences preferences = Util.getPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();

        if (preferences.getBoolean("FIRST_LOGIN", true)) {
            editor.putBoolean("FIRST_LOGIN", false);
            editor.apply();
            Thread timer = new Thread(){
                public void run(){
                    try {
                        sleep(4000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        startActivityMain();
                        finish();
                    }
                }
            };
            timer.start();
        } else {
            startActivityMain();
            finish();
        }
    }
}
