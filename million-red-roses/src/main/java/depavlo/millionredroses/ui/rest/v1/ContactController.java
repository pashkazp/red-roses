package depavlo.millionredroses.ui.rest.v1;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
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
			@RequestParam(value = "nameFilter", required = true) String filter) {

		log.debug("listAllContacts] - Get Contacts by use filter '{}'", filter);

		StreamingResponseBody body = contactService::writeTo;
		contactService.setFilter(filter);

		return new ResponseEntity<>(body, HttpStatus.OK);
	}

	/**
	 * Handle other exceptions.
	 *
	 * @param ex      the Exception
	 * @param request the WebRequest
	 * @return the response entity
	 */
	@ExceptionHandler(value = { Exception.class })
	@ResponseBody()
	public ResponseEntity<Object> handleOtherExceptions(Exception ex, WebRequest request) {

		log.debug("handleOtherExceptions] - Gets exception: {}", ex.getMessage());

		String headers = request.getHeader(HttpHeaders.ACCEPT);

		MediaType mt;
		if (headers.indexOf(MediaType.APPLICATION_XML_VALUE) == -1) {
			mt = MediaType.APPLICATION_JSON;
		} else {
			mt = MediaType.APPLICATION_XML;
		}

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(mt).body(ex.getMessage());
	}

}
