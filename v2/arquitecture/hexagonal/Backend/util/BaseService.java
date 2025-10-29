package [packageName].util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

/**
 * @author José Rene Balderravano Hernández
 */
public abstract class BaseService<T> extends BaseUtil {

	private Class<T> entityClass;
	private Object service;

	@Autowired
	private ApplicationContext applicationContext;

	@SuppressWarnings("unchecked")
	public BaseService() {
		Type superClass = getClass().getGenericSuperclass();
		Type type = ((ParameterizedType) superClass).getActualTypeArguments()[0];
		entityClass = (Class<T>) type;
	}

	/**
	 * Permite enviar un objecto fuente al repositorio para ser almacenado
	 * en la base de datos.
	 * 
	 * @param t Objecto fuente
	 * @return 
	 */
	public Object save(Object t) {
		
		List<Error> errors =  super.getValidatorUtil().validate(t);
		
		if(!errors.isEmpty())
			return errors;
		
		return (Object) super.prepareSendOfServiceToController(
				callMethod(getRepository(), "save", new Object[] { t }, entityClass));
	}

	/**
	 * Permite enviar un objecto fuente al repositorio y devolver un tipo de POJO
	 * del tipo requerido.
	 * 
	 * @param srcObject Objeto fuente que sera enviado al repositorio.
	 * @param returnPojoType Tipo de POJO retornado.
	 * @return
	 */
	protected Object save(Object srcObject, Class returnPojoType) {
			return (Object) getMapperUtil()
				.prepareTo(callMethod(getRepository(), "save", new Object[] { srcObject }, entityClass), returnPojoType);
	}

	public void update(Object t) {
		
	}

	public List<Object> findAll() {

		return (List<Object>) super.prepareListToSendOfServiceToController(
				callMethod(getRepository(), "findAll", null, null));
	}

	public T findById(Integer id) {
		return (T) super.prepareSendOfServiceToController(callMethod(getRepository(), "findById", new Object[] { id }, id.getClass()));
	}

	public Boolean delete(Integer id) {
		return (Boolean) callMethod(getRepository(), "delete", new Object[] { id }, id.getClass());
	}

	private Object callMethod(Object obj, String methodName, Object[] values, Class<?>... classes) {
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

	protected Object getRepository() {
		String sufix = entityClass.getSimpleName().replace("Entity", "");
		String beanName = sufix.substring(0, 1).toLowerCase() + sufix.substring(1) + "RepositoryImpl";
		service = applicationContext.getBean(beanName);
		System.out.println(service.getClass().getName());
		return service;
	}
	
	public List<Object> findBy(Map<String, Object> src) {
		Class clazzModel = super.getReflectUtil().findByClassName(entityClass.getCanonicalName());
		List<Field> fieldsTrg =  Arrays.asList(clazzModel.getDeclaredFields());
		Boolean exists = true;
		for (Map.Entry<String, Object> fieldSrc : src.entrySet()) {
			String key = fieldSrc.getKey();
			
			Optional<Field> field = fieldsTrg.stream().filter(c -> c.getName().equals(key)).findFirst();
			if(field.isEmpty()) {
				exists = false;
			}
		}
		
		if(!exists) {
			Error error = new Error();
			error.setMessage("Error campos invalidos. Revise la escructura del entity");
			List<Object> le = new ArrayList<Object>();
			le.add(error);
			return le;
		}
		
		return (List<Object>) super.prepareListToSendOfServiceToController(callMethod(getRepository(), "findBy", new Object[] { src }, src.getClass()));
	}
	
	
	public byte[] download() {
		
		try {
			return prepareToDownload((IBase) getRepository());
		} catch (IllegalArgumentException  e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;		
	}

}
