package ifcalc.beta.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

import ifcalc.beta.util.ResultadoCalculo;

public class NotasCalculadoraSemestral extends NotasCalculadora {

    public NotasCalculadoraSemestral(boolean zeroAcem, Double nota1, Double nota2, Double notaProvaFinal) {
        super(zeroAcem, nota1, nota2, notaProvaFinal);
    }

    @Override
    public ResultadoCalculo calculaNotas(ConfiguracoesDisciplinas configuracoesDisciplinas) {
        pesoB1 = configuracoesDisciplinas.getPeso(1);
        pesoB2 = configuracoesDisciplinas.getPeso(2);
        somaPesos = configuracoesDisciplinas.getSomaPesos();
        mediaNecessaria = configuracoesDisciplinas.getMedia();
        notaNecessariaTotal = configuracoesDisciplinas.getNotaNecessaria();
        ResultadoCalculo resultadoCalculo = null;

        if (!isEmpty(nota1) && !isEmpty(nota2) && !isEmpty(notaProvaFinal)) {
            resultadoCalculo = realizaCalculoCompleto();
        } else if (!isEmpty(nota1) && !isEmpty(nota2)) {
            resultadoCalculo = realizaCalculoNormal();
        } else if (!isEmpty(nota1)) {
            resultadoCalculo = realizeCalculoFuturo();
        }
        return resultadoCalculo;
    }

    private ResultadoCalculo realizeCalculoFuturo() {
        ResultadoCalculo result = new ResultadoCalculo();
        double notaNecessaria = ((notaNecessariaTotal - ((nota1 * pesoB1))) / pesoB2);
        double media = (((nota1 * pesoB1) + (0 * pesoB2)) / somaPesos);

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
                result.setMensagem("Você precisa de " + notaNecessariaInt + " no 2º Bimestre para ser aprovado.");
            } else {
                result.setNotaNecessaria(notaNecessariaDecimal.doubleValue());
                result.setMensagem("Você precisa de " + notaNecessariaDecimal + " no 2º Bimestre para ser aprovado.");
            }

        }

        return result;
    }

    private ResultadoCalculo realizaCalculoCompleto() {
        ResultadoCalculo result1 = calculoMetodo1Completo();
        ResultadoCalculo result2 = calculoMetodo2Completo();

        return (result1.compareTo(result2) > 0 ? result1 : result2);
    }

    private ResultadoCalculo realizaCalculoNormal() {
        ResultadoCalculo result = new ResultadoCalculo();
        double media = (((nota1 * pesoB1) + (nota2 * pesoB2)) / somaPesos);
        result.setMediaFinal(arredondaMedia(media));
        if (arredondaMedia(media) >= mediaNecessaria) {
            result.setSituacao("Aprovado");
            result.setMensagem("Parabéns, você está aprovado!");
        } else if (arredondaMedia(media) < mediaNecessaria && (arredondaMedia(media) >= (mediaNecessaria - (notaMax - mediaNecessaria)))) {
            result.setSituacao("Prova Final");
            result.setMensagem("Parabéns, você está aprovado!");

            double notaFinalSimples, notaSubs1, notaSubs2;

            notaFinalSimples = (mediaNecessaria * 2) - media;

            notaSubs1 = (notaNecessariaTotal - ((nota2 * pesoB2))) / pesoB1;
            notaSubs2 = (notaNecessariaTotal - ((nota1 * pesoB1))) / pesoB2;

            double notaNecessaria = 0;
            if (notaFinalSimples < notaSubs1 && notaFinalSimples < notaSubs2)
                notaNecessaria = notaFinalSimples;
            else if (notaSubs1 < notaFinalSimples && notaSubs1 < notaSubs2)
                notaNecessaria = notaSubs1;
            else
                notaNecessaria = notaSubs2;

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

    private ResultadoCalculo calculoMetodo1Completo() {
        ResultadoCalculo result = new ResultadoCalculo();
        double media = ((((nota1 * pesoB1) + (nota2 * pesoB2)) / somaPesos) + notaProvaFinal) / 2;
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

        double maiorMedia, notaSubs1, notaSubs2;

        notaSubs1 = ((notaProvaFinal * pesoB1) + (nota2 * pesoB2)) / somaPesos;
        notaSubs2 = ((nota1 * pesoB1) + (notaProvaFinal * pesoB2)) / somaPesos;

        maiorMedia = (notaSubs1 >= notaSubs2) ? notaSubs1 : notaSubs2;

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
}
