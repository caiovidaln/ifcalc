package ifcalc.beta.util;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.webkit.WebView;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutionException;

import ifcalc.beta.activities.ImportDisciplinasActivity;
import ifcalc.beta.model.TipoSistema;


public class ImportDisciplinasTask extends AsyncTask<ImportDisciplinasActivity, Integer, Document> {

    private String PREFIXO_URL = "https://suap.ifrn.edu.br/";
    private String URL_LOGIN = PREFIXO_URL + "accounts/login/";

    private ImportDisciplinasActivity activity;
    private String usuario;
    private String senha;
    private TipoSistema tipoSistema;
    private int typeError = 0;

    protected void onProgressUpdate(Integer... progress) {
        activity.setProgressInBar(progress[0]);
    }

    @Override
    protected Document doInBackground(ImportDisciplinasActivity... activities) {
        try {
            activity = activities[0];
            usuario = activity.getUsuario();
            senha = activity.getSenha();
            tipoSistema = activity.getTipoSistema();
        } catch (Exception e) {
            typeError = 10;
            return null;
        }

        switch (tipoSistema) {
            case SUAP_IFPB:
                PREFIXO_URL = "https://suap.ifpb.edu.br/";
                break;

            case SUAP_IFRN:
                PREFIXO_URL = "https://suap.ifrn.edu.br/";
                break;

        }

        Document document = null;

        try {
            publishProgress(5);

            Connection.Response loginForm = Jsoup
                    .connect(URL_LOGIN)
                    .timeout(10*1000)
                    .method(Connection.Method.GET).execute();

            publishProgress(20);

            Document doc = loginForm.parse();

            publishProgress(25);

            Elements elt = doc.getElementsByTag("input");
            String token = "";
            for (Element element : elt) {
                if(element.attr("name").equals("csrfmiddlewaretoken")) {
                    token = element.attr("value");
                }
            }

            publishProgress(30);

            loginForm = Jsoup
                    .connect(URL_LOGIN)
                    .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.98 Safari/537.36")
                    .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
                    .header("Accept-Language", "pt-BR,pt;q=0.8,en-US;q=0.6,en;q=0.4,es;q=0.2")
                    .header("Accept-Encoding", "gzip, deflate, br")
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .header("Referer", "https://suap.ifrn.edu.br/accounts/login/")
                    .data("csrfmiddlewaretoken", token)
                    .data("username", usuario)
                    .data("this_is_the_login_form", "1")
                    .data("password", senha)
                    .data("next", "")
                    .followRedirects(true)
                    .timeout(20*1000)
                    .method(Connection.Method.POST)
                    .cookies(loginForm.cookies()).execute();

            publishProgress(50);

            if (loginForm.headers().get("user").equals("anonymous")) {
                typeError = 20;
                return null;
            }

            document = Jsoup
                    .connect(PREFIXO_URL + "edu/aluno/" + usuario + "/?tab=boletim")
                    .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.98 Safari/537.36")
                    .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
                    .header("Accept-Language", "pt-BR,pt;q=0.8,en-US;q=0.6,en;q=0.4,es;q=0.2")
                    .header("Accept-Encoding", "gzip, deflate, br")
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .header("Referer", URL_LOGIN)
                    .timeout(20*1000)
                    .cookies(loginForm.cookies()).get();


            publishProgress(85);

        } catch (IOException e) {
            e.printStackTrace();
            typeError = 30;
            return null;
        }

        return document;
    }

    @Override
    protected void onPostExecute(Document document) {
        if (typeError > 0){
            activity.mostraErro(typeError);
        } else {
            activity.pegaDados(document);
        }
    }
}
