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
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public abstract class BaseController<DTO> extends BaseUtil {

	private Class<DTO> entityClass;
	private Object service;

	@Autowired
	private ApplicationContext applicationContext;

	public BaseController() {
		Type superClass = getClass().getGenericSuperclass();
		Type type = ((ParameterizedType) superClass).getActualTypeArguments()[0];
		entityClass = (Class<DTO>) type;

	}

	@CrossOrigin
	@PostMapping(path = "/save")
	public DTO save(@RequestBody DTO t) {

		return (DTO) callMethod(getService(), "save", new Object[] { t }, entityClass);
	}

	@CrossOrigin
	@PostMapping(path = "/update")
	public void update(DTO housingLocation) {
	}

	@CrossOrigin
	@GetMapping(path = "/findAll")
	public List<DTO> findAll() {

		return (List<DTO>) callMethod(getService(), "findAll", null, null);
	}

	@CrossOrigin(origins = "*")
	@GetMapping(path = "/findById/{id}")
	public DTO findById(@PathVariable("id") Integer id) {
		return (DTO) callMethod(getService(), "findById", new Object[] { id }, id.getClass());
	}

	@CrossOrigin
	@GetMapping(path = "/delete/{id}")
	public Boolean delete(@PathVariable("id") Integer id) {
		return (Boolean) callMethod(getService(), "delete", new Object[] {id}, id.getClass());
	}

	private Object callMethod(Object obj, String methodName, Object[] values, Class<?>... classes) {
		Object data = null;
		try {
			Optional<Method> x = (new ArrayList<>(Arrays.asList(obj.getClass().getMethods()))).stream()
					.filter(T -> T.getName().equals(methodName)).findFirst();

			data = x.get().invoke(obj, values);

		} catch (SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
		
		return data;
	}

	private Object getService() {
		String name = entityClass.getSimpleName().replace("DTO", "");
		String beanName = name.substring(0, 1).toLowerCase()
				+ name.substring(1) + "ServiceImpl";
		service = applicationContext.getBean(beanName);
		System.out.println(service.getClass().getName());
		return service;
	}

}
