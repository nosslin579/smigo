package org.smigo.species;

public class CrudResult {
    private final Integer id;
    private final Review review;

    public CrudResult(Integer id, Review review) {
        this.id = id;
        this.review = review;
    }

    public Integer getId() {
        return id;
    }

    public Review getReview() {
        return review;
    }
}
