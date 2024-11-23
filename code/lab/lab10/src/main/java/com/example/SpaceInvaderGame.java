package com.example;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SpaceInvaderGame extends Application {

    public static final int WIN_W = 600;
    public static final int WIN_H = 600;

    public static final int PLAYER_X = 350;
    public static final int PLAYER_Y = 550;
    public static final int PLAYER_W = 40;
    public static final int PLAYER_H = 40;

    public static final int ENEMY_X_START = 90;
    public static final int ENEMY_Y = 100;
    public static final int ENEMY_W = 30;
    public static final int ENEMY_H = 30;
    public static final int ENEMY_INTERVAL = 100;

    private final Pane root = new Pane();

    private double interval = 0;

    private final List<Line> EnemyBullet = new ArrayList<>();

    private final List<Line> PlayerBullet = new ArrayList<>();

    private final List<Sprite> EnemyList = new ArrayList<>();

    private final Sprite player = new Sprite(PLAYER_X, PLAYER_Y, PLAYER_W, PLAYER_H, "player", Color.BLUE);

    private final Random random = new Random();

    private Parent createContent() {
        root.setPrefSize(WIN_W, WIN_H);

        root.getChildren().add(player);


        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
            }
        };

        timer.start();

        createEnemies();

        return root;
    }

    private void createEnemies() {
        for (int i = 0; i < 5; i++) {
            Sprite s = new Sprite(ENEMY_X_START + i * ENEMY_INTERVAL, ENEMY_Y, ENEMY_W, ENEMY_H, "enemy", Color.RED);
            EnemyList.add(s);
            root.getChildren().add(s);
        }
    }

    private void update() {

        interval += 0.02;

        for (Sprite i : EnemyList) {
            // TODO enemies should shoot with random intervals
            if (!i.dead && random.nextDouble() < 0.008) {
                Line bullet = shoot(i);
                if (bullet != null) {
                    EnemyBullet.add(bullet);
                }
            }
//            Line temp = shoot(i);
//            if (temp != null) EnemyBullet.add(temp);


            if (!i.dead) {
                double move = random.nextDouble() * 2 - 1;
                i.moveX(move * 10);
                i.keepWithinBounds(0, WIN_W - ENEMY_W);
            }

        }

        for (Line i : EnemyBullet) {
            // enemy's bullet moves down
            i.setStartY(i.getStartY() + 1);
            i.setEndY(i.getEndY() + 1);

            // enemy's bullet hits the player
            if (i.getBoundsInParent().intersects(player.getBoundsInParent())) {
                player.dead = true; // player is dead
            }
        }

        for (Line bullet : PlayerBullet) {
            // TODO player's bullet should move up
            // TODO should also check whether the bullet hits each enemy
            bullet.setStartY(bullet.getStartY() - 5);
            bullet.setEndY(bullet.getEndY() - 5);

            for (Sprite enemy : EnemyList) {
                if (!enemy.dead && bullet.getBoundsInParent().intersects(enemy.getBoundsInParent())) {
                    enemy.dead = true; // Enemy is dead
                    bullet.setStroke(Color.TRANSPARENT); // Hide the bullet
                }
            }
        }

        // remove dead sprites from the screen
        root.getChildren().removeIf(n -> {
            if (n instanceof Sprite s) {
                return s.dead;
            }
            return false;
        });

        if (interval > 3) {
            interval = 0;
        }
    }

    private Line shoot(Sprite who) {
        if (who.dead) {
            return null;
        }

        // create line
        Line line = new Line(
                who.getTranslateX() + 15,
                who.getTranslateY() + 30,
                who.getTranslateX() + 15,
                who.getTranslateY() + 35
        );
        line.setStroke(who.type.equals("player") ? Color.TURQUOISE : Color.BLACK);
        line.setStrokeWidth(5);
        root.getChildren().add(line);
        return line;
    }

    @Override
    public void start(Stage stage) throws Exception {
        Scene scene = new Scene(createContent());

        scene.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case LEFT:
                    player.moveLeft();
                    break;
                case RIGHT:
                    player.moveRight();
                    break;
                case UP:
                    player.moveUp();
                    break;
                case DOWN:
                    player.moveDown();
                    break;
                case SPACE:
                    Line temp = shoot(player);
                    if (temp != null) PlayerBullet.add(temp);
                    break;
            }
        });

        stage.setScene(scene);
        stage.show();
    }

    private static class Sprite extends Rectangle {
        boolean dead = false;
        final String type;

        Sprite(int x, int y, int w, int h, String type, Color color) {
            super(w, h, color);

            this.type = type;
            setTranslateX(x);
            setTranslateY(y);
        }

        void moveLeft() {
            setTranslateX(getTranslateX() - 8);
        }

        void moveRight() {
            setTranslateX(getTranslateX() + 8);
        }

        void moveUp() {
            setTranslateY(getTranslateY() - 8);
        }

        void moveDown() {
            setTranslateY(getTranslateY() + 8);
        }

        void moveX(double dx) {
            setTranslateX(getTranslateX() + dx);
        }

        void keepWithinBounds(double minX, double maxX) {
            if (getTranslateX() < minX) {
                setTranslateX(minX);
            } else if (getTranslateX() > maxX) {
                setTranslateX(maxX);
            }
        }

    }

    public static void main(String[] args) {
        launch(args);
    }
}