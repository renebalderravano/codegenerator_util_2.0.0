package [packageName].util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

/**
 * @author José Rene Balderravano Hernández
 * @see {@link com.digiret.util.MapperUtil MapperUtil} 
 */
public class BaseUtil {
	
	@Autowired
    private Environment env;

	@Autowired
	private MapperUtil mapperUtil;
	
	@Autowired
	private ValidatorUtil validatorUtil;
	
	@Autowired
	private ReflectUtil reflectUtil;
	
	/**
	 * Este metodo permite convertir un DTO a Model
	 * <br><br>
	 * Nota: Este metodo siempre devuelve un DTO por lo que es necesario castear
	 * a modelo cuando se requiera trabajar con los datos en los casos de uso.
	 * 
	 * @param src Objeto fuente
	 * 
	 */
	public Object prepareSendOfServiceToController(Object src) {
		
		String beanName = src.getClass().getSimpleName();
		
		if(beanName.contains("DTO")) {
			return src;
		}
		else if(beanName.contains("Model")) {
			String pojoName = env.getProperty("packagesToScan")+ ".infrastructure.adapters.input.rest.dto." + beanName.replace("Model", "DTO");
			Class clazzModel = getReflectUtil().findByClassName(pojoName);
			Object trg = null;
			if (clazzModel != null)
				trg = getMapperUtil().prepareTo(src, clazzModel);

			return trg;
		}
		
		return null;		
	}
	
	public List<Object> prepareListToSendOfServiceToController(Object object) {
		
		List trgObj = new ArrayList();
		List srcObj = (ArrayList)object;
		for (Object obj : srcObj) {
			trgObj.add(prepareSendOfServiceToController(obj));
		}
		return trgObj;
	}

	public Object prepareSendOfRepositoryToService(Object src) {
		String beanName = src.getClass().getSimpleName().replace("Entity", "");
		String pojoName = env.getProperty("packagesToScan")+".domain.model." + beanName + "Model";
		Object trg = null;
		Class<?> clazzModel = getReflectUtil().findByClassName(pojoName);
		if (clazzModel == null) {
			clazzModel = getReflectUtil().findByClassName(pojoName.replace("Model", "DTO"));
			if (clazzModel != null)
				trg = getMapperUtil().prepareTo(src, clazzModel);

			return trg;
		} else {
			trg = getMapperUtil().prepareTo(src, clazzModel);
			return trg;
		}
	}

	public List<Object> prepareListToSendOfRepositoryToService(List<Object> src) {
		
		List trgObj = new ArrayList();
		for (Object obj : src) {
			
			
			trgObj.add(prepareSendOfRepositoryToService(obj));
		}

		return trgObj;
	}


	public MapperUtil getMapperUtil() {
		return mapperUtil;
	}

	public void setMapperUtil(MapperUtil mapperUtil) {
		this.mapperUtil = mapperUtil;
	}

	public ValidatorUtil getValidatorUtil() {
		return validatorUtil;
	}

	public void setValidatorUtil(ValidatorUtil validatorUtil) {
		this.validatorUtil = validatorUtil;
	}

	public ReflectUtil getReflectUtil() {
		return reflectUtil;
	}

	public void setReflectUtil(ReflectUtil reflectUtil) {
		this.reflectUtil = reflectUtil;
	}
	
}