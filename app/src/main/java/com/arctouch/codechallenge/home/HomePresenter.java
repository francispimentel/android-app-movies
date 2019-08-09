package com.arctouch.codechallenge.home;

interface HomePresenter {

    void loadInitialData();

    void dispose();

    void loadNextPage();

    void triggerSearch(String searchTerm);

    boolean allPagesLoaded();

    void setSearchTerm(String searchTerm);
}
