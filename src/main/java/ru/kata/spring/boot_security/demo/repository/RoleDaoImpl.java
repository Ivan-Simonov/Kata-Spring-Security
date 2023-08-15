package ru.kata.spring.boot_security.demo.repository;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;

import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Repository
public class RoleDaoImpl implements RoleDao{

    private final SessionFactory sessionFactory;

    public RoleDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Role> findByName(String name) {
        TypedQuery<Role> query = sessionFactory.getCurrentSession().createQuery("from Role r where r.name = :name");
        query.setParameter("name", name);
        return Optional.of(query.getSingleResult());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Role> findAll() {
        TypedQuery<Role> query=sessionFactory.getCurrentSession().createQuery("from Role");
        return query.getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Role> findById(Long id) {
        TypedQuery<Role> query = sessionFactory.getCurrentSession().createQuery("from Role r where r.id = :id");
        query.setParameter("id", id);
        return Optional.of(query.getSingleResult());
    }
}
