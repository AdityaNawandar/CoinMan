package com.whoadityanawandar.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;

import org.w3c.dom.css.Rect;


import java.util.ArrayList;
import java.util.Random;

public class CoinMan extends ApplicationAdapter {
    SpriteBatch batch;
    Texture background;
    Texture[] texturearrMan;
    Texture objtextureDizzyMan;
    int intManState = 0;
    int intPause;
    float fltGravity = 0.2f;
    float fltVelocity = 0;
    int intManY = 0;
    Rectangle rectMan;

    ArrayList<Integer> arrlstintCoinXs = new ArrayList<>();
    ArrayList<Integer> arrlstintCoinYs = new ArrayList<>();
    ArrayList<Rectangle> arrlstrectCoins = new ArrayList<Rectangle>();
    int intCoinCount;
    Texture objtextureCoin;

    ArrayList<Integer> arrlstintBombXs = new ArrayList<>();
    ArrayList<Integer> arrlstintBombYs = new ArrayList<>();
    ArrayList<Rectangle> arrlstrectBombs = new ArrayList<Rectangle>();
    Texture objtextureBomb;
    int intBombCount;

    BitmapFont objBitmapFont;
    int intScore = 0;
    int intGameState = 0;
    Random objRandom;
    float fltScreenHeight;
    float fltScreenWidth;

    @Override
    public void create() {
        batch = new SpriteBatch();
        background = new Texture("bg.png");
        texturearrMan = new Texture[4];
        texturearrMan[0] = new Texture("frame-1.png");
        texturearrMan[1] = new Texture("frame-2.png");
        texturearrMan[2] = new Texture("frame-3.png");
        texturearrMan[3] = new Texture("frame-4.png");
        fltScreenHeight = Gdx.graphics.getHeight();
        fltScreenWidth = Gdx.graphics.getWidth();
        intManY = (int) fltScreenHeight / 2;
        objtextureCoin = new Texture("coin.png");
        objtextureBomb = new Texture("bomb.png");
        objRandom = new Random();

        objtextureDizzyMan = new Texture("dizzy-1.png");
        objBitmapFont = new BitmapFont();
        objBitmapFont.setColor(Color.WHITE);
        objBitmapFont.getData().setScale(10);
    }

    public void generateCoins() {
        float fltHeight = objRandom.nextFloat() * fltScreenHeight;
        arrlstintCoinYs.add((int) fltHeight);
        arrlstintCoinXs.add((int) fltScreenWidth);
    }

    public void generateBombs() {
        float fltHeight = objRandom.nextFloat() * fltScreenHeight;
        arrlstintBombYs.add((int) fltHeight);
        arrlstintBombXs.add((int) fltScreenWidth);
    }

    @Override
    public void render() {
        batch.begin();
        batch.draw(background, 0, 0, fltScreenWidth, fltScreenHeight);

        if (intGameState == 1) {
            //Game is running
            gameLive();
        } else if (intGameState == 0) {
            //Waiting to start
            if (Gdx.input.justTouched()) {
                intScore = 0;
                intGameState = 1;
            }
        } else if (intGameState == -1) {
            //Game Over!
            if (Gdx.input.justTouched()) {
                resetGame();
            }
        }
        //if game over
        if (intGameState == -1) {
            batch.draw(objtextureDizzyMan, fltScreenWidth / 2 - (objtextureDizzyMan.getWidth() / 2), (intManY));
            //objBitmapFont.draw(batch, "Oops!", 100, 200);
        } else {
            //draw and get the man at the center of the screen
            batch.draw(texturearrMan[intManState], fltScreenWidth / 2 - (texturearrMan[intManState].getWidth() / 2), (intManY));
        }


        rectMan = new Rectangle((int) fltScreenWidth / 2 - (texturearrMan[intManState].getWidth() / 2), intManY, texturearrMan[intManState].getWidth(), texturearrMan[intManState].getHeight());

        //Hitting a coin
        for (int i = 0; i < arrlstrectCoins.size(); i++) {
            if (Intersector.overlaps(rectMan, arrlstrectCoins.get(i))) {
                //Collision
                intScore += 10;
                arrlstrectCoins.remove(i);
                arrlstintCoinXs.remove(i);
                arrlstintCoinYs.remove(i);
                break;
            }
        }

        //Hitting a bomb
        for (int i = 0; i < arrlstrectBombs.size(); i++) {
            if (Intersector.overlaps(rectMan, arrlstrectBombs.get(i))) {
                //Collision
                intGameState = -1;
            }
        }


        objBitmapFont.draw(batch, String.valueOf(intScore), 100, 200);

        batch.end();
    }

    private void gameLive() {
        //COINS
        if (intCoinCount < 100) {
            intCoinCount++;
        } else {
            intCoinCount = 0;
            generateCoins();
        }

        arrlstrectCoins.clear();
        for (int i = 0; i < arrlstintCoinXs.size(); i++) {
            batch.draw(objtextureCoin, arrlstintCoinXs.get(i), arrlstintCoinYs.get(i));
            arrlstintCoinXs.set(i, arrlstintCoinXs.get(i) - 4);
            arrlstrectCoins.add(new Rectangle(arrlstintCoinXs.get(i), arrlstintCoinYs.get(i), objtextureCoin.getWidth(), objtextureCoin.getHeight()));
        }

        //BOMBS
        if (intBombCount < 400) {
            intBombCount++;
        } else {
            intBombCount = 0;
            generateBombs();
        }
        arrlstrectBombs.clear();
        for (int i = 0; i < arrlstintBombXs.size(); i++) {
            batch.draw(objtextureBomb, arrlstintBombXs.get(i), arrlstintBombYs.get(i));
            arrlstintBombXs.set(i, arrlstintBombXs.get(i) - 4);
            arrlstrectBombs.add(new Rectangle(arrlstintBombXs.get(i), arrlstintBombYs.get(i), objtextureBomb.getWidth(), objtextureBomb.getHeight()));
        }
        //jump
        if (Gdx.input.justTouched()) {
            fltVelocity = -10;
        }
        //control running speed
        if (intPause < 8) {
            intPause++;
        } else {
            intPause = 0;

            if (intManState < 3) {
                intManState++;
            } else {
                intManState = 0;
            }
        }

        fltVelocity += fltGravity;
        //fall down
        intManY -= fltVelocity;
        //fall down but do not below the ground level
        if (intManY <= 0) {
            intManY = 0;
        }
    }

    private void resetGame() {
        intGameState = 0;
        intManY = (int) fltScreenHeight / 2;
        //intScore = 0;
        fltVelocity = 0;
        arrlstintCoinXs.clear();
        arrlstintCoinYs.clear();
        arrlstrectCoins.clear();
        intCoinCount = 0;
        arrlstintBombXs.clear();
        arrlstintBombYs.clear();
        arrlstrectBombs.clear();
        intBombCount = 0;
        //objBitmapFont.draw(batch, String.valueOf(intScore), 100, 200);
    }

    @Override
    public void dispose() {
        batch.dispose();

    }
}
