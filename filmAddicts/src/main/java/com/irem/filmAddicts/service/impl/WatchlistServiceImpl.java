package com.irem.filmAddicts.service.impl;

import com.irem.filmAddicts.entity.User;
import com.irem.filmAddicts.entity.Watchlist;
import com.irem.filmAddicts.repository.WatchlistRepository;
import com.irem.filmAddicts.service.WatchlistService;
import org.springframework.stereotype.Service;

@Service
public class WatchlistServiceImpl implements WatchlistService {
    private WatchlistRepository  watchlistRepository;

    public WatchlistServiceImpl(WatchlistRepository watchlistRepository) {
        this.watchlistRepository = watchlistRepository;

    }
    @Override
    public Watchlist findWatchlistByUser(User user) {
        return watchlistRepository.findByUser(user);
    }
}
