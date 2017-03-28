
import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

/**
 *
 * @author leandro
 */
@WebServlet(name = "ServletVerificador", urlPatterns = {"/verifique"})
public class ServletVerificador extends HttpServlet {

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>INE5646 - primoREST</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>INE5646 - primoREST</h1>");
            out.println(processeNumero(request.getParameter("numero")));
            out.println("</body>");
            out.println("</html>");
        }

    }

    private String processeNumero(String numero) {
        final String COR_PRIMO = "green";
        final String COR_NAO_PRIMO = "orange";
        final String COR_ERRO_NAO_EH_HUMERO = "red";
        StringBuilder sb = new StringBuilder("");
        String cor = COR_ERRO_NAO_EH_HUMERO;
        String msg = "[substituir pela mensagem apropriada]";

        try {

            int num = Integer.parseInt(numero);

            if (ehPrimo(num)) {
                cor = COR_PRIMO;
                msg = "É um número primo!";

            } else {
                cor = COR_NAO_PRIMO;
                msg = "Não é primo!";

            }

        } catch (Exception e) {
            cor = COR_ERRO_NAO_EH_HUMERO;
            msg = "Não é um número!";
            
        }

        return sb.append("<h2 style='color : ").append(cor).append("'>").append(numero).append(" : ").append(msg).append("</h2>").toString();
    }

    // retorna true se num for primo ou false caso contrário.
    private boolean ehPrimo(long num) {

        Client c = ClientBuilder.newClient();
        
        String numero = String.valueOf(num);
        String uri = "http://server.adriano.lima.vms.ufsc.br:8080/divisoresRESTful/webresources/" + numero;

        WebTarget wt = c.target(uri);

        String arrayDeDivisoresJSON = wt.request().get().readEntity(String.class);
        
        int[] divisores = new Gson().fromJson(arrayDeDivisoresJSON, int[].class);
        
        boolean primo = divisores.length == 2;
        
        return primo;
    }
}
