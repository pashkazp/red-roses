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

	private final ObjectMapper objectMapper;

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

				boolean hasPrintedPrevious = false;

				oos.write("[\n"); // begin Contacts array

				while (ci.hasNext()) { // endless loop
					Contact contact = ci.next(); // get contact
					Matcher matcher = pattern.matcher(contact.getName()); // test name field by filter

					entityManager.detach(contact); // clean persistent context

					if (!matcher.matches()) { // in Not match
						if (hasPrintedPrevious) {
							oos.write(",\n"); // delimit JSON records
						}
						oos.write(objectMapper.writeValueAsString(contact)); // write JSON representation of
						oos.flush(); // flush output
						hasPrintedPrevious = true;
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
