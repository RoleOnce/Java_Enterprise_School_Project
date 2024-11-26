package org.roleonce.projektarbete_web_services.repository;


import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.roleonce.projektarbete_web_services.dao.MovieDAO;
import org.roleonce.projektarbete_web_services.model.Movie;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MovieDAOImpl implements MovieDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Movie> findByTitle(String title) {
        String jpql = "SELECT m FROM Movie m WHERE LOWER(m.title) LIKE LOWER(CONCAT('%', :title, '%'))";
        TypedQuery<Movie> query = entityManager.createQuery(jpql, Movie.class);
        query.setParameter("title", title);
        return query.getResultList();
    }

    @Override
    public List<Movie> findByTitleAndOriginCountry(String title, List<String> originCountry) {
        String jpql = "SELECT m FROM Movie m WHERE m.title = :title AND m.originCountry IN :originCountries";
        TypedQuery<Movie> query = entityManager.createQuery(jpql, Movie.class);
        query.setParameter("title", title);
        query.setParameter("originCountries", originCountry);
        return query.getResultList();
    }

    @Override
    public List<Movie> findAllOrderByBudgetDesc() {
        String jpql = "SELECT m FROM Movie m ORDER BY m.budget DESC";
        TypedQuery<Movie> query = entityManager.createQuery(jpql, Movie.class);
        return query.getResultList();
    }

    @Override
    public List<Movie> findAllOrderByVoteAverageDesc() {
        String jpql = "SELECT m FROM Movie m ORDER BY m.voteAverage DESC";
        TypedQuery<Movie> query = entityManager.createQuery(jpql, Movie.class);
        return query.getResultList();
    }
}