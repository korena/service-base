
package com.siphyc.dao;


import com.siphyc.dao.exceptions.NonexistentEntityException;
import com.siphyc.dao.exceptions.RollbackFailureException;
import com.siphyc.model.Android;
import java.io.Serializable;
import java.util.List;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;


/**
 *
 * @author korena
 */
public class AndroidJpaController implements Serializable {

    @Inject
    public AndroidJpaController() {
        this.emf = JPAcommonUtils.getFacory();
    }
   
     private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Android android) throws RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(android);
            em.getTransaction().commit();
        } catch (SecurityException | IllegalStateException ex) {
            try {
                em.getTransaction().rollback();
            } catch (IllegalStateException | SecurityException | NullPointerException re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Android android) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            android = em.merge(android);
            em.getTransaction().commit();
        } catch (SecurityException | IllegalStateException ex) {
            try {
                em.getTransaction().rollback();
            } catch (IllegalStateException | SecurityException | NullPointerException re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = android.getId();
                if (findAndroid(id) == null) {
                    throw new NonexistentEntityException("The android with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Android android;
            try {
                android = em.getReference(Android.class, id);
                android.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The android with id " + id + " no longer exists.", enfe);
            }
            em.remove(android);
            em.getTransaction().commit();
        } catch (NonexistentEntityException | SecurityException | IllegalStateException ex) {
            try {
                em.getTransaction().rollback();
            } catch (IllegalStateException | SecurityException | NullPointerException re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Android> findAndroidEntities() {
        return findAndroidEntities(true, -1, -1);
    }

    public List<Android> findAndroidEntities(int maxResults, int firstResult) {
        return findAndroidEntities(false, maxResults, firstResult);
    }

    private List<Android> findAndroidEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Android.class));
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

    public Android findAndroid(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Android.class, id);
        } finally {
            em.close();
        }
    }

    public int getAndroidCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Android> rt = cq.from(Android.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
