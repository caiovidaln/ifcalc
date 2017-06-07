package ifcalc.beta.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import ifcalc.beta.model.Anotacao;
import ifcalc.beta.model.Disciplina;
import ifcalc.beta.model.TipoDisciplina;

public class DataSource {
    private SQLiteDatabase database;
    private final DbHelper dbHelper;
    private static DataSource dataSource;
    /** Constructor of the DataSource
     *   @param context
     */
    private DataSource(Context context) {
        dbHelper = new DbHelper(context);
    }
    /** Method to open the database
     */
    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public static DataSource getInstance(Context context) {
        if (dataSource == null) {
            dataSource = new DataSource(context);
        }
        return dataSource;
    }

    public List<Disciplina> getDisciplinas() {
        Cursor cursor = getDatabase().query(dbHelper.DISCIPLINA,
                dbHelper.COLUNAS_DISCIPLINA, null , null, null, null, dbHelper.ID + " DESC", null);

        List<Disciplina> disciplinas = new ArrayList<>();
        while (cursor.moveToNext()) {
            Disciplina model = createDisciplina(cursor);
            disciplinas.add(model);
        }
        cursor.close();
        return disciplinas;
    }

    public List<Anotacao> getAnotacoes() {
        Cursor cursor = getDatabase().query(dbHelper.ANOTACAO,
                dbHelper.COLUNAS_ANOTACAO, null , null, null, null, dbHelper.DATETIME + " DESC", null);

        List<Anotacao> anotacoes = new ArrayList<>();
        while (cursor.moveToNext()) {
            Anotacao model = createAnotacao(cursor);
            anotacoes.add(model);
        }
        cursor.close();
        return anotacoes;
    }

    public int deleteDisciplina(int id) {
        return getDatabase().delete(dbHelper.DISCIPLINA, dbHelper.ID + " = " + id, null);
    }

    public int deleteAnotacao(int id) {
        return getDatabase().delete(dbHelper.ANOTACAO, dbHelper.ID + " = " + id, null);
    }

    public boolean containDisciplinasWeb() {
        List<Disciplina> list = getDisciplinas();
        for (Disciplina disciplina : list) {
            if (disciplina.getIdWeb() != null && !disciplina.getIdWeb().trim().isEmpty())
                return true;
        }
        return false;
    }

    public Disciplina getDisciplina(int id) {
        Cursor cursor = getDatabase().query(dbHelper.DISCIPLINA,
                dbHelper.COLUNAS_DISCIPLINA, dbHelper.ID + " = " + id, null, null, null, null, null);

        Disciplina model = null;
        while (cursor.moveToNext()) {
            model = createDisciplina(cursor);
        }
        cursor.close();
        return model;
    }

    public Disciplina getDisciplina(String idWeb) {
        Cursor cursor = getDatabase().query(dbHelper.DISCIPLINA,
                dbHelper.COLUNAS_DISCIPLINA, dbHelper.ID_SUAP + " = '" + idWeb + "'", null, null, null, null, null);

        Disciplina model = null;
        while (cursor.moveToNext()) {
            model = createDisciplina(cursor);
        }
        cursor.close();
        return model;
    }

    public Anotacao getAnotacao(int id) {
        Cursor cursor = getDatabase().query(dbHelper.ANOTACAO,
                dbHelper.COLUNAS_ANOTACAO, dbHelper.ID + " = " + id, null, null, null, null, null);

        Anotacao model = null;
        while (cursor.moveToNext()) {
            model = createAnotacao(cursor);
        }
        cursor.close();
        return model;
    }

    public long saveAnotacao(Anotacao anotacao) {
        ContentValues valores = anotacao.getContentValues();

        if (anotacao.getID() != null)
            return getDatabase().update(dbHelper.ANOTACAO, valores, dbHelper.ID + " = " + anotacao.getID(), null);
        else
            return getDatabase().insertOrThrow(dbHelper.ANOTACAO, null, valores);
    }

    public long saveDisciplina(Disciplina disciplina) {
        ContentValues valores = disciplina.getContentValues();

        if (disciplina.getID() != null)
            return getDatabase().update(dbHelper.DISCIPLINA, valores, dbHelper.ID + " = " + disciplina.getID(), null);
        else
            return getDatabase().insertOrThrow(dbHelper.DISCIPLINA, null, valores);
    }

    public long saveDisciplinaWeb(Disciplina disciplina) {
        Disciplina disc = null;

        if (disciplina.getIdWeb() != null && !disciplina.getIdWeb().trim().isEmpty()) {
            disc = getDisciplina(disciplina.getIdWeb());
            if (disc != null) {
                disciplina.setTitulo(disc.getTitulo());
            }
        }

        ContentValues valores = disciplina.getContentValues();

        if (disc != null)
            return getDatabase().update(dbHelper.DISCIPLINA, valores, dbHelper.ID + " = " + disc.getID(), null);
        else
            return getDatabase().insertOrThrow(dbHelper.DISCIPLINA, null, valores);
    }

    /** Method to create a Disciplina
     *   @param cursor
     *   @return Disciplina
     */
    private Disciplina createDisciplina(Cursor cursor) {
        Disciplina model = null;
        Double nb1, nb2, nb3, nb4, npf;

        if (cursor.getString(cursor.getColumnIndex(dbHelper.NOTAB1)).trim().isEmpty())
            nb1 = null;
        else
            nb1 = cursor.getDouble(cursor.getColumnIndex(dbHelper.NOTAB1));

        if (cursor.getString(cursor.getColumnIndex(dbHelper.NOTAB2)).trim().isEmpty())
            nb2 = null;
        else
            nb2 = cursor.getDouble(cursor.getColumnIndex(dbHelper.NOTAB2));

        if (cursor.getString(cursor.getColumnIndex(dbHelper.NOTAB3)).trim().isEmpty())
            nb3 = null;
        else
            nb3 = cursor.getDouble(cursor.getColumnIndex(dbHelper.NOTAB3));

        if (cursor.getString(cursor.getColumnIndex(dbHelper.NOTAB4)).trim().isEmpty())
            nb4 = null;
        else
            nb4 = cursor.getDouble(cursor.getColumnIndex(dbHelper.NOTAB4));

        if (cursor.getString(cursor.getColumnIndex(dbHelper.PROVA_FINAL)).trim().isEmpty())
            npf = null;
        else
            npf = cursor.getDouble(cursor.getColumnIndex(dbHelper.PROVA_FINAL));


        if (cursor.getInt(cursor.getColumnIndex(dbHelper.TIPO_DISCI)) == 1) {
            model = new Disciplina(cursor.getInt(cursor.getColumnIndex(dbHelper.ID)),
                    cursor.getString(cursor.getColumnIndex(dbHelper.TITULO)),
                    nb1, nb2, nb3, nb4, npf,
                    cursor.getDouble(cursor.getColumnIndex(dbHelper.MEDIA)),
                    cursor.getString(cursor.getColumnIndex(dbHelper.SITUACAO)));
        } else if (cursor.getInt(cursor.getColumnIndex(dbHelper.TIPO_DISCI)) == 2){
            model = new Disciplina(cursor.getInt(cursor.getColumnIndex(dbHelper.ID)),
                    cursor.getString(cursor.getColumnIndex(dbHelper.TITULO)),
                    nb1, nb2, npf,
                    cursor.getDouble(cursor.getColumnIndex(dbHelper.MEDIA)),
                    cursor.getString(cursor.getColumnIndex(dbHelper.SITUACAO)));
        }

        model.setIdWeb(cursor.getString(cursor.getColumnIndex(dbHelper.ID_SUAP)));

        return model;
    }

    private Anotacao createAnotacao(Cursor cursor) {
        Anotacao model = new Anotacao(cursor.getInt(cursor.getColumnIndex(dbHelper.ID)),
                    cursor.getString(cursor.getColumnIndex(dbHelper.TITULO_ANOTACAO)),
                    cursor.getString(cursor.getColumnIndex(dbHelper.CORPO)),
                    cursor.getString(cursor.getColumnIndex(dbHelper.DATETIME)));
        return model;
    }

    /** Method to get a database
     *   @return SQLiteDatabase
     */
    private SQLiteDatabase getDatabase() {
        if (this.database == null) {
            this.database = this.dbHelper.getWritableDatabase();
        }

        return this.database;
    }
}
