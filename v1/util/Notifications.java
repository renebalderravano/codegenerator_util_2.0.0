package [packageName].util;

public class Notifications {
    private int count;
    private String message;

    public Notifications() {
		// TODO Auto-generated constructor stub
	}
    
    public Notifications(int count) {
        this.count = count;
    }
    
    public int getCount() {
        return count;
    }
    public void setCount(int count) {
        this.count = count;
    }
    public void increment() {
        this.count++;
    }

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
