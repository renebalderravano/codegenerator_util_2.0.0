package [packageName].util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Permite mapear una propiedad con diferente nombre o cuyo valor se encuentra en una propiedad<br>
 * de tipo objeto el cual puede ser DTO, Model o Entity.<br>
 * <br>
 * <b>Ejemplo:</b> <br>
 *  El objeto destino requiere que el campo con nombre "campo" de cual
 * su valor se encuentra en el objeto fuente con un nombre diferente o
 * dentro de un objeto el cual es otro objeto. <br>  <br>
 * 
 * <b>Ejemplo 1</b>: Cuando el campo destino tiene diferente nombre que el campo
 * en el objeto fuente.<br>
 * <br>
 * public class ObjetoFuente{ <br> <br>
 * 		private Integer campo;  <br> <br>
 * } <br>
 * public class ObjetoDestino{ <br>  <br>
 * @MapperMapping(srcClass= ObjetoFuente.class, srcFieldName="campo" ) <br>
 * private Integer campoSourceDifName; <br> <br>
 * } <br><br>
 * <b>Ejemplo 2</b>: Cuando el valor del campo destino
 *                         se encuentra dentro objeto fuente pero en un objeto
 *                         
 *                         el cual es una propiedad dentro de cicho objeto. <br> <br>
 * 
 *                         public class ObjetoHijo{ <br> <br>
 *                          private Integer campo;  <br> <br>
 *                          } <br> <br>
 * 
 *                         public class ObjetoFuente{ <br>
 *                          private ObjetoHijo objetoHijo;  <br> <br>
 *                         } <br> <br>
 * 
 *                         public class ObjetoObjetivo{ <br> <br>
 * @MapperMapping(srcClass= ObjetoFuente.class, srcFieldName="objetoHijo.campo"
 *                         )  <br>private Integer campoFuente;  <br> <br>}
 * 
 * 
 * @author José Rene Balderravano Hernández
 * @see {@link com.digiret.util.MapperUtil MapperUtil}
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface MapperMapping {
	public Class srcClass();

	/**
	 * Indica el nombre del en el objcampo destino en el cual será establecido; el
	 * valor del campo fuente con la sintaxis definida en el siguiente ejemplo.<br>
	 * 
	 * @return
	 */
	public String srcFieldName();

}
