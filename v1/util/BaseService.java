package [packageName].util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public abstract class BaseService<T> {

	private Class<T> entityClass;
	private Object service;

	@Autowired
	private ApplicationContext applicationContext;

	public BaseService() {
		Type superClass = getClass().getGenericSuperclass();
		Type type = ((ParameterizedType) superClass).getActualTypeArguments()[0];
		entityClass = (Class<T>) type;

	}

	public T save(T t) {
		
		return (T) callMethod(getRepository(), "save", new Object[]{t}, entityClass);
	}

	public void update(T housingLocation) {
	}

	public List<T> findAll() {		

		return (List<T>) callMethod(getRepository(), "findAll", null, null);
	}

	public T findById(Integer id) {

		return (T) callMethod(getRepository(), "findById", new Object[] {id}, id.getClass());
	}

	public Boolean delete(Integer id) {

		return (Boolean) callMethod(getRepository(), "delete", new Object[] {id}, id.getClass());
	}
	
	
	private Object callMethod(Object obj, String methodName, Object[] values, Class<?>...classes) {
	
		Object data = null;
		try {
			Optional<Method> x = (new ArrayList<>(Arrays.asList(obj.getClass().getMethods()))).stream()
					.filter(T -> T.getName().equals(methodName)).findFirst();

			data = x.get().invoke(obj, values);
		
		} catch (SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return data;
	}

	private Object getRepository() {
		
		String beanName =entityClass.getSimpleName().substring(0, 1).toLowerCase()+entityClass.getSimpleName().substring(1)  +"RepositoryImpl";
		service = applicationContext.getBean(beanName);
		System.out.println(service.getClass().getName());
		return service;
	}

}
