/*
 * 				Twidere - Twitter client for Android
 * 
 *  Copyright (C) 2012-2014 Mariotaku Lee <mariotaku.lee@gmail.com>
 * 
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.mariotaku.twidere.fragment.support;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.Loader;

import org.mariotaku.twidere.loader.support.UserSearchLoader;
import org.mariotaku.twidere.model.ParcelableUser;

import java.util.List;

public class SearchUsersFragment extends ParcelableUsersFragment {

    private int mPage = 1;

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mPage = savedInstanceState.getInt(EXTRA_PAGE, 1);
        }
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<List<ParcelableUser>> onCreateUsersLoader(final Context context, final Bundle args, boolean fromUser) {
        if (args == null) return null;
        final long account_id = args.getLong(EXTRA_ACCOUNT_ID);
        final String query = args.getString(EXTRA_QUERY);
        final int page = args.getInt(EXTRA_PAGE, 1);
        return new UserSearchLoader(context, account_id, query, page, getData(), fromUser);
    }

    @Override
    public void onLoadFinished(final Loader<List<ParcelableUser>> loader, final List<ParcelableUser> data) {
        super.onLoadFinished(loader, data);
        if (loader instanceof UserSearchLoader) {
            mPage = ((UserSearchLoader) loader).getPage();
        }
    }

    @Override
    public void onLoadMoreContents() {
        super.onLoadMoreContents();
        final Bundle loaderArgs = new Bundle(getArguments());
        loaderArgs.putBoolean(EXTRA_FROM_USER, true);
        loaderArgs.putInt(EXTRA_PAGE, mPage + 1);
        getLoaderManager().restartLoader(0, loaderArgs, this);
    }

    @Override
    public void onSaveInstanceState(final Bundle outState) {
        outState.putInt(EXTRA_PAGE, mPage);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroyView() {
        mPage = 1;
        super.onDestroyView();
    }

}
