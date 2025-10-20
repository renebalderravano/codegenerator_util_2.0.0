package [packageName].util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;

@Component
public class CSVUtil {

	/**
	 * Permite leer un archivo CSV con una lista de header predefinidos.
	 * <br>
	 * Nota: Valida que las columnas de la lista existan en el archivo.
	 * @param headers
	 * @param fileCSV
	 * @return
	 */
	public Table readCsvFile(String[] headers, byte[] fileCSV) {
		ByteArrayInputStream inputStream = new ByteArrayInputStream(fileCSV);
		try {
			
			CSVParser csvParser = CSVFormat.DEFAULT.withFirstRecordAsHeader()
					.parse(new InputStreamReader(inputStream, StandardCharsets.UTF_8));

			Set<String> csvHeaders = csvParser.getHeaderMap().keySet();

			Table table = new Table();
			if (validateHeaders(headers, csvHeaders)) {

				List<CSVRecord> csvRecords = csvParser.getRecords();
				
				List<Row> rows = new ArrayList<Row>();
				// Print CSV records
				for (CSVRecord record : csvRecords) {
					Row row = new Row();
					List<Column> columnsRow = new ArrayList<Column>();
					
					int i = 0;
					for (String headerName : headers) {
						Column column = new Column();
						column.setIndex(i);
						column.setName(headerName);
						column.setValue(record.get(headerName));
						columnsRow.add(column);
						i++;
					}
					row.setColumns(columnsRow);
					rows.add(row);
				}

				table.setRows(rows);
			}
			return table;

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public boolean validateHeaders(String[] headers, Set<String> csvHeaders) {
		
		for (String header : headers) {
            if (!csvHeaders.contains(header)) {
                return false;
            } 
        }
		return true;
	}

}
