package depavlo.millionredroses.ui.rest.v1;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.stream.Stream;

import javax.annotation.PostConstruct;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import depavlo.millionredroses.model.Contact;
import depavlo.millionredroses.repo.ContactRepository;
import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration
@Slf4j
@DisplayName("Testing Contact REST controller ContactController")
class ContactController2Test {
	@Autowired
	private WebApplicationContext webApplicationContext;

	private MockMvc mockMvc;

	private static final String URL = "/api/v1/hello/contacts?nameFilter=";
	@MockBean
	private ContactRepository repo;

	@PostConstruct
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}

	@Test
	@DisplayName("using JSON media type")
	public void test1() throws Exception {
		Contact contact1 = new Contact(1l, "Widget Name");
		Contact contact2 = new Contact(2l, "Widget 2 Name");
		final HttpServletResponse response = mock(HttpServletResponse.class);
		ServletOutputStream output = mock(ServletOutputStream.class);
		when(response.getOutputStream()).thenReturn(output);
		doReturn(Stream.of(contact1, contact2)).when(repo).getAll();
		mockMvc.perform(get(URL)
				.accept(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk());
	}

}
