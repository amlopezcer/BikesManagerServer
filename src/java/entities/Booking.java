/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
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

/**
 *
 * @author USUARIO
 */
@Entity
@Table(name = "booking")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Booking.findAll", query = "SELECT b FROM Booking b"),
    @NamedQuery(name = "Booking.deleteById", query = "DELETE FROM Booking b WHERE b.id = :id"),
    @NamedQuery(name = "Booking.findById", query = "SELECT b FROM Booking b WHERE b.id = :id"),
    @NamedQuery(name = "Booking.findByBookaddress", query = "SELECT b FROM Booking b WHERE b.bookaddress = :bookaddress"),
    @NamedQuery(name = "Booking.findByBookdate", query = "SELECT b FROM Booking b WHERE b.bookdate = :bookdate"),
    @NamedQuery(name = "Booking.findByBooktype", query = "SELECT b FROM Booking b WHERE b.booktype = :booktype"),
    @NamedQuery(name = "Booking.findByUsername", query = "SELECT b FROM Booking b WHERE b.username = :username"),
    @NamedQuery(name = "Booking.findByEntityid", query = "SELECT b FROM Booking b WHERE b.entityid = :entityid"),
    @NamedQuery(name = "Booking.findByMd5", query = "SELECT b FROM Booking b WHERE b.md5 = :md5")})
public class Booking implements Serializable {
    
    public static final int MAX_BOOKING_TIME = 60000; //1800000; //30'
    public static final int BOOKING_TYPE_BIKE = 1; //To identify the booking type
    public static final int BOOKING_TYPE_MOORINGS = 2; //To identify the booking type

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "bookaddress")
    private String bookaddress;
    @Basic(optional = false)
    @NotNull
    @Column(name = "bookdate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date bookdate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "booktype")
    private int booktype;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "username")
    private String username;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "entityid")
    private String entityid;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 32)
    @Column(name = "md5")
    private String md5;

    public Booking() {
    }

    public Booking(Integer id) {
        this.id = id;
    }

    public Booking(Integer id, String bookaddress, Date bookdate, int booktype, String username, String entityid, String md5) {
        this.id = id;
        this.bookaddress = bookaddress;
        this.bookdate = bookdate;
        this.booktype = booktype;
        this.username = username;
        this.entityid = entityid;
        this.md5 = md5;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBookaddress() {
        return bookaddress;
    }

    public void setBookaddress(String bookaddress) {
        this.bookaddress = bookaddress;
    }

    public Date getBookdate() {
        return bookdate;
    }

    public void setBookdate(Date bookdate) {
        this.bookdate = bookdate;
    }

    public int getBooktype() {
        return booktype;
    }

    public void setBooktype(int booktype) {
        this.booktype = booktype;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEntityid() {
        return entityid;
    }

    public void setEntityid(String entityid) {
        this.entityid = entityid;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
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
        if (!(object instanceof Booking)) {
            return false;
        }
        Booking other = (Booking) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Booking[ id=" + id + " ]";
    }
    
    public long getRemainingBookingTime() {
        long now = System.currentTimeMillis();

        return MAX_BOOKING_TIME - (now - bookdate.getTime());
    }
    
}
