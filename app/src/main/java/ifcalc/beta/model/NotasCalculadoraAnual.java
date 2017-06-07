package ifcalc.beta.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

import ifcalc.beta.util.ResultadoCalculo;

public class NotasCalculadoraAnual extends NotasCalculadora {

    public NotasCalculadoraAnual(boolean zeroAcem, Double nota1, Double nota2, Double nota3, Double nota4, Double notaProvaFinal) {
        super(zeroAcem, nota1, nota2, notaProvaFinal);
        this.nota3 =  nota3;
        this.nota4 =  nota4;
    }

    @Override
    public ResultadoCalculo calculaNotas(ConfiguracoesDisciplinas configuracoesDisciplinas) {
        pesoB1 = configuracoesDisciplinas.getPeso(1);
        pesoB2 = configuracoesDisciplinas.getPeso(2);
        pesoB3 = configuracoesDisciplinas.getPeso(3);
        pesoB4 = configuracoesDisciplinas.getPeso(4);
        somaPesos = configuracoesDisciplinas.getSomaPesos();
        mediaNecessaria = configuracoesDisciplinas.getMedia();
        notaNecessariaTotal = configuracoesDisciplinas.getNotaNecessaria();
        ResultadoCalculo resultadoCalculo = null;

        if (!isEmpty(nota1) && !isEmpty(nota2) && !isEmpty(nota3) && !isEmpty(nota4) && !isEmpty(notaProvaFinal)) {
            resultadoCalculo = realizaCalculoCompleto();
        } else if (!isEmpty(nota1) && !isEmpty(nota2) && !isEmpty(nota3) && !isEmpty(nota4)) {
            resultadoCalculo = realizaCalculoNormal();
        } else if (!isEmpty(nota1) && !isEmpty(nota2) && !isEmpty(nota3)) {
            resultadoCalculo = realizeCalculoFuturo1();
        } else if (!isEmpty(nota1) && !isEmpty(nota2)) {
            resultadoCalculo = realizeCalculoFuturo2();
        }
        return resultadoCalculo;
    }

    private ResultadoCalculo realizaCalculoCompleto() {
        ResultadoCalculo result1 = calculoMetodo1Completo();
        ResultadoCalculo result2 = calculoMetodo2Completo();

        return (result1.compareTo(result2) > 0 ? result1 : result2);
    }

    private ResultadoCalculo realizeCalculoFuturo1() {
        ResultadoCalculo result = new ResultadoCalculo();
        double notaNecessaria = ((notaNecessariaTotal - ((nota1 * pesoB1) + (nota2 * pesoB2) + (nota3 * pesoB3))) / pesoB4);
        double media = (((nota1 * pesoB1) + (nota2 * pesoB2) + (nota3 * pesoB3) + (0 * pesoB4)) / somaPesos);

        BigDecimal mediaDecimal = new BigDecimal(media).setScale(1, RoundingMode.HALF_EVEN);
        media = (zeroACem) ? Math.round(media) : mediaDecimal.doubleValue();

        result.setMediaFinal(arredondaMedia(media));
        if (arredondaMedia(media) >= mediaNecessaria) {
            result.setSituacao("Aprovado");
            result.setMensagem("Parabéns, você está aprovado!");
        } else {
            result.setSituacao("Cursando");

            BigDecimal notaNecessariaDecimal = new BigDecimal(notaNecessaria).setScale(1, RoundingMode.HALF_EVEN);
            int notaNecessariaInt = (int) Math.round(notaNecessaria);

            if (zeroACem) {
                result.setNotaNecessaria(notaNecessariaInt);
                result.setMensagem("Você precisa de " + notaNecessariaInt + " no 4º Bimestre para ser aprovado.");
            } else {
                result.setNotaNecessaria(notaNecessariaDecimal.doubleValue());
                result.setMensagem("Você precisa de " + notaNecessariaDecimal + " no 4º Bimestre para ser aprovado.");
            }

        }

        return result;
    }

    private ResultadoCalculo realizeCalculoFuturo2() {
        ResultadoCalculo result = new ResultadoCalculo();
        double notaNecessaria = ((notaNecessariaTotal - ((nota1 * pesoB1) + (nota2 * pesoB2))) / (pesoB3 + pesoB4));
        double media = (((nota1 * pesoB1) + (nota2 * pesoB2) + (0 * pesoB3) + (0 * pesoB4)) / somaPesos);

        BigDecimal mediaDecimal = new BigDecimal(media).setScale(1, RoundingMode.HALF_EVEN);
        media = (zeroACem) ? Math.round(media) : mediaDecimal.doubleValue();

        result.setMediaFinal(arredondaMedia(media));
        if (arredondaMedia(media) >= mediaNecessaria) {
            result.setSituacao("Aprovado");
            result.setMensagem("Parabéns, você está aprovado!");
        } else {
            result.setSituacao("Cursando");

            BigDecimal notaNecessariaDecimal = new BigDecimal(notaNecessaria).setScale(1, RoundingMode.HALF_EVEN);
            int notaNecessariaInt = (int) Math.round(notaNecessaria);

            if (zeroACem) {
                result.setNotaNecessaria(notaNecessariaInt);
                result.setMensagem("Você precisa de " + notaNecessariaInt + " no 3º e 4º Bimestre para ser aprovado.");
            } else {
                result.setNotaNecessaria(notaNecessariaDecimal.doubleValue());
                result.setMensagem("Você precisa de " + notaNecessariaDecimal + " no 3º e 4º Bimestre para ser aprovado.");
            }

        }

        return result;
    }

    private ResultadoCalculo calculoMetodo1Completo() {
        ResultadoCalculo result = new ResultadoCalculo();
        double media = ((((nota1 * pesoB1) + (nota2 * pesoB2) + (nota3 * pesoB3) + (nota4 * pesoB4)) / somaPesos) + notaProvaFinal) / 2;
        result.setMediaFinal(media);
        if (arredondaMedia(media) >= mediaNecessaria) {
            result.setSituacao("Aprovado");
            result.setMensagem("Parabéns, você está aprovado!");
        } else {
            result.setSituacao("Reprovado");
            result.setMensagem("Você está reprovado! :(");
        }

        return result;
    }

    private ResultadoCalculo calculoMetodo2Completo() {
        ResultadoCalculo result = new ResultadoCalculo();

        double maiorMedia, notaSubs1, notaSubs2, notaSubs3, notaSubs4;

        notaSubs1 = ((notaProvaFinal * pesoB1) + (nota2 * pesoB2) + (nota3 * pesoB3) + (nota4 * pesoB4)) / somaPesos;
        notaSubs2 = ((nota1 * pesoB1) + (notaProvaFinal * pesoB2) + (nota3 * pesoB3) + (nota4 * pesoB4)) / somaPesos;
        notaSubs3 = ((nota1 * pesoB1) + (nota2 * pesoB2) + (notaProvaFinal * pesoB3) + (nota4 * pesoB4)) / somaPesos;
        notaSubs4 = ((nota1 * pesoB1) + (nota2 * pesoB2) + (nota3 * pesoB3) + (notaProvaFinal * pesoB4)) / somaPesos;

        //Procura a maior media atingida pela substituicao
        if (notaSubs1 >= notaSubs2 && notaSubs1 >= notaSubs3 && notaSubs1 >= notaSubs4)
            maiorMedia = notaSubs1;
        else if (notaSubs2 >= notaSubs1 && notaSubs2 >= notaSubs3 && notaSubs2 >= notaSubs4)
            maiorMedia = notaSubs2;
        else if (notaSubs3 >= notaSubs1 && notaSubs3 >= notaSubs2 && notaSubs3 >= notaSubs4)
            maiorMedia = notaSubs3;
        else
            maiorMedia = notaSubs4;


        if (arredondaMedia(maiorMedia) >= mediaNecessaria) {
            result.setSituacao("Aprovado");
            result.setMensagem("Parabéns, você está aprovado!");
        } else {
            result.setSituacao("Reprovado");
            result.setMensagem("Você está reprovado! :(");
        }
        result.setMediaFinal(arredondaMedia(maiorMedia));

        return result;
    }

    private ResultadoCalculo realizaCalculoNormal() {
        ResultadoCalculo result = new ResultadoCalculo();
        double media = (((nota1 * pesoB1) + (nota2 * pesoB2) + (nota3 * pesoB3) + (nota4 * pesoB4)) / somaPesos);
        result.setMediaFinal(arredondaMedia(media));
        if (arredondaMedia(media) >= mediaNecessaria) {
            result.setSituacao("Aprovado");
            result.setMensagem("Parabéns, você está aprovado!");
        } else if (arredondaMedia(media) < mediaNecessaria && (arredondaMedia(media) >= (mediaNecessaria - (notaMax - mediaNecessaria)))) {
            result.setSituacao("Prova Final");
            result.setMensagem("Parabéns, você está aprovado!");

            double notaFinalSimples, notaSubs1, notaSubs2, notaSubs3, notaSubs4;

            notaFinalSimples = (mediaNecessaria * 2) - media;

            notaSubs1 = (notaNecessariaTotal - ((nota2 * pesoB2) + (nota3 * pesoB3) + (nota4 * pesoB4))) / pesoB1;
            notaSubs2 = (notaNecessariaTotal - ((nota1 * pesoB1) + (nota3 * pesoB3) + (nota4 * pesoB4))) / pesoB2;
            notaSubs3 = (notaNecessariaTotal - ((nota1 * pesoB1) + (nota2 * pesoB2) + (nota4 * pesoB4))) / pesoB3;
            notaSubs4 = (notaNecessariaTotal - ((nota1 * pesoB1) + (nota2 * pesoB2) + (nota3 * pesoB3))) / pesoB4;

            double notaNecessaria = 0;
            // Procura a menor nota necessaria para ser aprovado

            if (notaFinalSimples <= notaSubs1 && notaFinalSimples <= notaSubs2 && notaFinalSimples <= notaSubs3 && notaFinalSimples <= notaSubs4)
                notaNecessaria = notaFinalSimples; //Nota Simples eh a menor necessaria
            else if (notaSubs1 <= notaFinalSimples && notaSubs1 <= notaSubs2 && notaSubs1 <= notaSubs3 && notaSubs1 <= notaSubs4)
                notaNecessaria = notaSubs1; //Nota 1 eh a menor necessaria
            else if (notaSubs2 <= notaFinalSimples && notaSubs2 <= notaSubs1 && notaSubs2 <= notaSubs3 && notaSubs2 <= notaSubs4)
                notaNecessaria = notaSubs2; //Nota 2 eh a menor necessaria
            else if (notaSubs3 <= notaFinalSimples && notaSubs3 <= notaSubs1 && notaSubs3 <= notaSubs2 && notaSubs3 <= notaSubs4)
                notaNecessaria = notaSubs3; //Nota 3 eh a menor necessaria
            else
                notaNecessaria = notaSubs4; //Nota 4 eh a menor necessaria

            BigDecimal notaNecessariaDecimal = new BigDecimal(notaNecessaria).setScale(1, RoundingMode.HALF_EVEN);
            int notaNecessariaInt = (int) Math.round(notaNecessaria);

            if (zeroACem) {
                result.setNotaNecessaria(notaNecessariaInt);
                result.setMensagem("Você precisa de " + notaNecessariaInt + " na prova final para ser aprovado.");
            } else {
                result.setNotaNecessaria(notaNecessariaDecimal.doubleValue());
                result.setMensagem("Você precisa de " + notaNecessariaDecimal + " na prova final para ser aprovado.");
            }

        } else {
            result.setSituacao("Reprovado");
            result.setMensagem("Você está reprovado! :(");
        }

        return result;
    }
}
