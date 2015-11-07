package com.diogo.oliveira.server.entity.service;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 * @author diogo
 * @date 13/10/2015 09:46:49
 * 
 * @param <T>
 */
public abstract class AbstractFacade<T>
{
    private final Class<T> entityClass;

    public AbstractFacade(Class<T> entityClass)
    {
        this.entityClass = entityClass;
    }

    protected abstract EntityManager getEntityManager();

    public String create(T entity)
    {
        getEntityManager().persist(entity);
        return "";
    }

    public void edit(T entity)
    {
        getEntityManager().merge(entity);
    }

    public void remove(T entity)
    {
        getEntityManager().remove(getEntityManager().merge(entity));
    }

    public T find(Object id)
    {
        return getEntityManager().find(entityClass, id);
    }

    public List<T> findAll()
    {
        CriteriaQuery criteriaQuery = getEntityManager().getCriteriaBuilder().createQuery();
        criteriaQuery.select(criteriaQuery.from(entityClass));
        return getEntityManager().createQuery(criteriaQuery).getResultList();
    }

    public List<T> findRange(int[] range)
    {
        CriteriaQuery criteriaQuery = getEntityManager().getCriteriaBuilder().createQuery();
        criteriaQuery.select(criteriaQuery.from(entityClass));
        Query query = getEntityManager().createQuery(criteriaQuery);
        query.setMaxResults(range[1] - range[0] + 1);
        query.setFirstResult(range[0]);
        return query.getResultList();
    }

    public int count()
    {
        CriteriaQuery criteriaQuery = getEntityManager().getCriteriaBuilder().createQuery();
        Root<T> root = criteriaQuery.from(entityClass);
        criteriaQuery.select(getEntityManager().getCriteriaBuilder().count(root));
        Query query = getEntityManager().createQuery(criteriaQuery);
        return ((Long)query.getSingleResult()).intValue();
    }
}
