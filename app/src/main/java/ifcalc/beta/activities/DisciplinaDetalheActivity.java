package ifcalc.beta.activities;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.RoundingMode;

import ifcalc.beta.R;
import ifcalc.beta.database.DataSource;
import ifcalc.beta.model.ConfiguracoesDisciplinas;
import ifcalc.beta.model.Disciplina;
import ifcalc.beta.model.NotasCalculadora;
import ifcalc.beta.model.NotasCalculadoraAnual;
import ifcalc.beta.model.NotasCalculadoraSemestral;
import ifcalc.beta.model.TipoDisciplina;
import ifcalc.beta.util.ResultadoCalculo;
import ifcalc.beta.util.Util;

public class DisciplinaDetalheActivity extends BaseActivity {

    private ConfiguracoesDisciplinas confDisc;
    private boolean erroPesos;

    private TextView txtTituloDisc, txtBimestre1, txtBimestre2, txtBimestre3, txtBimestre4, txtProvaFinal;
    private TextView situacaoStatus, txtMensagem, txtResultado, txtMedia, txtNota;
    private TextView txtResultError;

    private TextView result, med, situ; //Apenas para aplicar a fonte personalizada

    private EditText edtTituloDisciplina, edtBimestre1, edtBimestre2, edtBimestre3, edtBimestre4, edtProvaFinal;

    private LinearLayout resultShow, resultError;

    private Double notaB1, notaB2, notaB3, notaB4, notaProvaFinal;

    private boolean zeroAcem = true;

    private Disciplina disciplina;
    private Integer idDisciplina;

    private TipoDisciplina tipoDisciplina;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disciplina_detalhe);
        setUpToolbar("Disciplina");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setUpViews();
        getIdDisciplina();
        getSettings();
        setUpListeners();
        closeKeyboard();
    }

    private void setUpListeners() {
        edtBimestre1.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                calculaNotas();
                return false;
            }
        });

        edtBimestre2.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                calculaNotas();
                return false;
            }
        });

        if (ehDisciplinaAnual()) {
            edtBimestre3.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    calculaNotas();
                    return false;
                }
            });

            edtBimestre4.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    calculaNotas();
                    return false;
                }
            });
        }
    }

    private void showConfirmationDelete() {
        closeKeyboard();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.confirma_excluir)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        DataSource.getInstance(getApplicationContext()).deleteDisciplina(idDisciplina);
                        finish();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        builder.show();
    }

    private void saveDisciplina() {
        closeKeyboard();
        disciplina.setTitulo(edtTituloDisciplina.getText().toString());

        if (disciplina.getTitulo().trim().isEmpty())
            showSnakeBarMessage(edtTituloDisciplina, "O titulo da disciplina não pode ficar em branco");
        else {
            try {
                if (DataSource.getInstance(this).saveDisciplina(disciplina) > 0) {
                    finish();
                    showSnakeBarMessage(edtBimestre1, "Disciplina salva com sucesso");
                } else {
                    showSnakeBarMessage(edtBimestre1, "Ocorreu um erro ao salvar a disciplina");
                }
            } catch (Exception e) {
                showSnakeBarMessage(edtBimestre1, "Ocorreu um erro ao salvar a disciplina");
            }
        }
    }

    private boolean ehDisciplinaAnual() {
        return tipoDisciplina == TipoDisciplina.ANUAL;
    }

    private boolean ehDisciplinaSemestral() {
        return tipoDisciplina == TipoDisciplina.SEMESTRAL;
    }

    private void setUpViews() {
        txtTituloDisc = (TextView) findViewById(R.id.textTitulo);
        txtBimestre1 = (TextView) findViewById(R.id.textbimestre1);
        txtBimestre2 = (TextView) findViewById(R.id.textbimestre2);
        txtBimestre3 = (TextView) findViewById(R.id.textbimestre3);
        txtBimestre4 = (TextView) findViewById(R.id.textbimestre4);
        txtProvaFinal = (TextView) findViewById(R.id.textprovaf);
        edtTituloDisciplina = (EditText) findViewById(R.id.tituloDisciplina);
        edtBimestre1 = (EditText) findViewById(R.id.bimestre1);
        edtBimestre2 = (EditText) findViewById(R.id.bimestre2);
        edtBimestre3 = (EditText) findViewById(R.id.bimestre3);
        edtBimestre4 = (EditText) findViewById(R.id.bimestre4);
        edtProvaFinal = (EditText) findViewById(R.id.provafinal);
        situacaoStatus  = (TextView) findViewById(R.id.situaresult);
        txtMensagem  = (TextView) findViewById(R.id.textprecisa);
        txtResultado  = (TextView) findViewById(R.id.resultadotext);
        txtMedia  = (TextView) findViewById(R.id.media);
        txtNota  = (TextView) findViewById(R.id.textNota);

        txtResultError  = (TextView) findViewById(R.id.txtResultError);

        resultShow = (LinearLayout) findViewById(R.id.resultShow);
        resultError = (LinearLayout) findViewById(R.id.resultError);

        result = (TextView) findViewById(R.id.result);
        med = (TextView) findViewById(R.id.mediatext);
        situ = (TextView) findViewById(R.id.situacao);

        //set font in views
        txtTituloDisc.setTypeface(pfBeausans);
        txtBimestre1.setTypeface(pfBeausans);
        txtBimestre2.setTypeface(pfBeausans);
        txtBimestre3.setTypeface(pfBeausans);
        txtBimestre4.setTypeface(pfBeausans);
        txtProvaFinal.setTypeface(pfBeausans);
        edtTituloDisciplina.setTypeface(pfBeausans);
        edtBimestre1.setTypeface(pfBeausans);
        edtBimestre2.setTypeface(pfBeausans);
        edtBimestre3.setTypeface(pfBeausans);
        edtBimestre4.setTypeface(pfBeausans);
        edtProvaFinal.setTypeface(pfBeausans);

        situacaoStatus.setTypeface(pfBeausans);
        txtMensagem.setTypeface(pfBeausans);
        txtResultado.setTypeface(pfBeausans);
        txtMedia.setTypeface(pfBeausans);
        txtNota.setTypeface(pfBeausans);

        txtResultError.setTypeface(pfBeausans);

        result.setTypeface(pfBeausans);
        med.setTypeface(pfBeausans);
        situ.setTypeface(pfBeausans);

        if (zeroAcem)
            txtNota.setText(getString(R.string.nota100));
        else
            txtNota.setText(getString(R.string.nota10));

    }

    private void showResultText() {
        resultShow.setVisibility(View.VISIBLE);
    }

    private void hideResultText() {
        resultShow.setVisibility(View.GONE);
    }

    private boolean getNotasEditText() {
        notaB1 = null;
        notaB2 = null;
        notaB3 = null;
        notaB4 = null;
        notaProvaFinal = null;

        try {
            //verifica se o campo esta em branco
            if (!edtTituloDisciplina.getText().toString().trim().isEmpty()) {
                disciplina.setTitulo(edtTituloDisciplina.getText().toString());
            }

            if (!edtBimestre1.getText().toString().trim().isEmpty()) {
                notaB1 = Double.parseDouble(edtBimestre1.getText().toString());
            }

            if (!edtBimestre2.getText().toString().trim().isEmpty()) {
                notaB2 = Double.parseDouble(edtBimestre2.getText().toString());
            }

            if (!edtBimestre3.getText().toString().trim().isEmpty()) {
                notaB3 = Double.parseDouble(edtBimestre3.getText().toString());
            }

            if (!edtBimestre4.getText().toString().trim().isEmpty()) {
                notaB4 = Double.parseDouble(edtBimestre4.getText().toString());
            }

            if (!edtProvaFinal.getText().toString().trim().isEmpty()) {
                notaProvaFinal = Double.parseDouble(edtProvaFinal.getText().toString());
            }

            disciplina.setNotaB1(notaB1);
            disciplina.setNotaB2(notaB2);

            if (ehDisciplinaAnual()) {
                disciplina.setNotaB3(notaB3);
                disciplina.setNotaB4(notaB4);
            }

            disciplina.setProvaFinal(notaProvaFinal);


            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void getSettings() {
        SharedPreferences preferences = Util.getPreferences(this);
        try {
            if(ehDisciplinaAnual()) {
                int pesob1 = Integer.parseInt(preferences.getString(ConfiguracaoActivity.PESO_B1_ANUAL, "2"));
                int pesob2 = Integer.parseInt(preferences.getString(ConfiguracaoActivity.PESO_B2_ANUAL, "2"));
                int pesob3 = Integer.parseInt(preferences.getString(ConfiguracaoActivity.PESO_B3_ANUAL, "3"));
                int pesob4 = Integer.parseInt(preferences.getString(ConfiguracaoActivity.PESO_B4_ANUAL, "3"));

                int media = Integer.parseInt(preferences.getString(ConfiguracaoActivity.MEDIA_ANUAL, "60"));
                confDisc = new ConfiguracoesDisciplinas(TipoDisciplina.ANUAL, media);

                if (preferences.getBoolean(ConfiguracaoActivity.CALCULO_PONDERADO, true)) {
                    confDisc.setPeso(1, pesob1);
                    confDisc.setPeso(2, pesob2);
                    confDisc.setPeso(3, pesob3);
                    confDisc.setPeso(4, pesob4);
                } else {
                    confDisc.setPeso(1, 1);
                    confDisc.setPeso(2, 1);
                    confDisc.setPeso(3, 1);
                    confDisc.setPeso(4, 1);
                }
            } else if(ehDisciplinaSemestral()) {
                int pesob1 = Integer.parseInt(preferences.getString(ConfiguracaoActivity.PESO_B1_SEMESTRAL, "2"));
                int pesob2 = Integer.parseInt(preferences.getString(ConfiguracaoActivity.PESO_B2_SEMESTRAL, "3"));

                int media = Integer.parseInt(preferences.getString(ConfiguracaoActivity.MEDIA_SEMESTRAL, "60"));
                confDisc = new ConfiguracoesDisciplinas(TipoDisciplina.SEMESTRAL, media);

                if (preferences.getBoolean(ConfiguracaoActivity.CALCULO_PONDERADO, true)) {
                    confDisc.setPeso(1, pesob1);
                    confDisc.setPeso(2, pesob2);
                } else {
                    confDisc.setPeso(1, 1);
                    confDisc.setPeso(2, 1);
                }
            }

            if (idDisciplina != null)
                calculaNotas();

            zeroAcem = true;
        } catch (Exception e) {
            e.printStackTrace();
//            showMessageErrorConfiguration();
            erroPesos = true;
        }
    }

    private void getIdDisciplina() {
        idDisciplina = getIntent().getIntExtra("ID_DISCIPLINA", -1);
        int tipoD = getIntent().getIntExtra("TIPO_DISCIPLINA", 0);
        switch (tipoD) {
            case 1:
                tipoDisciplina = TipoDisciplina.ANUAL;
                break;
            case 2:
                tipoDisciplina = TipoDisciplina.SEMESTRAL;
                break;
        }

        if (idDisciplina == -1)
            idDisciplina = null;

        setUpDisciplinaDetalhes();
    }

    private void setUpDisciplinaDetalhes() {
        LinearLayout layoutN3 = (LinearLayout) findViewById(R.id.layoutN3);
        LinearLayout layoutN4 = (LinearLayout) findViewById(R.id.layoutN4);

        if(idDisciplina != null) {
            disciplina = DataSource.getInstance(this).getDisciplina(idDisciplina);
            tipoDisciplina = disciplina.getTipoDisciplina();
            if (disciplina == null)
                finish();
            edtTituloDisciplina.setText(disciplina.getTitulo());

            setNotasinEditTexts();

        } else {

            getSupportActionBar().setTitle("Nova disciplina");
            disciplina = new Disciplina();
            disciplina.setTipoDisciplina(tipoDisciplina);
            setSituacaoErro();
        }

        if (ehDisciplinaSemestral()) {
            layoutN3.setVisibility(View.GONE);
            layoutN4.setVisibility(View.GONE);
        }

    }

    private void setNotasinEditTexts() {
        if (zeroAcem) {
            if (disciplina.getNotaB1() != null)
                edtBimestre1.setText(String.valueOf(disciplina.getNotaB1().intValue()));
            if (disciplina.getNotaB2() != null)
                edtBimestre2.setText(String.valueOf(disciplina.getNotaB2().intValue()));
            if (ehDisciplinaAnual()) {
                if (disciplina.getNotaB3() != null)
                    edtBimestre3.setText(String.valueOf(disciplina.getNotaB3().intValue()));
                if (disciplina.getNotaB4() != null)
                    edtBimestre4.setText(String.valueOf(disciplina.getNotaB4().intValue()));
            }

            if (disciplina.getProvaFinal() != null)
                edtProvaFinal.setText(String.valueOf(disciplina.getProvaFinal().intValue()));


        } else {
            if (disciplina.getNotaB1() != null)
                edtBimestre1.setText(disciplina.getNotaB1().toString());
            if (disciplina.getNotaB2() != null)
                edtBimestre2.setText(disciplina.getNotaB2().toString());
            if (ehDisciplinaAnual()) {
                if (disciplina.getNotaB3() != null)
                    edtBimestre3.setText(disciplina.getNotaB3().toString());
                if (disciplina.getNotaB4() != null)
                    edtBimestre4.setText(disciplina.getNotaB4().toString());
            }

            if (disciplina.getProvaFinal() != null)
                edtProvaFinal.setText(disciplina.getProvaFinal().toString());
        }


    }

    private void calculaNotas() {
        showResultText();
        hideResultError();
        verificaNotasReais(zeroAcem);
        NotasCalculadora notasCalculadora = null;
        ResultadoCalculo resultadoCalculo = null;
        if (getNotasEditText() && verificaNotasReais(zeroAcem)) {
            if (ehDisciplinaAnual())
                notasCalculadora = new NotasCalculadoraAnual(zeroAcem, notaB1, notaB2, notaB3, notaB4, notaProvaFinal);
            else if (ehDisciplinaSemestral())
                notasCalculadora = new NotasCalculadoraSemestral(zeroAcem, notaB1, notaB2, notaProvaFinal);
            if (!erroPesos) {
                resultadoCalculo = notasCalculadora.calculaNotas(confDisc);

                if (resultadoCalculo != null) {
                    situacaoStatus.setText(resultadoCalculo.getSituacao());
                    disciplina.setSituacao(resultadoCalculo.getSituacao());
                    txtMensagem.setText(resultadoCalculo.getMensagem());
                    if (zeroAcem){
                        txtMedia.setText(String.valueOf(Math.round(resultadoCalculo.getMediaFinal())));
                        disciplina.setMedia((double) Math.round(resultadoCalculo.getMediaFinal()));
                    } else {
                        BigDecimal media = new BigDecimal(resultadoCalculo.getMediaFinal()).setScale(1, RoundingMode.HALF_EVEN);
                        txtMedia.setText(String.valueOf(media));
                        disciplina.setMedia(media.doubleValue());
                    }
                } else if (notasCorretasIncompletas()) {
                    showResultError();
                    hideResultText();
                    //Poucas notas
                } else {
                    setSituacaoErro();
                    showResultError("Ocorreu um erro no cálculo, verifique as notas e tente novamente!");
                    hideResultText();
                    //erro ao fazer o calculo
                }
            } else {
                setSituacaoErro();
                showResultError("Os pesos não estão configurados corretamente!");
                hideResultText();
                //erro nos pesos
            }
        } else {
            setSituacaoErro();
            showResultError("Alguma(s) das notas são inválidas, verifique!");
            hideResultText();
        }

    }

    private void setSituacaoErro() {
        disciplina.setSituacao("Cursando");
        disciplina.setMedia(new Double(0));
    }

    private boolean notasCorretasIncompletas() {
        if (ehDisciplinaAnual()) {
            if (edtBimestre2.getText().toString().trim().isEmpty() &&
                    edtBimestre3.getText().toString().trim().isEmpty() &&
                    edtBimestre4.getText().toString().trim().isEmpty() &&
                    edtProvaFinal.getText().toString().trim().isEmpty()){

                disciplina.setSituacao("Cursando");

                if (edtBimestre1.getText().toString().trim().isEmpty())
                    disciplina.setMedia(new Double(0));
                else
                    disciplina.setMedia(getMediaNotasIncompletas());
                return true;
            }
        } else if (ehDisciplinaSemestral()) {
            if (edtBimestre1.getText().toString().trim().isEmpty() &&
                    edtBimestre2.getText().toString().trim().isEmpty() &&
                    edtProvaFinal.getText().toString().trim().isEmpty())
                return true;
        }

        return false;
    }

    private Double getMediaNotasIncompletas() {
        Double result = new Double(edtBimestre1.getText().toString());
        return result * confDisc.getPeso(1) / confDisc.getSomaPesos();
    }

    private void showResultError() {
        resultError.setVisibility(View.VISIBLE);
        txtResultError.setText(R.string.resultError);
    }

    private void showResultError(String message) {
        resultError.setVisibility(View.VISIBLE);
        txtResultError.setText(message);
    }

    private void hideResultError() {
        resultError.setVisibility(View.GONE);
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



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_detalhe, menu);
        MenuItem item = menu.findItem(R.id.action_delete);
        if (idDisciplina == null)
            item.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_delete:
                showConfirmationDelete();
                return true;
            case R.id.action_save:
                saveDisciplina();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
