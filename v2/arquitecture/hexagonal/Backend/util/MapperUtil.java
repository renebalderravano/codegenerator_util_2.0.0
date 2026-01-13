package [packageName].util;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Proporciona metodos que permiten realizar la transferencia de información
 * entre POJOS con campos del mismo tipo de dato y<br>
 * nombres similares y/o diferentes si se utiliza la anotación
 * {@link com.digiret.util.MapperMapping MapperMapping}.
 * 
 * @author José Rene Balderravano Hernández
 * @see {@link com.digiret.util.MapperMapping MapperMapping}
 */
@Component
public class MapperUtil {

	@Autowired
	private ReflectUtil reflectUtil;

	/**
	 * Permite transferir los valores de los campos en un objeto fuente a la
	 * instancia de un objeto destino. <br>
	 * NOTA: tranferira los valores siempre y cuando los nombres de los campos y sus
	 * tipos de datos sean iguales.
	 * 
	 * @param srcObject      Objeto fuente
	 * @param trgObjectClass Tipo de clase del objeto destino.
	 * @return
	 */
	public Object prepareTo(Object srcObject, Class trgObjectClass) {
		Object data = null;
		try {
			Object trgObj = reflectUtil.getInstanceByClassName(trgObjectClass.getCanonicalName());
			trgObj = copyValues(srcObject, trgObj);
			data = trgObj;
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		return data;
	}

	/**
	 * Permite transferir los valores de los campos en una lista objeto fuente a la
	 * instancia de un objeto destino. <br>
	 * 
	 * @param srcListObject
	 * @param trgObject
	 * @return
	 */
	public List<Object> prepareTo(List<Object> srcListObject, Class trgObject) {
		List<Object> data = new ArrayList<Object>();
		try {

			for (Object srcObject : srcListObject) {
				Object trgObj = reflectUtil.getInstanceByClassName(trgObject.getCanonicalName());
				;

				trgObj = copyValues(srcObject, trgObj);

				data.add(trgObj);

			}
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		return data;

	}

	/**
	 * Permite transferir los valores de los campos en un objeto fuente a un objeto
	 * destino.
	 * 
	 * @param srcObject Objeto fuente
	 * @param trgObject Objeto destino
	 * @return
	 */
	public Object prepareTo(Object srcObject, Object trgObject) {

		try {
			trgObject = copyValues(srcObject, trgObject);
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		return trgObject;
	}

	/**
	 * Permite realizar la tranferencia de valores entre campos de nombre y tipo de
	 * datos similares <br>
	 * 
	 * @param srcObject Objeto fuente
	 * @param trgObject Objeto Destino
	 * @return
	 */
	private Object copyValues(Object srcObject, Object trgObject) {

		List<Field> trgFields = Arrays.asList(trgObject.getClass().getDeclaredFields());
		List<Field> srcFields = Arrays.asList(srcObject.getClass().getDeclaredFields());

		try {
			for (Field fieldSource : srcFields) {
				fieldSource.setAccessible(true);
				Optional<Field> trgField = trgFields.stream().filter(f -> f.getName() == fieldSource.getName())
						.findFirst();

//				if (trgField.isEmpty()) {
//					MapperMapping mapperMapping = fieldSource.getDeclaredAnnotation(MapperMapping.class);
//					if (mapperMapping != null && !mapperMapping.srcClass().equals(trgObject.getClass()))
//						continue;
//
//					if (mapperMapping != null)
//						for (Field field : trgFields) {
//							if (field.getName().equals(mapperMapping.srcFieldName().toString())) {
//								Field trgF = field;
//								trgField = Optional.of(trgF);
//								break;
//							}
//						}
//				}

				if (trgField.isPresent()) {
					trgField.get().setAccessible(true);

					Object sourceValue = fieldSource.get(srcObject);
					String fieldTypeTrgName = trgField.get().getType().getName();
					if (fieldTypeTrgName.endsWith("Entity") || fieldTypeTrgName.endsWith("Model")
							|| fieldTypeTrgName.endsWith("DTO")) {
						Object objTrg = reflectUtil.getInstanceByClassName(fieldTypeTrgName);
						sourceValue = copyValues(sourceValue, objTrg);
						trgField.get().set(trgObject, sourceValue);
						continue;
					}

					if (trgField.get().getType().getName().equals(fieldSource.getType().getName())) {

						if (trgField.get().getName().equals("informationProfileDetails"))
							System.out.println();
						
						if (List.class.isAssignableFrom(trgField.get().getType())) {
							System.out.println("Field is a List");
							String classTemplateList = "";
							// 3. Get generic type information
							Type genericType = trgField.get().getGenericType();
							if (genericType instanceof ParameterizedType) {
								ParameterizedType paramType = (ParameterizedType) genericType;
								Type[] typeArgs = paramType.getActualTypeArguments();

								if (typeArgs.length > 0) {
									classTemplateList = typeArgs[0].getTypeName();
									System.out.println("Generic type: " + typeArgs[0].getTypeName());
								}
							}

							
							
							List<Object> lst = (List<Object>) sourceValue;
							
							List<Object> lstTrg = new ArrayList<Object>();
							for (Object src : lst) {
								Object objTrg = reflectUtil.getInstanceByClassName(classTemplateList);
								Object objTrgLst = copyValues(src, objTrg);
								lstTrg.add(objTrgLst);
							}
							
							sourceValue = lstTrg;
						}
				

						trgField.get().set(trgObject, sourceValue);
					}
				}

			}

			for (Field trgField : trgFields) {
				trgField.setAccessible(true);
				MapperMapping mapperMapping = trgField.getDeclaredAnnotation(MapperMapping.class);
				if (mapperMapping != null && !mapperMapping.srcClass().equals(srcObject.getClass()))
					continue;

				if (mapperMapping != null) {

					String[] srcFieldName = mapperMapping.srcFieldName().toString().split("\\.");
					Optional<Field> srcField = srcFields.stream().filter(f -> f.getName().equals(srcFieldName[0]))
							.findFirst();

					if (srcField.isPresent()) {
						srcField.get().setAccessible(true);

						Object objSrcValue = srcField.get().get(srcObject);
						Object valSrc = null;
						if (srcFieldName.length == 2) {
							Field fldSrc = objSrcValue.getClass().getDeclaredField(srcFieldName[1]);

							fldSrc.setAccessible(true);
							valSrc = fldSrc.get(objSrcValue);

							if (trgField.getType().getName().equals(fldSrc.getType().getName())) {
								trgField.set(trgObject, valSrc);
							}

						} else {

							String fieldTypeTrgName = trgField.getType().getName();
							if (fieldTypeTrgName.endsWith("Entity") || fieldTypeTrgName.endsWith("Model")
									|| fieldTypeTrgName.endsWith("DTO")) {

								Object objTrg = reflectUtil.getInstanceByClassName(fieldTypeTrgName);
								Field fld = objTrg.getClass().getDeclaredField("id");
								fld.setAccessible(true);
								fld.set(objTrg, objSrcValue);
								trgField.set(trgObject, objTrg);
							}
							if (trgField.getType().getName().equals(srcField.get().getType().getName())) {
								trgField.set(trgObject, objSrcValue);
							}
						}

					}

				}

			}

		} catch (IllegalArgumentException | IllegalAccessException | SecurityException | NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return trgObject;
	}
}
