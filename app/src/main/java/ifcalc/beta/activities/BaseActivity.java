package ifcalc.beta.activities;


import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import ifcalc.beta.R;
import ifcalc.beta.util.ImportDisciplinasTask;

public class BaseActivity extends AppCompatActivity {
    protected Toolbar toolbar;
    protected Typeface pfBeausans, swisRoman;
    protected boolean zeroACem;
    protected final String TAG = "IFCalc";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setUpFonts();
    }

    protected void setUpToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.app_name));
        setSupportActionBar(toolbar);
    }

    protected void startActivity(Class cl) {
        Intent it = new Intent(this, cl);
        startActivity(it);
    }

    protected void setUpToolbar(String title) {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
    }

    protected void closeKeyboard() {
        try {
            View view = getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        } catch (Exception e){
            Log.e(TAG, "Error occured when try close keyboard");
        }
    }

    protected void setUpPreferences() {
        zeroACem = true;
    }

    protected void showSnakeBarMessage(View view, String message) {
        try {
            Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
        } catch (Exception e) {
            Log.e(TAG, "Não foi possível exibir a mensagem a seguir na SnakeBar: " + message);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                startActivityConfiguracoes();
                return true;
            case R.id.action_about:
                startActivitySobre();
                return true;
            case R.id.action_help:
                startActivityAjuda();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected void setUpFonts() {
        pfBeausans = Typeface.createFromAsset(getAssets(), "fonts/pfbeausans.ttf");
        swisRoman = Typeface.createFromAsset(getAssets(), "fonts/swiscnroman.TTF");
    }

    protected void startActivityMain() {
        startActivity(new Intent(this, MainActivity.class));
    }

    protected void startActivitySobre() {
        startActivity(new Intent(this, SobreActivity.class));
    }

    protected void startActivityAjuda() {
        startActivity(new Intent(this, AjudaActivity.class));
    }

    protected void startActivityDisciplinas() {
        startActivity(new Intent(this, DisciplinasActivity.class));
    }

    protected void startActivityAnotacoes() {
        startActivity(new Intent(this, AnotacoesActivity.class));
    }

    protected void startActivityPrivacidade() {
        startActivity(new Intent(this, PoliticaPrivacidadeActivity.class));
    }

    protected void startActivityConfiguracoes() {
        startActivity(new Intent(this, ConfiguracaoActivity.class));
    }

    protected void startActivityImport() {
        startActivity(new Intent(this, ImportDisciplinasActivity.class));
    }


}
