package ifcalc.beta.activities.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.math.RoundingMode;

import ifcalc.beta.R;
import ifcalc.beta.activities.ConfiguracaoActivity;
import ifcalc.beta.model.ConfiguracoesDisciplinas;
import ifcalc.beta.model.NotasCalculadoraSemestral;
import ifcalc.beta.model.TipoDisciplina;
import ifcalc.beta.util.ResultadoCalculo;
import ifcalc.beta.util.Util;

public class CalculatorSemestralFragment extends CalculatorBaseFragment {

    public CalculatorSemestralFragment() {
    }

    public static CalculatorSemestralFragment newInstance() {
        CalculatorSemestralFragment fragment = new CalculatorSemestralFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_calculator_semestral, container, false);

        getSettings();
        setUpViews(view);
        setUpFonts();

        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void getSettings() {
        SharedPreferences preferences = Util.getPreferences(getContext());
        try {
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
            zeroAcem = true;
        } catch (Exception e) {
            showMessageErrorConfiguration();
            erroPesos = true;
        }
    }

    private void setUpViews(View view) {
        txtBimestre1 = (TextView) view.findViewById(R.id.textbimestre1);
        txtBimestre2 = (TextView) view.findViewById(R.id.textbimestre2);
        txtProvaFinal = (TextView) view.findViewById(R.id.textprovaf);
        btCalcular = (Button) view.findViewById(R.id.btcalcular);
        edtBimestre1 = (EditText) view.findViewById(R.id.bimestre1);
        edtBimestre2 = (EditText) view.findViewById(R.id.bimestre2);
        edtProvaFinal = (EditText) view.findViewById(R.id.provafinal);
        situacaoStatus  = (TextView) view.findViewById(R.id.situaresult);
        txtMensagem  = (TextView) view.findViewById(R.id.textprecisa);
        txtResultado  = (TextView) view.findViewById(R.id.resultadotext);
        txtMedia  = (TextView) view.findViewById(R.id.media);
        txtNota  = (TextView) view.findViewById(R.id.textNota);

        resultShow = (LinearLayout) view.findViewById(R.id.resultShow);

        //set font in views
        txtBimestre1.setTypeface(pfBeausans);
        txtBimestre2.setTypeface(pfBeausans);
        txtProvaFinal.setTypeface(pfBeausans);
        btCalcular.setTypeface(pfBeausans);
        edtBimestre1.setTypeface(pfBeausans);
        edtBimestre2.setTypeface(pfBeausans);
        edtProvaFinal.setTypeface(pfBeausans);

        situacaoStatus.setTypeface(pfBeausans);
        txtMensagem.setTypeface(pfBeausans);
        txtResultado.setTypeface(pfBeausans);
        txtMedia.setTypeface(pfBeausans);
        txtNota.setTypeface(pfBeausans);

        if (zeroAcem)
            txtNota.setText(getString(R.string.nota100));
        else
            txtNota.setText(getString(R.string.nota10));


        //set onClick Buttons
        btCalcular.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                calculaNotas(v);
            }
        });
    }

    private boolean getNotasEditText() {
        try {
            //verifica se o campo esta em branco
            if (!edtBimestre1.getText().toString().trim().isEmpty()) {
                notaB1 = Double.parseDouble(edtBimestre1.getText().toString());
            }

            if (!edtBimestre2.getText().toString().trim().isEmpty()) {
                notaB2 = Double.parseDouble(edtBimestre2.getText().toString());
            }

            if (!edtProvaFinal.getText().toString().trim().isEmpty()) {
                notaProvaFinal = Double.parseDouble(edtProvaFinal.getText().toString());
            }



            return true;
        } catch (Exception e) {
            showToast("Erro ao pegar notas");
            return false;
        }
    }

    private void limpaCamposDados() {
        notaB1 = null;
        notaB2 = null;
        notaProvaFinal = null;
        situacaoStatus.setText("");
        txtMensagem.setText("");
        txtMedia.setText("");
    }

    private void showResultText() {
        resultShow.setVisibility(View.VISIBLE);
    }

    private void hideResultText() {
        resultShow.setVisibility(View.GONE);
    }

    private void calculaNotas(View view) {

        limpaCamposDados();
        showResultText();
        verificaNotasReais(zeroAcem);
        closeKeyboard();

        ResultadoCalculo resultadoCalculo = null;
        if (getNotasEditText() && verificaNotasReais(zeroAcem)) {
            NotasCalculadoraSemestral notasCalculadoraSemestral = new NotasCalculadoraSemestral(zeroAcem, notaB1, notaB2, notaProvaFinal);
            if (!erroPesos) {
                resultadoCalculo = notasCalculadoraSemestral.calculaNotas(confDisc);

                if (resultadoCalculo != null) {
                    situacaoStatus.setText(resultadoCalculo.getSituacao());
                    txtMensagem.setText(resultadoCalculo.getMensagem());
                    if (zeroAcem)
                        txtMedia.setText(String.valueOf(Math.round(resultadoCalculo.getMediaFinal())));
                    else {
                        BigDecimal media = new BigDecimal(resultadoCalculo.getMediaFinal()).setScale(1, RoundingMode.HALF_EVEN);
                        txtMedia.setText(String.valueOf(media));
                    }
                } else {
                    showSnakeBarMessage(view, "Ocorreu um erro no cálculo, verifique as notas e tente novamente!");
                    hideResultText();
                    //erro ao fazer o calculo
                }
            } else {
                showSnakeBarMessage(view, "Os pesos não estão configurados corretamente!");
                hideResultText();
                //erro nos pesos
            }
        } else {
            showSnakeBarMessage(view, "Alguma(s) das notas são inválidas, verifique!");
            hideResultText();
        }

//        if (disciano) {
//
//            if (!erroconver) {
//
//                b1 = (bimestre1.getText().toString());
//                b2 = (bimestre2.getText().toString());
//                b3 = (bimestre3.getText().toString());
//                b4 = (bimestre4.getText().toString());
//                pf = (provafinal.getText().toString());
//
//                eh_brancoo(b1, b2, b3, b4, pf);
//
//                if (!b1.equals(null) && !b2.equals(null) && !b3.equals(null) && !b4.equals(null) && !pf.equals(null) && !b1.equals("") && !b2.equals("") && !b3.equals("") && !b4.equals("") && !pf.equals("")) {
//
//                    resultado = ((((bimes1 * pesob1a) + (bimes2 * pesob2a) + (bimes3 * pesob3a) + (bimes4 * pesob4a)) / somapesoa) + provaf) / 2;
//
//                    if (resultado >= mediaa) {
//
//                        situaresult.setText(String.valueOf("Aprovado"));
//                        textprecisa.setText(String.valueOf("Parabéns, você foi aprovado!"));
//                        resultadotext.setText(String.valueOf(""));
//                        resultadotext2.setText(String.valueOf(""));
//                        mediat.setText(String.valueOf(resultado));
//
//                    } else if (resultado < mediaa) {
//
//
//                        int nota1, nota2, nota3, nota4, f;
//                        boolean menormedia = false;
//
//                        nota1 = ((provaf * pesob1a) + (bimes2 * pesob2a) + (bimes3 * pesob3a) + (bimes4 * pesob4a)) / somapesoa;
//                        nota2 = ((bimes1 * pesob1a) + (provaf * pesob2a) + (bimes3 * pesob3a) + (bimes4 * pesob4a)) / somapesoa;
//                        nota3 = ((bimes1 * pesob1a) + (bimes2 * pesob2a) + (provaf * pesob3a) + (bimes4 * pesob4a)) / somapesoa;
//                        nota4 = ((bimes1 * pesob1a) + (bimes2 * pesob2a) + (bimes3 * pesob3a) + (provaf * pesob4a)) / somapesoa;
//
//
//                        if (nota1 >= mediaa) {
//
//
//                            menormedia = true;
//
//                            mediat.setText(String.valueOf(nota1));
//
//
//                        }
//                        if (nota2 >= mediaa && !menormedia) {
//
//
//                            menormedia = true;
//                            mediat.setText(String.valueOf(nota2));
//
//
//                        }
//                        if (nota3 >= mediaa && !menormedia) {
//
//                            menormedia = true;
//
//                            mediat.setText(String.valueOf(nota3));
//
//
//                        }
//
//                        if (nota4 >= mediaa && !menormedia) {
//
//                            menormedia = true;
//
//                            mediat.setText(String.valueOf(nota4));
//
//
//                        }
//
//                        situaresult.setText(String.valueOf("Aprovado"));
//                        textprecisa.setText(String.valueOf("Parabêns, você foi aprovado!"));
//                        resultadotext.setText(String.valueOf(""));
//                        resultadotext2.setText(String.valueOf(""));
//
//
//                        if (!menormedia) {
//
//                            situaresult.setText(String.valueOf("Reprovado"));
//                            textprecisa.setText(String.valueOf("Você está reprovado!"));
//                            resultadotext.setText(String.valueOf(""));
//                            resultadotext2.setText(String.valueOf(""));
//
//
//                            if (nota1 >= nota2 && nota1 >= nota3 && nota1 >= nota4) {
//
//                                mediat.setText(String.valueOf(nota1));
//
//                            } else if (nota2 >= nota1 && nota2 >= nota3 && nota2 >= nota4) {
//
//                                mediat.setText(String.valueOf(nota2));
//
//                            } else if (nota3 >= nota1 && nota3 >= nota2 && nota3 >= nota4) {
//
//                                mediat.setText(String.valueOf(nota3));
//
//                            } else if (nota4 >= nota1 && nota4 >= nota2 && nota4 >= nota3) {
//
//                                mediat.setText(String.valueOf(nota4));
//                            }
//                        }
//                    }
//
//                    result.setText(String.valueOf("Resultado:"));
//                    mediatext.setText(String.valueOf("-  Média anual:"));
//
//
//                    situacao.setText(String.valueOf("Situação:"));
//
//
//                } else if (!b1.equals(null) && !b2.equals(null) && !b3.equals(null) && !b4.equals(null) && !b1.equals("") && !b2.equals("") && !b3.equals("") && !b4.equals("")) {
//
//                    resultado = (((bimes1 * pesob1a) + (bimes2 * pesob2a) + (bimes3 * pesob3a) + (bimes4 * pesob4a)) / somapesoa);
//                    media = (((bimes1 * pesob1a) + (bimes2 * pesob2a) + (bimes3 * pesob3a) + (bimes4 * pesob4a)) / somapesoa);
//                    if (resultado >= mediaa) {
//                        situaresult.setText(String.valueOf("Aprovado"));
//                        textprecisa.setText(String.valueOf("Parabéns, você foi aprovado!"));
//                        resultadotext.setText(String.valueOf(""));
//                        resultadotext2.setText(String.valueOf(""));
//                        mediat.setText(String.valueOf(media));
//
//                    } else if (resultado < mediaa) {
//                        situaresult.setText(String.valueOf("Prova Final"));
//
//                        resultadotext.setText(String.valueOf(""));
//
//                        mediat.setText(String.valueOf(media));
//
//                        int nota1, nota2, nota3, nota4;
//
//                        nota1 = (notapassaa - ((bimes2 * pesob2a) + (bimes3 * pesob3a) + (bimes4 * pesob4a))) / pesob1a;
//
//                        nota2 = (notapassaa - ((bimes1 * pesob1a) + (bimes3 * pesob3a) + (bimes4 * pesob4a))) / pesob2a;
//
//                        nota3 = (notapassaa - ((bimes1 * pesob1a) + (bimes2 * pesob2a) + (bimes4 * pesob4a))) / pesob3a;
//
//                        nota4 = (notapassaa - ((bimes1 * pesob1a) + (bimes2 * pesob2a) + (bimes3 * pesob4a))) / pesob4a;
//
//                        int tArredon;
//                        if (nota1 <= nota2 && nota1 <= nota3 && nota1 <= nota4) {
//
//                            tArredon = (((nota1 * pesob1a) + (bimes2 * pesob2a) + (bimes3 * pesob3a) + (bimes4 * pesob4a)) / somapesoa);
//                            if (tArredon < mediaa) {
//                                nota1 += 1;
//
//                            }
//                            textprecisa.setText(String.valueOf("Para passar na prova final, você precisa tirar: " + String.valueOf(nota1)));
////						resultadotext2.setText(String.valueOf(nota1));
//
//                        } else if (nota2 <= nota1 && nota2 <= nota3 && nota2 <= nota4) {
//                            tArredon = (((bimes1 * pesob1a) + (nota2 * pesob2a) + (bimes3 * pesob3a) + (bimes4 * pesob4a)) / somapesoa);
//                            if (tArredon < mediaa) {
//                                nota2 += 1;
//
//                            }
//
//                            textprecisa.setText(String.valueOf("Para passar na prova final, você precisa tirar: " + String.valueOf(nota2)));
////						resultadotext2.setText(String.valueOf(nota2));
//
//                        } else if (nota3 <= nota1 && nota3 <= nota2 && nota3 <= nota4) {
//
//                            tArredon = (((bimes1 * pesob1a) + (bimes2 * pesob2a) + (nota3 * pesob3a) + (bimes4 * pesob4a)) / somapesoa);
//                            if (tArredon < mediaa) {
//                                nota3 += 1;
//
//                            }
//
//                            textprecisa.setText(String.valueOf("Para passar na prova final, você precisa tirar: " + String.valueOf(nota3)));
////						resultadotext2.setText(String.valueOf(nota3));
//
//                        } else if (nota4 <= nota1 && nota4 <= nota2 && nota4 <= nota3) {
//
//                            tArredon = (((bimes1 * pesob1a) + (bimes2 * pesob2a) + (bimes3 * pesob3a) + (nota4 * pesob4a)) / somapesoa);
//                            if (tArredon < mediaa) {
//                                nota4 += 1;
//
//                            }
//
//                            textprecisa.setText(String.valueOf("Para passar na prova final, você precisa tirar: " + String.valueOf(nota4)));
////						resultadotext2.setText(String.valueOf(nota4));
//                        }
//
//
//                    }
//
//                    result.setText(String.valueOf("Resultado:"));
//                    mediatext.setText(String.valueOf("-  Média anual:"));
//
//
//                    situacao.setText(String.valueOf("Situação:"));
//
//
//                } else if (!b1.equals(null) && !b2.equals(null) && !b3.equals(null) && !b1.equals("") && !b2.equals("") && !b3.equals("") && (b4.equals(null) || b4.equals("")) && (pf.equals(null) || pf.equals(""))) {
//
//
//                    resultado = ((notapassaa - ((bimes1 * pesob1a) + (bimes2 * pesob2a) + (bimes3 * pesob3a))) / pesob4a);
//
//                    int testearre;
//                    testearre = (((bimes1 * pesob1a) + (bimes2 * pesob2a) + (bimes3 * pesob3a) + (resultado * pesob4a)) / somapesoa);
//
//                    if (testearre < mediaa) {
//
//                        resultado += 1;
//
//                    }
//
//                    bimes4 = 0;
//                    media = (((bimes1 * pesob1a) + (bimes2 * pesob2a) + (bimes3 * pesob3a) + (bimes4 * pesob4a)) / somapesoa);
//                    mediat.setText(String.valueOf(media));
//                    if (resultado <= 0) {
//                        situaresult.setText(String.valueOf("Aprovado"));
//                        textprecisa.setText(String.valueOf("Parabéns, você foi aprovado!"));
//                        resultadotext.setText(String.valueOf(""));
//                        resultadotext2.setText(String.valueOf(""));
//
//                    } else if (resultado > 100) {
//                        situaresult.setText(String.valueOf("Prova Final"));
//                        textprecisa.setText(String.valueOf("Você ja está na Prova Final!"));
//                        resultadotext.setText(String.valueOf(""));
//                        resultadotext2.setText(String.valueOf(""));
//                    } else {
//                        situaresult.setText(String.valueOf("Cursando"));
//                        textprecisa.setText(String.valueOf("Para passar por média você precisa tirar: " + String.valueOf(resultado)));
//                        resultadotext.setText(String.valueOf(""));
//                        resultadotext2.setText(String.valueOf(""));
//                    }
//
//                    result.setText(String.valueOf("Resultado:"));
//                    mediatext.setText(String.valueOf("-  Média anual:"));
//
//
//                    situacao.setText(String.valueOf("Situação:"));
//
//                } else if (!b1.equals(null) && !b2.equals(null) && !b1.equals("") && !b2.equals("") && (b3.equals("") || b3.equals(null)) && (b4.equals("") || b4.equals(null)) && (pf.equals(null) || pf.equals(""))) {
//                    resultado = ((notapassaa - ((bimes1 * pesob1a) + (bimes2 * pesob2a))) / (pesob3a + pesob4a));
//
//                    if ((((bimes1 * pesob1a) + (bimes2 * pesob2a) + (resultado * pesob3a) + (resultado * pesob4a)) / somapesoa) < mediaa) {
//                        resultado += 1;
//                    }
//
//                    bimes4 = 0;
//                    bimes3 = 0;
//                    media = (((bimes1 * pesob1a) + (bimes2 * pesob2a) + (bimes3 * pesob3a) + (bimes4 * pesob4a)) / somapesoa);
//                    mediat.setText(String.valueOf(media));
//                    situaresult.setText(String.valueOf("Cursando"));
//                    textprecisa.setText(String.valueOf("Para passar por média você precisa tirar no 3º e 4º: " + String.valueOf(resultado)));
//                    resultadotext2.setText(String.valueOf(""));
//                    resultadotext.setText(String.valueOf(""));
//                    result.setText(String.valueOf("Resultado:"));
//                    mediatext.setText(String.valueOf("-  Média anual:"));
//                    situacao.setText(String.valueOf("Situação:"));
//
//                } else if (!b1.equals(null) && !b1.equals("") && (b2.equals("") || b2.equals(null)) && (b3.equals("") || b3.equals(null)) && (b4.equals("") || b4.equals(null)) && (pf.equals(null) || pf.equals(""))) {
//                    resultado = 0;
//                    bimes4 = 0;
//                    bimes3 = 0;
//                    bimes2 = 0;
//                    media = (((bimes1 * pesob1a) + (bimes2 * pesob2a) + (bimes3 * pesob3a) + (bimes4 * pesob4a)) / somapesoa);
//                    mediat.setText(String.valueOf(media));
//                    situaresult.setText(String.valueOf("Cursando"));
//                    textprecisa.setText(String.valueOf("Ainda não tem notas suficientes para realizar os cálculos!"));
//                    resultadotext2.setText(String.valueOf(""));
//                    resultadotext.setText(String.valueOf(""));
//                    result.setText(String.valueOf("Resultado:"));
//                    mediatext.setText(String.valueOf("-  Média anual:"));
//                    situacao.setText(String.valueOf("Situação:"));
//                } else {
//                    Context context = getApplicationContext();
//                    CharSequence text = "Não é possível fazer esse cálculo!";
//                    int duration = Toast.LENGTH_SHORT;
//
//                    Toast toast = Toast.makeText(context, text, duration);
//                    toast.show();
//
//                    situaresult.setText(String.valueOf(""));
//                    textprecisa.setText(String.valueOf(""));
//                    resultadotext.setText(String.valueOf(""));
//                    resultadotext2.setText(String.valueOf(""));
//                    mediat.setText(String.valueOf(""));
//                    result.setText(String.valueOf(""));
//                    mediatext.setText(String.valueOf(""));
//                    situacao.setText(String.valueOf(""));
//
//                }
//            } else {
//                erroConversao();
//            }//fim condi��o da convers�o
//        } else {
            //calculo materia semestral
//            if (!erroconver) {
//
//                b1 = (bimestre1.getText().toString());
//                b2 = (bimestre2.getText().toString());
//                pf = (provafinal.getText().toString());
//
//                eh_brancoosemes(b1, b2, pf);
//
//                if (!b1.equals(null) && !b2.equals(null) && !pf.equals(null) && !b1.equals("") && !b2.equals("") && !pf.equals("")) {
//
//                    resultado = ((((bimes1 * pesob1s) + (bimes2 * pesob2s)) / somapesos) + provaf) / 2;
//
//                    if (resultado >= medias) {
//
//                        situaresult.setText(String.valueOf("Aprovado"));
//                        textprecisa.setText(String.valueOf("Parabéns, você foi aprovado!"));
//                        resultadotext.setText(String.valueOf(""));
//                        resultadotext2.setText(String.valueOf(""));
//                        mediat.setText(String.valueOf(resultado));
//
//                    } else if (resultado < medias) {
//
//
//                        int nota1, nota2;
//                        boolean menormedia = false;
//
//                        nota1 = ((provaf * pesob1s) + (bimes2 * pesob2s)) / somapesos;
//                        nota2 = ((bimes1 * pesob1s) + (provaf * pesob2s)) / somapesos;
//
//
//                        if (nota1 >= medias) {
//
//
//                            menormedia = true;
//
//                            mediat.setText(String.valueOf(nota1));
//
//
//                        }
//                        if (nota2 >= medias && !menormedia) {
//
//
//                            menormedia = true;
//                            mediat.setText(String.valueOf(nota2));
//
//
//                        }
//
//
//                        situaresult.setText(String.valueOf("Aprovado"));
//                        textprecisa.setText(String.valueOf("Parabéns, você foi aprovado!"));
//                        resultadotext.setText(String.valueOf(""));
//                        resultadotext2.setText(String.valueOf(""));
//
//
//                        if (!menormedia) {
//
//                            situaresult.setText(String.valueOf("Reprovado"));
//                            textprecisa.setText(String.valueOf("Você está reprovado!"));
//                            resultadotext.setText(String.valueOf(""));
//                            resultadotext2.setText(String.valueOf(""));
//
//
//                            if (nota1 >= nota2) {
//
//                                mediat.setText(String.valueOf(nota1));
//
//                            } else if (nota2 >= nota1) {
//
//                                mediat.setText(String.valueOf(nota2));
//
//                            }
//
//
//                        }
//
//
//                    }
//
//
//                } else if (!b1.equals(null) && !b2.equals(null) && !b1.equals("") && !b2.equals("")) {
//
//                    resultado = (((bimes1 * pesob1s) + (bimes2 * pesob2s)) / somapesos);
//                    media = (((bimes1 * pesob1s) + (bimes2 * pesob2s)) / somapesos);
//                    if (resultado >= medias) {
//                        situaresult.setText(String.valueOf("Aprovado"));
//                        textprecisa.setText(String.valueOf("Parabéns, você foi aprovado!"));
//                        resultadotext.setText(String.valueOf(""));
//                        resultadotext2.setText(String.valueOf(""));
//                        mediat.setText(String.valueOf(media));
//
//                    } else if (resultado < medias) {
//                        situaresult.setText(String.valueOf("Prova Final"));
////						textprecisa.setText(String.valueOf("Para passar na prova final, voc� precisa tirar: "));
//                        resultadotext.setText(String.valueOf(""));
//
//                        mediat.setText(String.valueOf(media));
//
//                        int nota1, nota2, nota3, nota4;
//
//                        nota1 = (notapassas - ((bimes2 * pesob2s))) / pesob1s;
//
//                        nota2 = (notapassas - ((bimes1 * pesob1s))) / pesob2s;
//
//
//                        int tArredon;
//                        if (nota1 <= nota2) {
//
//                            tArredon = (((nota1 * pesob1s) + (bimes2 * pesob2s)) / somapesos);
//                            if (tArredon < medias) {
//                                nota1 += 1;
//
//                            }
//                            textprecisa.setText(String.valueOf("Para passar na prova final, você precisa tirar: " + String.valueOf(nota1)));
////						resultadotext2.setText(String.valueOf(nota1));
//
//                        } else if (nota2 <= nota1) {
//                            tArredon = (((bimes1 * pesob1s) + (nota2 * pesob2s)) / somapesos);
//                            if (tArredon < medias) {
//                                nota2 += 1;
//
//                            }
//                            textprecisa.setText(String.valueOf("Para passar na prova final, você precisa tirar: " + String.valueOf(nota2)));
////						resultadotext2.setText(String.valueOf(nota2));
//
//                        }
//
//
//                    }
//
//                    result.setText(String.valueOf("Resultado:"));
//                    mediatext.setText(String.valueOf("-  Média anual:"));
//
//
//                    situacao.setText(String.valueOf("Situação:"));
//
//
//                } else if (!b1.equals(null) && !b1.equals("") && (b2.equals(null) || b2.equals("")) && (pf.equals(null) || pf.equals(""))) {
//
//
//                    resultado = ((notapassas - ((bimes1 * pesob1s))) / pesob2s);
//
//                    int testearre;
//                    testearre = (((bimes1 * pesob1s) + (resultado * pesob2s)) / somapesos);
//
//                    if (testearre < medias) {
//
//                        resultado += 1;
//
//                    }
//
//                    bimes2 = 0;
//                    media = (((bimes1 * pesob1s) + (bimes2 * pesob2s)) / somapesos);
//                    mediat.setText(String.valueOf(media));
//                    if (resultado <= 0) {
//                        situaresult.setText(String.valueOf("Aprovado"));
//                        textprecisa.setText(String.valueOf("Parabéns, você foi aprovado!"));
//                        resultadotext.setText(String.valueOf(""));
//                        resultadotext2.setText(String.valueOf(""));
//
//                    } else if (resultado > 100) {
//                        situaresult.setText(String.valueOf("Prova Final"));
//                        textprecisa.setText(String.valueOf("Você ja está na Prova Final!"));
//                        resultadotext.setText(String.valueOf(""));
//                        resultadotext2.setText(String.valueOf(""));
//                    } else {
//                        situaresult.setText(String.valueOf("Cursando"));
//                        textprecisa.setText(String.valueOf("Para passar por média você precisa tirar: " + String.valueOf(resultado)));
//                        resultadotext.setText(String.valueOf(""));
//                        resultadotext2.setText(String.valueOf(""));
//                    }
//
//                    result.setText(String.valueOf("Resultado:"));
//                    mediatext.setText(String.valueOf("-  Média anual:"));
//
//
//                    situacao.setText(String.valueOf("Situação:"));
//
//                } else {
//                    Context context = getApplicationContext();
//                    CharSequence text = "Não é possível fazer esse cálculo!";
//                    int duration = Toast.LENGTH_SHORT;
//
//                    Toast toast = Toast.makeText(context, text, duration);
//                    toast.show();
//
//                    situaresult.setText(String.valueOf(""));
//                    textprecisa.setText(String.valueOf(""));
//                    resultadotext.setText(String.valueOf(""));
//                    resultadotext2.setText(String.valueOf(""));
//                    mediat.setText(String.valueOf(""));
//                    result.setText(String.valueOf(""));
//                    mediatext.setText(String.valueOf(""));
//                    situacao.setText(String.valueOf(""));
//
//                }
//            } else {
//                erroConversao();
//            }

    }

    private void showMessageErrorConfiguration() {

    }

    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

}
