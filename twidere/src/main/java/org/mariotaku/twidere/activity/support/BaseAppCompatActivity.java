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

package org.mariotaku.twidere.activity.support;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.view.MenuItem;

import org.mariotaku.twidere.Constants;
import org.mariotaku.twidere.R;
import org.mariotaku.twidere.activity.iface.IControlBarActivity;
import org.mariotaku.twidere.app.TwidereApplication;
import org.mariotaku.twidere.fragment.iface.IBaseFragment.SystemWindowsInsetsCallback;
import org.mariotaku.twidere.util.AsyncTwitterWrapper;
import org.mariotaku.twidere.util.KeyboardShortcutsHandler;
import org.mariotaku.twidere.util.KeyboardShortcutsHandler.KeyboardShortcutCallback;
import org.mariotaku.twidere.util.ThemeUtils;
import org.mariotaku.twidere.view.iface.IExtendedView.OnFitSystemWindowsListener;

import java.util.ArrayList;

@SuppressLint("Registered")
public class BaseAppCompatActivity extends ThemedAppCompatActivity implements Constants,
        OnFitSystemWindowsListener, SystemWindowsInsetsCallback, IControlBarActivity,
        KeyboardShortcutCallback {

    // Utility classes
    private KeyboardShortcutsHandler mKeyboardShortcutsHandler;

    // Registered listeners
    private ArrayList<ControlBarOffsetListener> mControlBarOffsetListeners = new ArrayList<>();

    // Data fields
    private boolean mInstanceStateSaved;
    private boolean mIsVisible;
    private Rect mSystemWindowsInsets;

    @Override
    public boolean getSystemWindowsInsets(Rect insets) {
        if (mSystemWindowsInsets == null) return false;
        insets.set(mSystemWindowsInsets);
        return true;
    }

    @Override
    public int getThemeColor() {
        return ThemeUtils.getUserAccentColor(this);
    }

    @Override
    public int getThemeResourceId() {
        return ThemeUtils.getNoActionBarThemeResource(this);
    }

    public TwidereApplication getTwidereApplication() {
        return (TwidereApplication) getApplication();
    }

    public AsyncTwitterWrapper getTwitterWrapper() {
        return getTwidereApplication() != null ? getTwidereApplication().getTwitterWrapper() : null;
    }

    public boolean isVisible() {
        return mIsVisible;
    }

    @Override
    public void onFitSystemWindows(Rect insets) {
        if (mSystemWindowsInsets == null)
            mSystemWindowsInsets = new Rect(insets);
        else {
            mSystemWindowsInsets.set(insets);
        }
        notifyControlBarOffsetChanged();
    }

    @Override
    public boolean onKeyUp(int keyCode, @NonNull KeyEvent event) {
        if (handleKeyboardShortcutSingle(mKeyboardShortcutsHandler, keyCode, event)) return true;
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (handleKeyboardShortcutRepeat(mKeyboardShortcutsHandler, keyCode, event.getRepeatCount(), event))
            return true;
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.back: {
                onBackPressed();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void startActivity(final Intent intent) {
        super.startActivity(intent);
    }

    @Override
    public boolean handleKeyboardShortcutSingle(@NonNull KeyboardShortcutsHandler handler, int keyCode, @NonNull KeyEvent event) {
        return false;
    }

    @Override
    public boolean handleKeyboardShortcutRepeat(@NonNull KeyboardShortcutsHandler handler, int keyCode, int repeatCount, @NonNull KeyEvent event) {
        return false;
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mKeyboardShortcutsHandler = TwidereApplication.getInstance(this).getKeyboardShortcutsHandler();
    }


    @Override
    protected void onStart() {
        super.onStart();
        mIsVisible = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mInstanceStateSaved = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        mInstanceStateSaved = true;
        super.onSaveInstanceState(outState);
    }

    @Override
    public void startActivityForResult(final Intent intent, final int requestCode) {
        super.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onStop() {
        mIsVisible = false;
        super.onStop();
    }

    @Override
    public void setControlBarOffset(float offset) {

    }

    @Override
    public void setControlBarVisibleAnimate(boolean visible) {

    }

    @Override
    public float getControlBarOffset() {
        return 0;
    }

    @Override
    public int getControlBarHeight() {
        return 0;
    }

    @Override
    public void notifyControlBarOffsetChanged() {
        final float offset = getControlBarOffset();
        for (final ControlBarOffsetListener l : mControlBarOffsetListeners) {
            l.onControlBarOffsetChanged(this, offset);
        }
    }

    @Override
    public void registerControlBarOffsetListener(ControlBarOffsetListener listener) {
        mControlBarOffsetListeners.add(listener);
    }

    @Override
    public void unregisterControlBarOffsetListener(ControlBarOffsetListener listener) {
        mControlBarOffsetListeners.remove(listener);
    }

}
