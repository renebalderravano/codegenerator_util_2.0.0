package [packageName].util;

/**
 * Indica los tipo de validación disponibles.
 */
public enum ValidationType {

	/**
	 * Indica tipo de validación: Email.
	 */
	EMAIL,
	
	/**
	 * Indica tipo de validación: Telefono.
	 */
	PHONE,
	
	/**
	 * Indica tipo de validación: longitud.
	 * <br>
	 * Nota: Sirve solo para campos de tipo string.
	 */
	LENGHT,
	/**
	 * Indica tipo de validación: RFC(Registro Federal de Contribuyentes).
	 */
	RFC,
	/**
	 * Indica tipo de validación: CURP(Clave Unica de Registro Poblacional).
	 */
	CURP,
	/**
	 * Indica tipo de validación: Rango de valores con un valor minimo y maximo.
	 */
	RANGE,
	/**
	 * Indica tipo de validación: Valida que el valor del campo no sea nulo.
	 */
	IS_NOT_NULL;
	
}
