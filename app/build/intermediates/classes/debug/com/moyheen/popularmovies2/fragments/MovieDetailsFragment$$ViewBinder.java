// Generated code from Butter Knife. Do not modify!
package com.moyheen.popularmovies2.fragments;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class MovieDetailsFragment$$ViewBinder<T extends com.moyheen.popularmovies2.fragments.MovieDetailsFragment> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131558482, "field 'movieTitle'");
    target.movieTitle = finder.castView(view, 2131558482, "field 'movieTitle'");
    view = finder.findRequiredView(source, 2131558483, "field 'moviePoster'");
    target.moviePoster = finder.castView(view, 2131558483, "field 'moviePoster'");
    view = finder.findRequiredView(source, 2131558484, "field 'movieYear'");
    target.movieYear = finder.castView(view, 2131558484, "field 'movieYear'");
    view = finder.findRequiredView(source, 2131558485, "field 'movieRating'");
    target.movieRating = finder.castView(view, 2131558485, "field 'movieRating'");
    view = finder.findRequiredView(source, 2131558486, "field 'favorite'");
    target.favorite = finder.castView(view, 2131558486, "field 'favorite'");
    view = finder.findRequiredView(source, 2131558489, "field 'noTrailer'");
    target.noTrailer = finder.castView(view, 2131558489, "field 'noTrailer'");
    view = finder.findRequiredView(source, 2131558491, "field 'noReview'");
    target.noReview = finder.castView(view, 2131558491, "field 'noReview'");
    view = finder.findRequiredView(source, 2131558487, "field 'movieDescription'");
    target.movieDescription = finder.castView(view, 2131558487, "field 'movieDescription'");
    view = finder.findRequiredView(source, 2131558488, "field 'mLayoutTrailers'");
    target.mLayoutTrailers = finder.castView(view, 2131558488, "field 'mLayoutTrailers'");
    view = finder.findRequiredView(source, 2131558490, "field 'mLayoutReviews'");
    target.mLayoutReviews = finder.castView(view, 2131558490, "field 'mLayoutReviews'");
  }

  @Override public void unbind(T target) {
    target.movieTitle = null;
    target.moviePoster = null;
    target.movieYear = null;
    target.movieRating = null;
    target.favorite = null;
    target.noTrailer = null;
    target.noReview = null;
    target.movieDescription = null;
    target.mLayoutTrailers = null;
    target.mLayoutReviews = null;
  }
}
