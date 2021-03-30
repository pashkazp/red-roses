package depavlo.millionredroses.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.doReturn;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import depavlo.millionredroses.model.Contact;
import depavlo.millionredroses.repo.ContactRepository;

@SpringBootTest
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

	@Test
	@DisplayName("Test Serialize")
	void test1() throws JsonMappingException, JsonProcessingException {
		Contact contact1 = new Contact(1l, "Widget Name");
		Contact contact2 = new Contact(2l, "Widget 2 Name");
		contactService.setFilter("dfgdgdfgdfgdfg");
		doReturn(List.of(contact1, contact2).stream()).when(repo).getAll();
		contactService.writeTo(byteArrayOutputStream);
		List<Contact> contactsOut = om.readValue(byteArrayOutputStream.toString(), new TypeReference<List<Contact>>() {
		});
		assertEquals(contactsOut.size(), 2);
		assertEquals(contactsOut.get(0).getId(), contact1.getId());
		assertEquals(contactsOut.get(0).getName(), contact1.getName());
		assertEquals(contactsOut.get(1).getId(), contact2.getId());
		assertEquals(contactsOut.get(1).getName(), contact2.getName());
	}

	@ParameterizedTest
	@DisplayName("Testing regexp")
	@MethodSource("regexpAndPassCountAndListCounts")
	void test2(String regExp, int passedCount, List<Contact> contacts)
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
