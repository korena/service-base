/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.siphyc.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author korena
 */
@Entity
@Table(name = "iphone")
@NamedQueries({
    @NamedQuery(name = "Iphone.findAll", query = "SELECT i FROM Iphone i"),
    @NamedQuery(name = "Iphone.findById", query = "SELECT i FROM Iphone i WHERE i.id = :id"),
    @NamedQuery(name = "Iphone.findByModel", query = "SELECT i FROM Iphone i WHERE i.model = :model"),
    @NamedQuery(name = "Iphone.findByStatus", query = "SELECT i FROM Iphone i WHERE i.status = :status"),
    @NamedQuery(name = "Iphone.findByUpdated", query = "SELECT i FROM Iphone i WHERE i.updated = :updated"),
    @NamedQuery(name = "Iphone.findByCreated", query = "SELECT i FROM Iphone i WHERE i.created = :created")})
public class Iphone implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "customer")
    private String customer;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "model")
    private String model;
    @Basic(optional = false)
    @NotNull
    @Column(name = "status")
    private boolean status;
    @Basic(optional = false)
    @NotNull
    @Column(name = "updated")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updated;
    @Basic(optional = false)
    @NotNull
    @Column(name = "created")
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;

    public Iphone() {
    }

    public Iphone(Integer id) {
        this.id = id;
    }

    public Iphone(Integer id, String customer, String model, boolean status, Date updated, Date created) {
        this.id = id;
        this.customer = customer;
        this.model = model;
        this.status = status;
        this.updated = updated;
        this.created = created;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Iphone)) {
            return false;
        }
        Iphone other = (Iphone) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.siphyc.model.Iphone[ id=" + id + " ]";
    }
    
}
