package pl.mareklangiewicz.myintent;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import pl.mareklangiewicz.myfragments.MyFragment;
import pl.mareklangiewicz.myutils.MyLogLevel;
import pl.mareklangiewicz.myviews.IMyNavigation;

public final class MIStartFragment extends MyFragment implements PlayStopButton.Listener, Countdown.Listener {

    private @Nullable View mRootView;
    private @Nullable MenuItem mSearchItem;
    private @Nullable SearchView mSearchView;
    private @Nullable EditText mEditText;

    private final @NonNull MyMDAndroLogAdapter mAdapter = new MyMDAndroLogAdapter(log.getHistory());

    Function1<Unit, Unit> sub = null;

    private @Nullable RecyclerView mRecyclerView;
    private @Nullable FloatingActionButton mFAB;

    private @Nullable PlayStopButton mPSButton;
    private @Nullable Countdown mCountdown;


    private final Runnable mRunUpdateButtons = new Runnable() {
        @Override public void run() {
            updateFAB();
            updatePS();
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState); //just for logging

        setHasOptionsMenu(true);

        mRootView = inflater.inflate(R.layout.mi_log_fragment, container, false);

        mCountdown = new Countdown((ProgressBar) mRootView.findViewById(R.id.progress_bar));
        mCountdown.setListener(this);

        mEditText = (EditText) mRootView.findViewById(R.id.edit_text);
        //noinspection ConstantConditions
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override public void afterTextChanged(Editable s) { updatePS(); }
        });

        mFAB = getFAB();

        mPSButton = new PlayStopButton((ImageView) mRootView.findViewById(R.id.play_stop_image_view));
        mPSButton.setListener(this);

        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.mi_log_recycler_view);
        mRecyclerView.setAdapter(mAdapter);

        sub = log.getHistory().getChanges().invoke(new Function1<Unit, Unit>() {
            @Override public Unit invoke(Unit unit) {
                mAdapter.notifyDataSetChanged();
                return null;
            }
        });
        mAdapter.notifyDataSetChanged(); // to make sure we are up to date

        //TODO SOMEDAY: some nice simple header with fragment title
        inflateMenu(R.menu.mi_log_local);
        updateCheckedItem();

        //noinspection ConstantConditions
        mFAB.setImageResource(R.drawable.mi_ic_mic_white_24dp);
//        CoordinatorLayout.LayoutParams lparams = ((CoordinatorLayout.LayoutParams) mFAB.getLayoutParams());
//        lparams.setAnchorId(R.id.mi_log_recycler_view);
//        int margin = (int)((MyActivity)getActivity()).dp2px(8);
//        lparams.setMargins(margin, margin, margin, margin);
//        lparams.anchorGravity = Gravity.END;

        mFAB.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                if(mCountdown != null)
                    mCountdown.cancel();
                if(mEditText != null)
                    mEditText.setText("");
                ((MIActivity) getActivity()).onCommand("start custom action listen");
            }
        });

        return mRootView;

    }

    @Override public void onResume() {
        super.onResume();
        lazyUpdateButtons();
    }

    @Override public void onStop() {
        if(mCountdown != null)
            mCountdown.cancel();
        super.onStop();
    }

    @Override
    public void onDestroyView() {

        //noinspection ConstantConditions
        mRootView.removeCallbacks(mRunUpdateButtons);
        mRootView = null;

        if(mPSButton != null) {
            mPSButton.setListener(null);
            mPSButton.setState(PlayStopButton.HIDDEN);
            mPSButton = null;
        }

        if(mFAB != null) {
            mFAB.setOnClickListener(null);
            mFAB.hide();
            mFAB = null;
        }

        if(mCountdown != null) {
            mCountdown.cancel();
            mCountdown.setListener(null);
            mCountdown = null;
        }

        mSearchItem = null;
        mSearchView = null;

        mEditText = null;

        if(sub != null) {
            sub.invoke(Unit.INSTANCE);
            sub = null;
        }
        //noinspection ConstantConditions
        mRecyclerView.setAdapter(null);
        mRecyclerView = null;
        super.onDestroyView();
    }

    @Override public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.mi_log_options, menu);
        mSearchItem = menu.findItem(R.id.action_search);
        mSearchView = (SearchView) mSearchItem.getActionView();
        SearchManager manager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        //noinspection ConstantConditions
        mSearchView.setSearchableInfo(manager.getSearchableInfo(getActivity().getComponentName()));
        mSearchView.setIconifiedByDefault(true);
    }

    @Override
    public boolean onItemSelected(IMyNavigation nav, MenuItem item) {
        @IdRes int id = item.getItemId();
        if(id == R.id.log_level_error) {
            log.getHistory().setLevel(MyLogLevel.ERROR);
            return true;
        }
        else if(id == R.id.log_level_warning) {
            log.getHistory().setLevel(MyLogLevel.WARN);
            return true;
        }
        else if(id == R.id.log_level_info) {
            log.getHistory().setLevel(MyLogLevel.INFO);
            return true;
        }
        else if(id == R.id.log_level_debug) {
            log.getHistory().setLevel(MyLogLevel.DEBUG);
            return true;
        }
        else if(id == R.id.log_level_verbose) {
            log.getHistory().setLevel(MyLogLevel.VERBOSE);
            return true;
        }
        else if(id == R.id.clear_log_history) {
            log.getHistory().clear();
            return true;
        }
        else if(id == R.id.log_some_assert) {
            log.a("some assert");
            return true;
        }
        else if(id == R.id.log_some_error) {
            log.e("some error");
            return true;
        }
        else if(id == R.id.log_some_warning) {
            log.w("some warning");
            return true;
        }
        else if(id == R.id.log_some_info) {
            log.i("some info");
            return true;
        }
        else if(id == R.id.log_some_debug) {
            log.d("some debug");
            return true;
        }
        else if(id == R.id.log_some_verbose) {
            log.v("some verbose");
            return true;
        }
        return super.onItemSelected(nav, item);
    }

    private void updateCheckedItem() {
        switch(log.getHistory().getLevel()) {
            case ERROR:
                setCheckedItem(R.id.log_level_error);
                break;
            case WARN:
                setCheckedItem(R.id.log_level_warning);
                break;
            case INFO:
                setCheckedItem(R.id.log_level_info);
                break;
            case DEBUG:
                setCheckedItem(R.id.log_level_debug);
                break;
            case VERBOSE:
                setCheckedItem(R.id.log_level_verbose);
                break;
        }
    }

    @Override public void onDrawerSlide(View drawerView, float slideOffset) {

        if(slideOffset == 0) {
            lazyUpdateButtons();
        }
        else {
            if(mFAB != null)
                mFAB.hide();
            if(mPSButton != null) {
                mPSButton.setState(PlayStopButton.HIDDEN);
            }
        }
    }

    @Override public void onDrawerClosed(View drawerView) {
        lazyUpdateButtons();
    }

    private boolean isSomethingOnOurFragment() {
        IMyNavigation lnav = getLocalNavigation();
        IMyNavigation gnav = getGlobalNavigation();
        return (lnav != null && lnav.overlaps(mRootView)) || (gnav != null && gnav.overlaps(mRootView));
    }

    private void updateFAB() {
        if(mFAB == null)
            return;
        if(isSomethingOnOurFragment())
            mFAB.hide();
        else
            mFAB.show();
    }


    private void lazyUpdateButtons() {
        if(mRootView == null) {
            log.d("lazyUpdateButtons: mRootView == null");
            return;
        }
        mRootView.removeCallbacks(mRunUpdateButtons);
        mRootView.postDelayed(mRunUpdateButtons, 300);
    }

    private void updatePS() {
        if(mPSButton == null) {
            log.v("mPSButton is null.");
            return;
        }
        if(mCountdown == null || mEditText == null || isSomethingOnOurFragment())
            mPSButton.setState(PlayStopButton.HIDDEN);
        else
            mPSButton.setState(mCountdown.isRunning() ? PlayStopButton.STOP : PlayStopButton.PLAY);
    }

    /**
     * Starts counting to start given command.
     * It will start the command if user doesn't press stop fast enough.
     * If no command is given it will try to get command from EditText
     */
    public void play(@Nullable String cmd) {

        if(mSearchItem != null)
            mSearchItem.collapseActionView();

        if(mCountdown == null) {
            log.e("Countdown not initialized.");
        }
        else if(mEditText == null) { // maybe we will just clear it, but we still requre it to be initialized for simplicity
            log.e("Edit Text not initialized.");
        }
        else {
            if(cmd == null || cmd.isEmpty())
                cmd = mEditText.getText().toString();

            if(cmd.isEmpty()) {
                log.w("No command provided.");
            }
            else {
                mEditText.setText("");
                mCountdown.start(cmd);
            }
        }

        updatePS();
    }

    @Override public void onPlayStopChanged(int oldState, int newState, boolean byUser) {

        if(!byUser)
            return;
        if(oldState == PlayStopButton.PLAY) {
            play(null);

        }
        else if(oldState == PlayStopButton.STOP) {
            if(mCountdown != null)
                mCountdown.cancel();

        }
        else if(oldState == PlayStopButton.HIDDEN) {
            log.d("Clicked on hidden button. ignoring..");

        }
    }

    @Override public void onCountdownStarted(@NonNull String cmd) {
        log.w(cmd);
        updatePS();
    }

    @Override public void onCountdownFinished(@NonNull String cmd) {

        try {
            boolean ok = ((MIActivity) getActivity()).onCommand(cmd);
            if(ok)
                MIContract.CmdRecent.insert(getActivity(), cmd);
        }
        catch(RuntimeException e) {
            log.e(e.getMessage(), e);
        }

        updatePS();
    }

    @Override public void onCountdownCancelled(@NonNull String cmd) {
        log.w("cancelled");
        updatePS();
    }
}