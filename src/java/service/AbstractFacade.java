
package service;

import entities.Bikestation;
import entities.Bikeuser;
import entities.Booking;
import java.util.List;
import java.util.Objects;
import javax.persistence.EntityManager;
import javax.persistence.Query;


public abstract class AbstractFacade<T> {

    //Standard server responses (%s to get de entity involved)
    private final String RESPONSE_OK = "%s_SERVER_OK";
    private final String RESPONSE_KO = "%s_SERVER_KO";
    
    private Class<T> entityClass;

    public AbstractFacade(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    protected abstract EntityManager getEntityManager();

    public void create(T entity) {
        getEntityManager().persist(entity);
    }
    
    public void edit(T entity) {
        getEntityManager().merge(entity);
    }
    
    public void remove(T entity) {
        getEntityManager().remove(getEntityManager().merge(entity));
    }

    public T find(Object id) {
        return getEntityManager().find(entityClass, id);
    }

    public List<T> findAll() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        return getEntityManager().createQuery(cq).getResultList();
    }

    public List<T> findRange(int[] range) {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        q.setMaxResults(range[1] - range[0] + 1);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        javax.persistence.criteria.Root<T> rt = cq.from(entityClass);
        cq.select(getEntityManager().getCriteriaBuilder().count(rt));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }
    
    /**
     * *****************
     * Custom methods
     * 
     * Booking
     * *****************
     */
    
    //Custom booking creator (just to assign a correct user server ID)
    public String createNewBooking(T entity) { 
        Booking b = (Booking) entity;      
        b.setId(getNewBookingID());
        getEntityManager().persist(entity);
        return String.format(RESPONSE_OK, b.getEntityid());
    }
    
    //Getting a new user ID from the server (the highest)
    private int getNewBookingID() {
        List<Booking> bookingList = (List<Booking>) findAll(); //getting the booking list
        
        if (bookingList.isEmpty())
            return 1;
        
        int currentID; 
        int newID = bookingList.get(0).getId();
        
        for(Booking booking: bookingList) {
            currentID = booking.getId();
            if(currentID > newID)
                newID = currentID;
        }
        
        return newID + 1;
    }
    
    
    /**
     * *****************
     * Custom methods
     * 
     * BikeStation
     * *****************
     */
    
    // Edit for the Bikestation (taking, leaving bikes...)
    public String editBikeStation(T entity, String operation) {
        /**
         * Same string as in the app, to confirm the op which is being processed, 
         * no need of a "leave" one because it is an if - else staetment
         */
        final String OP_TAKE = "take";
        final String OP_LEAVE = "leave";
        final String OP_BOOK_BIKE = "book_bike";
        final String OP_BOOK_MOORINGS = "book_moorings";
        
        Bikestation b = (Bikestation) entity;
        boolean isStatusOk = false;
        int availableMoorings =  b.getTotalmoorings() - b.getAvailablebikes() -  b.getReservedbikes() - b.getReservedmoorings() + 1; //Because the value comes updated form the app, necessary to check limit conditions
        
        switch (operation) {
            case OP_TAKE:
            case OP_BOOK_BIKE: //To take or book bikes requires the same condition
                isStatusOk = (b.getAvailablebikes() + 1) > 0; //+1 because the value comes updated form the app with a -1, necessary to check limit conditions
                break;
            case OP_LEAVE:
            case OP_BOOK_MOORINGS: //Same as before, same condition
                isStatusOk = availableMoorings > 0;
                break;
        }
        
        if(isStatusOk) {
            getEntityManager().merge(entity);
            return String.format(RESPONSE_OK, b.getEntityid());
        }
        
        return String.format(RESPONSE_KO, b.getEntityid());
    }
    
    public String editBasicBikeStation(T entity) {
        Bikestation b = (Bikestation) entity;
        getEntityManager().merge(entity);
        return String.format(RESPONSE_OK, b.getEntityid());
    }
    
    //Return a bike station object found by the address
    public Bikestation findByStationAddress(String address) {
        String addressAux = address.replaceAll("_", " "); //" " come as "_" from the app to avoid issues with urls
        
        EntityManager em = getEntityManager();
        Query query = em.createNamedQuery("Bikestation.findByAddress", Bikestation.class);
        query.setParameter("address", addressAux);
        
        try {
            Bikestation b = (Bikestation) query.getSingleResult();
            return b;
        } catch(Exception e) {
            System.out.println(e.getLocalizedMessage());
            return null;
        }
    }
    
    /**
     * *****************
     * Custom methods
     * 
     * BikeUser
     * *****************
     */
    
    //Custom user creator (just to assign a correct user server ID)
    public String createNewUser(T entity) { 
        Bikeuser b = (Bikeuser) entity;
        //Check if the username or email are availables
        if(isUsernameAvailable(b.getUsername()) && isEmailAvailable(b.getEmail())) {
            b.setId(getNewUserID());
            getEntityManager().persist(entity);
            return String.format(RESPONSE_OK, b.getEntityid());
        }
        
        return String.format(RESPONSE_KO, b.getEntityid());
    }
    
    //Checking if the username is available
    private boolean isUsernameAvailable(String username) {
        EntityManager em = getEntityManager();
        Query query = em.createNamedQuery("Bikeuser.findByUsername", Bikeuser.class);
        query.setParameter("username", username);
        
        try {
            Bikeuser b = (Bikeuser) query.getSingleResult(); //Needed to generate the following exception
            return false;
        } catch(Exception e) {
            System.out.println(e.getLocalizedMessage());
            return true;
        }
    }
    
    //Checking if the email is available
    private boolean isEmailAvailable(String email) {
        EntityManager em = getEntityManager();
        Query query = em.createNamedQuery("Bikeuser.findByEmail", Bikeuser.class);
        query.setParameter("email", email);
        
        try {
            Bikeuser b = (Bikeuser) query.getSingleResult(); //Needed to generate the following exception
            return false;
        } catch(Exception e) {
            System.out.println(e.getLocalizedMessage());
            return true;
        }
    }
    
    //Getting a new user ID from the server (the highest)
    private int getNewUserID() {
        List<Bikeuser> bikeUserList = (List<Bikeuser>) findAll(); //getting the users list
        if (bikeUserList.isEmpty())
            return 1;
        
        int currentID; 
        int newID = bikeUserList.get(0).getId();
        
        for(Bikeuser bikeuser: bikeUserList) {
            currentID = bikeuser.getId();
            if(currentID > newID)
                newID = currentID;
        }
        
        return newID + 1;
    }
    
    //Return a user object found by the username
    public Bikeuser findByUsername(String username) {
        EntityManager em = getEntityManager();
        Query query = em.createNamedQuery("Bikeuser.findByUsername", Bikeuser.class);
        query.setParameter("username", username);
        
        try {
            Bikeuser b = (Bikeuser) query.getSingleResult();
            return b;
        } catch(Exception e) {
            System.out.println(e.getLocalizedMessage());
            return null;
        }
    }
    
    //For operational data (take or leave bike, bookings, balance...), no checks needed
    public String editOperationalBikeUser(T entity) {
        Bikeuser b = (Bikeuser) entity;
        getEntityManager().merge(entity);
        return String.format(RESPONSE_OK, b.getEntityid());
    }
    
    //For basic data (username, mail...) checks needed
    public String editBasicBikeUser(T entity) {
        Bikeuser b = (Bikeuser) entity;
        //Check if the username or email are availables
        if(isUsernameUpdatable(b) && isEmailUpdatable(b)) {
            getEntityManager().merge(entity);
            return String.format(RESPONSE_OK, b.getEntityid());
        }
        
        return String.format(RESPONSE_KO, b.getEntityid());
    }
    
    //Check if username is available for update
    private boolean isUsernameUpdatable(Bikeuser bikeuser) {
        EntityManager em = getEntityManager();
        Query query = em.createNamedQuery("Bikeuser.findByUsername", Bikeuser.class);
        query.setParameter("username", bikeuser.getUsername());
        
        try {
            Bikeuser b = (Bikeuser) query.getSingleResult();
            return Objects.equals(b.getId(), bikeuser.getId()); //If username and Id match, its me, so I can update myself
        } catch(Exception e) {
            System.out.println(e.getLocalizedMessage());
            return true;
        }    
    }
   
    //Check if email is available for update
    private boolean isEmailUpdatable(Bikeuser bikeuser) {
        EntityManager em = getEntityManager();
        Query query = em.createNamedQuery("Bikeuser.findByEmail", Bikeuser.class);
        query.setParameter("email", bikeuser.getEmail());
        
        try {
            Bikeuser b = (Bikeuser) query.getSingleResult();
            return Objects.equals(b.getId(), bikeuser.getId()); //If email and Id match, its me, so I can update myself
        } catch(Exception e) {
            System.out.println(e.getLocalizedMessage());
            return true;
        }
    }
    
}
