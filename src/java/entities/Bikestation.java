/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "bikestation")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Bikestation.findAll", query = "SELECT b FROM Bikestation b"),
    @NamedQuery(name = "Bikestation.findById", query = "SELECT b FROM Bikestation b WHERE b.serverId = :id"),
    @NamedQuery(name = "Bikestation.findByAddress", query = "SELECT b FROM Bikestation b WHERE b.address = :address"),
    @NamedQuery(name = "Bikestation.findByTotal", query = "SELECT b FROM Bikestation b WHERE b.total = :total"),
    @NamedQuery(name = "Bikestation.findByAvailable", query = "SELECT b FROM Bikestation b WHERE b.available = :available"),
    @NamedQuery(name = "Bikestation.findByBroken", query = "SELECT b FROM Bikestation b WHERE b.broken = :broken"),
    @NamedQuery(name = "Bikestation.findByReserved", query = "SELECT b FROM Bikestation b WHERE b.reserved = :reserved"),
    @NamedQuery(name = "Bikestation.findByLatitude", query = "SELECT b FROM Bikestation b WHERE b.latitude = :latitude"),
    @NamedQuery(name = "Bikestation.findByLongitude", query = "SELECT b FROM Bikestation b WHERE b.longitude = :longitude"),
    @NamedQuery(name = "Bikestation.findByMd5", query = "SELECT b FROM Bikestation b WHERE b.md5 = :md5"),
    @NamedQuery(name = "Bikestation.findByTimestampBike", query = "SELECT b FROM Bikestation b WHERE b.timestampBike = :timestampBike"),
    
    @NamedQuery(name = "Bikestation.getAvailable", query = "SELECT b.available FROM Bikestation b WHERE b.serverId = :id"),
    @NamedQuery(name = "Bikestation.getBroken", query = "SELECT b.broken FROM Bikestation b WHERE b.serverId = :id"),
    @NamedQuery(name = "Bikestation.getReserved", query = "SELECT b.reserved FROM Bikestation b WHERE b.serverId = :id"),
    @NamedQuery(name = "Bikestation.getTotal", query = "SELECT b.total FROM Bikestation b WHERE b.serverId = :id")})

public class Bikestation implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Integer serverId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "address")
    private String address;
    @Basic(optional = false)
    @NotNull
    @Column(name = "total")
    private int total;
    @Basic(optional = false)
    @NotNull
    @Column(name = "available")
    private int available;
    @Basic(optional = false)
    @NotNull
    @Column(name = "broken")
    private int broken;
    @Basic(optional = false)
    @NotNull
    @Column(name = "reserved")
    private int reserved;
    @Basic(optional = false)
    @NotNull
    @Column(name = "latitude")
    private float latitude;
    @Basic(optional = false)
    @NotNull
    @Column(name = "longitude")
    private float longitude;
    @Size(max = 32)
    @Column(name = "md5")
    private String md5;
    @Basic(optional = false)
    @NotNull
    @Column(name = "timestampBike")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestampBike;

    public Bikestation() {
    }

    public Bikestation(Integer serverId) {
        this.serverId = serverId;
    }

    public Bikestation(Integer serverId, String address, int total, int available, int broken, int reserved, float latitude, float longitude, Date timestamp) {
        this.serverId = serverId;
        this.address = address;
        this.total = total;
        this.available = available;
        this.broken = broken;
        this.reserved = reserved;
        this.latitude = latitude;
        this.longitude = longitude;
        this.timestampBike = timestamp;
    }

    //<editor-fold defaultstate="collapsed" desc="GETTERS & SETTERS">
    public Integer getServerId() {
        return serverId;
    }
    
    public void setServerId(Integer serverId) {
        this.serverId = serverId;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public int getTotal() {
        return total;
    }
    
    public void setTotal(int total) {
        this.total = total;
    }
    
    public int getAvailable() {
        return available;
    }
    
    public void setAvailable(int available) {
        this.available = available;
    }
    
    public int getBroken() {
        return broken;
    }
    
    public void setBroken(int broken) {
        this.broken = broken;
    }
    
    public int getReserved() {
        return reserved;
    }
    
    public void setReserved(int reserved) {
        this.reserved = reserved;
    }
    
    public float getLatitude() {
        return latitude;
    }
    
    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }
    
    public float getLongitude() {
        return longitude;
    }
    
    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }
    
    public String getMd5() {
        return md5;
    }
    
    public void setMd5(String md5) {
        this.md5 = md5;
    }
    
    public Date getTimestampBike() {
        return timestampBike;
    }
    
    public void setTimestampBike(Date timestampBike) {
        this.timestampBike = timestampBike;
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="EQUALS & HASHCODE">
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 73 * hash + Objects.hashCode(this.serverId);
        hash = 73 * hash + Objects.hashCode(this.address);
        hash = 73 * hash + this.total;
        hash = 73 * hash + this.available;
        hash = 73 * hash + this.broken;
        hash = 73 * hash + this.reserved;
        hash = 73 * hash + Float.floatToIntBits(this.latitude);
        hash = 73 * hash + Float.floatToIntBits(this.longitude);
        hash = 73 * hash + Objects.hashCode(this.md5);
        hash = 73 * hash + Objects.hashCode(this.timestampBike);
        return hash;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Bikestation other = (Bikestation) obj;
        if (this.total != other.total) {
            return false;
        }
        if (this.available != other.available) {
            return false;
        }
        if (this.broken != other.broken) {
            return false;
        }
        if (this.reserved != other.reserved) {
            return false;
        }
        if (Float.floatToIntBits(this.latitude) != Float.floatToIntBits(other.latitude)) {
            return false;
        }
        if (Float.floatToIntBits(this.longitude) != Float.floatToIntBits(other.longitude)) {
            return false;
        }
        if (!Objects.equals(this.address, other.address)) {
            return false;
        }
        if (!Objects.equals(this.md5, other.md5)) {
            return false;
        }
        if (!Objects.equals(this.serverId, other.serverId)) {
            return false;
        }
        if (!Objects.equals(this.timestampBike, other.timestampBike)) {
            return false;
        }
        return true;
    }
//</editor-fold>
    
    
    
    
}

   
