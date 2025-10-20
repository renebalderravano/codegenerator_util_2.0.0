package [packageName].util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

public abstract class BaseRepository<ENTITY> extends BaseUtil {

	@Autowired
	private SessionFactory sf;

	private Class<ENTITY> entityClass;

	public BaseRepository() {
		Type superClass = getClass().getGenericSuperclass();
		entityClass = (Class<ENTITY>) ((ParameterizedType) superClass).getActualTypeArguments()[0];
	}

	@SuppressWarnings("deprecation")
	public Object save(Object m) {
		Session s = sf.getCurrentSession();
		s.beginTransaction();
		Object entity = getMapperUtil().prepareTo(m, entityClass);
		s.saveOrUpdate(entity);
		s.getTransaction().commit();
		Object w = getMapperUtil().prepareTo(entity,m);
		return w;
	}

	public void update(Object m) {
		Session session = this.sf.getCurrentSession();
		session.update(getMapperUtil().prepareTo(m, entityClass));
	}

	public List<Object> findAll() {
		Session session = this.sf.getCurrentSession();
		EntityManager em = session.getEntityManagerFactory().createEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<ENTITY> q = cb.createQuery(entityClass);
		Root<ENTITY> root = q.from(entityClass);
		q.select(root);
		List l = em.createQuery(q).getResultList();
		List d = (List<Object>) super.prepareListToSendOfRepositoryToService(l);
		return d ;
	}

	public Object findById(Integer id) {
		Session session = this.sf.getCurrentSession();
		ENTITY hl = (ENTITY) session.byId(entityClass).load(id);
		return getMapperUtil().prepareTo(hl, entityClass);
	}

	@SuppressWarnings("deprecation")
	public Boolean delete(Integer id) {
		try {
			Session session = this.sf.getCurrentSession();
			ENTITY hl = session.byId(entityClass).load(id);
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
