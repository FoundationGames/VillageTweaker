package foundationgames.villagetweaker.api;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public interface StructurePoolAddCallback {

    Event<StructurePoolAddCallback> EVENT = EventFactory.createArrayBacked(StructurePoolAddCallback.class,
            listeners -> initialPool -> {
                for (StructurePoolAddCallback listener : listeners) {
                    listener.add(initialPool);
                }
            }
    );

    void add(StructurePoolModifier initialPool);
}