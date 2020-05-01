# VillageTweaker
Fork of Structurized by Draylar

VillageTweaker is a library for Fabric that allows for the easy addition of villager types, professions, and structures.

Define a simple point of interest type (workstation)
```java
public static final FabricPointOfInterestType EXAMPLE_POI = new FabricPointOfInterestType(
    new Identifier("example", "example"),
    Blocks.BLACK_WOOL, //workstation block
    1, //ticket count
    1 //search distance
);
```
Register it
```java
@Override
public void onInitialize() {
    [...]
    VillageTweaker.registerPOI(EXAMPLE_POI);
}
```
Define a villager profession
```java
public static final FabricVillagerProfession EXAMPLE_VILLAGER = new FabricVillagerProfession(
    new Identifier("example", "example"),
    EXAMPLE_POI, //workstation
    ImmutableSet.of(), //gatherable items (like farmers and seeds) ImmutableSet<Item>
    ImmutableSet.of(), //secondary job sites ImmutableSet<Block>
    SoundEvents.ENTITY_SHEEP_AMBIENT //work sound
);
```
Register it
```java
@Override
public void onInitialize() {
    [...]
    VillageTweaker.registerProfession(EXAMPLE_VILLAGER);
}
```
Add a village house (assuming you configured the jigsaw blocks and stuff to the right pool)
```java
@Override
public void onInitialize() {
    [...]
    VillageTweaker.addStructureToPool(
        "minecraft:village/plains/houses", //pool to add to
        "minecraft:village/snowy/houses/snowy_fisher_cottage",  //your structure
        25 //weight
    );
}
```
![Results](https://github.com/FoundationGames/MinecraftUtilsDownloads/raw/master/villagetweaker/showcase.png)<br>
Here are the results! (This library comes with textures for the example:example villager)<br>
More features on the way... dont use any other features because they are extremely broken and not finished
