package [packageName].util;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
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
	
	@Autowired
	private XLSXUtil xlsxUtil;

	/**
	 * Este metodo permite convertir un DTO a Model <br>
	 * <br>
	 * Nota: Este metodo siempre devuelve un DTO por lo que es necesario castear a
	 * modelo cuando se requiera trabajar con los datos en los casos de uso.
	 * 
	 * @param src Objeto fuente
	 * 
	 */
	public Object prepareSendOfServiceToController(Object src) {

		String beanName = src.getClass().getSimpleName();

		if (beanName.contains("DTO")) {
			return src;
		} else if (beanName.contains("Model")) {
			String pojoName = env.getProperty("packagesToScan") + ".infrastructure.adapters.input.rest.dto."
					+ beanName.replace("Model", "DTO");
			Class<?> clazzModel = getReflectUtil().findByClassName(pojoName);
			Object trg = null;
			if (clazzModel != null)
				trg = getMapperUtil().prepareTo(src, clazzModel);

			return trg;
		}

		return null;
	}

	public List<Object> prepareListToSendOfServiceToController(Object object) {

		List<Object> trgObj = new ArrayList<Object>();
		List<?> srcObj = (ArrayList<?>) object;
		for (Object obj : srcObj) {
			trgObj.add(prepareSendOfServiceToController(obj));
		}
		return trgObj;
	}

	public Object prepareSendOfRepositoryToService(Object src) {
		String beanName = src.getClass().getSimpleName().replace("Entity", "");
		String pojoName = env.getProperty("packagesToScan") + ".domain.model." + beanName + "Model";
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

		List<Object> trgObj = new ArrayList<Object>();
		for (Object obj : src) {
			trgObj.add(prepareSendOfRepositoryToService(obj));
		}

		return trgObj;
	}

	public byte[] prepareToDownload(IBase iBase) {
		try {
			List list = iBase.findAll();
			Table table = prepareToTable(list);
			byte[] workbook = xlsxUtil.createWorkbook(table );
			
			return workbook;
		} catch (IOException | IllegalArgumentException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}
	
	public Table prepareToTable(List<Object> list) throws IllegalArgumentException, IllegalAccessException {
		Table table = new Table();
		List<Row> rows = new ArrayList<Row>();
		String[] headers = null;
		for (Object entity : list) {
			headers = Arrays.asList(entity.getClass().getDeclaredFields()).stream().map(Field::getName)
					.toArray(String[]::new);
			List<Field> fields = Arrays.asList(entity.getClass().getDeclaredFields());
			Row row = new Row();
			List<Column> columns = new ArrayList<Column>();
			for (int i = 0; i < headers.length; i++) {
				String header = headers[i];
				Field optFld = fields.stream().filter(fld -> fld.getName().equals(header)).findFirst().get();
				optFld.setAccessible(true);
				Object value = optFld.get(entity);
				Column column = new Column();
				column.setIndex(i);
				column.setName(header);
				column.setValue(value);
				columns.add(column);				
			}
			
			row.setColumns(columns);
			rows.add(row);
		}
		table.setHeaders(headers);
		table.setRows(rows);
		return table;
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