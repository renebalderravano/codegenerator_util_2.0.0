package [packageName].util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public abstract class BaseRepository<T> {

	@Autowired
	private SessionFactory sf;

	private Class<T> entityClass;

	public BaseRepository() {
		Type superClass = getClass().getGenericSuperclass();
		Type type = ((ParameterizedType) superClass).getActualTypeArguments()[0];
		entityClass = (Class<T>) type;
	}

	@SuppressWarnings("deprecation")
	public T save(T t) {
		Session s = sf.getCurrentSession();
		s.beginTransaction();		
		s.saveOrUpdate(t);		
		s.getTransaction().commit();
		return t;
	}

	public void update(T housingLocation) {
		Session session = this.sf.getCurrentSession();
		session.update(housingLocation);
	}

	public List<T> findAll() {
		Session session = this.sf.getCurrentSession();
		EntityManager em = session.getEntityManagerFactory().createEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<T> q = cb.createQuery(entityClass);
		Root<T> root = q.from(entityClass);
		q.select(root);
		List<T> l = em.createQuery(q).getResultList();
		return l;
	}

	public T findById(Integer id) {
		Session session = this.sf.getCurrentSession();
		T hl = (T) session.byId(entityClass).load(id);
		return hl;
	}

	@SuppressWarnings("deprecation")
	public Boolean delete(Integer id) {
		try {
			Session session = this.sf.getCurrentSession();
			T hl = (T) session.byId(entityClass).load(id);
			session.beginTransaction();	
			session.delete(hl);
			session.getTransaction().commit();
		} catch (Exception e) {
			return false;
		}		
		return true;
	}
	
	public SessionFactory getSf() {
		return sf;
	}

}
