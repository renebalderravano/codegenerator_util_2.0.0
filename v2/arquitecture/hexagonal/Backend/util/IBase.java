package [packageName].util;

import java.util.List;
import java.util.Map;

public interface IBase {
	public Object save(Object t);
	public Object save(List<Object> m);
	public void update(Object t);
	public List findAll();
	public Object findById(Integer id);
	public List<Object> findBy(Map<String, Object> src);
	public Boolean delete(Integer id);
	abstract List<Object> findBy(Map<String, Object> src, Class<?> returnPojoType);
}
