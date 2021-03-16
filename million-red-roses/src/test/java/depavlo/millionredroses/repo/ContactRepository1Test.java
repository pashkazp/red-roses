package depavlo.millionredroses.repo;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import depavlo.millionredroses.model.Contact;

@SpringBootTest
@DisplayName("Testing Contact repo")
class ContactRepository1Test {
	@Autowired
	private ContactRepository repo;

	@Test
	@DisplayName("getAll Stream contacts from database")
	@Transactional(readOnly = true)
	void test() {
		Stream<Contact> contacts = repo.getAll();

		Assertions.assertEquals(10, contacts.collect(Collectors.toList()).size(), "getAll should return 10 contacts");
	}

}
