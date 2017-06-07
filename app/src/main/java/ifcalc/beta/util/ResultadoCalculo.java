package ifcalc.beta.util;

public class ResultadoCalculo implements Comparable {
    private String situacao;
    private String mensagem;
    private double mediaFinal;
    private double notaNecessaria;

    public ResultadoCalculo() {
    }

    public ResultadoCalculo(String situacao, String mensagem, double mediaFinal, double notaNecessaria) {
        this.situacao = situacao;
        this.mensagem = mensagem;
        this.mediaFinal = mediaFinal;
        this.notaNecessaria = notaNecessaria;
    }
    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public double getMediaFinal() {
        return mediaFinal;
    }

    public void setMediaFinal(double mediaFinal) {
        this.mediaFinal = mediaFinal;
    }

    public double getNotaNecessaria() {
        return notaNecessaria;
    }

    public void setNotaNecessaria(double notaNecessaria) {
        this.notaNecessaria = notaNecessaria;
    }

    @Override
    public int compareTo(Object object) {
        if (object instanceof ResultadoCalculo) {
            ResultadoCalculo rest = (ResultadoCalculo) object;
            if(this.getMediaFinal() >= rest.getMediaFinal())
                return 1;
            else
                return -1;
        }
        return 0;
    }
}
