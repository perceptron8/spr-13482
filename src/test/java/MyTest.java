import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@RunWith(JUnitPlatform.class)
@SpringJUnitConfig(MyConfiguration.class)
public class MyTest {
	@Autowired
	private Validator validator;
	
	@Test
	public void validate() {
		MyBean bean = new MyBean();
		bean.setProperty(Arrays.asList("no", "element", "can", "be", null));
		Errors errors = new BeanPropertyBindingResult(bean, "bean");
		validator.validate(bean, errors);
		assertTrue(errors.hasErrors());
	}
}
