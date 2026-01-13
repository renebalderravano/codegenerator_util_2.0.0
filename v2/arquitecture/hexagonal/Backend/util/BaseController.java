package [packageName].util;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

/**
 * 
 * @author José Rene Balderravano Hernández
 */
@SuppressWarnings("unchecked")
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
		System.out.println("[Método /save] Se procede a querer guardar el objeto...");
		return (DTO) callMethod(getService(), "save", new Object[] { t }, Object.class);
	}

	@CrossOrigin
	@PostMapping(path = "/update")
	public void update(DTO t) {
	}

	@CrossOrigin
	@GetMapping(path = "/findAll")
	public List<DTO> findAll() {

		return (List) callMethod(getService(), "findAll", null, null);
	}

	
	@CrossOrigin(origins = "*")
	@GetMapping(path = "/findById/{id}")
	public DTO findById(@PathVariable("id") Integer id) {
		System.out.println("[Método /findById] Se intenta obtener el objeto con id: " + id.toString());
		Object result = callMethod(getService(), "findById", new Object[] { id }, id.getClass());
		
		//DTO result = (DTO) callMethod(getService(), "findById", new Object[] { id }, id.getClass());
		System.out.println("resultado: " + result);
		//System.out.println("result properties:" + result.getClass().getDeclaredMethods());
		return (DTO) result;
		
	}
	
	@CrossOrigin(origins = "*")
	@PostMapping(path = "/findBy")
	public List<Object> findBy(@RequestBody Map<String, Object> src) {
		List<Object> result = (List<Object>) callMethod(getService(), "findBy", new Object[] { (Map)src }, Map.class);
		return result;
	}

	@CrossOrigin
	@GetMapping(path = "/delete/{id}")
	public Boolean delete(@PathVariable("id") Integer id) {
		return (Boolean) callMethod(getService(), "delete", new Object[] {id}, id.getClass());
	}
	
	
	@CrossOrigin
	@GetMapping(path = "/download")
	public ResponseEntity<byte[]> download() {
		byte[] file = (byte[]) callMethod(getService(), "download", null, null);
		
		 HttpHeaders headers = new HttpHeaders();
		    headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		    headers.setContentDisposition(ContentDisposition.attachment().filename("data.csv").build());

		    return new ResponseEntity<>(file, headers, HttpStatus.OK);

//		return ResponseEntity.ok()
//	        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=archivo.csv")
//	        .contentType(MediaType.APPLICATION_OCTET_STREAM)
//	        .body(file );
	}
	
	@CrossOrigin
	@PostMapping(path = "/upload")
	public Result upload(@RequestPart("file") MultipartFile file) {
		try {
			Result result = (Result) callMethod(getService(), "upload",  new Object[] {file.getBytes()} , null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;		
	}

	private Object callMethod(Object obj, String methodName, Object[] values, Class<?>... classes) {
		Object data = null;
		try {
			
			List<Class<?>> expectedParams = new ArrayList<Class<?>>();
				
			if(classes != null)
				expectedParams.addAll(Arrays.asList(classes));
			
			Optional<Method> x = (new ArrayList<>(Arrays.asList(obj.getClass().getMethods()))).stream()
					.filter(T -> T.getName().equals(methodName))
					.filter(m -> {
						
						 if (classes == null || classes.length == 0) {
			                    return true; // No se validan parámetros
			                }

						
		                Class<?>[] actualParams = m.getParameterTypes();
		                return expectedParams.size() == actualParams.length &&
		                       expectedParams.equals(Arrays.asList(actualParams));
		            }).findFirst();
			data = x.get().invoke(obj, values);
		} catch (SecurityException | IllegalAccessException | 
				 IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return data;
	}

	protected Object getService() {
		String name = entityClass.getSimpleName().replace("DTO", "");
		String beanName = name.substring(0, 1).toLowerCase()
				+ name.substring(1) + "ServiceImpl";
		service = applicationContext.getBean(beanName);
		System.out.println(service.getClass().getName());
		return service;
	}

}
