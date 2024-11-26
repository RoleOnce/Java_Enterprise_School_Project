package org.roleonce.projektarbete_web_services.dao;

import org.roleonce.projektarbete_web_services.model.Movie;

import java.util.List;

public interface MovieDAO {

    List<Movie> findByTitle(String title);
    List<Movie> findByTitleAndOriginCountry(String title, List<String> originCountry);
    List<Movie> findAllOrderByBudgetDesc();
    List<Movie> findAllOrderByVoteAverageDesc();

}
