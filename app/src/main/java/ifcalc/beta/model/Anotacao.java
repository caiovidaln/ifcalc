package ifcalc.beta.model;

import android.content.ContentValues;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

import ifcalc.beta.database.DbHelper;

public class Anotacao {
    private Integer _id;
    private String titulo;
    private String corpo;
    private Date data;

    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public Anotacao() {
    }

    public Anotacao(String titulo, String corpo) {
        this.titulo = titulo;
        this.corpo = corpo;
        this.data = new Date();
    }

    public Anotacao(Integer _id, String titulo, String corpo, String data) {
        this._id = _id;
        this.titulo = titulo;
        this.corpo = corpo;
        try {
            this.data = formatter.parse(data);
        } catch (Exception e) {
            this.data = new Date();
        }
    }

    public Integer getID() {
        return _id;
    }

    public void setID(Integer _id) {
        this._id = _id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getCorpo() {
        return corpo;
    }

    public void setCorpo(String corpo) {
        this.corpo = corpo;
    }

    public Date getData() {
        return data;
    }

    public String getFormattedDate(String format) {
        SimpleDateFormat formt = new SimpleDateFormat(format);
        return formt.format(this.data);
    }

    public void setData(Date data) {
        this.data = data;
    }

    public ContentValues getContentValues() {
        ContentValues valores = new ContentValues();

        valores.put(DbHelper.TITULO_ANOTACAO, this.getTitulo());
        valores.put(DbHelper.CORPO, this.getCorpo());
        valores.put(DbHelper.DATETIME, formatter.format(this.getData()));

        return valores;
    }
}
