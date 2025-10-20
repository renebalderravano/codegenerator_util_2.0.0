package [packageName].util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 *Especifica que el valor del campo de un objeto de tipo POJO séra validado.<br><br>
 *Si la anotación no contiene la propiedad validationType por defecto la clase <b>ValidationUtil</b><br>
 *validara si el valor del campo del objecto fuente no es nulo.
 *
 *@author José Rene Balderravano Hernández
 *@see {@link [packageName].util.ValidatorUtil ValidatorUtil}
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ValidatorMapping {

	public ValidationType validationType() default ValidationType.IS_NOT_NULL;

}
