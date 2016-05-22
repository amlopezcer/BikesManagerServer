
package service;

import entities.Bikestation;
import entities.Bikeuser;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;


public abstract class AbstractFacade<T> {

    //Standard server responses (used in customiza PUT and POST methods)
    private final String RESPONSE_OK = "SERVER_OK";
    private final String RESPONSE_KO = "SERVER_KO";
    
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
     * Customize methods
     */
    
    // Edit for the Bikestation (taking and leaving bikes)
    public String editBikeStation(T entity, Integer id, String op) {
        /**
         * Same string as in the app, to confirm the op which is being processed, 
         * no need of a "leave" one because it is an if - else staetment
         */
        final String OP_TAKE = "take";

        //Getting available bikes for the ID requested
        EntityManager em = getEntityManager();
        Query query = em.createNamedQuery("Bikestation.getAvailable", Bikestation.class);
        query.setParameter("id", id);
        int available = (int)query.getSingleResult();
        
        boolean isStatusOk;
        
        if(op.equals(OP_TAKE)) //If I'm taking a bike, I need at least 1 available
            isStatusOk = available > 0;
        else { //leaving bike           
            query = em.createNamedQuery("Bikestation.getTotal", Bikestation.class);
            query.setParameter("id", id);
            int total = (int)query.getSingleResult();
            
            query = em.createNamedQuery("Bikestation.getBroken", Bikestation.class);
            query.setParameter("id", id);
            int broken = (int)query.getSingleResult();
            
            query = em.createNamedQuery("Bikestation.getReserved", Bikestation.class);
            query.setParameter("id", id);
            int reserved = (int)query.getSingleResult();
            
            isStatusOk = total > available + broken + reserved;
        }
        
        if(isStatusOk) {
            getEntityManager().merge(entity);
            return RESPONSE_OK;
        }
        
        return RESPONSE_KO;
    }
    
    //Custom user creator (just to assign a correct user server ID)
    public String createNewUser(T entity) { 
        Bikeuser bikeuser = (Bikeuser) entity;
        //Check if the username or email are availables
        if(isUsernameAvailable(bikeuser.getUsername()) && isEmailAvailable(bikeuser.getEmail())) {
            bikeuser.setId(getNewUserID());
            getEntityManager().persist(entity);
            return RESPONSE_OK;
        }
        
        return RESPONSE_KO;
    }
    
    //Checking if the username is available
    private boolean isUsernameAvailable(String username) {
        EntityManager em = getEntityManager();
        Query query = em.createNamedQuery("Bikeuser.findByUsername", Bikeuser.class);
        query.setParameter("username", username);
        
        try {
            Bikeuser b = (Bikeuser) query.getSingleResult();
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
            Bikeuser b = (Bikeuser) query.getSingleResult();
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
    
    public String editBikeUser(T entity) {
        getEntityManager().merge(entity);
        return RESPONSE_OK;
    }
    
}
