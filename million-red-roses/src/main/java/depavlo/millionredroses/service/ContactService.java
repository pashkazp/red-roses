package depavlo.millionredroses.service;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import depavlo.millionredroses.model.Contact;
import depavlo.millionredroses.repo.ContactRepository;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContactService implements StreamingResponseBody {

	private final ContactRepository dao;
	private final EntityManager entityManager;

	@Setter
	private String filter;

	@Override
	@Transactional(readOnly = true)
	public void writeTo(OutputStream outputStream) {

		log.debug("writeTo] - Write filtered contacts to OutputStream {}", outputStream);

		Pattern pattern = Pattern.compile(filter);

		try (Stream<Contact> contactResultStream = dao.getAll()) {
			try (PrintWriter oos = new PrintWriter(outputStream)) {

				Iterator<Contact> ci = contactResultStream.iterator();

				oos.write("[\n"); // begin Contacts array

				if (ci.hasNext()) {// if there is at least one contact

					while (true) { // endless loop
						Contact contact = ci.next(); // get contact
						Matcher matcher = pattern.matcher(contact.getName()); // test name field by filter
						if (!matcher.matches()) { // in Not match

							entityManager.detach(contact); // clean persistent context

							oos.write(new ObjectMapper().writeValueAsString(contact)); // write JSON representation of
																						// Contact
							if (ci.hasNext()) { // if there is one more contact
								oos.write(",\n"); // delimit JSON records
								oos.flush(); // flush output
							} else {
								break; // if there are no more contacts, interrupt the endless loop
							}
						}
					}
				}

				oos.write("\n]"); // end Contacts array
				oos.flush(); // flush output
			} catch (JsonProcessingException e) {
				log.error("writeTo] - Exception {}", e);
				throw new RuntimeException("Error on JSON processing. " + e.getMessage(), e.getCause());
			}
		}
	}

}
