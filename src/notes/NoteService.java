package notes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.security.sasl.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import notes.models.Message;
import notes.models.User;

@Path("/")
public class NoteService {

	private static final String USER = "user";
	private ObjectMapper mapper = new ObjectMapper();
	private EntityManager em = PersistenceManager.getEm();

	public NoteService() {
		mapper.configure(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.setVisibilityChecker(
				mapper.getSerializationConfig().getDefaultVisibilityChecker().withFieldVisibility(JsonAutoDetect.Visibility.NONE)
						.withGetterVisibility(JsonAutoDetect.Visibility.ANY).withSetterVisibility(JsonAutoDetect.Visibility.ANY));
	}

	private boolean authorize(HttpServletRequest req) throws AuthenticationException {
		if (req.getSession().getAttribute(USER) == null) {
			throw new AuthenticationException();
		}
		return true;
	}

	@Path("/getMessages")
	@GET
	public String getMessages() throws JsonGenerationException, JsonMappingException, IOException {
		TypedQuery<Message> q = em.createNamedQuery("Message.all", Message.class);
		List<Message> resultList = q.getResultList();
		return mapper.writeValueAsString(new ArrayList<>(resultList));
	}

	@Path("/addMessage")
	@GET
	public String addMessage(@Context HttpServletRequest req, @QueryParam("message") String message)
			throws JsonGenerationException, JsonMappingException, IOException {
		authorize(req);
		Message m = new Message();
		m.setText(message);
		m.setOwner(getCurrentUser(req));
		m = PersistenceManager.save(m, Message.class);
		return mapper.writeValueAsString(m);
	}

	@Path("/editMessage")
	@GET
	public void editMessage(@Context HttpServletRequest req, @QueryParam("message") String message,
			@QueryParam("messageId") String id) throws AuthenticationException {
		authorize(req);
		Message m = PersistenceManager.getEm().find(Message.class, Integer.valueOf(id));
		m.setText(message);
		PersistenceManager.save(m);
	}

	@Path("/addUser")
	@GET
	public void addUser(@Context HttpServletRequest req, @QueryParam("user") String user, @QueryParam("messageId") String id)
			throws AuthenticationException {
		authorize(req);
		User u = PersistenceManager.getEm().find(User.class, user);
		Message m = PersistenceManager.getEm().find(Message.class, Integer.valueOf(id));
		if (u == null || m == null) {
			throw new IllegalArgumentException("Nieprawid³owe argumenty");
		}
		if (m.getEditors().contains(u)) {
			throw new IllegalArgumentException("U¿ytkownik ju¿ jest edytorem!");
		}
		m.getEditors().add(u);
		PersistenceManager.save(m);
	}

	@Path("/removeUser")
	@GET
	public void removeUser(@Context HttpServletRequest req, @QueryParam("user") String user, @QueryParam("messageId") String id)
			throws AuthenticationException {
		authorize(req);
		User u = PersistenceManager.getEm().find(User.class, user);
		Message m = PersistenceManager.getEm().find(Message.class, Integer.valueOf(id));
		if (u == null || m == null) {
			throw new IllegalArgumentException("Nieprawid³owe argumenty");
		}
		if (!m.getEditors().contains(u)) {
			throw new IllegalArgumentException("U¿ytkownik nie jest edytorem!");
		}
		m.getEditors().remove(u);
		PersistenceManager.save(m);
	}

	@Path("/deleteMessage")
	@GET
	public void deleteMessage(@Context HttpServletRequest req, @QueryParam("messageId") String messageId)
			throws AuthenticationException {
		authorize(req);
		Message m = PersistenceManager.getEm().find(Message.class, Integer.valueOf(messageId));
		if (m == null) {
			throw new IllegalArgumentException("Nie znaleziono wiadomoœci o id " + messageId);
		}
		if (m.getOwner().equals(getCurrentUser(req))) {
			PersistenceManager.delete(m);
		} else {
			throw new AuthenticationException("Nie mo¿na usun¹æ cudzej wiadomoœci");
		}
	}

	@Path("/getUsers")
	@GET
	public String getUsers(@Context HttpServletRequest req) throws JsonGenerationException, JsonMappingException, IOException {
		TypedQuery<User> query = em.createNamedQuery("User.all", User.class);
		return mapper.writeValueAsString(query.getResultList());
	}

	@Path("/getUser")
	@GET
	public String getUser(@Context HttpServletRequest req) throws JsonGenerationException, JsonMappingException, IOException {
		User u = getCurrentUser(req);
		return mapper.writeValueAsString(u);
	}

	@Path("/login")
	@GET
	public String login(@Context HttpServletRequest req, @QueryParam("login") String login,
			@QueryParam("password") String password) throws JsonParseException, JsonMappingException, IOException {
		TypedQuery<User> query = em.createNamedQuery("User.find", User.class);
		query.setParameter(1, login);
		query.setParameter(2, password);
		List<User> found = query.getResultList();
		if (found.size() != 1) {
			return null;
		}
		User u = found.get(0);
		boolean logged = u != null;
		if (logged) {
			u.setLastLogin(new Date());
			PersistenceManager.save(u);
			req.getSession().setAttribute(USER, u);
			return mapper.writeValueAsString(u);
		} else {
			return null;
		}
	}

	@Path("/logout")
	@GET
	public void logout(@Context HttpServletRequest req) {
		req.getSession().removeAttribute(USER);
	}

	@Path("/register")
	@GET
	public void register(@Context HttpServletRequest req, @QueryParam("login") String login,
			@QueryParam("password") String password, @QueryParam("name") String name) throws AuthenticationException {
		if (login == null || login.isEmpty()) {
			throw new IllegalArgumentException("Login nie mo¿e byæ pusty!");
		}
		User existing = em.find(User.class, login);
		if (existing != null) {
			throw new IllegalArgumentException(message("U¿ytkownik o loginie", login, "ju¿ istnieje"));
		}
		User u = new User();
		u.setLogin(login);
		u.setPassword(password);
		u.setName(name);
		u.vaildate();
		PersistenceManager.save(u);
	}

	private String message(String... args) {
		return String.join(" ", args);

	}

	private User getCurrentUser(HttpServletRequest req) {
		return (User) req.getSession().getAttribute(USER);
	}

}
