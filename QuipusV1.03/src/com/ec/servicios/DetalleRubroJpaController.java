/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.servicios;

import com.ec.entidades.DetalleRubro;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
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
public class DetalleRubroJpaController implements Serializable {

    public DetalleRubroJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(DetalleRubro detalleRubro) throws PreexistingEntityException, Exception {
        if (detalleRubro.getRubroCollection() == null) {
            detalleRubro.setRubroCollection(new ArrayList<Rubro>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Rubro idRubro = detalleRubro.getIdRubro();
            if (idRubro != null) {
                idRubro = em.getReference(idRubro.getClass(), idRubro.getIdRubro());
                detalleRubro.setIdRubro(idRubro);
            }
            Collection<Rubro> attachedRubroCollection = new ArrayList<Rubro>();
            for (Rubro rubroCollectionRubroToAttach : detalleRubro.getRubroCollection()) {
                rubroCollectionRubroToAttach = em.getReference(rubroCollectionRubroToAttach.getClass(), rubroCollectionRubroToAttach.getIdRubro());
                attachedRubroCollection.add(rubroCollectionRubroToAttach);
            }
            detalleRubro.setRubroCollection(attachedRubroCollection);
            em.persist(detalleRubro);
            if (idRubro != null) {
                DetalleRubro oldIdDetalleRubroOfIdRubro = idRubro.getIdDetalleRubro();
                if (oldIdDetalleRubroOfIdRubro != null) {
                    oldIdDetalleRubroOfIdRubro.setIdRubro(null);
                    oldIdDetalleRubroOfIdRubro = em.merge(oldIdDetalleRubroOfIdRubro);
                }
                idRubro.setIdDetalleRubro(detalleRubro);
                idRubro = em.merge(idRubro);
            }
            for (Rubro rubroCollectionRubro : detalleRubro.getRubroCollection()) {
                DetalleRubro oldIdDetalleRubroOfRubroCollectionRubro = rubroCollectionRubro.getIdDetalleRubro();
                rubroCollectionRubro.setIdDetalleRubro(detalleRubro);
                rubroCollectionRubro = em.merge(rubroCollectionRubro);
                if (oldIdDetalleRubroOfRubroCollectionRubro != null) {
                    oldIdDetalleRubroOfRubroCollectionRubro.getRubroCollection().remove(rubroCollectionRubro);
                    oldIdDetalleRubroOfRubroCollectionRubro = em.merge(oldIdDetalleRubroOfRubroCollectionRubro);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findDetalleRubro(detalleRubro.getIdDetalleRubro()) != null) {
                throw new PreexistingEntityException("DetalleRubro " + detalleRubro + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(DetalleRubro detalleRubro) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            DetalleRubro persistentDetalleRubro = em.find(DetalleRubro.class, detalleRubro.getIdDetalleRubro());
            Rubro idRubroOld = persistentDetalleRubro.getIdRubro();
            Rubro idRubroNew = detalleRubro.getIdRubro();
            Collection<Rubro> rubroCollectionOld = persistentDetalleRubro.getRubroCollection();
            Collection<Rubro> rubroCollectionNew = detalleRubro.getRubroCollection();
            if (idRubroNew != null) {
                idRubroNew = em.getReference(idRubroNew.getClass(), idRubroNew.getIdRubro());
                detalleRubro.setIdRubro(idRubroNew);
            }
            Collection<Rubro> attachedRubroCollectionNew = new ArrayList<Rubro>();
            for (Rubro rubroCollectionNewRubroToAttach : rubroCollectionNew) {
                rubroCollectionNewRubroToAttach = em.getReference(rubroCollectionNewRubroToAttach.getClass(), rubroCollectionNewRubroToAttach.getIdRubro());
                attachedRubroCollectionNew.add(rubroCollectionNewRubroToAttach);
            }
            rubroCollectionNew = attachedRubroCollectionNew;
            detalleRubro.setRubroCollection(rubroCollectionNew);
            detalleRubro = em.merge(detalleRubro);
            if (idRubroOld != null && !idRubroOld.equals(idRubroNew)) {
                idRubroOld.setIdDetalleRubro(null);
                idRubroOld = em.merge(idRubroOld);
            }
            if (idRubroNew != null && !idRubroNew.equals(idRubroOld)) {
                DetalleRubro oldIdDetalleRubroOfIdRubro = idRubroNew.getIdDetalleRubro();
                if (oldIdDetalleRubroOfIdRubro != null) {
                    oldIdDetalleRubroOfIdRubro.setIdRubro(null);
                    oldIdDetalleRubroOfIdRubro = em.merge(oldIdDetalleRubroOfIdRubro);
                }
                idRubroNew.setIdDetalleRubro(detalleRubro);
                idRubroNew = em.merge(idRubroNew);
            }
            for (Rubro rubroCollectionOldRubro : rubroCollectionOld) {
                if (!rubroCollectionNew.contains(rubroCollectionOldRubro)) {
                    rubroCollectionOldRubro.setIdDetalleRubro(null);
                    rubroCollectionOldRubro = em.merge(rubroCollectionOldRubro);
                }
            }
            for (Rubro rubroCollectionNewRubro : rubroCollectionNew) {
                if (!rubroCollectionOld.contains(rubroCollectionNewRubro)) {
                    DetalleRubro oldIdDetalleRubroOfRubroCollectionNewRubro = rubroCollectionNewRubro.getIdDetalleRubro();
                    rubroCollectionNewRubro.setIdDetalleRubro(detalleRubro);
                    rubroCollectionNewRubro = em.merge(rubroCollectionNewRubro);
                    if (oldIdDetalleRubroOfRubroCollectionNewRubro != null && !oldIdDetalleRubroOfRubroCollectionNewRubro.equals(detalleRubro)) {
                        oldIdDetalleRubroOfRubroCollectionNewRubro.getRubroCollection().remove(rubroCollectionNewRubro);
                        oldIdDetalleRubroOfRubroCollectionNewRubro = em.merge(oldIdDetalleRubroOfRubroCollectionNewRubro);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = detalleRubro.getIdDetalleRubro();
                if (findDetalleRubro(id) == null) {
                    throw new NonexistentEntityException("The detalleRubro with id " + id + " no longer exists.");
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
            DetalleRubro detalleRubro;
            try {
                detalleRubro = em.getReference(DetalleRubro.class, id);
                detalleRubro.getIdDetalleRubro();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The detalleRubro with id " + id + " no longer exists.", enfe);
            }
            Rubro idRubro = detalleRubro.getIdRubro();
            if (idRubro != null) {
                idRubro.setIdDetalleRubro(null);
                idRubro = em.merge(idRubro);
            }
            Collection<Rubro> rubroCollection = detalleRubro.getRubroCollection();
            for (Rubro rubroCollectionRubro : rubroCollection) {
                rubroCollectionRubro.setIdDetalleRubro(null);
                rubroCollectionRubro = em.merge(rubroCollectionRubro);
            }
            em.remove(detalleRubro);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<DetalleRubro> findDetalleRubroEntities() {
        return findDetalleRubroEntities(true, -1, -1);
    }

    public List<DetalleRubro> findDetalleRubroEntities(int maxResults, int firstResult) {
        return findDetalleRubroEntities(false, maxResults, firstResult);
    }

    private List<DetalleRubro> findDetalleRubroEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(DetalleRubro.class));
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

    public DetalleRubro findDetalleRubro(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(DetalleRubro.class, id);
        } finally {
            em.close();
        }
    }

    public int getDetalleRubroCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<DetalleRubro> rt = cq.from(DetalleRubro.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
