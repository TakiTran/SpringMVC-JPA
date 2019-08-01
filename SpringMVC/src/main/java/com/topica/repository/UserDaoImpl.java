package com.topica.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;


import org.springframework.stereotype.Repository;

import com.topica.model.User;

@Repository
public class UserDaoImpl implements UserDao {

	@PersistenceContext
	private EntityManager entityManager;

	@Transactional
	@Override
	public List<User> getUser(String username) {
		TypedQuery<User> query = entityManager.createQuery("FROM User where username = :username", User.class);
		query.setParameter("username", username);
		List<User> users = null;
		users = query.getResultList();
		return users;
	}

}
