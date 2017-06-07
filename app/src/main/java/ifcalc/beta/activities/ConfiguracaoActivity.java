package ifcalc.beta.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;

import ifcalc.beta.R;
import ifcalc.beta.util.Util;

public class ConfiguracaoActivity extends BaseActivity {

    private EditText edtPesoB1Anual, edtPesoB2Anual, edtPesoB3Anual, edtPesoB4Anual;
    private EditText edtPesoB1Semestral, edtPesoB2Semestral;
    private EditText edtMediaAnual, edtMediaSemestral;
    private Switch switchCalculoPonderado;
    private LinearLayout layoutPesos;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editorPreferences;

    public static final String CALCULO_PONDERADO = "calculoPonderado";
    public static final String PESO_B1_ANUAL = "pesob1a";
    public static final String PESO_B2_ANUAL = "pesob2a";
    public static final String PESO_B3_ANUAL = "pesob3a";
    public static final String PESO_B4_ANUAL = "pesob4a";
    public static final String PESO_B1_SEMESTRAL = "pesob1s";
    public static final String PESO_B2_SEMESTRAL = "pesob2s";
    public static final String MEDIA_ANUAL = "mediaa";
    public static final String MEDIA_SEMESTRAL = "medias";

    public static final String LOGIN = "suap_login";
    public static final String PASSWORD = "suap_password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracao);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        preferences = Util.getPreferences(this);
        editorPreferences = preferences.edit();

        setUpViews();
        setUpFields();
        setUpListeners();
    }

    @Override
    protected void onStart() {
        super.onStart();
        closeKeyboard();
    }

    private void setUpViews() {
        edtPesoB1Anual = (EditText) findViewById(R.id.edtPesoB1Anual);
        edtPesoB2Anual = (EditText) findViewById(R.id.edtPesoB2Anual);
        edtPesoB3Anual = (EditText) findViewById(R.id.edtPesoB3Anual);
        edtPesoB4Anual = (EditText) findViewById(R.id.edtPesoB4Anual);

        edtPesoB1Semestral = (EditText) findViewById(R.id.edtPesoB1Semestral);
        edtPesoB2Semestral = (EditText) findViewById(R.id.edtPesoB2Semestral);

        edtMediaAnual = (EditText) findViewById(R.id.edtMediaAnual);
        edtMediaSemestral = (EditText) findViewById(R.id.edtMediaSemestral);

        switchCalculoPonderado = (Switch) findViewById(R.id.switchCalculoPonderado);

        layoutPesos = (LinearLayout) findViewById(R.id.layoutPesos);
    }

    private void setUpFields() {
        switchCalculoPonderado.setChecked(preferences.getBoolean(CALCULO_PONDERADO, true));

        edtPesoB1Anual.setText(preferences.getString(PESO_B1_ANUAL, "2"));
        edtPesoB2Anual.setText(preferences.getString(PESO_B2_ANUAL, "2"));
        edtPesoB3Anual.setText(preferences.getString(PESO_B3_ANUAL, "3"));
        edtPesoB4Anual.setText(preferences.getString(PESO_B4_ANUAL, "3"));

        edtPesoB1Semestral.setText(preferences.getString(PESO_B1_SEMESTRAL, "2"));
        edtPesoB2Semestral.setText(preferences.getString(PESO_B2_SEMESTRAL, "3"));

        if (switchCalculoPonderado.isChecked())
            layoutPesos.setVisibility(View.VISIBLE);
         else
            layoutPesos.setVisibility(View.GONE);

        edtMediaAnual.setText(preferences.getString(MEDIA_ANUAL, "60"));
        edtMediaSemestral.setText(preferences.getString(MEDIA_SEMESTRAL, "60"));

    }

    private void setUpListeners() {
        edtPesoB1Anual.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                saveConfiguration();
                return false;
            }
        });

        edtPesoB2Anual.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                saveConfiguration();
                return false;
            }
        });

        edtPesoB2Anual.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                saveConfiguration();
                return false;
            }
        });

        edtPesoB4Anual.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                saveConfiguration();
                return false;
            }
        });

        edtPesoB1Semestral.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                saveConfiguration();
                return false;
            }
        });

        edtPesoB2Semestral.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                saveConfiguration();
                return false;
            }
        });

        edtMediaAnual.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                saveConfiguration();
                return false;
            }
        });

        edtMediaSemestral.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                saveConfiguration();
                return false;
            }
        });

        switchCalculoPonderado.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                saveConfiguration();
            }
        });

    }

    private void saveConfiguration() {
        editorPreferences.putBoolean(CALCULO_PONDERADO, switchCalculoPonderado.isChecked());

        editorPreferences.putString(PESO_B1_ANUAL, edtPesoB1Anual.getText().toString());
        editorPreferences.putString(PESO_B2_ANUAL, edtPesoB2Anual.getText().toString());
        editorPreferences.putString(PESO_B3_ANUAL, edtPesoB3Anual.getText().toString());
        editorPreferences.putString(PESO_B4_ANUAL, edtPesoB4Anual.getText().toString());

        editorPreferences.putString(PESO_B1_SEMESTRAL, edtPesoB1Semestral.getText().toString());
        editorPreferences.putString(PESO_B2_SEMESTRAL, edtPesoB2Semestral.getText().toString());

        editorPreferences.putString(MEDIA_ANUAL, edtMediaAnual.getText().toString());
        editorPreferences.putString(MEDIA_SEMESTRAL, edtMediaSemestral.getText().toString());

        editorPreferences.apply();

        if (switchCalculoPonderado.isChecked())
            layoutPesos.setVisibility(View.VISIBLE);
        else
            layoutPesos.setVisibility(View.GONE);
    }

}
