package [packageName].util;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.security.KeyRep;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaBuilder.In;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.metamodel.SingularAttribute;

public abstract class BaseDAO<ENTITY> extends BaseUtil {

	@Autowired
	private SessionFactory sf;
	
	private Class<ENTITY> entityClass;

	public BaseDAO() {
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
		Object w = super.prepareSendOfRepositoryToService(entity);
		return w;
	}
	
	@SuppressWarnings("deprecation")
	public Object save(List<Object> m) {
		Session s = sf.getCurrentSession();
		s.beginTransaction();
		List<Object> entity = getMapperUtil().prepareTo(m, entityClass);
		for (Object obj : entity) {

			s.saveOrUpdate(obj);	
		}
		s.getTransaction().commit();
		m=  (List<Object>) super.prepareListToSendOfRepositoryToService(entity);
		return m;
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

		return super.prepareSendOfRepositoryToService(hl);
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
	
	public List<Object> findBy(Map<String, Object> src, Class<?> returnPojoType) {
		// TODO Auto-generated method stub
		return null;
	} 
	
	public List<Object> findBy(Map<String, Object> src) {
		cb = getCriteriaBuilder();
		CriteriaQuery<ENTITY> ctr = getCriteriaQuery(cb);
		Root<ENTITY> root =  getRoot(ctr);
		
		List<Predicate> predicates = new ArrayList<>();
		for (Map.Entry<String, Object> field : src.entrySet()) {
			String key = field.getKey();
			Object val = field.getValue();
			
			if(val instanceof LinkedHashMap) {
				try {
					Field fieldClass = entityClass.getDeclaredField(key);
					
					Object ref = getReflectUtil().getInstanceByClassName(fieldClass.getType().getName());
					Join<ENTITY, Object> join = createJoin(root, key, JoinType.INNER);
					Map<String, Object> fieldRef = (Map<String, Object>) val;
					
					for (Map.Entry<String, Object> fieldAux : fieldRef.entrySet()) {
						String keyRef = fieldAux.getKey();
						Object valRef = fieldAux.getValue();
						predicates.add(cb.equal(join.get(keyRef), valRef));
					}
				} catch (NoSuchFieldException | SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} 
			else if(val instanceof List) {
				Join<ENTITY, Object> join = createJoin(root, key, JoinType.INNER);
				List fieldRef = (List) val;
				List ids = new ArrayList<>();
				String fieldJoin = "";
				for (Object fieldAux : fieldRef) {
					Map<String, Object> data = (Map<String, Object>) fieldAux;
					for (Map.Entry<String, Object> fieldAux2 : data.entrySet()) {
						String keyRef = fieldAux2.getKey();
						Object valRef = fieldAux2.getValue();
						fieldJoin = keyRef;
						ids.add(valRef);
					}
				}
				
				CriteriaBuilder.In<?> inClasule = (In<?>) join.get(fieldJoin).in(ids);
				predicates.add(inClasule);
			}
			else {

				predicates.add(cb.equal(root.get(key), val));
			}
		}
	    ctr.select(root).where(predicates.toArray(new Predicate[0]));
	
		List<Object> l = (List<Object>) getEm().createQuery(ctr).getResultList();
		List<Object> res = (List<Object>) super.prepareListToSendOfRepositoryToService(l);
		return res;
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
		root = q.from(entityClass);
		q.select(root);
		return root;
	}

	protected CriteriaBuilder getCriteriaBuilder() {
		return getEm().getCriteriaBuilder();
	}

	protected void setCb(CriteriaBuilder cb) {
		this.cb = cb;
	}
	
	protected Join<ENTITY, Object> createJoin(Root<ENTITY> root, String referenceTableName,  JoinType joinType) {
		Join<ENTITY, Object> join = (Join<ENTITY, Object>) root.fetch(referenceTableName, joinType);		
		return join;
	}
	
	protected Predicate addConditions(Path<Object> pathField, String field, Object value, String condicitionalOperator) {
		
		
		switch (condicitionalOperator) {
		case "=":
			return cb.equal(pathField, value);
		case ">":
			return cb.greaterThan(root.get(""), (Expression<? extends String>) value);
		case "<":
			return cb.equal(pathField, value);
		case ">=":
			return cb.equal(pathField, value);
		case "<=":
			return cb.lessThanOrEqualTo(root.get(""), (Expression<? extends String>) value);
		case "<>":
			return cb.notEqual(pathField, value);

		default:
			return null;
		}
		
		
	}
	
	
}