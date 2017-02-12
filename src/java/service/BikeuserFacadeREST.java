/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import entities.Bikeuser;
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
@Path("entities.bikeuser")
public class BikeuserFacadeREST extends AbstractFacade<Bikeuser> {

    @PersistenceContext(unitName = "BikesManagerPU")
    private EntityManager em;

    public BikeuserFacadeREST() {
        super(Bikeuser.class);
    }

    @POST
    @Override
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public synchronized String createNewUser(Bikeuser entity) {
        return super.createNewUser(entity); //Custom creator for the assignment of a new server ID
    }

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String edit(@PathParam("id") Integer id, Bikeuser entity) {
        return super.editOperationalBikeUser(entity); //Edit for operational operations (take or leave bike, deposit money...)
    }
    
    @PUT
    @Path("basicdata/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String editBasic(@PathParam("id") Integer id, Bikeuser entity) {
        return super.editBasicBikeUser(entity); //For basic data (username, mail...)
    }
    
    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String remove(@PathParam("id") Integer id) {
        return super.removeUser(super.find(id)); //Custom removal method to return a string with the Bikeuser instance
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Bikeuser find(@PathParam("id") Integer id) {
        return super.find(id);
    }
    
    //A custom find by username GET
    @GET
    @Override
    @Path("user/{username}")
    @Produces(MediaType.APPLICATION_JSON)
    public Bikeuser findByUsername(@PathParam("username") String username) {
        return super.findByUsername(username);
    }
    
    @GET
    @Override
    @Produces(MediaType.APPLICATION_JSON)
    public List<Bikeuser> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Bikeuser> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
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
