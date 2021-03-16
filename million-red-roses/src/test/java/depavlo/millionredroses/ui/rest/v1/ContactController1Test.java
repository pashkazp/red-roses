package depavlo.millionredroses.ui.rest.v1;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.stream.Stream;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import depavlo.millionredroses.repo.ContactRepository;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ContactController.class)
@DisplayName("Testing Contact REST controller ContactController")
class ContactController1Test {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ContactRepository repo;
	@MockBean
	EntityManager entityManager;

	private static final String URL = "/api/v1/hello/contacts";
	private static final String FakeURL = "/api/v1/hello/contact";

	@Test
	@DisplayName("Check invalid URL")
	void test1() throws Exception {

		mockMvc.perform(get(FakeURL))
				.andExpect(status().is(HttpStatus.NOT_FOUND.value()));
	}

	@DisplayName("Check invalid request type")
	@ParameterizedTest
	@MethodSource("mvcRequest")
	void test2(RequestBuilder mvcRequest) throws Exception {
		MvcResult result = mockMvc.perform(mvcRequest).andReturn();
		int status = result.getResponse().getStatus();
		assertEquals(HttpStatus.METHOD_NOT_ALLOWED.value(), status, "Incorrect Response Status");
	}

	static Stream<Arguments> mvcRequest() {
		return Stream.of(
				arguments(MockMvcRequestBuilders.post(URL)),
				arguments(MockMvcRequestBuilders.put(URL)),
				arguments(MockMvcRequestBuilders.delete(URL)));
	}

	@Test
	@DisplayName("Check missing param")
	void test3() throws Exception {
		mockMvc.perform(get(URL))
				.andExpect(status().is(HttpStatus.INTERNAL_SERVER_ERROR.value()));
	}

}
