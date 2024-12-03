package org.roleonce.enterprise_project.dao;

import org.roleonce.enterprise_project.model.Movie;

import java.util.List;

public interface MovieDAO {

    List<Movie> findByTitle(String title);
    List<Movie> findByTitleAndOriginCountry(String title, List<String> originCountry);
    List<Movie> findAllOrderByBudgetDesc();
    List<Movie> findAllOrderByVoteAverageDesc();

}