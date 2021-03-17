package depavlo.millionredroses.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.doReturn;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import depavlo.millionredroses.model.Contact;
import depavlo.millionredroses.repo.ContactRepository;
import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration
@Slf4j
@DisplayName("Testing ContactService")
class ContactServiceTest {
	@MockBean
	private ContactRepository repo;
	@Autowired
	private ContactService contactService;

	@Autowired
	private ObjectMapper om;

	private ByteArrayOutputStream byteArrayOutputStream;

	@BeforeEach
	void init() {
		byteArrayOutputStream = new ByteArrayOutputStream();
	}

	@ParameterizedTest
	@DisplayName("Testing regexp")
	@MethodSource("regexpAndPassCountAndListCounts")
	void test1(String regExp, int passedCount, List<Contact> contacts)
			throws JsonMappingException, JsonProcessingException {

		contactService.setFilter(regExp);
		List<Contact> contactsIn = contacts;
		doReturn(contactsIn.stream()).when(repo).getAll();

		contactService.writeTo(byteArrayOutputStream);

		List<Contact> contactsOut = om.readValue(byteArrayOutputStream.toString(), new TypeReference<List<Contact>>() {
		});
		assertEquals(passedCount, contactsOut.size());
	}

	static Stream<Arguments> regexpAndPassCountAndListCounts() {
		return Stream.of(
				arguments(".*2.*", 1, List.of(
						new Contact(1l, "Widget Name"),
						new Contact(2l, "Widget 2 Name"))),
				arguments(".*", 0, List.of(
						new Contact(1l, "Widget Name"),
						new Contact(2l, "Widget 2 Name"))),
				arguments(".*", 0, List.of()));
	}

}
