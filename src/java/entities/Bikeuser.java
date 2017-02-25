
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
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;


@Entity
@Table(name = "bikeuser")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Bikeuser.findAll", query = "SELECT b FROM Bikeuser b"),
    @NamedQuery(name = "Bikeuser.updateTimedOutBookings", query = "UPDATE Bikeuser b SET b.booktaken = :booktaken, b.slotstaken = :slotstaken WHERE b.id = :id"),
    @NamedQuery(name = "Bikeuser.findById", query = "SELECT b FROM Bikeuser b WHERE b.id = :id"),
    @NamedQuery(name = "Bikeuser.findByUsername", query = "SELECT b FROM Bikeuser b WHERE b.username = :username"),
    @NamedQuery(name = "Bikeuser.findByPassword", query = "SELECT b FROM Bikeuser b WHERE b.password = :password"),
    @NamedQuery(name = "Bikeuser.findByFullname", query = "SELECT b FROM Bikeuser b WHERE b.fullname = :fullname"),
    @NamedQuery(name = "Bikeuser.findByEmail", query = "SELECT b FROM Bikeuser b WHERE b.email = :email"),
    @NamedQuery(name = "Bikeuser.findByMd5", query = "SELECT b FROM Bikeuser b WHERE b.md5 = :md5"),
    @NamedQuery(name = "Bikeuser.findByBiketaken", query = "SELECT b FROM Bikeuser b WHERE b.biketaken = :biketaken"),
    @NamedQuery(name = "Bikeuser.findByBooktaken", query = "SELECT b FROM Bikeuser b WHERE b.booktaken = :booktaken"),
    @NamedQuery(name = "Bikeuser.findBySlotstaken", query = "SELECT b FROM Bikeuser b WHERE b.slotstaken = :slotstaken"),
    @NamedQuery(name = "Bikeuser.findByBookaddress", query = "SELECT b FROM Bikeuser b WHERE b.bookaddress = :bookaddress"),
    @NamedQuery(name = "Bikeuser.findBySlotsaddress", query = "SELECT b FROM Bikeuser b WHERE b.slotsaddress = :slotsaddress"),
    @NamedQuery(name = "Bikeuser.findByBookdate", query = "SELECT b FROM Bikeuser b WHERE b.bookdate = :bookdate"),
    @NamedQuery(name = "Bikeuser.findBySlotsdate", query = "SELECT b FROM Bikeuser b WHERE b.slotsdate = :slotsdate"),
    @NamedQuery(name = "Bikeuser.findByBalance", query = "SELECT b FROM Bikeuser b WHERE b.balance = :balance")})
public class Bikeuser implements Serializable {
   
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "entityid")
    private String entityid;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "username")
    private String username;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "password")
    private String password;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "fullname")
    private String fullname;
    @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "email")
    private String email;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 32)
    @Column(name = "md5")
    private String md5;
    @Basic(optional = false)
    @NotNull
    @Column(name = "biketaken")
    private boolean biketaken;
    @Basic(optional = false)
    @NotNull
    @Column(name = "booktaken")
    private boolean booktaken;
    @Basic(optional = false)
    @NotNull
    @Column(name = "slotstaken")
    private boolean slotstaken;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "bookaddress")
    private String bookaddress;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "slotsaddress")
    private String slotsaddress;
    @Basic(optional = false)
    @NotNull
    @Column(name = "bookdate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date bookdate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "slotsdate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date slotsdate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "balance")
    private float balance;

    public Bikeuser() {
    }

    public Bikeuser(Integer id) {
        this.id = id;
    }

    public Bikeuser(Integer id, String username, String password, String fullname, String email, String md5, boolean biketaken, boolean booktaken, boolean slotstaken, String bookaddress, String slotsaddress, Date bookdate, Date slotsdate, float balance) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.fullname = fullname;
        this.email = email;
        this.md5 = md5;
        this.biketaken = biketaken;
        this.booktaken = booktaken;
        this.slotstaken = slotstaken;
        this.bookaddress = bookaddress;
        this.slotsaddress = slotsaddress;
        this.bookdate = bookdate;
        this.slotsdate = slotsdate;
        this.balance = balance;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public boolean getBiketaken() {
        return biketaken;
    }

    public void setBiketaken(boolean biketaken) {
        this.biketaken = biketaken;
    }

    public boolean getBooktaken() {
        return booktaken;
    }

    public void setBooktaken(boolean booktaken) {
        this.booktaken = booktaken;
    }

    public boolean getSlotstaken() {
        return slotstaken;
    }

    public void setSlotstaken(boolean slotstaken) {
        this.slotstaken = slotstaken;
    }

    public String getBookaddress() {
        return bookaddress;
    }

    public void setBookaddress(String bookaddress) {
        this.bookaddress = bookaddress;
    }

    public String getSlotsaddress() {
        return slotsaddress;
    }

    public void setSlotsaddress(String slotsaddress) {
        this.slotsaddress = slotsaddress;
    }

    public Date getBookdate() {
        return bookdate;
    }

    public void setBookdate(Date bookdate) {
        this.bookdate = bookdate;
    }

    public Date getSlotsdate() {
        return slotsdate;
    }

    public void setSlotsdate(Date slotsdate) {
        this.slotsdate = slotsdate;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
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
        if (!(object instanceof Bikeuser)) {
            return false;
        }
        Bikeuser other = (Bikeuser) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Bikeuser[ id=" + id + " ]";
    }

    public String getEntityid() {
        return entityid;
    }

    public void setEntityid(String entityid) {
        this.entityid = entityid;
    }
    
    
    public void cancelBikeBooking() {
        booktaken = false;
        bookaddress = "None";
    }
    
    public void cancelSlotsBooking() {
        slotstaken = false;
        slotsaddress = "None";
    }
    
    public boolean isBikeBookingTimedOut(){
        long now = System.currentTimeMillis();
        long remainingTime = Booking.MAX_BOOKING_TIME - (now - bookdate.getTime());
      
        return booktaken && (remainingTime <= 0);
    }
    
    public boolean isSlotsBookingTimedOut(){
        long now = System.currentTimeMillis();
        long remainingTime = Booking.MAX_BOOKING_TIME - (now - slotsdate.getTime());
      
        return slotstaken && (remainingTime <= 0);
    }
    
}
