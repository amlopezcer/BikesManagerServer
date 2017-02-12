
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
        Booking booking = (Booking) entity;      
        booking.setId(getNewBookingID());
        getEntityManager().persist(entity);
        return String.format(RESPONSE_OK, booking.getEntityid());
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
    
    public String deleteBookingByUsername(String username, Integer bookingType) {
        EntityManager em = getEntityManager();
        Query query = em.createNamedQuery("Booking.findByUsername", Booking.class);
        query.setParameter("username", username);
        List<Booking> bookingList = query.getResultList(); //The user can have 2 bookings (bike and moorings)
        
        String response = "";
        
        for(Booking currentBooking: bookingList) {
            if(currentBooking.getBooktype() == bookingType) {
                deleteBooking(currentBooking);
                response = String.format(RESPONSE_OK, currentBooking.getEntityid());
            }
        }
        
        return response;
    }
    
    
    /**
     * *****************
     * Custom methods
     * 
     * BikeStation
     * *****************
     */
    
    public List<T> findAllWithoutTimedOutBookings() {
        //Get booking list 
        EntityManager em = getEntityManager();
        Query query = em.createNamedQuery("Booking.findAll", Booking.class);
        List<Booking> bookingList = query.getResultList();
        
        if(!bookingList.isEmpty()) {
            for(Booking currentBooking : bookingList) {
                //Look for timed out bookings
                if(currentBooking.getRemainingBookingTime() <= 0) {
                    //Delete the booking and update station affected
                    deleteBooking(currentBooking);
                    updateStationWithoutTimedOutBookings(currentBooking);
                }
            }
        }
         
        //Standard findAll
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        return getEntityManager().createQuery(cq).getResultList();
    }
    
    private void deleteBooking(Booking booking) {
        EntityManager em = getEntityManager();
        Query query = em.createNamedQuery("Booking.deleteById", Booking.class);
        query.setParameter("id", booking.getId());
        query.executeUpdate(); 
    }
    
    private void updateStationWithoutTimedOutBookings(Booking timedOutBooking) {
        Bikestation bikeStation = findByStationAddress(timedOutBooking.getBookaddress());
                    
        if(timedOutBooking.getBooktype() == Booking.BOOKING_TYPE_BIKE) {
            bikeStation.cancelBikeBooking();
        } else
            bikeStation.cancelMooringsBooking();
        
        EntityManager em = getEntityManager();
        Query query = em.createNamedQuery("Bikestation.updateTimedOutBookings", Bikestation.class);                  
        query.setParameter("reservedmoorings", bikeStation.getReservedmoorings());
        query.setParameter("reservedbikes", bikeStation.getReservedbikes());
        query.setParameter("availablebikes", bikeStation.getAvailablebikes());
        query.setParameter("id", bikeStation.getId());
        query.executeUpdate();  
    }
    
    // Edit for the Bikestation (taking, leaving bikes...)
    public synchronized String editBikeStation(T entity, String operation) {        
        Bikestation bikeStation = (Bikestation) entity;
        
        //Get current DB bikestation
        EntityManager em = getEntityManager();
        Query query = em.createNamedQuery("Bikestation.findById", Bikestation.class);
        query.setParameter("id", bikeStation.getId());       
        Bikestation currenBikeStation = (Bikestation) query.getSingleResult();
 
        if(currenBikeStation.isBikeStationUpdatable(operation)){
            getEntityManager().merge(entity);
            return String.format(RESPONSE_OK, bikeStation.getEntityid());
        }
        
        return String.format(RESPONSE_KO, bikeStation.getEntityid());
    }
    
    public String editBasicBikeStation(T entity) {
        Bikestation bikeStation = (Bikestation) entity;
        getEntityManager().merge(entity);
        return String.format(RESPONSE_OK, bikeStation.getEntityid());
    }
    
    //Return a bike station object found by the address
    public Bikestation findByStationAddress(String address) {
        String addressAux = address.replaceAll("_", " "); //" " come as "_" from the app to avoid issues with urls
        
        EntityManager em = getEntityManager();
        Query query = em.createNamedQuery("Bikestation.findByAddress", Bikestation.class);
        query.setParameter("address", addressAux);
        
        try {
            Bikestation bikeStation = (Bikestation) query.getSingleResult();
            return bikeStation;
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
        Bikeuser bikeUser = (Bikeuser) entity;
        //Check if the username or email are availables
        if(isUsernameAvailable(bikeUser.getUsername()) && isEmailAvailable(bikeUser.getEmail())) {
            bikeUser.setId(getNewUserID());
            getEntityManager().persist(entity);
            return String.format(RESPONSE_OK, bikeUser.getEntityid());
        }
        
        return String.format(RESPONSE_KO, bikeUser.getEntityid());
    }
    
    //Checking if the username is available
    private boolean isUsernameAvailable(String username) {
        EntityManager em = getEntityManager();
        Query query = em.createNamedQuery("Bikeuser.findByUsername", Bikeuser.class);
        query.setParameter("username", username);
        
        try {
            Bikeuser bikeUser = (Bikeuser) query.getSingleResult(); //Needed to generate the following exception
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
    
    //Return a user object found by the username with its timed out bookings cleared
    public Bikeuser findByUsername(String username) {
        EntityManager em = getEntityManager();
        Query query = em.createNamedQuery("Bikeuser.findByUsername", Bikeuser.class);
        query.setParameter("username", username);
        
        try {
            Bikeuser bikeUser = (Bikeuser) query.getSingleResult();
            boolean updateUser = false;
            if(bikeUser.isBikeBookingTimedOut()) {
                bikeUser.cancelBikeBooking();
                updateUser = true;
            }

            if(bikeUser.isMooringsBookingTimedOut()) {
                bikeUser.cancelMooringsBooking();
                updateUser = true;
            }

            if(updateUser) {
                query = em.createNamedQuery("Bikeuser.updateTimedOutBookings", Bikeuser.class);                  
                query.setParameter("booktaken", bikeUser.getBooktaken());
                query.setParameter("mooringstaken", bikeUser.getMooringstaken());
                query.setParameter("id", bikeUser.getId());
                query.executeUpdate();
            }

            return bikeUser;
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            return null;
        }
    }
    
    //For operational data (take or leave bike, bookings, balance...), no checks needed
    public String editOperationalBikeUser(T entity) {
        Bikeuser bikeUser = (Bikeuser) entity;
        getEntityManager().merge(entity);
        return String.format(RESPONSE_OK, bikeUser.getEntityid());
    }
    
    //For basic data (username, mail...) checks needed
    public String editBasicBikeUser(T entity) {
        Bikeuser bikeUser = (Bikeuser) entity;
        //Check if the username or email are availables
        if(isUsernameUpdatable(bikeUser) && isEmailUpdatable(bikeUser)) {
            getEntityManager().merge(entity);
            return String.format(RESPONSE_OK, bikeUser.getEntityid());
        }
        
        return String.format(RESPONSE_KO, bikeUser.getEntityid());
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
    
    public String removeUser(T entity) {
        getEntityManager().remove(getEntityManager().merge(entity));
  
        Bikeuser bikeUser = (Bikeuser) entity;
        return String.format(RESPONSE_OK, bikeUser.getEntityid());
    }
    
}
