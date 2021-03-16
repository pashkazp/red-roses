package depavlo.millionredroses.repo;

import static org.mockito.Mockito.doReturn;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import depavlo.millionredroses.model.Contact;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Testing Contact repo")
class ContactRepository2Test {
	@MockBean
	private ContactRepository repo;

	@Test
	@DisplayName("getAll Stream contacts from Mocked data")
	void test() {
		Contact contact1 = new Contact(1l, "Widget Name");
		Contact contact2 = new Contact(2l, "Widget 2 Name");

		doReturn(Stream.of(contact1, contact2)).when(repo).getAll();
		Stream<Contact> contacts = repo.getAll();

		Assertions.assertEquals(2, contacts.collect(Collectors.toList()).size(), "getAll should return 2 contacts");
	}

}
