package [packageName].util;

import java.util.List;


public interface IBase<T> {

	public T save(T housingLocation);
	public void update(T housingLocation);
	public List<T> findAll();
	public T findById(Integer id);
	public Boolean delete(Integer id);
}
