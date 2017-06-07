package ifcalc.beta.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.util.ArrayList;
import java.util.List;

import ifcalc.beta.R;
import ifcalc.beta.adapters.DisciplinasAdapter;
import ifcalc.beta.database.DataSource;
import ifcalc.beta.model.Disciplina;

public class DisciplinasActivity extends BaseActivity {
    private RecyclerView rvDisciplinas;
    private DisciplinasAdapter disciplinasAdapter;
    private FloatingActionMenu floatingActionMenu;
    private FloatingActionButton floatingImportButton;
    private FloatingActionButton floatingAddButton;
    private TextView txtSemDisciplinas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disciplinas);
        setUpToolbar(getString(R.string.title_activity_disciplinas));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setUpPreferences();
        setUpList();

        setUpFloatingButtons();

    }

    private void setUpFloatingButtons() {
        floatingActionMenu = (FloatingActionMenu) findViewById(R.id.fab);
        floatingImportButton = (FloatingActionButton) findViewById(R.id.importar_disciplinas);
        floatingAddButton = (FloatingActionButton) findViewById(R.id.adicionar_disciplinas);

        floatingImportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityImport();
                floatingActionMenu.close(true);
            }
        });

        floatingAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createDialogTiposDisciplinas();
                floatingActionMenu.close(true);
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        loadDataList();
        invalidateOptionsMenu();
    }

    private void createDialogTiposDisciplinas() {
        String[] options = {"Anual", "Semestral"};
        final Intent it = new Intent(this, DisciplinaDetalheActivity.class);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Selecione o tipo da disciplina")
                .setItems(options, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        it.putExtra("TIPO_DISCIPLINA", which+1);
                        dialog.dismiss();
                        startActivity(it);
                    }
                });
        builder.show();

    }

    private void setUpList() {
        rvDisciplinas = (RecyclerView)findViewById(R.id.listDisciplinas);
        rvDisciplinas.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        rvDisciplinas.setLayoutManager(llm);

        loadDataList();
    }

    private void loadDataList() {
        List<Disciplina> disciplinasList = DataSource.getInstance(this).getDisciplinas();

        disciplinasAdapter = new DisciplinasAdapter(this, disciplinasList, zeroACem);
        rvDisciplinas.setAdapter(disciplinasAdapter);

        txtSemDisciplinas = (TextView) findViewById(R.id.txtDisci);
        txtSemDisciplinas.setTypeface(pfBeausans);

        if (disciplinasList.isEmpty())
            txtSemDisciplinas.setVisibility(View.VISIBLE);
        else
            txtSemDisciplinas.setVisibility(View.GONE);
    }

    private void showConfirmationRefresh() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String[] itens = {"Usar os dados já salvos"};
        final boolean[] checked = {true};
        builder.setMessage(R.string.confirma_refresh)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        Intent it = new Intent(getApplicationContext(), ImportDisciplinasActivity.class);
                        if (checked[0])
                            it.putExtra("action", "1");
                        startActivity(it);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        builder.setMultiChoiceItems(itens, checked, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                checked[i] = b;
            }
        });
        builder.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_disciplinas, menu);
        MenuItem item = menu.findItem(R.id.action_refresh);
        if (DataSource.getInstance(this).containDisciplinasWeb())
            item.setVisible(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_refresh:
                showConfirmationRefresh();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
