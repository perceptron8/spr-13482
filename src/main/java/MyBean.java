import java.util.List;

import javax.validation.constraints.NotNull;

public class MyBean {
	private List<@NotNull String> property;

	public List<String> getProperty() {
		return property;
	}
	public void setProperty(List<String> property) {
		this.property = property;
	}
}
