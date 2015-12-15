/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.servicios;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.ec.entidades.DetalleRubro;
import com.ec.entidades.Factura;
import com.ec.entidades.Rubro;
import com.ec.servicios.exceptions.NonexistentEntityException;
import com.ec.servicios.exceptions.PreexistingEntityException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author david
 */
public class RubroJpaController implements Serializable {

    public RubroJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Rubro rubro) throws PreexistingEntityException, Exception {
        if (rubro.getFacturaCollection() == null) {
            rubro.setFacturaCollection(new ArrayList<Factura>());
        }
        if (rubro.getDetalleRubroCollection() == null) {
            rubro.setDetalleRubroCollection(new ArrayList<DetalleRubro>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            DetalleRubro idDetalleRubro = rubro.getIdDetalleRubro();
            if (idDetalleRubro != null) {
                idDetalleRubro = em.getReference(idDetalleRubro.getClass(), idDetalleRubro.getIdDetalleRubro());
                rubro.setIdDetalleRubro(idDetalleRubro);
            }
            Collection<Factura> attachedFacturaCollection = new ArrayList<Factura>();
            for (Factura facturaCollectionFacturaToAttach : rubro.getFacturaCollection()) {
                facturaCollectionFacturaToAttach = em.getReference(facturaCollectionFacturaToAttach.getClass(), facturaCollectionFacturaToAttach.getIdFactu());
                attachedFacturaCollection.add(facturaCollectionFacturaToAttach);
            }
            rubro.setFacturaCollection(attachedFacturaCollection);
            Collection<DetalleRubro> attachedDetalleRubroCollection = new ArrayList<DetalleRubro>();
            for (DetalleRubro detalleRubroCollectionDetalleRubroToAttach : rubro.getDetalleRubroCollection()) {
                detalleRubroCollectionDetalleRubroToAttach = em.getReference(detalleRubroCollectionDetalleRubroToAttach.getClass(), detalleRubroCollectionDetalleRubroToAttach.getIdDetalleRubro());
                attachedDetalleRubroCollection.add(detalleRubroCollectionDetalleRubroToAttach);
            }
            rubro.setDetalleRubroCollection(attachedDetalleRubroCollection);
            em.persist(rubro);
            if (idDetalleRubro != null) {
                idDetalleRubro.getRubroCollection().add(rubro);
                idDetalleRubro = em.merge(idDetalleRubro);
            }
            for (Factura facturaCollectionFactura : rubro.getFacturaCollection()) {
                facturaCollectionFactura.getRubroCollection().add(rubro);
                facturaCollectionFactura = em.merge(facturaCollectionFactura);
            }
            for (DetalleRubro detalleRubroCollectionDetalleRubro : rubro.getDetalleRubroCollection()) {
                Rubro oldIdRubroOfDetalleRubroCollectionDetalleRubro = detalleRubroCollectionDetalleRubro.getIdRubro();
                detalleRubroCollectionDetalleRubro.setIdRubro(rubro);
                detalleRubroCollectionDetalleRubro = em.merge(detalleRubroCollectionDetalleRubro);
                if (oldIdRubroOfDetalleRubroCollectionDetalleRubro != null) {
                    oldIdRubroOfDetalleRubroCollectionDetalleRubro.getDetalleRubroCollection().remove(detalleRubroCollectionDetalleRubro);
                    oldIdRubroOfDetalleRubroCollectionDetalleRubro = em.merge(oldIdRubroOfDetalleRubroCollectionDetalleRubro);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findRubro(rubro.getIdRubro()) != null) {
                throw new PreexistingEntityException("Rubro " + rubro + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Rubro rubro) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Rubro persistentRubro = em.find(Rubro.class, rubro.getIdRubro());
            DetalleRubro idDetalleRubroOld = persistentRubro.getIdDetalleRubro();
            DetalleRubro idDetalleRubroNew = rubro.getIdDetalleRubro();
            Collection<Factura> facturaCollectionOld = persistentRubro.getFacturaCollection();
            Collection<Factura> facturaCollectionNew = rubro.getFacturaCollection();
            Collection<DetalleRubro> detalleRubroCollectionOld = persistentRubro.getDetalleRubroCollection();
            Collection<DetalleRubro> detalleRubroCollectionNew = rubro.getDetalleRubroCollection();
            if (idDetalleRubroNew != null) {
                idDetalleRubroNew = em.getReference(idDetalleRubroNew.getClass(), idDetalleRubroNew.getIdDetalleRubro());
                rubro.setIdDetalleRubro(idDetalleRubroNew);
            }
            Collection<Factura> attachedFacturaCollectionNew = new ArrayList<Factura>();
            for (Factura facturaCollectionNewFacturaToAttach : facturaCollectionNew) {
                facturaCollectionNewFacturaToAttach = em.getReference(facturaCollectionNewFacturaToAttach.getClass(), facturaCollectionNewFacturaToAttach.getIdFactu());
                attachedFacturaCollectionNew.add(facturaCollectionNewFacturaToAttach);
            }
            facturaCollectionNew = attachedFacturaCollectionNew;
            rubro.setFacturaCollection(facturaCollectionNew);
            Collection<DetalleRubro> attachedDetalleRubroCollectionNew = new ArrayList<DetalleRubro>();
            for (DetalleRubro detalleRubroCollectionNewDetalleRubroToAttach : detalleRubroCollectionNew) {
                detalleRubroCollectionNewDetalleRubroToAttach = em.getReference(detalleRubroCollectionNewDetalleRubroToAttach.getClass(), detalleRubroCollectionNewDetalleRubroToAttach.getIdDetalleRubro());
                attachedDetalleRubroCollectionNew.add(detalleRubroCollectionNewDetalleRubroToAttach);
            }
            detalleRubroCollectionNew = attachedDetalleRubroCollectionNew;
            rubro.setDetalleRubroCollection(detalleRubroCollectionNew);
            rubro = em.merge(rubro);
            if (idDetalleRubroOld != null && !idDetalleRubroOld.equals(idDetalleRubroNew)) {
                idDetalleRubroOld.getRubroCollection().remove(rubro);
                idDetalleRubroOld = em.merge(idDetalleRubroOld);
            }
            if (idDetalleRubroNew != null && !idDetalleRubroNew.equals(idDetalleRubroOld)) {
                idDetalleRubroNew.getRubroCollection().add(rubro);
                idDetalleRubroNew = em.merge(idDetalleRubroNew);
            }
            for (Factura facturaCollectionOldFactura : facturaCollectionOld) {
                if (!facturaCollectionNew.contains(facturaCollectionOldFactura)) {
                    facturaCollectionOldFactura.getRubroCollection().remove(rubro);
                    facturaCollectionOldFactura = em.merge(facturaCollectionOldFactura);
                }
            }
            for (Factura facturaCollectionNewFactura : facturaCollectionNew) {
                if (!facturaCollectionOld.contains(facturaCollectionNewFactura)) {
                    facturaCollectionNewFactura.getRubroCollection().add(rubro);
                    facturaCollectionNewFactura = em.merge(facturaCollectionNewFactura);
                }
            }
            for (DetalleRubro detalleRubroCollectionOldDetalleRubro : detalleRubroCollectionOld) {
                if (!detalleRubroCollectionNew.contains(detalleRubroCollectionOldDetalleRubro)) {
                    detalleRubroCollectionOldDetalleRubro.setIdRubro(null);
                    detalleRubroCollectionOldDetalleRubro = em.merge(detalleRubroCollectionOldDetalleRubro);
                }
            }
            for (DetalleRubro detalleRubroCollectionNewDetalleRubro : detalleRubroCollectionNew) {
                if (!detalleRubroCollectionOld.contains(detalleRubroCollectionNewDetalleRubro)) {
                    Rubro oldIdRubroOfDetalleRubroCollectionNewDetalleRubro = detalleRubroCollectionNewDetalleRubro.getIdRubro();
                    detalleRubroCollectionNewDetalleRubro.setIdRubro(rubro);
                    detalleRubroCollectionNewDetalleRubro = em.merge(detalleRubroCollectionNewDetalleRubro);
                    if (oldIdRubroOfDetalleRubroCollectionNewDetalleRubro != null && !oldIdRubroOfDetalleRubroCollectionNewDetalleRubro.equals(rubro)) {
                        oldIdRubroOfDetalleRubroCollectionNewDetalleRubro.getDetalleRubroCollection().remove(detalleRubroCollectionNewDetalleRubro);
                        oldIdRubroOfDetalleRubroCollectionNewDetalleRubro = em.merge(oldIdRubroOfDetalleRubroCollectionNewDetalleRubro);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = rubro.getIdRubro();
                if (findRubro(id) == null) {
                    throw new NonexistentEntityException("The rubro with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Rubro rubro;
            try {
                rubro = em.getReference(Rubro.class, id);
                rubro.getIdRubro();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The rubro with id " + id + " no longer exists.", enfe);
            }
            DetalleRubro idDetalleRubro = rubro.getIdDetalleRubro();
            if (idDetalleRubro != null) {
                idDetalleRubro.getRubroCollection().remove(rubro);
                idDetalleRubro = em.merge(idDetalleRubro);
            }
            Collection<Factura> facturaCollection = rubro.getFacturaCollection();
            for (Factura facturaCollectionFactura : facturaCollection) {
                facturaCollectionFactura.getRubroCollection().remove(rubro);
                facturaCollectionFactura = em.merge(facturaCollectionFactura);
            }
            Collection<DetalleRubro> detalleRubroCollection = rubro.getDetalleRubroCollection();
            for (DetalleRubro detalleRubroCollectionDetalleRubro : detalleRubroCollection) {
                detalleRubroCollectionDetalleRubro.setIdRubro(null);
                detalleRubroCollectionDetalleRubro = em.merge(detalleRubroCollectionDetalleRubro);
            }
            em.remove(rubro);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Rubro> findRubroEntities() {
        return findRubroEntities(true, -1, -1);
    }

    public List<Rubro> findRubroEntities(int maxResults, int firstResult) {
        return findRubroEntities(false, maxResults, firstResult);
    }

    private List<Rubro> findRubroEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Rubro.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Rubro findRubro(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Rubro.class, id);
        } finally {
            em.close();
        }
    }

    public int getRubroCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Rubro> rt = cq.from(Rubro.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
