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
import com.ec.entidades.Proveedor;
import com.ec.entidades.Rubro;
import java.util.ArrayList;
import java.util.Collection;
import com.ec.entidades.Detalle;
import com.ec.entidades.Factura;
import com.ec.servicios.exceptions.NonexistentEntityException;
import com.ec.servicios.exceptions.PreexistingEntityException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author david
 */
public class FacturaJpaController implements Serializable {

    public FacturaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Factura factura) throws PreexistingEntityException, Exception {
        if (factura.getRubroCollection() == null) {
            factura.setRubroCollection(new ArrayList<Rubro>());
        }
        if (factura.getDetalleCollection() == null) {
            factura.setDetalleCollection(new ArrayList<Detalle>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Proveedor idProveedor = factura.getIdProveedor();
            if (idProveedor != null) {
                idProveedor = em.getReference(idProveedor.getClass(), idProveedor.getIdProveedor());
                factura.setIdProveedor(idProveedor);
            }
            Collection<Rubro> attachedRubroCollection = new ArrayList<Rubro>();
            for (Rubro rubroCollectionRubroToAttach : factura.getRubroCollection()) {
                rubroCollectionRubroToAttach = em.getReference(rubroCollectionRubroToAttach.getClass(), rubroCollectionRubroToAttach.getIdRubro());
                attachedRubroCollection.add(rubroCollectionRubroToAttach);
            }
            factura.setRubroCollection(attachedRubroCollection);
            Collection<Detalle> attachedDetalleCollection = new ArrayList<Detalle>();
            for (Detalle detalleCollectionDetalleToAttach : factura.getDetalleCollection()) {
                detalleCollectionDetalleToAttach = em.getReference(detalleCollectionDetalleToAttach.getClass(), detalleCollectionDetalleToAttach.getIdDetalle());
                attachedDetalleCollection.add(detalleCollectionDetalleToAttach);
            }
            factura.setDetalleCollection(attachedDetalleCollection);
            em.persist(factura);
            if (idProveedor != null) {
                idProveedor.getFacturaCollection().add(factura);
                idProveedor = em.merge(idProveedor);
            }
            for (Rubro rubroCollectionRubro : factura.getRubroCollection()) {
                rubroCollectionRubro.getFacturaCollection().add(factura);
                rubroCollectionRubro = em.merge(rubroCollectionRubro);
            }
            for (Detalle detalleCollectionDetalle : factura.getDetalleCollection()) {
                Factura oldIdFactuOfDetalleCollectionDetalle = detalleCollectionDetalle.getIdFactu();
                detalleCollectionDetalle.setIdFactu(factura);
                detalleCollectionDetalle = em.merge(detalleCollectionDetalle);
                if (oldIdFactuOfDetalleCollectionDetalle != null) {
                    oldIdFactuOfDetalleCollectionDetalle.getDetalleCollection().remove(detalleCollectionDetalle);
                    oldIdFactuOfDetalleCollectionDetalle = em.merge(oldIdFactuOfDetalleCollectionDetalle);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findFactura(factura.getIdFactu()) != null) {
                throw new PreexistingEntityException("Factura " + factura + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Factura factura) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Factura persistentFactura = em.find(Factura.class, factura.getIdFactu());
            Proveedor idProveedorOld = persistentFactura.getIdProveedor();
            Proveedor idProveedorNew = factura.getIdProveedor();
            Collection<Rubro> rubroCollectionOld = persistentFactura.getRubroCollection();
            Collection<Rubro> rubroCollectionNew = factura.getRubroCollection();
            Collection<Detalle> detalleCollectionOld = persistentFactura.getDetalleCollection();
            Collection<Detalle> detalleCollectionNew = factura.getDetalleCollection();
            if (idProveedorNew != null) {
                idProveedorNew = em.getReference(idProveedorNew.getClass(), idProveedorNew.getIdProveedor());
                factura.setIdProveedor(idProveedorNew);
            }
            Collection<Rubro> attachedRubroCollectionNew = new ArrayList<Rubro>();
            for (Rubro rubroCollectionNewRubroToAttach : rubroCollectionNew) {
                rubroCollectionNewRubroToAttach = em.getReference(rubroCollectionNewRubroToAttach.getClass(), rubroCollectionNewRubroToAttach.getIdRubro());
                attachedRubroCollectionNew.add(rubroCollectionNewRubroToAttach);
            }
            rubroCollectionNew = attachedRubroCollectionNew;
            factura.setRubroCollection(rubroCollectionNew);
            Collection<Detalle> attachedDetalleCollectionNew = new ArrayList<Detalle>();
            for (Detalle detalleCollectionNewDetalleToAttach : detalleCollectionNew) {
                detalleCollectionNewDetalleToAttach = em.getReference(detalleCollectionNewDetalleToAttach.getClass(), detalleCollectionNewDetalleToAttach.getIdDetalle());
                attachedDetalleCollectionNew.add(detalleCollectionNewDetalleToAttach);
            }
            detalleCollectionNew = attachedDetalleCollectionNew;
            factura.setDetalleCollection(detalleCollectionNew);
            factura = em.merge(factura);
            if (idProveedorOld != null && !idProveedorOld.equals(idProveedorNew)) {
                idProveedorOld.getFacturaCollection().remove(factura);
                idProveedorOld = em.merge(idProveedorOld);
            }
            if (idProveedorNew != null && !idProveedorNew.equals(idProveedorOld)) {
                idProveedorNew.getFacturaCollection().add(factura);
                idProveedorNew = em.merge(idProveedorNew);
            }
            for (Rubro rubroCollectionOldRubro : rubroCollectionOld) {
                if (!rubroCollectionNew.contains(rubroCollectionOldRubro)) {
                    rubroCollectionOldRubro.getFacturaCollection().remove(factura);
                    rubroCollectionOldRubro = em.merge(rubroCollectionOldRubro);
                }
            }
            for (Rubro rubroCollectionNewRubro : rubroCollectionNew) {
                if (!rubroCollectionOld.contains(rubroCollectionNewRubro)) {
                    rubroCollectionNewRubro.getFacturaCollection().add(factura);
                    rubroCollectionNewRubro = em.merge(rubroCollectionNewRubro);
                }
            }
            for (Detalle detalleCollectionOldDetalle : detalleCollectionOld) {
                if (!detalleCollectionNew.contains(detalleCollectionOldDetalle)) {
                    detalleCollectionOldDetalle.setIdFactu(null);
                    detalleCollectionOldDetalle = em.merge(detalleCollectionOldDetalle);
                }
            }
            for (Detalle detalleCollectionNewDetalle : detalleCollectionNew) {
                if (!detalleCollectionOld.contains(detalleCollectionNewDetalle)) {
                    Factura oldIdFactuOfDetalleCollectionNewDetalle = detalleCollectionNewDetalle.getIdFactu();
                    detalleCollectionNewDetalle.setIdFactu(factura);
                    detalleCollectionNewDetalle = em.merge(detalleCollectionNewDetalle);
                    if (oldIdFactuOfDetalleCollectionNewDetalle != null && !oldIdFactuOfDetalleCollectionNewDetalle.equals(factura)) {
                        oldIdFactuOfDetalleCollectionNewDetalle.getDetalleCollection().remove(detalleCollectionNewDetalle);
                        oldIdFactuOfDetalleCollectionNewDetalle = em.merge(oldIdFactuOfDetalleCollectionNewDetalle);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = factura.getIdFactu();
                if (findFactura(id) == null) {
                    throw new NonexistentEntityException("The factura with id " + id + " no longer exists.");
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
            Factura factura;
            try {
                factura = em.getReference(Factura.class, id);
                factura.getIdFactu();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The factura with id " + id + " no longer exists.", enfe);
            }
            Proveedor idProveedor = factura.getIdProveedor();
            if (idProveedor != null) {
                idProveedor.getFacturaCollection().remove(factura);
                idProveedor = em.merge(idProveedor);
            }
            Collection<Rubro> rubroCollection = factura.getRubroCollection();
            for (Rubro rubroCollectionRubro : rubroCollection) {
                rubroCollectionRubro.getFacturaCollection().remove(factura);
                rubroCollectionRubro = em.merge(rubroCollectionRubro);
            }
            Collection<Detalle> detalleCollection = factura.getDetalleCollection();
            for (Detalle detalleCollectionDetalle : detalleCollection) {
                detalleCollectionDetalle.setIdFactu(null);
                detalleCollectionDetalle = em.merge(detalleCollectionDetalle);
            }
            em.remove(factura);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Factura> findFacturaEntities() {
        return findFacturaEntities(true, -1, -1);
    }

    public List<Factura> findFacturaEntities(int maxResults, int firstResult) {
        return findFacturaEntities(false, maxResults, firstResult);
    }

    private List<Factura> findFacturaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Factura.class));
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

    public Factura findFactura(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Factura.class, id);
        } finally {
            em.close();
        }
    }

    public int getFacturaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Factura> rt = cq.from(Factura.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
