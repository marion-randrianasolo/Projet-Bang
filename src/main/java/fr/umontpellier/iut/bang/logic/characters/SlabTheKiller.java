package fr.umontpellier.iut.bang.logic.characters;

public class SlabTheKiller extends BangCharacter {
    public SlabTheKiller() {
        super("Slab the Killer", 4);
    }

    @Override
    public int getNumberOfMissedRequired() {
        return 2;
    }
}
