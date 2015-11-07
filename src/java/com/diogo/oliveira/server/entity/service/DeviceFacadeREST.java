package com.diogo.oliveira.server.entity.service;

import com.diogo.oliveira.server.entity.Device;
import com.google.android.gcm.server.Sender;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import static com.diogo.oliveira.server.util.Constants.GCM_API_KEY;

/**
 *
 * @author Diogo Oliveira
 * @date 07/11/2015 10:15:49
 */
@Stateless
@Path("device")
public class DeviceFacadeREST extends AbstractFacade<Device>
{
    @PersistenceContext(unitName = "GCM_PERSISTENCE")
    private EntityManager entityManager;

    public DeviceFacadeREST()
    {
        super(Device.class);
    }

    @POST
    @Override
    @Consumes(
            {
                MediaType.APPLICATION_JSON
            })
    public String create(Device device)
    {
        try
        {
            if(new Sender(GCM_API_KEY).checkingRegistrationId(device.getRegistrationId()))
            {
                TypedQuery<Device> queryExists = getEntityManager().createNamedQuery("Device.findByRegistrationId", Device.class);
                queryExists.setParameter("registrationId", device.getRegistrationId());

                if(queryExists.getResultList().isEmpty())
                {
                    /* Gera um novo Id */
                    TypedQuery<Integer> query = getEntityManager().createNamedQuery("Device.gerateId", Integer.class);

                    device.setId(query.getSingleResult());
                    device.setRegistrationDate(Calendar.getInstance().getTimeInMillis());

                    super.create(device);
                }
                else
                {
                    device = queryExists.getSingleResult();
                }
            }
            else
            {
                device.setRegistrationId(com.google.android.gcm.server.Constants.INVALID_REGISTRATION);
            }
        }
        catch(IOException ex)
        {
            Logger.getLogger(DeviceFacadeREST.class.getName()).log(Level.SEVERE, null, ex);
        }

        return new Gson().toJson(device);
    }

    @PUT
    @Path("{id}")
    @Consumes(
            {
                MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON
            })
    public void edit(@PathParam("id") Integer id, Device entity)
    {
        super.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Integer id)
    {
        super.remove(super.find(id));
    }

    @GET
    @Path("{id}")
    @Produces(
            {
                MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON
            })
    public Device find(@PathParam("id") Integer id)
    {
        return super.find(id);
    }

    @GET
    @Override
    @Produces(
            {
                MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON
            })
    public List<Device> findAll()
    {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces(
            {
                MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON
            })
    public List<Device> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to)
    {
        return super.findRange(new int[]
        {
            from, to
        });
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST()
    {
        return String.valueOf(super.count());
    }

    @Override
    protected EntityManager getEntityManager()
    {
        return entityManager;
    }
}
