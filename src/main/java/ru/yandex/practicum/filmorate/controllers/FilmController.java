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
    private final FilmService service;

    @PostMapping
    private Film addFilm(@Valid @RequestBody Film film) {
        log.info("Film '" + film.getName() + "' added successfully");
        return service.addFilm(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        log.info("Movie data update: {}", film);
        return service.updateFilm(film);
    }

    @GetMapping
    public List<Film> getAllFilms() {
        return service.getAllFilms();
    }

    @GetMapping("/{filmId}")
    public Film getFilmById(@PathVariable Integer filmId) {
        return service.getFilmById(filmId);
    }

    @PutMapping("/{filmId}/like/{userId}")
    public Film likeFilm(@PathVariable Integer filmId,
                         @PathVariable Integer userId) {
        return service.likeFilm(filmId, userId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public Film unlikeFilm(@PathVariable Integer filmId,
                           @PathVariable Integer userId) {
        return service.unlikeFilm(filmId, userId);
    }

    @GetMapping("/popular")
    public List<Film> getMostLikedFilms(@RequestParam(required = false, defaultValue = "10") Integer count) {
        return service.getMostLikedFilms(count);
    }
}
