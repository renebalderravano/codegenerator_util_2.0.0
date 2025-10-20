package [packageName].util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Proporciona metodos que facilitan la validación de los campos en algún objeto
 * fuente.
 * 
 * @author José Rene Balderravano Hernández
 * @see {@link [packageName].util.ValidatorMapping ValidatorMapping}
 */
@Component
public class ValidatorUtil {

	@Autowired
	private ReflectUtil reflectUtil;

	/**
	 * Valida los campos que contienen la anotacion
	 * {@link [packageName].util.ValidatorMapping ValidatorMapping} existentes en un
	 * objeto fuente.
	 * 
	 * @param src Objecto fuente que será validado.
	 * @return
	 */
	public List<Error> validate(Object src) {
		List<Error> errors = new ArrayList<Error>();
		try {
			List<Field> fields = reflectUtil.getFieldByClass(src.getClass());

			for (Field field : fields) {
				ValidatorMapping validatorMapping = field.getDeclaredAnnotation(ValidatorMapping.class);
				if (validatorMapping != null) {
					Error error = isValid(src, field, ValidationType.IS_NOT_NULL);
					if (error == null) {
						error = isValid(src, field, validatorMapping.validationType());
					}

					if (error != null)
						errors.add(error);
				}
			}

		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return errors;
	}

	/**
	 * Verifica si un campo en un POJO contiene una anotacion del tipo
	 * ValidatorMapping si la contiene determina el tipo de validación
	 * 
	 * @param value          Objecto fuente
	 * @param validationType Tipo de validación
	 * @return
	 */
	private Error isValid(Object src, Field field, ValidationType validationType) {

		Error error = new Error();
		try {

			Object value = reflectUtil.getValueByField(field, src);

			boolean isValid = switch (validationType) {
			case EMAIL: {
				yield emailValidator(value.toString());
			}
			case PHONE: {
				yield phoneValidator(value.toString());
			}
			case RFC: {
				
				yield rfcValidator(value.toString());
			}
			case CURP: {
				yield curpValidator(value.toString());
			}
			case IS_NOT_NULL:
				yield value != null;
			default:
				yield true;

			};

			if (!isValid) {
				
				if(!validationType.equals(ValidationType.IS_NOT_NULL)) {
					error.setCode("ERR-002");// Aqui va el código de error definido por el desarrollador
					error.setMessage("Error");
					error.setCause("El Formato del campo \""+field.getName()+"\" es inválido.");
				}else {
					error.setCode("ERR-001");// Aqui va el código de error definido por el desarrollador
					error.setMessage("Error");
					error.setCause("El valor del campo \""+field.getName()+"\" no puede ser nulo.");
				}
				return error;
			}

		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Permite validar un email con los siguientes formatos como validos:<br>
	 * username@domain.com <br>
	 * user.name@domain.com <br>
	 * user-name@domain.com<br>
	 * username@domain.co.in <br>
	 * user_name@domain.com<br>
	 * <br>
	 * La siguiente lista muestra ejemplos invalidos:<br>
	 * 
	 * username.@domain.com<br>
	 * .user.name@domain.com<br>
	 * user-name@domain.com.<br>
	 * username@.com<br>
	 * 
	 * @param emailAddress Dirección de correo electronico
	 * @return
	 */
	private boolean emailValidator(String emailAddress) {

		String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
				+ "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

		return Pattern.compile(regexPattern).matcher((CharSequence) emailAddress).matches();
	}

	/**
	 * Permite validar un numero telefonico con lo siguientes ejemplos formatos como
	 * validos e invalidos: <br>
	 * telefono1 = "+52 81 1234 5678"; <br>
	 * telefono2 = "81 1234 5678";<br>
	 * telefonoInvalido = "123-456-789";<br>
	 * 
	 * @param phone Telefono propcionado
	 * @return
	 */
	private boolean phoneValidator(String phone) {
		String regex = "^\\+?52?\\s?\\d{2}\\s?\\d{4}\\s?\\d{4}$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(phone);
		return matcher.matches();
	}

	/**
	 * Valida la estructura de una cadena con forma de RFC con homoclave con el
	 * siguientes ejemplo como validos e invalido: <br>
	 * rfcValido = "GODE561231GR8"; // RFC válido<br>
	 * rfcInvalido = "1234567890"; // RFC inválido <br>
	 * 
	 * @param rfc RFC(Registro Federal de contribuyentes).
	 * @return
	 */
	private boolean rfcValidator(String rfc) {
		// Expresión regular para validar RFC con homoclave
		String regex = "^[A-ZÑ&]{3,4}\\d{6}[A-Z0-9]{3}$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(rfc);
		return matcher.matches();
	}

	/**
	 * Valida el formato de un CURP.
	 * 
	 * @param curp CURP(Clave Unica de Registro de Población)
	 * @return
	 */
	private boolean curpValidator(String curp) {
		// Expresión regular para validar el formato de la CURP
		String regex = "^[A-Z]{4}\\d{6}[HM][A-Z]{5}[A-Z0-9]{2}$";
		return Pattern.matches(regex, curp);
	}

}
