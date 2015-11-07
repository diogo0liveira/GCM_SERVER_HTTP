package com.diogo.oliveira.server.servlet;

import com.diogo.oliveira.server.entity.Device;
import com.diogo.oliveira.server.util.Constants;
import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.MulticastResult;
import com.google.android.gcm.server.Sender;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Diogo Oliveira
 * @date 07/11/2015 10:47:37
 */
public class PushServlet extends HttpServlet
{
    @PersistenceContext(unitName = "GCM_SERVER_HTTPPU")
    private EntityManager entityManager;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        response.setContentType("text/html;charset=UTF-8");

        try(PrintWriter out = response.getWriter())
        {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>GCM HTTP</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>MENSAVEM ENVIDA...</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        processRequest(request, response);
    }

    @Override
    @SuppressWarnings("CallToPrintStackTrace")
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        String strRegistrationId = String.valueOf(request.getParameter("inputRegistrationId"));
        String strMensagem = String.valueOf(request.getParameter("inputMensagem"));
        List<String> listDevices = new ArrayList<>();

        if(strRegistrationId == null || strRegistrationId.trim().isEmpty())
        {
            TypedQuery<Device> query = entityManager.createNamedQuery("Device.findAll", Device.class);
            List<Device> listCliente = query.getResultList();

            listCliente.stream().forEach((cliente)
                    -> 
                    {
                        listDevices.add(cliente.getRegistrationId());
            });
        }
        else
        {
            listDevices.add(strRegistrationId);
        }

        if(listDevices.size() > 0)
        {
            Sender sender = new Sender(Constants.GCM_API_KEY);

            Message message = new Message.Builder()
                    //.timeToLive(30)
                    .delayWhileIdle(false)
                    .addData("message", strMensagem)
                    .build();

            try
            {
                MulticastResult multicastResult = sender.send(message, listDevices, 1);

                if(multicastResult.getResults() != null)
                {
                    int canonicalRegId = multicastResult.getCanonicalIds();

                    if(canonicalRegId != 0)
                    {

                    }
                }
                else
                {
                    int error = multicastResult.getFailure();
                    System.out.println("Broadcast failure: " + error);
                }

            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }

        processRequest(request, response);
    }
}
