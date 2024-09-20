package com.backend.before.strategy;

import com.backend.before.exception.BadRequestException;
import com.backend.before.exception.ErrorMessages;

public enum TemperatureRating {
    FIVE_STARS(5, 40),
    FOUR_STARS(4, 30),
    THREE_STARS(3, 10),
    TWO_STARS(2, -10),
    ONE_STAR(1, -30);

    private final int stars;
    private final int temperatureChange;

    TemperatureRating(int stars, int temperatureChange) {
        this.stars = stars;
        this.temperatureChange = temperatureChange;
    }

    public static TemperatureRating fromStars(int stars) {
        for (TemperatureRating rating : values()) {
            if (rating.getStars() == stars) {
                return rating;
            }
        }
        throw new BadRequestException(ErrorMessages.INVALID_RATING);
    }

    public int getStars() {
        return stars;
    }

    public int getTemperatureChange() {
        return temperatureChange;
    }
}
