
package entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
    @NamedQuery(name = "Bikestation.findById", query = "SELECT b FROM Bikestation b WHERE b.id = :id"),
    @NamedQuery(name = "Bikestation.findByAddress", query = "SELECT b FROM Bikestation b WHERE b.address = :address"),
    @NamedQuery(name = "Bikestation.findByTotalmoorings", query = "SELECT b FROM Bikestation b WHERE b.totalmoorings = :totalmoorings"),
    @NamedQuery(name = "Bikestation.findByReservedmoorings", query = "SELECT b FROM Bikestation b WHERE b.reservedmoorings = :reservedmoorings"),
    @NamedQuery(name = "Bikestation.findByAvailablebikes", query = "SELECT b FROM Bikestation b WHERE b.availablebikes = :availablebikes"),
    @NamedQuery(name = "Bikestation.findByReservedbikes", query = "SELECT b FROM Bikestation b WHERE b.reservedbikes = :reservedbikes"),
    @NamedQuery(name = "Bikestation.findByLatitude", query = "SELECT b FROM Bikestation b WHERE b.latitude = :latitude"),
    @NamedQuery(name = "Bikestation.findByLongitude", query = "SELECT b FROM Bikestation b WHERE b.longitude = :longitude"),
    @NamedQuery(name = "Bikestation.findByMd5", query = "SELECT b FROM Bikestation b WHERE b.md5 = :md5"),
    @NamedQuery(name = "Bikestation.findByChangetimestamp", query = "SELECT b FROM Bikestation b WHERE b.changetimestamp = :changetimestamp"),
    @NamedQuery(name = "Bikestation.findByBasicfare", query = "SELECT b FROM Bikestation b WHERE b.basicfare = :basicfare"),
    @NamedQuery(name = "Bikestation.findByEntityid", query = "SELECT b FROM Bikestation b WHERE b.entityid = :entityid")})
public class Bikestation implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "address")
    private String address;
    @Basic(optional = false)
    @NotNull
    @Column(name = "totalmoorings")
    private int totalmoorings;
    @Basic(optional = false)
    @NotNull
    @Column(name = "reservedmoorings")
    private int reservedmoorings;
    @Basic(optional = false)
    @NotNull
    @Column(name = "availablebikes")
    private int availablebikes;
    @Basic(optional = false)
    @NotNull
    @Column(name = "reservedbikes")
    private int reservedbikes;
    @Basic(optional = false)
    @NotNull
    @Column(name = "latitude")
    private float latitude;
    @Basic(optional = false)
    @NotNull
    @Column(name = "longitude")
    private float longitude;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 32)
    @Column(name = "md5")
    private String md5;
    @Basic(optional = false)
    @NotNull
    @Column(name = "changetimestamp")
    @Temporal(TemporalType.TIMESTAMP)
    private Date changetimestamp;
    @Basic(optional = false)
    @NotNull
    @Column(name = "basicfare")
    private float basicfare;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "entityid")
    private String entityid;

    public Bikestation() {
    }

    public Bikestation(Integer id) {
        this.id = id;
    }

    public Bikestation(Integer id, String address, int totalmoorings, int reservedmoorings, int availablebikes, int reservedbikes, float latitude, float longitude, String md5, Date changetimestamp, float basicfare, String entityid) {
        this.id = id;
        this.address = address;
        this.totalmoorings = totalmoorings;
        this.reservedmoorings = reservedmoorings;
        this.availablebikes = availablebikes;
        this.reservedbikes = reservedbikes;
        this.latitude = latitude;
        this.longitude = longitude;
        this.md5 = md5;
        this.changetimestamp = changetimestamp;
        this.basicfare = basicfare;
        this.entityid = entityid;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getTotalmoorings() {
        return totalmoorings;
    }

    public void setTotalmoorings(int totalmoorings) {
        this.totalmoorings = totalmoorings;
    }

    public int getReservedmoorings() {
        return reservedmoorings;
    }

    public void setReservedmoorings(int reservedmoorings) {
        this.reservedmoorings = reservedmoorings;
    }

    public int getAvailablebikes() {
        return availablebikes;
    }

    public void setAvailablebikes(int availablebikes) {
        this.availablebikes = availablebikes;
    }

    public int getReservedbikes() {
        return reservedbikes;
    }

    public void setReservedbikes(int reservedbikes) {
        this.reservedbikes = reservedbikes;
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

    public Date getChangetimestamp() {
        return changetimestamp;
    }

    public void setChangetimestamp(Date changetimestamp) {
        this.changetimestamp = changetimestamp;
    }

    public float getBasicfare() {
        return basicfare;
    }

    public void setBasicfare(float basicfare) {
        this.basicfare = basicfare;
    }

    public String getEntityid() {
        return entityid;
    }

    public void setEntityid(String entityid) {
        this.entityid = entityid;
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
        if (!(object instanceof Bikestation)) {
            return false;
        }
        Bikestation other = (Bikestation) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Bikestation[ id=" + id + " ]";
    }
    
}
