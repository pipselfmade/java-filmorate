package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class GenreService {
    private final GenreStorage genreStorage;

    public Genre getGenre(Integer genreId) {
        Optional<Genre> genre = genreStorage.getGenre(genreId);

        if (genre.isEmpty()) {
            throw new ObjectNotFoundException("Genre with id " + genreId + " not found");
        }

        log.debug("Genre with id'" + genreId + "' returned successfully");
        return genre.get();
    }

    public Collection<Genre> getAllGenres() {
        List<Genre> genreList = genreStorage.getAllGenres();

        if (genreList.isEmpty()) {
            throw new ObjectNotFoundException("Genres not found");
        }

        log.debug("All genres returned successfully");
        return genreList;
    }
}
