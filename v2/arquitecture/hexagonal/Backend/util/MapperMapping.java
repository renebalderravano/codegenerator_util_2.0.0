package [packageName].util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface MapperMapping {
	public Class srcClass();
	
	/**
	 * Define el nombre del campo destino en el cual será establecido; el valor del campo fuente con la sintaxis definida en el siguiente ejemplo.<br>
	 * 
	 * <b> Sintaxis requerida con ejemplo:</b><br>
	 *  Ejemplo: El objeto destino requiere que el campo con nombre "nombre" de cual su valor se encuentra en el objeto fuente con un nombre diferente o <br>
	 *  dentro de un objeto el cual es otro objeto. <br>
	 *  
	 *  <b>Ejemplo 1</b>: Cuando el campo destino tiene diferente nombre que el campo en el objeto fuente.<br>
	 *  <br>
	 *  public class ObjetoFuente{
	 *  	private Integer campoSourceDifName;
	 *  }
	 *  <br>
	 *  public class ObjetoObjetivo{
	 *  	@MapperMapping(srcClass = ObjetoFuente.class, srcFieldName="campoSourceDifName" )
	 *  	private Integer campoSourceDifName;
	 *  }
	 *  <br>
	 *  <b>Ejemplo 2</b>: Cuando el valor del campo destino se encuentra dentro objeto fuente pero en un objeto <br>
	 *   el cual es una propiedad dentro de cicho objeto
	 * 
	 *   public class ObjetoHijo{
	 *  	private String campo;
	 *  }
	 *  
	 *  public class ObjetoFuente{
	 *  	private ObjetoHijo objetoHijo;
	 *  }
	 *  
	 *  public class ObjetoObjetivo{
	 *  	@MapperMapping(srcClass = ObjetoFuente.class, srcFieldName="objetoHijo.campo" )
	 *  	private Integer campoFuente;
	 *  }
	 *  
	 *  
	 * @return
	 */
	public String srcFieldName();
}
