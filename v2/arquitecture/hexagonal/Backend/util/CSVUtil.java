package [packageName].util;

import java.io.ByteArrayInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;

@Component
public class CSVUtil {

	/**
	 * Permite leer un archivo CSV con una lista de header predefinidos. <br>
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

	public byte[] createCsvFile(String[] headers, Table table) {
		// Define the file path for the CSV
		String filePath = "example.csv";

		String[][] data = null;
		if(table.getRows() != null) {

			// Data to be written to the
			data = new String[table.getRows().size()][table.getRows().get(0).getColumns().size()];

			int i = 0;
			for (Row row : table.getRows()) {
				int j = 0;
				for (String header : headers) {
					Optional<Column> col = row.getColumns().stream().filter(c -> c.getName().equals(header))
							.findFirst();
					data[i][j] = col.isPresent() && col.get().getValue() != null ? col.get().getValue().toString() : "";
					j++;
				}
				i++;
			}
		}

		try (FileWriter writer = new FileWriter(filePath);
				CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader(headers))) {
			// Write data rows
			if (data != null) {
				for (String[] row : data) {
					csvPrinter.printRecord((Object[]) row);
				}
			} else {
				Object[] row = new Object[headers.length];
				for (int i = 0; i < headers.length; i++) {
					row[i] = "wertyuio";
				}
				csvPrinter.printRecord((Object[]) row);
			}

			System.out.println("CSV file created successfully at: " + filePath);

			csvPrinter.flush();
			
//			File file = new File("example.csv");
//			byte[] byteArray = new byte[(int) file.length()];
//			try (FileInputStream inputStream = new FileInputStream(file)) {
//			inputStream.read(byteArray);
//			}
//			
//			return byteArray;
			
//			try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
//				out.writeBytes(writer.getEncoding().getBytes());
//				return out.toByteArray();
//			}
			
	         writer.close();
	         
	            // Step 2 and 3: Read the file content and convert it to a byte array
	            byte[] byteArray = Files.readAllBytes(Paths.get("example.csv"));
			
			return byteArray;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	public Table convertToTable(List<Map<String, Object>> data) {
		Table table = new Table();
		List<Row> rows = new ArrayList<Row>();
		for (Map<String, Object> rowSrc : data) {
			Row row = new Row();
			List<Column> columns = new ArrayList<>();
			for (Entry<String, Object> colSrc : rowSrc.entrySet()) {
				Column column = new Column();
				column.setName(colSrc.getKey());
				column.setValue(colSrc.getValue());
				columns.add(column);
			}
			row.setColumns(columns);
			rows.add(row);
		}
		table.setRows(rows);

		return table;

	}

}
