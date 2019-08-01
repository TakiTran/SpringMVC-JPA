package com.topica.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.topica.model.Word;
import com.topica.pagination.PaginationResult;

@Repository
@Transactional
public class WordDaoImpl implements WordDao {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public PaginationResult<Word> getAll(int page) {
		org.hibernate.query.Query<Word> query = (org.hibernate.query.Query<Word>)entityManager.createQuery("FROM Word", Word.class);
		PaginationResult<Word> result = new PaginationResult<Word>(query, page, PaginationResult.recordInPage, 10);
		return result;
	}

	@Override
	public List<Word> absoluteSearch(String word, int type) {
		List<Word> words = null;
		TypedQuery<Word> query = entityManager.createQuery("FROM Word as w where w.type = :type and w.key = :word", Word.class);
		query.setParameter("type", type);
		query.setParameter("word", word);
		words = query.getResultList();
		return words;
	}
	
	@Override
	public List<Word> relativeSearch(String word, int type) {
		List<Word> words = null;
		TypedQuery<Word> query = entityManager.createQuery("FROM Word as w where w.type = :type and w.key like concat('%',:word,'%')", Word.class);
		query.setParameter("type", type);
		query.setParameter("word", word);
		words = query.getResultList();
		return words;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public PaginationResult<Word> relativeSearchPage(String word, int type, int page) {
		org.hibernate.query.Query<Word> query = (org.hibernate.query.Query<Word>) entityManager.createQuery("FROM Word as w where w.type = :type and w.key like concat('%',:word,'%')");
		query.setParameter("type", type);
		query.setParameter("word", word);
		PaginationResult<Word> result = new PaginationResult<Word>(query, page, PaginationResult.recordInPage, 10);
		return result;
	}

	@Transactional
	@Override
	public void saveWord(Word word) {
		try {
			entityManager.persist(word);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	@Transactional
	@Override
	public void updateWord(Word word) {
		Query query = entityManager.createQuery("UPDATE Word as w SET w.mean = :mean where w.id = :id");
		query.setParameter("mean", word.getMean());
		query.setParameter("id", word.getId());
		query.executeUpdate();
	}

	@Transactional
	@Override
	public void deleteWord(Word word) {
		Query query = entityManager.createQuery("DELETE Word as w where w.id = :id");
		query.setParameter("id", word.getId());
		query.executeUpdate();
	}

	@Override
	public boolean checkKeyExists(String key) {
		TypedQuery<Word> query = entityManager.createQuery("FROM Word as w where w.key = :key", Word.class);
		query.setParameter("key", key);
		List<Word> words = query.getResultList();
		if(words.isEmpty()) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public List<Word> getByType(int type) {
		List<Word> words = null;
		TypedQuery<Word> query = entityManager.createQuery("FROM Word as w where w.type = :type", Word.class);
		query.setParameter("type", type);
		words = query.getResultList();
		return words;
	}

	@Override
	public Word findById(int id) {
		Word w = null;
		TypedQuery<Word> query = entityManager.createQuery("FROM Word as w where w.id = :id", Word.class);
		query.setParameter("id", id);
		List<Word> words = query.getResultList();
		if(!words.isEmpty()) {
			w = words.get(0);
		}
		return w;
	}

}
