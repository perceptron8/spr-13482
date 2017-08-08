import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;

@RunWith(JUnitPlatform.class)
public class SpringValidatorAdapterTests {
	private final Validator nativeValidator = Validation.buildDefaultValidatorFactory().getValidator();

	private final SpringValidatorAdapter validatorAdapter = new SpringValidatorAdapter(nativeValidator);


	@Test
	public void testListElementConstraint() {
		BeanWithListElementConstraint bean = new BeanWithListElementConstraint();
		bean.setProperty(Arrays.asList("no", "element", "can", "be", null));
		
		Errors errors = new BeanPropertyBindingResult(bean, "bean");
		validatorAdapter.validate(bean, errors);
		
		assertEquals(1, errors.getFieldErrorCount("property[4]"));
		assertNull(errors.getFieldValue("property[4]"));
	}

	@Test
	public void testMapEntryKeyConstraint() {
		Map<String, String> property = new HashMap<>();
		property.put(null, "key is not allowed");
		
		BeanWithMapEntryConstraint bean = new BeanWithMapEntryConstraint();
		bean.setProperty(property);
		
		Errors errors = new BeanPropertyBindingResult(bean, "bean");
		validatorAdapter.validate(bean, errors);
		
		assertThat(errors.getFieldErrorCount("property[]"), is(1));
		assertNull(errors.getFieldValue("property[]"));
	}

	@Test
	public void testMapEntryValueConstraint() {
		Map<String, String> property = new HashMap<>();
		property.put("no value can be", null);
		
		BeanWithMapEntryConstraint bean = new BeanWithMapEntryConstraint();
		bean.setProperty(property);
		
		Errors errors = new BeanPropertyBindingResult(bean, "bean");
		validatorAdapter.validate(bean, errors);
		
		assertThat(errors.getFieldErrorCount("property[no value can be]"), is(1));
		assertNull(errors.getFieldValue("property[no value can be]"));
	}

	@Test
	public void testMapEntryConstraint() {
		Map<String, String> property = new HashMap<>();
		property.put("", ""); // blanks not allowed
		
		BeanWithMapEntryConstraint bean = new BeanWithMapEntryConstraint();
		bean.setProperty(property);
		
		Errors errors = new BeanPropertyBindingResult(bean, "bean");
		validatorAdapter.validate(bean, errors);
		
		assertThat(errors.getFieldErrorCount("property[]"), is(2));
		assertThat(errors.getFieldValue("property[]"), is(""));
	}


	public class BeanWithListElementConstraint {

		private List<@NotNull String> property;

		public List<String> getProperty() {
			return property;
		}
		public void setProperty(List<String> property) {
			this.property = property;
		}
	}


	public class BeanWithMapEntryConstraint {

		private Map<@NotBlank String, @NotBlank String> property;

		public Map<String, String> getProperty() {
			return property;
		}
		public void setProperty(Map<String, String> property) {
			this.property = property;
		}
	}
}
