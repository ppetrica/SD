import ejb.CourseEntity;

import javax.persistence.*;

public class DBManager {
    public static <T> void saveEntity(T entity) {
        EntityManagerFactory factory =
                Persistence.createEntityManagerFactory("bazaDeDateSQLite");
        EntityManager em = factory.createEntityManager();

        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        em.persist(entity);
        transaction.commit();

        em.close();
        factory.close();
    }

    public static void removeEntity(int id) throws PersistenceException{
        EntityManagerFactory factory;

        factory = Persistence.createEntityManagerFactory("bazaDeDateSQLite");

        EntityManager entityManager = factory.createEntityManager();

        EntityTransaction transaction = entityManager.getTransaction();

        transaction.begin();
        try {
            entityManager.remove(entityManager.find(CourseEntity.class, id));

            transaction.commit();
        } catch (PersistenceException e) {
            transaction.rollback();

            throw e;
        } finally {
            entityManager.close();
            factory.close();
        }
    }
}
