package com.irem.filmAddicts.repository;

import com.irem.filmAddicts.entity.User;
import com.irem.filmAddicts.entity.Watchlist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WatchlistRepository extends JpaRepository<Watchlist, Long> {
    Watchlist findByUser(User user);
}
