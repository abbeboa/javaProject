package se.liu.ida.albpe868.tddd78.javaproject;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Christian on 2015-05-06.
 */
public class GameObjectsList {
    private List<AbstractGameObject> gameObjects;

    public GameObjectsList() {
        this.gameObjects = new ArrayList<>();
    }

    public void addObject(AbstractGameObject object) {
        gameObjects.add(object);
    }

    public void updateObjects(GamePanel gamePanel) {
        for (int i = 0; i < gameObjects.size(); i++) {
            gameObjects.get(i).update(gamePanel);
        }
    }

    public void drawGameObjects(Graphics g, GamePanel gamePanel) {
        for (int i = 0; i < gameObjects.size(); i++) {
            gameObjects.get(i).drawGameObject(g, gamePanel);
        }
    }

    public List<AbstractGameObject> getList() {
        return gameObjects;
    }

    public AbstractGameObject getObject(int index) {
        return gameObjects.get(index);
    }

    public void removeGameObjects(Collection<Integer> gameObjectsIdsToRemove) {
        for (int i = 0; i < gameObjects.size(); i++) {
            if (gameObjectsIdsToRemove.contains(gameObjects.get(i).getId())) {
                gameObjects.remove(i);
            }
        }
    }

    public void clearList() {
        gameObjects.clear();
    }
}
