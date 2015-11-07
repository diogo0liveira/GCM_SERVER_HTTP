package com.diogo.oliveira.server.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Diogo Oliveira
 * @date 07/11/2015 10:47:37
 */
@Entity
@Table(name = "DEVICE")
@XmlRootElement
@NamedQueries(
        {
            @NamedQuery(name = "Device.findAll", query = "SELECT d FROM Device d"),
            @NamedQuery(name = "Device.findById", query = "SELECT d FROM Device d WHERE d.id = :id"),
            @NamedQuery(name = "Device.findByRegistrationId", query = "SELECT d FROM Device d WHERE d.registrationId = :registrationId"),
            @NamedQuery(name = "Device.findByRegistrationDate", query = "SELECT d FROM Device d WHERE d.registrationDate = :registrationDate")
        })
public class Device implements Serializable
{
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "REGISTRATION_ID")
    private String registrationId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "REGISTRATION_DATE")
    private long registrationDate;

    public Device()
    {
    }

    public Device(Integer id)
    {
        this.id = id;
    }

    public Device(Integer id, String registrationId, long registrationDate)
    {
        this.id = id;
        this.registrationId = registrationId;
        this.registrationDate = registrationDate;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getRegistrationId()
    {
        return registrationId;
    }

    public void setRegistrationId(String registrationId)
    {
        this.registrationId = registrationId;
    }

    public long getRegistrationDate()
    {
        return registrationDate;
    }

    public void setRegistrationDate(long registrationDate)
    {
        this.registrationDate = registrationDate;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object)
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if(!(object instanceof Device))
        {
            return false;
        }
        Device other = (Device)object;
        if((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)))
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "com.diogo.oliveira.server.entity.Device[ id=" + id + " ]";
    }

}
