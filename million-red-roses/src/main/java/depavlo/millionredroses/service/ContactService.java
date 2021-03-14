package depavlo.millionredroses.service;

import java.io.IOException;
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
		Pattern pattern = Pattern.compile(filter);
		try (Stream<Contact> contactResultStream = dao.getAll()) {
			try (PrintWriter oos = new PrintWriter(outputStream)) {
				Iterator<Contact> ci = contactResultStream.iterator();

				oos.write("[\n");

				if (ci.hasNext()) {

					while (true) {
						Contact contact = ci.next();
						Matcher matcher = pattern.matcher(contact.getName());
						if (!matcher.matches()) {
							oos.write(new ObjectMapper().writeValueAsString(contact));
							if (ci.hasNext()) {
								oos.write(",\n");
								oos.flush();
							} else {
								break;
							}
						}
						entityManager.detach(contact);
					}
				}

				oos.write("\n]");
				oos.flush();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
