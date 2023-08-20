package ru.kata.spring.boot_security.demo.repository;

import org.hibernate.SessionFactory;
import org.hibernate.boot.model.naming.IllegalIdentifierException;
import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.model.User;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Repository
public class UserDaoImpl implements UserDao {

    private final SessionFactory sessionFactory;

    public UserDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    @Transactional (readOnly = true)
    public Optional<User> findByEmail(String email) {
        TypedQuery<User> query = sessionFactory.getCurrentSession().createQuery("FROM User u WHERE u.email = :email");
        query.setParameter("email", email);
        return Optional.of(query.getSingleResult());
    }

    @Override
    @Transactional (readOnly = true)
    public Optional<User> findById(Long id) {
        TypedQuery<User> query = sessionFactory.getCurrentSession().createQuery("FROM User u WHERE u.id = :id");
        query.setParameter("id", id);
        return Optional.of(query.getSingleResult());
    }

    @Override
    @Transactional
    public void save(User user) {
        sessionFactory.getCurrentSession().saveOrUpdate(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findAll() {
        TypedQuery<User> query=sessionFactory.getCurrentSession().createQuery("FROM User");
        return query.getResultList();
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Optional<User> user = findById(id);
        if (user.isEmpty()) {
            throw new IllegalIdentifierException("Could not find the user with id: " + id);
        } else {
            sessionFactory.getCurrentSession().delete(user.get());
        }
    }
}
