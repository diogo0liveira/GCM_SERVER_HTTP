package com.diogo.oliveira.server.entity.service;

import java.util.Set;
import javax.ws.rs.core.Application;

/**
 *
 * @author Diogo Oliveira
 * @date 05/11/2015 17:11:28
 */
@javax.ws.rs.ApplicationPath("resources")
public class ApplicationConfig extends Application
{
    @Override
    public Set<Class<?>> getClasses()
    {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method. It is automatically populated with all resources defined in the project. If required,
     * comment out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources)
    {
        resources.add(com.diogo.oliveira.server.entity.service.DeviceFacadeREST.class);
    }
}
