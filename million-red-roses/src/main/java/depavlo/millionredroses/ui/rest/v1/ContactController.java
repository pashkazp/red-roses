package depavlo.millionredroses.ui.rest.v1;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import depavlo.millionredroses.service.ContactService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/hello/contacts")
@RequiredArgsConstructor
@Validated
@Slf4j
public class ContactController {

	private final ContactService contactService;

	@GetMapping(value = "", produces = { "application/json" })
	public ResponseEntity<StreamingResponseBody> listAllContacts(
			@RequestParam(value = "nameFilter", required = true) String filter, HttpServletResponse response) {

		StreamingResponseBody body = contactService::writeTo;
		contactService.setFilter(filter);

		return new ResponseEntity<>(body, HttpStatus.OK);
	}
}
