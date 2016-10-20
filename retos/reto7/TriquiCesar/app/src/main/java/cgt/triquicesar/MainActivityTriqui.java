package cgt.triquicesar;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivityTriqui extends AppCompatActivity {

    // Represents the internal state of the game
    private TriquiJuego mJuego;
    private boolean mGameOver = false;

    private int mHumanWins = 0;
    private int mComputerWins = 0;
    private int mTies = 0;
    private char mGoFirst = TriquiJuego.HUMAN_PLAYER;

    // Buttons making up the board
    private Button mBoardButtons[];

    // Various text displayed
    private TextView mInfoTextView;

    static final int DIALOG_DIFFICULTY_ID = 0;
    static final int DIALOG_QUIT_ID = 1;
    static final int DIALOG_ABOUT = 2;

    // vista de tablero
    private BoardView mBoardView;

    //sonidos
    MediaPlayer mHumanMediaPlayer;
    MediaPlayer mComputerMediaPlayer;

    //reto 6
    static final String TAG = "game.MainActivityTriqui";
    private TextView mHumanScoreTextView;
    private TextView mComputerScoreTextView;
    private TextView mTieScoreTextView;
    private SharedPreferences mPrefs;

    //reto 7
    private boolean mSoundOn = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity_triqui);

        mJuego = new TriquiJuego();
        // Restore the scores from the persistent preference data source
        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        mSoundOn = mPrefs.getBoolean("sound", true);
        String difficultyLevel = mPrefs.getString("difficulty_level",
                getResources().getString(R.string.difficulty_harder));
        if (difficultyLevel.equals(getResources().getString(
                R.string.difficulty_easy)))
            mJuego.setDifficultyLevel(TriquiJuego.DifficultyLevel.Easy);
        else if (difficultyLevel.equals(getResources().getString(
                R.string.difficulty_harder)))
            mJuego.setDifficultyLevel(TriquiJuego.DifficultyLevel.Harder);
        else
            mJuego.setDifficultyLevel(TriquiJuego.DifficultyLevel.Expert);

        mHumanWins = mPrefs.getInt("mHumanWins", 0);
        mComputerWins = mPrefs.getInt("mComputerWins", 0);
        mTies = mPrefs.getInt("mTies", 0);

        mInfoTextView = (TextView) findViewById(R.id.information);

        mHumanScoreTextView = (TextView) findViewById(R.id.player_score);
        mComputerScoreTextView = (TextView) findViewById(R.id.computer_score);
        mTieScoreTextView = (TextView) findViewById(R.id.tie_score);

        mBoardView = (BoardView) findViewById(R.id.board);
        mBoardView.setColor(mPrefs.getInt("board_color", 0xFFCCCCCC));
        mBoardView.setGame(mJuego);

        // Listen for touches on the board
        mBoardView.setOnTouchListener(mTouchListener);

        //startNewGame();

        if (savedInstanceState == null) {
            startNewGame();
        } else {
            // Restore the game's state
            mJuego.setBoardState(savedInstanceState.getCharArray("board"));
            mGameOver = savedInstanceState.getBoolean("mGameOver");
            mInfoTextView.setText(savedInstanceState.getCharSequence("info"));
            mHumanWins = savedInstanceState.getInt("mHumanWins");
            mComputerWins = savedInstanceState.getInt("mComputerWins");
            mTies = savedInstanceState.getInt("mTies");
            mGoFirst = savedInstanceState.getChar("mGoFirst");


            endGame(mJuego.checkForWinner());
            if(!mGameOver) {
                mInfoTextView.setText(mGoFirst == TriquiJuego.COMPUTER_PLAYER ? R.string.turn_computer: R.string.turn_human);
                mBoardView.invalidate();
            }

        }

        displayScores();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putCharArray("board", mJuego.getBoardState());
        outState.putBoolean("mGameOver", mGameOver);
        outState.putInt("mHumanWins", Integer.valueOf(mHumanWins));
        outState.putInt("mComputerWins", Integer.valueOf(mComputerWins));
        outState.putInt("mTies", Integer.valueOf(mTies));
        outState.putCharSequence("info", mInfoTextView.getText());
        outState.putChar("mGoFirst", mGoFirst);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mJuego.setBoardState(savedInstanceState.getCharArray("board"));
        mGameOver = savedInstanceState.getBoolean("mGameOver");
        mInfoTextView.setText(savedInstanceState.getCharSequence("info"));
        mHumanWins = savedInstanceState.getInt("mHumanWins");
        mComputerWins = savedInstanceState.getInt("mComputerWins");
        mTies = savedInstanceState.getInt("mTies");
        mGoFirst = savedInstanceState.getChar("mGoFirst");
    }

    // Set up the game board.
    private void startNewGame() {

        mJuego.clearBoard();
        mBoardView.invalidate(); // Redraw the board
        mGameOver = false;
        // Human goes first
        mInfoTextView.setText(R.string.first_human);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        //menu.add(R.string.new_game);
        return true;
    }

    // acciones para el menu principal de la aplicacion
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //startNewGame();
        //return true;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        switch (item.getItemId()) {
            case R.id.new_game:
                startNewGame();
                return true;
            case R.id.settings:
                startActivityForResult(new Intent(this, Settings.class), 0);
                return true;
            case R.id.ai_about:
                Context context = getApplicationContext();
                LayoutInflater inflater = (LayoutInflater) context
                        .getSystemService(LAYOUT_INFLATER_SERVICE);
                View aboutView;
                aboutView = inflater.inflate(R.layout.about_dialog, null);
                builder.setView(aboutView);
                builder.setPositiveButton("OK", null);
                builder.create().show();
                return true;
            case R.id.reset:
                mHumanWins = 0;
                mComputerWins = 0;
                mTies = 0;
                displayScores();
                return true;
        }
        return false;
    }

    private void displayScores() {
        mHumanScoreTextView.setText(Integer.toString(mHumanWins));
        mComputerScoreTextView.setText(Integer.toString(mComputerWins));
        mTieScoreTextView.setText(Integer.toString(mTies));

    }

    private boolean setMove(char player, int location) {
        if (mJuego.setMove(player, location)) {
            mBoardView.invalidate(); // Redraw the board
            return true;
        }
        return false;
    }

    private void endGame(int winner) {
        switch (winner) {
            case 0:
                return;
            case 1:
                mInfoTextView.setText(R.string.result_tie);
                mTies++;
                mTieScoreTextView.setText(Integer.toString(mTies));
                break;
            case 2:
                String defaultMessage = getResources().getString(R.string.result_human_wins);
                mInfoTextView.setText(mPrefs.getString("victory_message", defaultMessage));
                mHumanWins++;
                mHumanScoreTextView.setText(Integer.toString(mHumanWins));
                break;
            default:
                mInfoTextView.setText(R.string.result_computer_wins);
                mComputerWins++;
                mComputerScoreTextView.setText(Integer.toString(mComputerWins));
                break;
        }
        mGameOver = true;
    }

    private void turnComputer() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                Log.v(TAG, "Android Jugando...");
                int move = mJuego.getComputerMove();
                setMove(TriquiJuego.COMPUTER_PLAYER, move);
                mGoFirst = mGoFirst == TriquiJuego.HUMAN_PLAYER? TriquiJuego.COMPUTER_PLAYER:TriquiJuego.HUMAN_PLAYER;
                mBoardView.invalidate();
                if (mSoundOn) {
                    try {
                        mComputerMediaPlayer.start(); // Play the sound effect
                    } catch (Exception e) {
                    }
                }
                int winner = mJuego.checkForWinner();
                if (winner == 0) {
                    mInfoTextView.setText(R.string.turn_human);
                }
                else
                    endGame(winner);
            }
        }, 1000);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        Dialog dialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        Context context = getApplicationContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.about_dialog, null);
        //builder.setView(layout);
        //builder.setPositiveButton("OK", null);
        //Dialog dialog = builder.create();

        int selected = 2;
        //TriquiJuego.DifficultyLevel dificultadActual;

        switch(id) {
            case DIALOG_DIFFICULTY_ID:

                builder.setTitle(R.string.difficulty_choose);

                final CharSequence[] levels = {
                        getResources().getString(R.string.difficulty_easy),
                        getResources().getString(R.string.difficulty_harder),
                        getResources().getString(R.string.difficulty_expert)};

                // selected is the radio button that should be selected.

                /*selected = 2;

                dificultadActual = TriquiJuego.getDifficultyLevel();

                System.out.println("!!!!!!!!!!!!!!!!!!!! diff: " + dificultadActual);

                if(dificultadActual.equals(TriquiJuego.DifficultyLevel.Easy)){
                    selected = 0;
                }
                if(dificultadActual.equals(TriquiJuego.DifficultyLevel.Harder)){
                    selected = 1;
                }
                if(dificultadActual.equals(TriquiJuego.DifficultyLevel.Expert)){
                    selected = 2;
                }*/

                builder.setSingleChoiceItems(levels, selected,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {
                                dialog.dismiss();   // Close dialog

                                // Display the selected difficulty level
                                switch(item){
                                    case 0:
                                        TriquiJuego.setDifficultyLevel(TriquiJuego.DifficultyLevel.Easy);
                                        break;
                                    case 1:
                                        TriquiJuego.setDifficultyLevel(TriquiJuego.DifficultyLevel.Harder);
                                        break;
                                    case 2:
                                        TriquiJuego.setDifficultyLevel(TriquiJuego.DifficultyLevel.Expert);
                                        break;
                                }

                                Toast.makeText(getApplicationContext(), levels[item],
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                dialog = builder.create();

                break;
            case DIALOG_ABOUT:

                builder.setView(layout);
                builder.setPositiveButton("OK", null);
                dialog = builder.create();

                break;
            case DIALOG_QUIT_ID:
                // Create the quit confirmation dialog

                builder.setMessage(R.string.quit_question)
                        .setCancelable(false)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                MainActivityTriqui.this.finish();
                            }
                        })
                        .setNegativeButton(R.string.no, null);
                dialog = builder.create();

                break;

        }

        return dialog;
    }

    // Handles clicks on the game board buttons
    public class ButtonClickListener implements View.OnClickListener {
        int location;

        public ButtonClickListener(int location) {
            this.location = location;
        }

        @Override
        public void onClick(View view) {
            if (mBoardButtons[location].isEnabled()) {
                setMove(TriquiJuego.HUMAN_PLAYER, location);

                // If no winner yet, let the computer make a move
                int winner = mJuego.checkForWinner();
                if (winner == 0) {
                    mInfoTextView.setText(R.string.turn_computer);
                    int move = mJuego.getComputerMove();
                    setMove(TriquiJuego.COMPUTER_PLAYER, move);
                    winner = mJuego.checkForWinner();
                }

                if (winner == 0)
                    mInfoTextView.setText(R.string.turn_human);
                else if (winner == 1)
                    mInfoTextView.setText(R.string.result_tie);
                else if (winner == 2)
                    mInfoTextView.setText(R.string.result_human_wins);
                else
                    mInfoTextView.setText(R.string.result_computer_wins);
            }
        }
    }

    // Listen for touches on the board
    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {

        public boolean onTouch(View v, MotionEvent event) {
            // Determine which cell was touched
            int col = (int) event.getX() / mBoardView.getBoardCellWidth();
            int row = (int) event.getY() / mBoardView.getBoardCellHeight();
            int pos = row * 3 + col;

            if (!mGameOver && setMove(TriquiJuego.HUMAN_PLAYER, pos)) {

                mGoFirst = mGoFirst == TriquiJuego.HUMAN_PLAYER ? TriquiJuego.COMPUTER_PLAYER
                        : TriquiJuego.HUMAN_PLAYER;
                if (mSoundOn) {
                    try {
                        mHumanMediaPlayer.start(); // Play the sound effect
                    } catch (Exception e) {

                    }
                }

                int winner = mJuego.checkForWinner();
                if (winner == 0) {
                    mInfoTextView.setText(R.string.turn_computer);
                    turnComputer();
                } else
                    endGame(winner);
            }

            // So we aren't notified of continued events when finger is moved
            return false;
        }
    };

    @Override
    protected void onResume() {
        super.onResume();

        //mHumanMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.human);
        //mComputerMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.cpu);
        mHumanMediaPlayer = MediaPlayer.create(getApplicationContext(),
                R.raw.human);
        mComputerMediaPlayer = MediaPlayer.create(getApplicationContext(),
                R.raw.cpu);
    }

    @Override
    protected void onStop() {
        super.onStop();

        // Save the current scores
        SharedPreferences.Editor ed = mPrefs.edit();
        ed.putInt("mHumanWins", mHumanWins);
        ed.putInt("mComputerWins", mComputerWins);
        ed.putInt("mTies", mTies);

        /*
        int selected = 0;
        switch (mJuego.getDifficultyLevel()) {
            case Easy:
                selected = 0;
                break;
            case Harder:
                selected = 1;
                break;
            case Expert:
                selected = 2;
                break;
        }
        ed.putInt("mDifficultyLevel", selected);
        */
        ed.commit();
    }

    @Override
    protected void onPause() {
        super.onPause();

        mHumanMediaPlayer.release();
        mComputerMediaPlayer.release();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESULT_CANCELED) {
            // Apply potentially new settings
            mSoundOn = mPrefs.getBoolean("sound", true);
            String difficultyLevel = mPrefs.getString("difficulty_level",
                    getResources().getString(R.string.difficulty_harder));
            if (difficultyLevel.equals(getResources().getString(
                    R.string.difficulty_easy))) {
                mJuego.setDifficultyLevel(TriquiJuego.DifficultyLevel.Easy);

            } else if (difficultyLevel.equals(getResources().getString(
                    R.string.difficulty_harder))) {
                mJuego.setDifficultyLevel(TriquiJuego.DifficultyLevel.Harder);
            } else {
                mJuego.setDifficultyLevel(TriquiJuego.DifficultyLevel.Expert);
            }
            mBoardView.setColor(mPrefs.getInt("board_color", 0xFFCCCCCC));
        }
    }


}
