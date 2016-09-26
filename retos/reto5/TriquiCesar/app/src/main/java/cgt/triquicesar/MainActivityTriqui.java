package cgt.triquicesar;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Handler;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity_triqui);

        mInfoTextView = (TextView) findViewById(R.id.information);

        mJuego = new TriquiJuego();
        mBoardView = (BoardView) findViewById(R.id.board);
        mBoardView.setGame(mJuego);

        // Listen for touches on the board
        mBoardView.setOnTouchListener(mTouchListener);

        startNewGame();
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

        switch (item.getItemId()) {
            case R.id.new_game:
                startNewGame();
                return true;
            case R.id.ai_difficulty:
                showDialog(DIALOG_DIFFICULTY_ID);
                return true;
            case R.id.ai_about:
                showDialog(DIALOG_ABOUT);
                return true;
            case R.id.quit:
                showDialog(DIALOG_QUIT_ID);
                return true;
        }
        return false;
    }

    private boolean setMove(char player, int location) {

        if (mJuego.setMove(player, location)) {
            mBoardView.invalidate();   // Redraw the board
            return true;
        }
        return false;
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

                // If no winner yet, let the computer make a move
                mHumanMediaPlayer.start();    // Play the sound effect
                int winner = mJuego.checkForWinner();

                if (winner == 0) {
                    mInfoTextView.setText(R.string.turn_computer);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            int move = mJuego.getComputerMove();
                            setMove(TriquiJuego.COMPUTER_PLAYER, move);
                            mBoardView.invalidate();
                            mComputerMediaPlayer.start();    // Play the sound effect
                            int winner = mJuego.checkForWinner();
                            if (winner == 0)
                                mInfoTextView.setText(R.string.turn_human);
                            else {
                                if (winner == 1)
                                    mInfoTextView.setText(R.string.result_tie);
                                else if (winner == 2)
                                    mInfoTextView.setText(R.string.result_human_wins);
                                else
                                    mInfoTextView.setText(R.string.result_computer_wins);
                                mGameOver = true;
                            }
                        }
                    }, 1000);

                } else {
                    if (winner == 1)
                        mInfoTextView.setText(R.string.result_tie);
                    else if (winner == 2)
                        mInfoTextView.setText(R.string.result_human_wins);
                    else
                        mInfoTextView.setText(R.string.result_computer_wins);
                    mGameOver = true;
                }
            }

            // So we aren't notified of continued events when finger is moved
            return false;
        }
    };

    @Override
    protected void onResume() {
        super.onResume();

        mHumanMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.human);
        mComputerMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.cpu);
    }

    @Override
    protected void onPause() {
        super.onPause();

        mHumanMediaPlayer.release();
        mComputerMediaPlayer.release();
    }


}
