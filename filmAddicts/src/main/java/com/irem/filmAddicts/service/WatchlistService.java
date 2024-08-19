package com.irem.filmAddicts.service;

import com.irem.filmAddicts.entity.User;
import com.irem.filmAddicts.entity.Watchlist;

public interface WatchlistService {
    Watchlist findWatchlistByUser(User user);
}
