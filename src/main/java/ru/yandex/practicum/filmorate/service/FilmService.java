package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public Film addFilm(Film film) {
        validate(film);
        return filmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) {
        Optional<Film> optionalFilm = filmStorage.updateFilm(film);
        validate(film);
        if (optionalFilm.isEmpty()) {
            log.error("Incorrect ID error: Film with this ID does not exist when updating");
            throw new ObjectNotFoundException("Film with this ID does not exist when updating");
        }

        log.info("Film '" + film.getName() + "' updated successfully");
        return optionalFilm.get();
    }

    public List<Film> getAllFilms() {
        log.info("All users returned successfully");
        return filmStorage.getAllFilms();
    }

    public Film getFilmById(Integer filmId) {
        Optional<Film> optionalFilm = filmStorage.getFilmById(filmId);

        if (optionalFilm.isEmpty()) {
            log.error("Incorrect ID error: Film with this ID does not exist when getting by ID");
            throw new ObjectNotFoundException("Film with this ID does not exist when getting by ID");
        }

        log.info("Film with ID '" + filmId + "' returned successfully");
        return optionalFilm.get();
    }

    public Film likeFilm(Integer filmId, Integer userId) {
        Film film = getFilmById(filmId);
        Optional<User> optionalUser = userStorage.getUserById(userId);

        if (optionalUser.isEmpty()) {
            log.error("Incorrect ID error: User with this ID does not exist when liking film");
            throw new ObjectNotFoundException("User with this ID does not exist when liking film");
        }

        film.getLikesIds().add(userId);
        log.info("Film with ID '" + filmId + "' successfully liked by user with ID '" + userId + "'");
        return film;
    }

    public Film unlikeFilm(Integer filmId, Integer userId) {
        Film film = getFilmById(filmId);

        if (!film.getLikesIds().contains(userId)) {
            log.error("Incorrect Argument error: Film '" + film + "' has not likes from user with ID '" + userId + "' when unliking");
            throw new ObjectNotFoundException("Film '" + film + "' has not likes from user with ID '" + userId + "' when unliking");
        }

        film.getLikesIds().remove(userId);
        log.info("Film with id '" + filmId + "' successfully unliked by user with ID '" + userId + "'");
        return film;
    }

    public List<Film> getMostLikedFilms(Integer count) {
        List<Film> films = new ArrayList<>(getAllFilms());

        log.info("Most popular films returned successfully");
        return films.stream()
                .sorted(this::compare)
                .skip(0)
                .limit(count)
                .collect(Collectors.toList());
    }

    private int compare(Film f0, Film f1) {
        return -1 * (f0.getLikesIds().size() - f1.getLikesIds().size());
    }

    public static void validate(Film film) {
        if (StringUtils.isBlank(film.getName())) {
            log.error("Validation error: Films name can't be blank");
            throw new ValidationException("Films name can't be blank");
        }

        if (film.getDescription().length() > 200) {
            log.error("Validation error: Films description can't be longer than 200 chars");
            throw new ValidationException("Films description can't be longer than 200 chars");
        }

        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.error("Validation error: Films release date can't be before 28.12.1895");
            throw new ValidationException("Films release date can't be before 28.12.1895");
        }

        if (film.getDuration() < 0) {
            log.error("Validation error: Films duration can't be negative");
            throw new ValidationException("Films duration can't be negative");
        }
    }
}
