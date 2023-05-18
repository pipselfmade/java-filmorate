package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;



@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
@Slf4j
public class FilmController {
    private final FilmService filmService;

    @PostMapping
    private Film addFilm(@Valid @RequestBody Film film) {
        log.info("Film '" + film.getName() + "' added successfully");
        return filmService.addFilm(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        log.info("Movie data update: {}", film);
        return filmService.updateFilm(film);
    }

    @DeleteMapping("/{filmId}")
    private Film deleteFilm(@Valid @PathVariable Integer filmId) {
        log.info("Film '" + filmService.deleteFilm(filmId).getName() + "' delete successfully");
        return filmService.deleteFilm(filmId);
    }

    @GetMapping
    public List<Film> getAllFilms() {
        return filmService.getAllFilms();
    }

    @GetMapping("/{filmId}")
    public Film getFilmById(@PathVariable Integer filmId) {
        return filmService.getFilmById(filmId);
    }

    @PutMapping("/{filmId}/like/{userId}")
    public Film likeFilm(@PathVariable Integer filmId,
                         @PathVariable Integer userId) {
        return filmService.likeFilm(filmId, userId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public Film unlikeFilm(@PathVariable Integer filmId,
                           @PathVariable Integer userId) {
        return filmService.unlikeFilm(filmId, userId);
    }

    @GetMapping("/popular")
    public List<Film> getMostLikedFilms(@RequestParam(required = false, defaultValue = "10") Integer count) {
        return filmService.getMostLikedFilms(count);
    }
}
