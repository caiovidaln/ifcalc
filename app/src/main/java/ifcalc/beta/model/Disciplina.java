package ifcalc.beta.model;

import android.content.ContentValues;

import java.math.BigDecimal;
import java.math.RoundingMode;

import ifcalc.beta.database.DbHelper;

public class Disciplina {
    private Integer _id;
    private String titulo;
    private Double notaB1;
    private Double notaB2;
    private Double notaB3;
    private Double notaB4;
    private Double provaFinal;
    private Double media;
    private String situacao;
    private TipoDisciplina tipoDisciplina;
    private String idWeb;

    public Disciplina() {
        this.idWeb = "";
    }

    public Disciplina(Integer _id, String titulo, Double notaB1, Double notaB2, Double provaFinal, Double media, String situacao) {
        this._id = _id;
        this.titulo = titulo;
        this.notaB1 = notaB1;
        this.notaB2 = notaB2;
        this.provaFinal = provaFinal;
        this.media = media;
        this.situacao = situacao;
        this.tipoDisciplina = TipoDisciplina.SEMESTRAL;
        this.idWeb = "";
    }

    public Disciplina(Integer _id, String titulo, Double notaB1, Double notaB2, Double notaB3, Double notaB4, Double provaFinal, Double media, String situacao) {
        this._id = _id;
        this.titulo = titulo;
        this.notaB1 = notaB1;
        this.notaB2 = notaB2;
        this.notaB3 = notaB3;
        this.notaB4 = notaB4;
        this.provaFinal = provaFinal;
        this.media = media;
        this.situacao = situacao;
        this.tipoDisciplina = TipoDisciplina.ANUAL;
        this.idWeb = "";
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

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public Double getNotaB1() {
        return notaB1;
    }

    public void setNotaB1(Double notaB1) {
        this.notaB1 = notaB1;
    }

    public Double getNotaB2() {
        return notaB2;
    }

    public void setNotaB2(Double notaB2) {
        this.notaB2 = notaB2;
    }

    public Double getNotaB3() {
        return notaB3;
    }

    public void setNotaB3(Double notaB3) {
        this.notaB3 = notaB3;
    }

    public Double getNotaB4() {
        return notaB4;
    }

    public void setNotaB4(Double notaB4) {
        this.notaB4 = notaB4;
    }

    public Double getProvaFinal() {
        return provaFinal;
    }

    public void setProvaFinal(Double provaFinal) {
        this.provaFinal = provaFinal;
    }

    public Double getMedia() {
        return media;
    }

    public String getMedia(boolean zeroACem) {
        BigDecimal mediaDecimal = new BigDecimal(media).setScale(1, RoundingMode.HALF_EVEN);
        return (zeroACem) ? String.valueOf((int) (Math.round(media))) : String.valueOf(mediaDecimal.doubleValue());
    }

    public void setMedia(Double media) {
        this.media = media;
    }

    public TipoDisciplina getTipoDisciplina() {
        return tipoDisciplina;
    }

    public void setTipoDisciplina(TipoDisciplina tipoDisciplina) {
        this.tipoDisciplina = tipoDisciplina;
    }

    public ContentValues getContentValues() {
        ContentValues valores = new ContentValues();

        if(this.getTipoDisciplina() == TipoDisciplina.ANUAL) {
            valores.put(DbHelper.TIPO_DISCI, 1);
        } else if(this.getTipoDisciplina() == TipoDisciplina.SEMESTRAL) {
            valores.put(DbHelper.TIPO_DISCI, 2);
        }
        valores.put(DbHelper.TITULO, this.getTitulo());

        if (this.getNotaB1() == null)
            valores.put(DbHelper.NOTAB1, "");
        else
            valores.put(DbHelper.NOTAB1, this.getNotaB1());

        if (this.getNotaB2() == null)
            valores.put(DbHelper.NOTAB2, "");
        else
            valores.put(DbHelper.NOTAB2, this.getNotaB2());

        if (this.getNotaB3() == null)
            valores.put(DbHelper.NOTAB3, "");
        else
            valores.put(DbHelper.NOTAB3, this.getNotaB3());

        if (this.getNotaB4() == null)
            valores.put(DbHelper.NOTAB4, "");
        else
            valores.put(DbHelper.NOTAB4, this.getNotaB4());

        if (this.getProvaFinal() == null)
            valores.put(DbHelper.PROVA_FINAL, "");
        else
            valores.put(DbHelper.PROVA_FINAL, this.getProvaFinal());

        valores.put(DbHelper.MEDIA, this.getMedia());
        valores.put(DbHelper.SITUACAO, this.getSituacao());
        valores.put(DbHelper.ID_SUAP, this.getIdWeb());

        return valores;
    }

    public String getIdWeb() {
        return idWeb;
    }

    public void setIdWeb(String idWeb) {
        this.idWeb = idWeb;
    }
}
