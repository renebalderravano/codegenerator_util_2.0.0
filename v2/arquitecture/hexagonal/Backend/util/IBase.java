package [packageName].util;

import java.util.List;

public interface IBase {
	public Object save(Object t);
	public void update(Object t);
	public List findAll();
	public Object findById(Integer id);
	public Boolean delete(Integer id);
}
