package [packageName].util;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class Table {
	private String[] headers;
	
	private List<Row> rows;
}
