package se.liu.ida.albpe868.tddd78.javaproject;

import java.awt.*;
import java.awt.image.ImageObserver;
import java.util.*;
import java.util.List;

/**
 * This class contains the gameObjectsList and help methods. For example add object / remove objects to the list.
 *
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
        Iterator<AbstractGameObject> iter = createArrayIterator();
        while (iter.hasNext()) {
            AbstractGameObject object = iter.next();
            object.update(gamePanel);
        }
    }

    public void drawGameObjects(Graphics g, ImageObserver gamePanel) {
        Iterator<AbstractGameObject> iter = createIterator();
        while (iter.hasNext()) {
            AbstractGameObject object = iter.next();
            object.drawGameObject(g, gamePanel);
        }
    }

    public void removeGameObjects(Collection<Integer> gameObjectsIdsToRemove) {
        for (int i = 0; i < gameObjects.size(); i++) {
            if (gameObjectsIdsToRemove.contains(gameObjects.get(i).getId())) {
                gameObjects.remove(i);
            }
        }
    }

    public List<AbstractGameObject> getGameObjects() {
        return gameObjects;
    }

    public AbstractGameObject getObject(int index) {
        return gameObjects.get(index);
    }

    public void clearList() {
        gameObjects.clear();
    }

    public Iterator<AbstractGameObject> createIterator() {
        return gameObjects.iterator();
    }

    public Iterator<AbstractGameObject> createArrayIterator() {
        AbstractGameObject[] gameObjectsArray = gameObjects.toArray(new AbstractGameObject[gameObjects.size()]);
        return new ArrayIterator(gameObjectsArray);
    }
}
