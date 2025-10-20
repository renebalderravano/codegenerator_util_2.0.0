package [packageName].util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

/**
 * Proporciona metodos que permiten realizar la transferencia de información entre POJOS
 * con campos de tipo y nombre similares.
 * 
 * @author José Rene Balderravano Hernández
 * @see {@link [packageName].util.MapperMapping MapperMapping}
 */
@Component
public class MapperUtil {

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
			Class<?> clazz = Class.forName(trgObjectClass.getCanonicalName());
			Object trgObj = clazz.newInstance();
			trgObj = copyValues(srcObject, trgObj);
			data = trgObj;
		} catch (SecurityException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return data;
	}

	public List<Object> prepareTo(List<Object> srcListObject, Class trgObject) {
		List<Object> data = new ArrayList<Object>();
		try {

			for (Object srcObject : srcListObject) {
				Class<?> clazz = Class.forName(trgObject.getCanonicalName());
				Object trgObj = clazz.newInstance();

				trgObj = copyValues(srcObject, trgObj);

				data.add(trgObj);

			}
		} catch (SecurityException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return data;

	}

	/**
	 * Permite transferir los valores de los campos en un objeto fuente a un objeto
	 * destino; siempre y cuando los nombres de los campos y sus tipos de datos sean
	 * iguales.
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
	 * Permite realizar la tranferencia de valores entre campos de nombre y tipo de datos similares
	 * <br>
	 * @param srcObject Objeto fuente
	 * @param trgObject Objeto Destino
	 * @return
	 */
	private Object copyValues(Object srcObject, Object trgObject) {
		List<Field> trgFields = Arrays.asList(trgObject.getClass().getDeclaredFields());
		Field[] fieldsSource = srcObject.getClass().getDeclaredFields();

		for (Field fieldSource : fieldsSource) {
			try {
				fieldSource.setAccessible(true);
				String fieldSourceName = fieldSource.getName();
				Field fieldTarget;

			

				Optional<Field> trgField = trgFields.stream().filter(f -> f.getName() == fieldSource.getName())
						.findFirst();

				if (trgField.isEmpty()) {
					
					if(fieldSource.getName().equals("name"))
						System.out.println();
					
					MapperMapping mapperMapping = fieldSource.getDeclaredAnnotation(MapperMapping.class);					
					if (mapperMapping != null && !mapperMapping.trgClass().equals(trgObject.getClass()))
						continue;
					
					if (mapperMapping != null)
						for (Field field : trgFields) {
							if (field.getName().equals(mapperMapping.trgFieldName().toString())) {
								Field trgF = field;
								trgField = Optional.of(trgF);
								break;
							}
						}

				}

				if (trgField.isPresent()) {
					trgField.get().setAccessible(true);
					if (trgField.get().getType().getName().equals(fieldSource.getType().getName())) {
						Object sourceValue;
						sourceValue = fieldSource.get(srcObject);

						trgField.get().set(trgObject, sourceValue);
					}
				}
			} catch (IllegalArgumentException | IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return trgObject;
	}
}
