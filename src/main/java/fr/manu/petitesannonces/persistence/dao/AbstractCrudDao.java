/**
 * 
 */
package fr.manu.petitesannonces.persistence.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnitUtil;

/**
 * @author Manu
 *
 */
public abstract class AbstractCrudDao<PrimaryKey extends Serializable, T> {

	private final Class<T> persistentClass;

    private PersistenceUnitUtil persistenceUnitUtil;

//    @Autowired
//    private SessionFactory sessionFactory;
	
	@PersistenceContext
    private EntityManager entityManager;

    public AbstractCrudDao() {
        this.persistentClass = (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[1];
    }
    
    @PostConstruct
    public void init() {
        persistenceUnitUtil = entityManager.getEntityManagerFactory().getPersistenceUnitUtil();
    }
     
//    protected Session getSession(){
//        return sessionFactory.getCurrentSession();
//    }
    
    protected EntityManager getEntityManager() {
		return entityManager;
	}
 
    public T getByKey(PrimaryKey key) {
//        return (T) getSession().get(persistentClass, key);
    	return entityManager.find(persistentClass, key);
    }
 
	public void persist(T entity) {
//        getSession().persist(entity);
		entityManager.persist(entity);
    }
    
    public T merge(T entity) {
//    	return (T) getSession().merge(entity);
    	return entityManager.merge(entity);
    }
 
    public void delete(T entity) {
//        getSession().delete(entity);
    	entityManager.remove(entity);
    }
     
//    protected Criteria createEntityCriteria(){
//        return getSession().createCriteria(persistentClass);
//    }
}
