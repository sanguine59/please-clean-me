package factory;

import singleton.NamePool;

public abstract class BaseFactory implements NpcFactory {
    protected String getRandomName() {
        return NamePool.getInstance().addName();
    }
}
