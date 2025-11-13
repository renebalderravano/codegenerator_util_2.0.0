package [packageName].util;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import aj.org.objectweb.asm.commons.Method;

@Component
public class ReflectUtil {

	public Class findByClassName(String className) {
		try {
			Class<?> clazz = Class.forName(className);
			return clazz;
		} catch (ClassNotFoundException e) {
			System.err.println("Class not found: " + e.getMessage());
		}
		return null;
	}

	public Object getInstanceByClassName(String className) {
		try {
			Class<?> clazz = Class.forName(className);
			return clazz.newInstance();
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			System.err.println("Class not found: " + e.getMessage());
		}
		return null;
	}

	public List<Field> getFieldByClass(Class clazz) {
		return Arrays.asList(clazz.getDeclaredFields());
	}

	public Field getFieldByName(List<Field> trgFields, String fieldName) {

		Optional<Field> trgField = trgFields.stream().filter(f -> f.getName() == fieldName).findFirst();
		return trgField.get();
	}

	public Field getFieldByName(Object src, String fieldName) {
		Optional<Field> trgField = (Arrays.asList(src.getClass().getDeclaredFields())).stream()
				.filter(f -> f.getName() == fieldName).findFirst();
		return trgField.get();
	}

	public Object getValueByField(Field field, Object srcObj) {
		Object value = null;
		try {
			field.setAccessible(true);
			value = field.get(srcObj);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return value;
	}

	public static Object castFromString(String value, Class<?> targetType) {
		if (value == null || targetType == null)
			return null;

		try {
			if(value == null || value.equals(""))
				return null;
			if (targetType == String.class) {
				return value;
			} else if (targetType == int.class || targetType == Integer.class) {
				return Integer.parseInt(value);
			} else if (targetType == long.class || targetType == Long.class) {
				return Long.parseLong(value);
			} else if (targetType == double.class || targetType == Double.class) {
				return Double.parseDouble(value);
			} else if (targetType == float.class || targetType == Float.class) {
				return Float.parseFloat(value);
			} else if (targetType == boolean.class || targetType == Boolean.class) {
				return Boolean.parseBoolean(value);
			} else if (targetType == short.class || targetType == Short.class) {
				return Short.parseShort(value);
			} else if (targetType == byte.class || targetType == Byte.class) {
				return Byte.parseByte(value);
			} else if (targetType == char.class || targetType == Character.class) {
				return value.charAt(0);
			} else if (targetType == BigDecimal.class) {
				return new BigDecimal(value);
			} else if (targetType == Date.class) {
				// Ajusta el formato según tu caso
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				return sdf.parse(value);
			} else {
//				// Intenta usar valueOf(String) si existe
//				Method valueOfMethod = targetType.getMethod("valueOf", String.class);
//				return valueOfMethod.invoke(null, value);
			}
		} catch (Exception e) {
			throw new IllegalArgumentException("No se pudo convertir '" + value + "' a " + targetType.getName(), e);
		}
		
		return null;
	}

}
