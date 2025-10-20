package [packageName].util;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

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
}
