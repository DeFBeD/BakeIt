package com.example.android.bakeit.UI;


import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.android.bakeit.Model.BakingInstructions;
import com.example.android.bakeit.R;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;


public class VideoFragment extends Fragment implements ExoPlayer.EventListener {

    SimpleExoPlayerView mSimpleExoPlayerView;
    SimpleExoPlayer mExoPlayer;
    MediaSessionCompat mMediaSession;
    PlaybackStateCompat.Builder mStateBuilder;

    String videoUri;
    ArrayList<BakingInstructions> mBakingInstructions;
    int stepId;
    String instructionsDescription;
    String thumbnails;
    TextView description;
    ImageView thumbNailImage;
    Button nextStep;
    long playBackPosition;
    boolean playWhenReady;
    RelativeLayout mRelativeLayout;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();

        if (bundle != null) {

            mBakingInstructions = bundle.getParcelableArrayList("baking_steps");
            stepId = bundle.getInt("baking_instructions_video_id");
            videoUri = bundle.getString("baking_instructions_video_string");
            instructionsDescription = bundle.getString("baking_instructions_description");
            thumbnails = bundle.getString("baking_thumbnail");
        }

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video, container, false);
        mRelativeLayout = view.findViewById(R.id.buttonRLayout);

        if (savedInstanceState != null) {

            mBakingInstructions = savedInstanceState.getParcelableArrayList("steps");
            stepId = savedInstanceState.getInt("saved_baking_instructions_video_id");
            videoUri = savedInstanceState.getString("video_uri");
            instructionsDescription = savedInstanceState.getString("saved_description");
            thumbnails = savedInstanceState.getString("saved_thumbnail");
            //get play when ready boolean

            playBackPosition = savedInstanceState.getLong("playback_position");
            playWhenReady = savedInstanceState.getBoolean("play_when_ready");

        }
        stepId = stepId + 1;

        mSimpleExoPlayerView = view.findViewById(R.id.playerView);
        thumbNailImage = view.findViewById(R.id.thumbnail);

        description = view.findViewById(R.id.instructions_description);
        description.setText(instructionsDescription);

        initializeMediaSession();

        handleItemsOnScreen();


        nextStep = view.findViewById(R.id.next_step_button);
        nextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextButtonStep(stepId);
            }
        });

        return view;
    }

    public void handleItemsOnScreen() {
        if (thumbnails != null && !thumbnails.isEmpty()) {

            Glide.with(this)
                    .load(thumbnails)
                    .into(thumbNailImage);

            toggleVisibility(thumbNailImage, true);
        } else {
            // Hide image view
            toggleVisibility(thumbNailImage, false);
        }


        if (videoUri != null && !videoUri.isEmpty()) {


            toggleVisibility(mSimpleExoPlayerView, true);

            initializePlayer(Uri.parse(videoUri));


            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                // Expand video, hide description, hide system UI
                landscapeVideoView(mSimpleExoPlayerView);
                // toggleVisibility(description, false);
                hideUI();
            }


        } else {
            toggleVisibility(mSimpleExoPlayerView, false);

        }

    }

    public void nextButtonStep(int Position) {
        int size = mBakingInstructions.size();

        stepId = stepId + 1;

        if (stepId == size) {
            stepId = stepId - 1;
            Snackbar snackbar = Snackbar
                    .make(mRelativeLayout, R.string.noMoreStepsAvailable, Snackbar.LENGTH_LONG);

            snackbar.show();
        }

        instructionsDescription = mBakingInstructions.get(Position).getDescription();
        description.setText(instructionsDescription);

        videoUri = mBakingInstructions.get(Position).getVideoURL();
        thumbnails = mBakingInstructions.get(Position).getThumbnailURL();

        releasePlayer();

        handleItemsOnScreen();

    }

    private void toggleVisibility(View view, boolean show) {
        if (show) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
        }
    }

    //following 2 methods help with the exoview going into landscape
    private void landscapeVideoView(SimpleExoPlayerView mExoplayerView) {
        mExoplayerView.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
        mExoplayerView.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
    }

    private void hideUI() {
        getActivity().getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }

    private void initializePlayer(Uri mediaUri) {
        if (mExoPlayer == null) {
            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            mSimpleExoPlayerView.setPlayer(mExoPlayer);


            mExoPlayer.addListener(this);

            // Prepare the MediaSource.
            String userAgent = Util.getUserAgent(getContext(), "bakeit");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);
        }
    }

    /**
     * Release ExoPlayer.
     */
    private void releasePlayer() {
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }

    }

    private void initializeMediaSession() {

        mMediaSession = new MediaSessionCompat(getActivity(), "VideoFragment");

        // Enable callbacks from MediaButtons and TransportControls.
        mMediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        // Do not let MediaButtons restart the player when the app is not visible.
        mMediaSession.setMediaButtonReceiver(null);

        // Set an initial PlaybackState with ACTION_PLAY, so media buttons can start the player.
        mStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE);

        mMediaSession.setPlaybackState(mStateBuilder.build());


        // MySessionCallback has methods that handle callbacks from a media controller.
        mMediaSession.setCallback(new MySessionCallback());

        // Start the Media Session since the activity is active.
        mMediaSession.setActive(true);

    }

    /**
     * Media Session Callbacks, where all external clients control the player.
     */
    private class MySessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            mExoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            mExoPlayer.setPlayWhenReady(false);
        }

        @Override
        public void onSkipToPrevious() {
            mExoPlayer.seekTo(0);
        }
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("steps", mBakingInstructions);
        outState.putString("video_uri", videoUri);
        outState.putInt("saved_baking_instructions_video_id", stepId);
        outState.putString("saved_description", instructionsDescription);
        outState.putString("saved_thumbnail", thumbnails);
        outState.putLong("playback_position", playBackPosition);
        outState.putBoolean("play_when_ready", playWhenReady);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
        if (mMediaSession != null) {
            mMediaSession.setActive(false);
        }
    }

    @Override
    public void onPause() {
        //releasing in Pause and saving current position for resuming
        super.onPause();
        if (mExoPlayer != null) {
            playBackPosition = mExoPlayer.getCurrentPosition();
            //getting play when ready so that player can be properly store state on rotation
            playWhenReady = mExoPlayer.getPlayWhenReady();
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mExoPlayer != null) {
            //resuming properly
            mExoPlayer.setPlayWhenReady(playWhenReady);
            mExoPlayer.seekTo(playBackPosition);
        } else {
            //Correctly initialize and play properly fromm seekTo function
            initializeMediaSession();
            initializePlayer(Uri.parse(videoUri));
            mExoPlayer.setPlayWhenReady(playWhenReady);
            mExoPlayer.seekTo(playBackPosition);
        }
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if ((playbackState == ExoPlayer.STATE_READY) && playWhenReady) {
            mStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                    mExoPlayer.getCurrentPosition(), 1f);
        } else if ((playbackState == ExoPlayer.STATE_READY)) {
            mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                    mExoPlayer.getCurrentPosition(), 1f);
        }
        mMediaSession.setPlaybackState(mStateBuilder.build());

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }

}
