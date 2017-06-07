package ifcalc.beta.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import ifcalc.beta.R;
import ifcalc.beta.activities.AnotacaoDetalheActivity;
import ifcalc.beta.activities.DisciplinaDetalheActivity;
import ifcalc.beta.model.Disciplina;
import ifcalc.beta.util.Util;

public class DisciplinasAdapter extends RecyclerView.Adapter<DisciplinasAdapter.DisciplinaViewHolder> {

    private List<Disciplina> disciplinaList;
    private boolean zeroACem;
    private Context context;

    public DisciplinasAdapter(Context context, List<Disciplina> disciplinas, boolean zeroACem) {
        this.context = context;
        this.disciplinaList = disciplinas;
        this.zeroACem = zeroACem;
    }

    public static class DisciplinaViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        int _id;
        TextView tituloDisciplina;
        TextView mediaDisciplina;
        TextView situacaoDisciplina;

        TextView situacaoTxt;
        TextView mediaTxt;

        DisciplinaViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.card_disciplinas);
            tituloDisciplina = (TextView) itemView.findViewById(R.id.titulo);
            mediaDisciplina = (TextView) itemView.findViewById(R.id.media);
            situacaoDisciplina = (TextView) itemView.findViewById(R.id.situacao);
            situacaoTxt = (TextView) itemView.findViewById(R.id.txtSituacao);
            mediaTxt = (TextView) itemView.findViewById(R.id.txtMedia);

            cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent it = new Intent(v.getContext(), DisciplinaDetalheActivity.class);
                    it.putExtra("ID_DISCIPLINA", _id);
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
                                            Intent it = new Intent(v.getContext(), DisciplinaDetalheActivity.class);
                                            it.putExtra("ID_DISCIPLINA", _id);
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
    public DisciplinaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_disciplinas, parent, false);
        DisciplinaViewHolder dvh = new DisciplinaViewHolder(v);
        return dvh;
    }

    @Override
    public void onBindViewHolder(DisciplinaViewHolder disciplinaViewHolder, int position) {
        disciplinaViewHolder._id = disciplinaList.get(position).getID();
        disciplinaViewHolder.tituloDisciplina.setText(disciplinaList.get(position).getTitulo());
        disciplinaViewHolder.mediaDisciplina.setText(disciplinaList.get(position).getMedia(zeroACem));
        disciplinaViewHolder.situacaoDisciplina.setText(disciplinaList.get(position).getSituacao());

        disciplinaViewHolder.tituloDisciplina.setTypeface(Util.getFontTheme(context));
        disciplinaViewHolder.mediaDisciplina.setTypeface(Util.getFontTheme(context));
        disciplinaViewHolder.situacaoDisciplina.setTypeface(Util.getFontTheme(context));
        disciplinaViewHolder.mediaTxt.setTypeface(Util.getFontTheme(context));
        disciplinaViewHolder.situacaoTxt.setTypeface(Util.getFontTheme(context));

        if (disciplinaList.get(position).getSituacao().equals("Reprovado"))
            disciplinaViewHolder.situacaoDisciplina.setTextColor(context.getResources().getColor(R.color.colorAccent));
        else if (disciplinaList.get(position).getSituacao().equals("Cursando"))
            disciplinaViewHolder.situacaoDisciplina.setTextColor(context.getResources().getColor(R.color.preto));
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return this.disciplinaList.size();
    }

}
