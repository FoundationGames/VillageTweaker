package foundationgames.villagetweaker.api;

import com.google.common.collect.ImmutableSet;
import foundationgames.villagetweaker.mixin.PointOfInterestTypeAccessor;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.Identifier;
import net.minecraft.world.poi.PointOfInterestType;

import java.util.Set;
import java.util.function.Predicate;

public class FabricPointOfInterestType {
    private PointOfInterestType underlying;
    private Identifier id;

    public FabricPointOfInterestType(Identifier id, Set<BlockState> blockStates, int ticketCount, Predicate<PointOfInterestType> completionCondition, int searchDistance) {
        underlying = new PointOfInterestType(id.toString(), blockStates, ticketCount, completionCondition, searchDistance);
        for(BlockState state : blockStates) {
            PointOfInterestTypeAccessor.stateMap().put(state, underlying);
        }
        this.id = id;
    }
    public FabricPointOfInterestType(Identifier id, Set<BlockState> blockStates, int ticketCount, int searchDistance) {
        underlying = new PointOfInterestType(id.toString(), blockStates, ticketCount, searchDistance);
        for(BlockState state : blockStates) {
            PointOfInterestTypeAccessor.stateMap().put(state, underlying);
        }
        this.id = id;
    }
    public FabricPointOfInterestType(Identifier id, Block block, int ticketCount, Predicate<PointOfInterestType> completionCondition, int searchDistance) {
        this(id, getAllStatesOf(block), ticketCount, completionCondition, searchDistance);
    }
    public FabricPointOfInterestType(Identifier id, Block block, int ticketCount, int searchDistance) {
        this(id, getAllStatesOf(block), ticketCount, searchDistance);
    }

    public PointOfInterestType getUnderlying() {
        return underlying;
    }

    private static Set<BlockState> getAllStatesOf(Block block) {
        return ImmutableSet.copyOf(block.getStateManager().getStates());
    }

    public Identifier getId() {
        return id;
    }
}
