
package service;

import entities.Bikestation;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


@Stateless
@Path("entities.bikestation")
public class BikestationFacadeREST extends AbstractFacade<Bikestation> {

    @PersistenceContext(unitName = "BikesManagerPU")
    private EntityManager em;

    public BikestationFacadeREST() {
        super(Bikestation.class);
    }

    @POST
    @Override
    @Consumes(MediaType.APPLICATION_JSON)
    public void create(Bikestation entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String edit(@PathParam("id") Integer id, Bikestation entity) {
        return super.editBasicBikeStation(entity);
    }
    
    @PUT
    @Path("{operation}/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public synchronized String edit(@PathParam("id") Integer id, @PathParam("operation") String op, Bikestation entity) {
        return super.editBikeStation(entity, op); //Custom editor to distinguish between taking and leaving bikes
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Integer id) {
        super.remove(super.find(id));
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Bikestation find(@PathParam("id") Integer id) {
        return super.find(id);
    }
    
    //A custom find by address
    @GET
    @Override
    @Path("stationAddress/{address}")
    @Produces(MediaType.APPLICATION_JSON)
    public Bikestation findByStationAddress(@PathParam("address") String address) {
        return super.findByStationAddress(address);
    }

    @GET
    @Override
    @Produces(MediaType.APPLICATION_JSON)
    public List<Bikestation> findAll() {
        //return super.findAll();
        return super.findAllWithoutTimedOutBookings();
    }

    @GET
    @Path("{from}/{to}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Bikestation> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(super.count());
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
}
