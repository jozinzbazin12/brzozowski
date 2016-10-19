package notes;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class PersistenceManager {
	private static EntityManager em;
	static {
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("notes");
		em = factory.createEntityManager();
	}

	public static <T> T save(T o, Class<T> type) {
		em.getTransaction().begin();
		T result = em.merge(o);
		em.flush();
		em.getTransaction().commit();
		return result;
	}

	public static void save(Object o) {
		save(o, Object.class);
	}

	public static void delete(Object o) {
		em.getTransaction().begin();
		em.remove(o);
		em.getTransaction().commit();
	}

	public static EntityManager getEm() {
		return em;
	}

}
