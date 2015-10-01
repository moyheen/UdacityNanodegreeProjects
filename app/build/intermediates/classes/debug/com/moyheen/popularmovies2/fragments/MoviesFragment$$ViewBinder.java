// Generated code from Butter Knife. Do not modify!
package com.moyheen.popularmovies2.fragments;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class MoviesFragment$$ViewBinder<T extends com.moyheen.popularmovies2.fragments.MoviesFragment> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131558492, "field 'mProgressBar'");
    target.mProgressBar = finder.castView(view, 2131558492, "field 'mProgressBar'");
    view = finder.findRequiredView(source, 2131558493, "field 'mRecyclerView'");
    target.mRecyclerView = finder.castView(view, 2131558493, "field 'mRecyclerView'");
  }

  @Override public void unbind(T target) {
    target.mProgressBar = null;
    target.mRecyclerView = null;
  }
}
