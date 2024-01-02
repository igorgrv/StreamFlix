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

    public static CategoryEnum fromString(String category) {
        for (CategoryEnum enumValue : CategoryEnum.values()) {
            if (enumValue.category.equalsIgnoreCase(category)) {
                return enumValue;
            }
        }
        throw new IllegalArgumentException("No category found given: " + category);
    }
}
