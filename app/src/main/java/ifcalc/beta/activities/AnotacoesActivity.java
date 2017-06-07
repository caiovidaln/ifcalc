package ifcalc.beta.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.util.List;

import ifcalc.beta.R;
import ifcalc.beta.adapters.AnotacoesAdapter;
import ifcalc.beta.adapters.DisciplinasAdapter;
import ifcalc.beta.database.DataSource;
import ifcalc.beta.model.Anotacao;
import ifcalc.beta.model.Disciplina;

public class AnotacoesActivity extends BaseActivity {

    private RecyclerView rvAnotacoes;
    private AnotacoesAdapter anotacoesAdapter;
    private FloatingActionButton floatingActionButton;
    private TextView txtSemAnotacoes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anotacoes);
        setUpToolbar(getString(R.string.title_activity_anotacoes));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setUpList();
        setUpFloatingButtons();

    }

    private void setUpFloatingButtons() {
        floatingActionButton = (FloatingActionButton) findViewById(R.id.adicionar_anotacoes);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AnotacaoDetalheActivity.class));
            }
        });
    }

    private void setUpList() {
        rvAnotacoes = (RecyclerView)findViewById(R.id.listAnotacoes);
        rvAnotacoes.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        rvAnotacoes.setLayoutManager(llm);

        loadDataList();
    }

    private void loadDataList() {
        List<Anotacao> anotacoesList = DataSource.getInstance(this).getAnotacoes();

        anotacoesAdapter = new AnotacoesAdapter(this, anotacoesList);
        rvAnotacoes.setAdapter(anotacoesAdapter);

        txtSemAnotacoes = (TextView) findViewById(R.id.txtAnota);
        txtSemAnotacoes.setTypeface(pfBeausans);

        if (anotacoesList.isEmpty())
            txtSemAnotacoes.setVisibility(View.VISIBLE);
        else
            txtSemAnotacoes.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadDataList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

}
