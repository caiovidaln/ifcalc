package ifcalc.beta.activities.fragments;


import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import ifcalc.beta.R;
import ifcalc.beta.model.ConfiguracoesDisciplinas;

public class CalculatorBaseFragment extends Fragment {

    protected ConfiguracoesDisciplinas confDisc;
    protected boolean erroPesos;

    protected TextView txtBimestre1, txtBimestre2, txtBimestre3, txtBimestre4, txtProvaFinal;
    protected TextView situacaoStatus, txtMensagem, txtResultado, txtMedia, txtNota;

    protected Button btCalcular;

    protected EditText edtBimestre1, edtBimestre2, edtBimestre3, edtBimestre4, edtProvaFinal;

    protected Typeface pfBeausans;
    protected Typeface swisRoman;

    protected LinearLayout resultShow;

    protected Double notaB1, notaB2, notaB3, notaB4, notaProvaFinal;

    protected boolean zeroAcem = true;

    public CalculatorBaseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpFonts();
    }

    protected void showSnakeBarMessage(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
    }

    protected void startActivity(Class cl) {
        Intent it = new Intent(getActivity(), cl);
        startActivity(it);
    }

    protected void setUpFonts() {
        pfBeausans = Typeface.createFromAsset(getActivity().getAssets(), "fonts/pfbeausans.ttf");
        swisRoman = Typeface.createFromAsset(getActivity().getAssets(), "fonts/swiscnroman.TTF");
    }

    protected void closeKeyboard() {
        try {
            View view = getActivity().getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        } catch (Exception e){
            Log.e("IFCalc", "Error occured when try close keyboard");
        }
    }

    protected boolean verificaNotasReais(boolean zeroACem){
        if (notaB1 != null)
            if (notaB1 < 0 || (!zeroACem && notaB1 > 10) || notaB1 > 100)
                return false;

        if (notaB2 != null)
            if (notaB2 < 0 || (!zeroACem && notaB2 > 10) || notaB2 > 100)
                return false;

        if (notaB3 != null)
            if (notaB3 < 0 || (!zeroACem && notaB3 > 10) || notaB3 > 100)
                return false;

        if (notaB4 != null)
            if (notaB4 < 0 || (!zeroACem && notaB4 > 10) || notaB4 > 100)
                return false;

        if (notaProvaFinal != null)
            if (notaProvaFinal < 0 || (!zeroACem && notaProvaFinal > 10) || notaProvaFinal > 100)
                return false;

        return true;
    }

}
