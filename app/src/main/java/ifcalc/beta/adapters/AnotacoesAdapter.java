package ifcalc.beta.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ifcalc.beta.R;
import ifcalc.beta.activities.AnotacaoDetalheActivity;
import ifcalc.beta.activities.DisciplinaDetalheActivity;
import ifcalc.beta.database.DataSource;
import ifcalc.beta.model.Anotacao;
import ifcalc.beta.model.Disciplina;
import ifcalc.beta.util.Util;

public class AnotacoesAdapter extends RecyclerView.Adapter<AnotacoesAdapter.AnotacaoViewHolder> {

    private List<Anotacao> anotacaoList;
    private boolean zeroACem;
    private Context context;

    public AnotacoesAdapter(Context context, List<Anotacao> anotacoes) {
        this.context = context;
        this.anotacaoList = anotacoes;
    }

    public static class AnotacaoViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        int _id;
        TextView titulo;
        TextView dataTxt;
        TextView horaTxt;

        AnotacaoViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.card_anotacoes);
            titulo = (TextView) itemView.findViewById(R.id.titulo);
            dataTxt = (TextView) itemView.findViewById(R.id.data);
            horaTxt = (TextView) itemView.findViewById(R.id.hora);

            cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent it = new Intent(v.getContext(), AnotacaoDetalheActivity.class);
                    it.putExtra("ID_ANOTACAO", _id);
                    v.getContext().startActivity(it);
                }
            });

            final String[] options = {"Editar"};

            cv.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    final View v = view;
                    builder.setTitle(R.string.selecione_opcao)
                            .setItems(options, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which) {
                                        case 0:
                                            Intent it = new Intent(v.getContext(), AnotacaoDetalheActivity.class);
                                            it.putExtra("ID_ANOTACAO", _id);
                                            v.getContext().startActivity(it);
                                            break;
                                    }
                                }
                            });
                    builder.show();
                    return true;
                }
            });
        }
    }

    @Override
    public AnotacaoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_anotacoes, parent, false);
        AnotacaoViewHolder dvh = new AnotacaoViewHolder(v);
        return dvh;
    }

    @Override
    public void onBindViewHolder(AnotacaoViewHolder anotacaoViewHolder, int position) {
        anotacaoViewHolder._id = anotacaoList.get(position).getID();
        anotacaoViewHolder.titulo.setText(anotacaoList.get(position).getTitulo());
        anotacaoViewHolder.dataTxt.setText(anotacaoList.get(position).getFormattedDate("dd/MM/yyyy"));
        anotacaoViewHolder.horaTxt.setText(anotacaoList.get(position).getFormattedDate("HH:mm"));

        anotacaoViewHolder.titulo.setTypeface(Util.getFontTheme(context));
        anotacaoViewHolder.dataTxt.setTypeface(Util.getFontTheme(context));
        anotacaoViewHolder.horaTxt.setTypeface(Util.getFontTheme(context));
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return this.anotacaoList.size();
    }

}
