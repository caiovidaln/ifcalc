package ifcalc.beta.model;

public class ConfiguracoesDisciplinas {
    private int[] pesos;
    private TipoDisciplina tipoDisc;
    private int media;

    public ConfiguracoesDisciplinas(TipoDisciplina tipoDisc, int media) {
        setQtdPesos(tipoDisc);
        this.tipoDisc = tipoDisc;
        this.media = media;
    }

    private void setQtdPesos(TipoDisciplina tipoDisc) {
        switch (tipoDisc){
            case SEMESTRAL:
                this.pesos = new int[2];
                break;
            case ANUAL:
                this.pesos = new int[4];
                break;
            case TRIMESTRAL:
                this.pesos = new int[3];
                break;
        }
    }

    public int[] getPesos() {
        return pesos;
    }

    public void setPeso(int bimestre, int valor) {
        pesos[bimestre-1] = valor;
    }

    public int getPeso(int bimestre) {
        return pesos[bimestre-1];
    }

    public int getSomaPesos(){
        int soma = 0;
        for (int peso : pesos) {
            soma += peso;
        }
        return soma;
    }

    public int getNotaNecessaria() {
        return media * getSomaPesos();
    }

    public TipoDisciplina getTipoDisc() {
        return tipoDisc;
    }

    public int getMedia() {
        return media;
    }

    public void setMedia(int media) {
        this.media = media;
    }
}
