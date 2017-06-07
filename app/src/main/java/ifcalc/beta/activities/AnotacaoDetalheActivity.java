package ifcalc.beta.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import java.util.Date;

import ifcalc.beta.R;
import ifcalc.beta.database.DataSource;
import ifcalc.beta.model.Anotacao;

public class AnotacaoDetalheActivity extends BaseActivity {

    private EditText edtTitulo, edtCorpo;

    private Integer idAnotacao;

    private Anotacao anotacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anotacao_detalhe);
        setUpToolbar(getString(R.string.title_activity_anotacao_detalhe));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setUpViews();
        getIdAnotacao();
    }

    private void setUpViews() {
        edtTitulo = (EditText) findViewById(R.id.titulo);
        edtCorpo = (EditText) findViewById(R.id.corpo);

        edtTitulo.setTypeface(pfBeausans);
        edtCorpo.setTypeface(pfBeausans);

        edtTitulo.setHint(R.string.tituloAnotacao);
        edtCorpo.setHint(R.string.corpoAnotacao);
    }

    private void getIdAnotacao() {
        idAnotacao = getIntent().getIntExtra("ID_ANOTACAO", -1);

        if (idAnotacao == -1)
            idAnotacao = null;

        setUpAnotacaoDetalhes();
    }

    private void setUpAnotacaoDetalhes() {
        if(idAnotacao != null) {
            anotacao = DataSource.getInstance(this).getAnotacao(idAnotacao);
            if (anotacao == null)
                finish();

            edtTitulo.setText(anotacao.getTitulo());
            edtCorpo.setText(anotacao.getCorpo());

        } else {
            anotacao = new Anotacao();
            getSupportActionBar().setTitle("Nova anotação");
        }


    }

    private void saveAnotacao() {
        closeKeyboard();
        anotacao.setTitulo(edtTitulo.getText().toString());
        anotacao.setCorpo(edtCorpo.getText().toString());
        anotacao.setData(new Date());

        if (anotacao.getTitulo().trim().isEmpty())
            showSnakeBarMessage(edtTitulo, "O titulo da anotacao não pode ficar em branco");
        else {
            try {
                if (DataSource.getInstance(this).saveAnotacao(anotacao) > 0) {
                    finish();
                    showSnakeBarMessage(edtTitulo, "Anotação salva com sucesso");
                } else {
                    showSnakeBarMessage(edtTitulo, "Ocorreu um erro ao salvar a anotação");
                }
            } catch (Exception e) {
                e.printStackTrace();
                showSnakeBarMessage(edtTitulo, "Ocorreu um erro ao salvar a anotação");
            }
        }
    }

    private void showConfirmationDelete() {
        closeKeyboard();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.confirma_excluir_anotacao)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        DataSource.getInstance(getApplicationContext()).deleteAnotacao(idAnotacao);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_detalhe, menu);
        MenuItem item = menu.findItem(R.id.action_delete);
        if (idAnotacao == null)
            item.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_delete:
                showConfirmationDelete();
                return true;
            case R.id.action_save:
                saveAnotacao();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
