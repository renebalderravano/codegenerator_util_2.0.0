package [packageName].util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.digiret.infrastructure.adapters.output.persistence.entity.UsuarioEntity;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.AbstractQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
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
		List d =  null;
		try {
			List l = getEm().createQuery(getCriteriaQuery(getCriteriaBuilder())).getResultList();
			d = (List<Object>) super.prepareListToSendOfRepositoryToService(l);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
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
	
	public List<Object> findBy(Map<String, Object> src) {
		
		cb = getCriteriaBuilder();
		CriteriaQuery<ENTITY> ctr = getCriteriaQuery(cb);
		Root<ENTITY> root =  getRoot(ctr);
		
		List<Predicate> predicates = new ArrayList<>();
		for (Map.Entry<String, Object> field : src.entrySet()) {
			String key = field.getKey();
			Object val = field.getValue();
			
			 predicates.add(cb.equal(root.get(key), val));
			
		}
	    ctr.select(root).where(predicates.toArray(new Predicate[0]));
	
		List<Object> l = (List<Object>) getEm().createQuery(ctr).getResultList();
		return (List<Object>) super.prepareListToSendOfRepositoryToService(l);
	}

	public SessionFactory getSf() {
		return sf;
	}
	
	private EntityManager em;
	private Root<ENTITY> root;
	private CriteriaBuilder cb;
	
	protected EntityManager getEm() {
		Session session = this.sf.getCurrentSession();
		return session.getEntityManagerFactory().createEntityManager();
	}
	
	protected CriteriaQuery<ENTITY> getCriteriaQuery(CriteriaBuilder cb ) {
		CriteriaQuery<ENTITY> q = cb.createQuery(entityClass);
		getRoot(q);
//		q.select(root);
		return q;
	}
	
	protected Root<ENTITY> getRoot(CriteriaQuery<ENTITY> q) {
		Root<ENTITY> root = q.from(entityClass);
		q.select(root);
		return root;
	}

	protected CriteriaBuilder getCriteriaBuilder() {
		return getEm().getCriteriaBuilder();
	}

	protected void setCb(CriteriaBuilder cb) {
		this.cb = cb;
	}
}
