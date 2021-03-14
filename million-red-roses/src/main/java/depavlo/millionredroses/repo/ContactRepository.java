package depavlo.millionredroses.repo;

import java.util.stream.Stream;

import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import depavlo.millionredroses.model.Contact;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {

	final String fetchSize = String.valueOf(Integer.MIN_VALUE);

	@QueryHints(value = {
			@QueryHint(name = "HINT_FETCH_SIZE", value = "" + Integer.MIN_VALUE),
			@QueryHint(name = "HINT_CACHEABLE", value = "false"),
			@QueryHint(name = "READ_ONLY", value = "true")
	})
	@Query(value = "SELECT * FROM contacts", nativeQuery = true)
	Stream<Contact> getAll();

}
