package [packageName].configuration;

import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class SingletonTokenHolder {
	
    private Map<String, String> value;

    public Map<String, String> getValue() {
        return value;
    }

    public void setValue(Map<String, String> value) {
        this.value = value;
    }
}