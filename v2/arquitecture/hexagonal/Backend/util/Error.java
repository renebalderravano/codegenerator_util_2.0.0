package [packageName].util;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class Error {

	private String code;
	private String message;
	private String cause;
}
