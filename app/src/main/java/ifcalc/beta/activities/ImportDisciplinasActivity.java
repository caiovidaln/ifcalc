package ifcalc.beta.activities;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.media.MediaCodec;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import ifcalc.beta.R;
import ifcalc.beta.database.DataSource;
import ifcalc.beta.model.Disciplina;
import ifcalc.beta.model.TipoSistema;
import ifcalc.beta.util.ImportDisciplinasTask;
import ifcalc.beta.util.Util;

public class ImportDisciplinasActivity extends BaseActivity {

    private Button btImportar;
    private EditText edtUsuario, edtSenha;
    private ImportDisciplinasTask importDisciplinasTask;
    private Document paginaBoletim;
    private ProgressBar progressBar, progressBarMini;
    private LinearLayout loginLayout;
    private LinearLayout layoutImport;
    private RelativeLayout layoutPrivacidade;
    private TextView txtInfoStatus, txtPrivacidade;
    private CheckBox salvarDados;
    private boolean refresh;
    private TipoSistema tipoSistema = TipoSistema.SUAP_IFRN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_disciplinas);
        setUpToolbar(getString(R.string.title_activity_import_disciplinas));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setUpViews();
        getExtras();
    }

    private void getExtras() {
        edtUsuario.setText(Util.getPreferences(this).getString(ConfiguracaoActivity.LOGIN, ""));
        edtSenha.setText(Util.getPreferences(this).getString(ConfiguracaoActivity.PASSWORD, ""));

        String action = getIntent().getStringExtra("action");
        if (action != null && action.equals("1")) {
            refresh = true;
            importDisciplinas();
        }
    }

    private void setUpViews() {
        btImportar = (Button) findViewById(R.id.importarButton);

        edtUsuario = (EditText) findViewById(R.id.edtUsuario);
        edtSenha = (EditText) findViewById(R.id.edtSenha);

        loginLayout = (LinearLayout) findViewById(R.id.loginLayout);
        layoutImport = (LinearLayout) findViewById(R.id.layoutImport);

        progressBar = (ProgressBar) findViewById(R.id.progressBarHorizontal);
        progressBarMini = (ProgressBar) findViewById(R.id.progressBarMini);

        txtInfoStatus = (TextView) findViewById(R.id.txtInfoStatus);
        txtPrivacidade = (TextView) findViewById(R.id.txtPrivacidade);
        layoutPrivacidade = (RelativeLayout) findViewById(R.id.layoutPrivacidade);
        salvarDados = (CheckBox) findViewById(R.id.salvarDados);
        salvarDados.setChecked(true);

        loginLayout.setVisibility(View.VISIBLE);
        layoutImport.setVisibility(View.GONE);

        txtPrivacidade.setPaintFlags(txtPrivacidade.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        layoutPrivacidade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityPrivacidade();
            }
        });

        btImportar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                importDisciplinas();
            }
        });
    }

    private void importDisciplinas() {
        if (edtUsuario.getText().toString().trim().isEmpty())
            showSnakeBarMessage(edtUsuario, "O campo usuário é obrigatório");
        else if (edtSenha.getText().toString().trim().isEmpty())
            showSnakeBarMessage(edtSenha, "O campo senha é obrigatório");
        else {
            importDisciplinasTask = new ImportDisciplinasTask();
            try {
                importDisciplinasTask.execute(this);
                loginLayout.setVisibility(View.GONE);
                layoutImport.setVisibility(View.VISIBLE);
                progressBarMini.setVisibility(View.VISIBLE);
                setProgressInBar(0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setProgressInBar(int progress) {
        progressBar.setProgress(progress);

        if (progress < 20) {
            txtInfoStatus.setText("Estabelecendo conexão...");
        } else if (progress < 50) {
            txtInfoStatus.setText("Realizando login...");
        } else if (progress < 85) {
            txtInfoStatus.setText("Baixando disciplinas...");
        } else if (progress < 100){
            txtInfoStatus.setText("Salvando disciplinas...");
        } else {
            txtInfoStatus.setText("Importação finalizada");
            progressBarMini.setVisibility(View.GONE);
        }
    }

    public String getUsuario() {
        return edtUsuario.getText().toString();
    }

    public String getSenha() {
        return edtSenha.getText().toString();
    }

    public TipoSistema getTipoSistema() {
        return tipoSistema;
    }

    public void mostraErro(int typeError) {
        switch (typeError) {
            case 10:
                showDialogErroInterno();
                break;

            case 20:
                showDialogDadosErrados();
                break;

            case 30:
                showDialogConexaoErro();
                break;
        }
    }

    private void pegaDados_IFRN(Elements linhasBoletim, List<Disciplina> disciplinasSuccess, List<Disciplina> disciplinasError) {
        try {
            for (int i = linhasBoletim.size()-1; i >= 0; i--) {
                Element linha = linhasBoletim.get(i);
                Elements colunas = linha.getElementsByTag("td");

                String idWeb = colunas.get(1).text().split(" - ")[0].trim();
                String titulo = colunas.get(1).text().split(" - ")[1].split(Pattern.quote("("))[0];
                String situacao = colunas.get(6).text();

                if (colunas.size() == 20) { //disciplina anual
                    String nota1 = colunas.get(7).text();
                    String nota2 = colunas.get(9).text();
                    String nota3 = colunas.get(11).text();
                    String nota4 = colunas.get(13).text();
                    String notapf = colunas.get(16).text();

                    String media = colunas.get(15).text();

                    Double nb1 = (nota1.trim().equals("-")) ? null : new Double(nota1.trim());
                    Double nb2 = (nota2.trim().equals("-")) ? null : new Double(nota2.trim());
                    Double nb3 = (nota3.trim().equals("-")) ? null : new Double(nota3.trim());
                    Double nb4 = (nota4.trim().equals("-")) ? null : new Double(nota4.trim());
                    Double npf = (notapf.trim().equals("-")) ? null : new Double(notapf.trim());

                    Double med = (media.trim().equals("-")) ? 0 : new Double(media.trim());

                    Disciplina disciplina = new Disciplina(null, titulo, nb1, nb2, nb3, nb4, npf, med, situacao);
                    disciplina.setIdWeb(idWeb);

                    int id = (int) DataSource.getInstance(this).saveDisciplinaWeb(disciplina);
                    if (id > 0){
                        disciplina.setID(new Integer(id));
                        disciplinasSuccess.add(disciplina);
                    } else {
                        disciplinasError.add(disciplina);
                    }
                } else if (colunas.size() == 17) { //disciplina semestral
                    if (colunas.get(7).hasAttr("colspan")) {
                        String nota1 = colunas.get(8).text();
                        String nota2 = colunas.get(10).text();
                        String notapf = colunas.get(13).text();

                        String media = colunas.get(12).text();

                        Double nb1 = (nota1.trim().equals("-")) ? null : new Double(nota1.trim());
                        Double nb2 = (nota2.trim().equals("-")) ? null : new Double(nota2.trim());
                        Double npf = (notapf.trim().equals("-")) ? null : new Double(notapf.trim());

                        Double med = (media.trim().equals("-")) ? 0 : new Double(media.trim());

                        Disciplina disciplina = new Disciplina(null, titulo, nb1, nb2, npf, med, situacao);
                        disciplina.setIdWeb(idWeb);

                        int id = (int) DataSource.getInstance(this).saveDisciplinaWeb(disciplina);
                        if (id > 0){
                            disciplina.setID(new Integer(id));
                            disciplinasSuccess.add(disciplina);
                        } else {
                            disciplinasError.add(disciplina);
                        }
                    } else if (colunas.get(11).hasAttr("colspan")) {
                        String nota1 = colunas.get(7).text();
                        String nota2 = colunas.get(9).text();
                        String notapf = colunas.get(13).text();

                        String media = colunas.get(12).text();


                        Double nb1 = (nota1.trim().equals("-")) ? null : new Double(nota1.trim());
                        Double nb2 = (nota2.trim().equals("-")) ? null : new Double(nota2.trim());
                        Double npf = (notapf.trim().equals("-")) ? null : new Double(notapf.trim());

                        Double med = (media.trim().equals("-")) ? 0 : new Double(media.trim());

                        Disciplina disciplina = new Disciplina(null, titulo, nb1, nb2, npf, med, situacao);
                        disciplina.setIdWeb(idWeb);

                        int id = (int) DataSource.getInstance(this).saveDisciplinaWeb(disciplina);
                        if (id > 0){
                            disciplina.setID(new Integer(id));
                            disciplinasSuccess.add(disciplina);
                        } else {
                            disciplinasError.add(disciplina);
                        }
                    }
                } else if (colunas.size() == 16) {
                    String nota1 = colunas.get(7).text();
                    String nota2 = colunas.get(9).text();
                    String notapf = colunas.get(12).text();

                    String media = colunas.get(11).text();


                    Double nb1 = (nota1.trim().equals("-")) ? null : new Double(nota1.trim());
                    Double nb2 = (nota2.trim().equals("-")) ? null : new Double(nota2.trim());
                    Double npf = (notapf.trim().equals("-")) ? null : new Double(notapf.trim());

                    Double med = (media.trim().equals("-")) ? 0 : new Double(media.trim());

                    Disciplina disciplina = new Disciplina(null, titulo, nb1, nb2, npf, med, situacao);
                    disciplina.setIdWeb(idWeb);

                    int id = (int) DataSource.getInstance(this).saveDisciplinaWeb(disciplina);
                    if (id > 0){
                        disciplina.setID(new Integer(id));
                        disciplinasSuccess.add(disciplina);
                    } else {
                        disciplinasError.add(disciplina);
                    }
                }
            }
        } catch (Exception e) {
            showDialogErro("Ocorreu um erro ao procurar as notas, tente novamente mais tarde!");
        }
    }

    private void pegaDados_IFPB(Elements linhasBoletim, List<Disciplina> disciplinasSuccess, List<Disciplina> disciplinasError) {
        for (int i = linhasBoletim.size()-1; i >= 0; i--) {
            Element linha = linhasBoletim.get(i);
            Elements colunas = linha.getElementsByTag("td");

            String idWeb = colunas.get(1).text().split(" - ")[0].trim();
            String titulo = colunas.get(1).text().split(" - ")[1].split(Pattern.quote("("))[0];
            String situacao = colunas.get(6).text();

            if (colunas.size() == 13) { //disciplina semestral
                String nota1 = colunas.get(7).text();
                String nota2 = colunas.get(9).text();
                String nota3 = colunas.get(11).text();
                String nota4 = colunas.get(13).text();
                String notapf = colunas.get(16).text();

                String media = colunas.get(15).text();

                Double nb1 = (nota1.trim().equals("-")) ? null : new Double(nota1.trim());
                Double nb2 = (nota2.trim().equals("-")) ? null : new Double(nota2.trim());
                Double nb3 = (nota3.trim().equals("-")) ? null : new Double(nota3.trim());
                Double nb4 = (nota4.trim().equals("-")) ? null : new Double(nota4.trim());
                Double npf = (notapf.trim().equals("-")) ? null : new Double(notapf.trim());

                Double med = (media.trim().equals("-")) ? 0 : new Double(media.trim());

                Disciplina disciplina = new Disciplina(null, titulo, nb1, nb2, nb3, nb4, npf, med, situacao);
                disciplina.setIdWeb(idWeb);

                int id = (int) DataSource.getInstance(this).saveDisciplinaWeb(disciplina);
                if (id > 0){
                    disciplina.setID(new Integer(id));
                    disciplinasSuccess.add(disciplina);
                } else {
                    disciplinasError.add(disciplina);
                }
            } else if (colunas.size() == 17) { //disciplina semestral
                if (colunas.get(7).hasAttr("colspan")) {
                    String nota1 = colunas.get(8).text();
                    String nota2 = colunas.get(10).text();
                    String notapf = colunas.get(13).text();

                    String media = colunas.get(12).text();

                    Double nb1 = (nota1.trim().equals("-")) ? null : new Double(nota1.trim());
                    Double nb2 = (nota2.trim().equals("-")) ? null : new Double(nota2.trim());
                    Double npf = (notapf.trim().equals("-")) ? null : new Double(notapf.trim());

                    Double med = (media.trim().equals("-")) ? 0 : new Double(media.trim());

                    Disciplina disciplina = new Disciplina(null, titulo, nb1, nb2, npf, med, situacao);
                    disciplina.setIdWeb(idWeb);

                    int id = (int) DataSource.getInstance(this).saveDisciplinaWeb(disciplina);
                    if (id > 0){
                        disciplina.setID(new Integer(id));
                        disciplinasSuccess.add(disciplina);
                    } else {
                        disciplinasError.add(disciplina);
                    }
                } else if (colunas.get(11).hasAttr("colspan")) {
                    String nota1 = colunas.get(7).text();
                    String nota2 = colunas.get(9).text();
                    String notapf = colunas.get(13).text();

                    String media = colunas.get(12).text();


                    Double nb1 = (nota1.trim().equals("-")) ? null : new Double(nota1.trim());
                    Double nb2 = (nota2.trim().equals("-")) ? null : new Double(nota2.trim());
                    Double npf = (notapf.trim().equals("-")) ? null : new Double(notapf.trim());

                    Double med = (media.trim().equals("-")) ? 0 : new Double(media.trim());

                    Disciplina disciplina = new Disciplina(null, titulo, nb1, nb2, npf, med, situacao);
                    disciplina.setIdWeb(idWeb);

                    int id = (int) DataSource.getInstance(this).saveDisciplinaWeb(disciplina);
                    if (id > 0){
                        disciplina.setID(new Integer(id));
                        disciplinasSuccess.add(disciplina);
                    } else {
                        disciplinasError.add(disciplina);
                    }
                }
            } else if (colunas.size() == 16) {
                String nota1 = colunas.get(7).text();
                String nota2 = colunas.get(9).text();
                String notapf = colunas.get(12).text();

                String media = colunas.get(11).text();


                Double nb1 = (nota1.trim().equals("-")) ? null : new Double(nota1.trim());
                Double nb2 = (nota2.trim().equals("-")) ? null : new Double(nota2.trim());
                Double npf = (notapf.trim().equals("-")) ? null : new Double(notapf.trim());

                Double med = (media.trim().equals("-")) ? 0 : new Double(media.trim());

                Disciplina disciplina = new Disciplina(null, titulo, nb1, nb2, npf, med, situacao);
                disciplina.setIdWeb(idWeb);

                int id = (int) DataSource.getInstance(this).saveDisciplinaWeb(disciplina);
                if (id > 0){
                    disciplina.setID(new Integer(id));
                    disciplinasSuccess.add(disciplina);
                } else {
                    disciplinasError.add(disciplina);
                }
            }
        }
    }

    public void pegaDados(Document boletim) {

        List<Disciplina> disciplinasSuccess = new ArrayList<>();
        List<Disciplina> disciplinasError = new ArrayList<>();

        try {
            Element table = boletim.getElementsByClass("borda").first();
            Element tbody = table.getElementsByTag("tbody").first();
            Elements linhasBoletim = tbody.getElementsByTag("tr");

            switch (tipoSistema) {
                case SUAP_IFRN:
                    pegaDados_IFRN(linhasBoletim, disciplinasSuccess, disciplinasError);
                    break;

                case SUAP_IFPB:
                    pegaDados_IFPB(linhasBoletim, disciplinasSuccess, disciplinasError);
                    break;
            }
        } catch (Exception e) {
            showDialogErro("Ocorreu um erro ao procurar as notas, tente novamente mais tarde!");
        }


        showDialogDisciplinas(disciplinasSuccess, disciplinasError);
        setProgressInBar(100);
        saveDados();
    }

    private void saveDados(){
        if (salvarDados.isChecked()) {
            SharedPreferences.Editor editor = Util.getPreferences(this).edit();
            editor.putString(ConfiguracaoActivity.LOGIN, edtUsuario.getText().toString());
            editor.putString(ConfiguracaoActivity.PASSWORD, edtSenha.getText().toString());
            editor.apply();
        }
    }

    private void showDialogDadosErrados() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Não foi possível acessar o SUAP com esses dados, confira-os e tente novamente!");
        builder.setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        builder.show();
        layoutImport.setVisibility(View.GONE);
        loginLayout.setVisibility(View.VISIBLE);
    }

    private void showDialogErro(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Ocorreu um erro ao tentar conectar ao SUAP, tente novamente mais tarde!");
        builder.setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        builder.show();
        layoutImport.setVisibility(View.GONE);
        loginLayout.setVisibility(View.VISIBLE);
    }

    private void showDialogErroInterno() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Ocorreu um erro ao tentar conectar ao SUAP, tente novamente mais tarde!");
        builder.setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        builder.show();
        layoutImport.setVisibility(View.GONE);
        loginLayout.setVisibility(View.VISIBLE);
    }

    private void showDialogConexaoErro() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Ocorreu um erro ao tentar conectar ao SUAP, o site está acessível nesse momento? Aguarde e tente novamente!");
        builder.setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        builder.show();
        layoutImport.setVisibility(View.GONE);
        loginLayout.setVisibility(View.VISIBLE);
    }

    private void showDialogDisciplinas(List<Disciplina> disciplinasSuccess, List<Disciplina> disciplinasError) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        if (!disciplinasError.isEmpty() && disciplinasSuccess.isEmpty()) {
            builder.setMessage("Infelizmente não conseguimos importar suas disciplinas! Deseja tentar novamente?");
            builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    importDisciplinas();
                    dialog.dismiss();
                }
            });
            builder.setNeutralButton(R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            });
        } else if (!disciplinasSuccess.isEmpty() && disciplinasError.isEmpty()) {
            if (refresh)
                builder.setMessage("Suas disciplinas foram atualizadas!");
            else
                builder.setMessage("Foram baixadas " + disciplinasSuccess.size() + " disciplinas com sucesso!");
            builder.setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                    if (refresh)
                        finish();
                }
            });
        } else if (!disciplinasSuccess.isEmpty() && !disciplinasError.isEmpty()){
            builder.setMessage("Foram baixadas " + disciplinasSuccess.size() + " disciplinas, porém não foi possível importar " +
                    disciplinasError.size() + " disciplinas! :(");
            builder.setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                    if (refresh)
                        finish();
                }
            });
        } else {
            builder.setMessage("Infelizmente não encontramos nenhuma disciplina que pudesse importar! :(");
            builder.setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                    if (refresh)
                        finish();
                }
            });
        }

        builder.show();
    }

}
