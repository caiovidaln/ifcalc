package ifcalc.beta.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

import ifcalc.beta.util.ResultadoCalculo;

public abstract class NotasCalculadora {
    protected Double nota1;
    protected Double nota2;
    protected Double nota3;
    protected Double nota4;
    protected Double notaProvaFinal;
    protected int pesoB1;
    protected int pesoB2;
    protected int pesoB3;
    protected int pesoB4;
    protected int somaPesos;
    protected int mediaNecessaria;
    protected int notaNecessariaTotal;
    protected int notaMax;
    protected boolean zeroACem;


    public NotasCalculadora(boolean zeroAcem, Double nota1, Double nota2, Double notaProvaFinal) {
        notaMax = zeroAcem ? 100 : 10;
        this.zeroACem = zeroAcem;
        this.nota1 = nota1;
        this.nota2 = nota2;
        this.notaProvaFinal = notaProvaFinal;
    }

    public abstract ResultadoCalculo calculaNotas(ConfiguracoesDisciplinas configuracoesDisciplinas);

    public boolean isEmpty(Double nota) {
        return nota == null;
    }

    public Double getNota1() {
        return nota1;
    }

    public void setNota1(Double nota1) {
        this.nota1 = nota1;
    }

    public Double getNota2() {
        return nota2;
    }

    public void setNota2(Double nota2) {
        this.nota2 = nota2;
    }

    public Double getNotaProvaFinal() {
        return notaProvaFinal;
    }

    public void setNotaProvaFinal(Double notaProvaFinal) {
        this.notaProvaFinal = notaProvaFinal;
    }

    protected double arredondaMedia(double media) {
        BigDecimal mediaDecimal = new BigDecimal(media).setScale(1, RoundingMode.HALF_EVEN);
        return (zeroACem) ? Math.round(media) : mediaDecimal.doubleValue();
    }
}
