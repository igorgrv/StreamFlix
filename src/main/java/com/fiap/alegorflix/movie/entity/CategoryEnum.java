package com.fiap.alegorflix.movie.entity;

public enum CategoryEnum {
    COMEDY("Comedy"),
    SCIENCE_FICTION("Science Fiction"),
    ACTION("Action"),
    DRAMA("Drama"),
    ROMANCE("Romance"),
    OTHER("Other");

    private String category;

    CategoryEnum(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }
}
