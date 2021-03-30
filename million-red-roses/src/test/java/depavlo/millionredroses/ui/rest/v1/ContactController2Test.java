package depavlo.millionredroses.ui.rest.v1;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.stream.Stream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import depavlo.millionredroses.model.Contact;
import depavlo.millionredroses.repo.ContactRepository;
import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Testing Contact REST controller ContactController")
@Slf4j
class ContactController2Test {
	@Autowired
	private MockMvc mockMvc;

	private static final String URL = "/api/v1/hello/contacts?nameFilter=";
	@MockBean
	private ContactRepository repo;

	@Test
	@DisplayName("using JSON media type")
	public void test1() throws Exception {
		Contact contact1 = new Contact(1l, "Widget Name");
		Contact contact2 = new Contact(2l, "Widget 2 Name");
		final HttpServletResponse response = mock(HttpServletResponse.class);
		ServletOutputStream output = mock(ServletOutputStream.class);
		when(response.getOutputStream()).thenReturn(output);
		doReturn(Stream.of(contact1, contact2)).when(repo).getAll();
		ResultActions resultActions = mockMvc.perform(get(URL)
				.accept(MediaType.APPLICATION_JSON_VALUE))

				// Validate the response code and content type
				.andExpect(status().isOk())
				.andDo(print());

		// Validate headers
//				.andExpect(header().string(HttpHeaders.LOCATION, "/rest/widgets"))

		MvcResult result = resultActions.andReturn();
		String contentAsString = result.getResponse().getContentAsString();
		log.info(contentAsString);

	}

}
