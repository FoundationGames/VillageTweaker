package foundationgames.villagetweaker.api;

import foundationgames.villagetweaker.mixin.StructurePoolAccessor;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.structure.pool.StructurePoolElement;

public class StructurePoolModifier {

    private final StructurePool underlying;

    public StructurePoolModifier(StructurePool underlying) {
        this.underlying = underlying;
    }

    public void addStructurePoolElement(StructurePoolElement element) {
        ((StructurePoolAccessor) underlying).getElements().add(element);
    }

    public void addStructurePoolElement(StructurePoolElement element, int weight) {
        for (int i = 0; i < weight; i++) {
            ((StructurePoolAccessor) underlying).getElements().add(element);
        }
    }

    public StructurePool getUnderlying() {
        return underlying;
    }
}
