package net.minso.mobbounties.Core.Player;

import net.minso.mobbounties.Core.Quests.Quest;

import java.util.UUID;

public class PlayerData {

    private UUID uuid;

    private Slot slot1;
    private Slot slot2;
    private Slot slot3;

    private Slot slot4;
    private Slot slot5;
    private Slot slot6;

    private PlayerData(UUID uuid) {
        this.uuid = uuid;
    }

    public void setSlot(Slots numSlot, Slot slot) {
        switch (numSlot) {
            case SLOT1:
                this.slot1 = slot;
            case SLOT2:
                this.slot2 = slot;
            case SLOT3:
                this.slot3 = slot;
            case SLOT4:
                this.slot4 = slot;
            case SLOT5:
                this.slot5 = slot;
            case SLOT6:
                this.slot6 = slot;
        }
    }

    public Slot getSlot(Slots numSlot) {
        switch (numSlot) {
            case SLOT1:
                return slot1;
            case SLOT2:
                return slot2;
            case SLOT3:
                return slot3;
            case SLOT4:
                return slot4;
            case SLOT5:
                return slot5;
            case SLOT6:
                return slot6;
        }
        return null;
    }

}

enum Slots {
    SLOT1,
    SLOT2,
    SLOT3,
    SLOT4,
    SLOT5,
    SLOT6;
}
