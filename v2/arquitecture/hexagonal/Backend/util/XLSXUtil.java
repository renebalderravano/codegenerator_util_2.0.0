package [packageName].util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

/**
 * @author José Rene Balderravano Hernández
 */
@Component
public class XLSXUtil {
	public byte[] createWorkbook(Table table)  throws IOException {
		
	    try (Workbook workbook = new XSSFWorkbook()) {
            Sheet hoja = workbook.createSheet("Datos");

            // Crear encabezado
             org.apache.poi.ss.usermodel.Row encabezado = hoja.createRow(0);
             
            for (int i = 0; i < table.getHeaders().length; i++) {
            	 ((org.apache.poi.ss.usermodel.Row) encabezado).createCell(i).setCellValue(table.getHeaders()[i]);
			}

            // Crear filas de datos             
             String[][] datos = new String[table.getRows().size()][table.getRows().get(0).getColumns().size()];
     		int i = 0;
             for (Row row : table.getRows()) {
     			int j = 0;
     			for (String header : table.getHeaders()) {
     				Optional<Column> col = row.getColumns().stream().filter(c -> c.getName().equals(header)).findFirst();
     				datos[i][j] = col.isPresent() && col.get().getValue() != null ? (String) col.get().getValue().toString() : "";
     				j++;
     			}
     			i++;
     		}
            for (i = 0; i < datos.length; i++) {
                 org.apache.poi.ss.usermodel.Row fila = hoja.createRow(i + 1);
                for (int j = 0; j < datos[i].length; j++) {
                    ((org.apache.poi.ss.usermodel.Row) fila).createCell(j).setCellValue(datos[i][j].toString());
                }
            }
            
            LocalDate localDate = LocalDate.now();

            // Guardar archivo
            try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                workbook.write(out);
                return out.toByteArray();
            }

        }

	}

}
