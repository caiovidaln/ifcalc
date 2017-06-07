package ifcalc.beta.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbHelper extends SQLiteOpenHelper {

    public static final String ID = "_id";

    public static final String DISCIPLINA = "DISCIPLINA";
    public static final String TITULO = "materia";
    public static final String NOTAB1 = "bimest1";
    public static final String NOTAB2 = "bimest2";
    public static final String NOTAB3 = "bimest3";
    public static final String NOTAB4 = "bimest4";
    public static final String MEDIA = "media1";
    public static final String PROVA_FINAL = "provafinal";
    public static final String SITUACAO = "situacao";
    public static final String TIPO_DISCI = "tipodisci";
    public static final String ID_SUAP = "id_web";

    public static final String ANOTACAO = "ANOTACAO";
    public static final String TITULO_ANOTACAO = "titulo";
    public static final String CORPO = "corpo";
    public static final String DATETIME = "datetime";

    private static final String DB_NAME = "materiasatt.db";
    private static final int DB_VERSION = 6;

    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        createTables(database);
    }

    @Override
    public void onUpgrade(SQLiteDatabase dataBase, int oldVersion, int newVersion) {
        try {
            if(oldVersion < 4) {
                dataBase.execSQL("ALTER TABLE lista RENAME TO " + DISCIPLINA);
                dataBase.execSQL("ALTER TABLE " + DISCIPLINA + " ADD COLUMN " + ID_SUAP + " TEXT NOT NULL;");
            } else if(oldVersion == 4) {
                dataBase.execSQL("CREATE TABLE IF NOT EXISTS " + DISCIPLINA + " (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        TITULO + " TEXT NOT NULL, " + NOTAB1 + " NUMBER NOT NULL, " + NOTAB2 + " NUMBER NOT NULL, "
                        + NOTAB3 + " NUMBER NOT NULL, " + NOTAB4 + " NUMBER NOT NULL, " + PROVA_FINAL + " NUMBER NOT NULL, "
                        + MEDIA + " NUMBER NOT NULL, " + SITUACAO + " TEXT NOT NULL, " + TIPO_DISCI + " TEXT NOT NULL);");

                dataBase.execSQL("CREATE TABLE IF NOT EXISTS " + ANOTACAO + " (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        TITULO_ANOTACAO + " TEXT NOT NULL, " + CORPO + " TEXT NOT NULL, " + DATETIME + " TEXT NOT NULL);");

                dataBase.execSQL("INSERT INTO " + DISCIPLINA + " SELECT " + TITULO + ", " +
                        NOTAB1 + ", " + NOTAB2 + ", " + NOTAB3 + ", " +
                        NOTAB4 + ", " + PROVA_FINAL + ", " + MEDIA + ", " + SITUACAO + ", " +
                        TIPO_DISCI + " FROM disciplinasnew;");

                dataBase.execSQL("DROP TABLE IF EXISTS disciplinasnew;");

                dataBase.execSQL("ALTER TABLE " + DISCIPLINA + " ADD COLUMN " + ID_SUAP + " TEXT NOT NULL;");
            }
        } catch (Exception e) {
            dataBase.execSQL("DROP TABLE IF EXISTS lista;");
            dataBase.execSQL("DROP TABLE IF EXISTS disciplinasnew;");
            dataBase.execSQL("DROP TABLE IF EXISTS " + DISCIPLINA + ";");
            dataBase.execSQL("DROP TABLE IF EXISTS " + ANOTACAO + ";");
            createTables(dataBase);
        }
        dataBase.setVersion(newVersion);
    }
    /** Method to create the table in the database
     *   @param dataBase
     */
    private void createTables(SQLiteDatabase dataBase) {
        Log.i(DB_NAME, "Criando Tabelas do Banco de Dados");

        dataBase.execSQL("CREATE TABLE IF NOT EXISTS " + DISCIPLINA + " (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                TITULO + " TEXT NOT NULL, " + NOTAB1 + " NUMBER NOT NULL, " + NOTAB2 + " NUMBER NOT NULL, "
                + NOTAB3 + " NUMBER NOT NULL, " + NOTAB4 + " NUMBER NOT NULL, " + PROVA_FINAL + " NUMBER NOT NULL, "
                + MEDIA + " NUMBER NOT NULL, " + SITUACAO + " TEXT NOT NULL, " + TIPO_DISCI + " TEXT NOT NULL, "
                + ID_SUAP + " TEXT NOT NULL);");

        dataBase.execSQL("CREATE TABLE IF NOT EXISTS " + ANOTACAO + " (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                TITULO_ANOTACAO + " TEXT NOT NULL, " + CORPO + " TEXT NOT NULL, " + DATETIME + " TEXT NOT NULL);");

    }

    public static final String[] COLUNAS_DISCIPLINA = new String [] {
            ID, TITULO, NOTAB1, NOTAB2, NOTAB3, NOTAB4, PROVA_FINAL, MEDIA, SITUACAO, TIPO_DISCI, ID_SUAP
    };

    public static final String[] COLUNAS_ANOTACAO = new String [] {
            ID, TITULO_ANOTACAO, CORPO, DATETIME
    };
}
