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

}



class Slot {

    private Quest quest;
    private int progress;
    private long cooldown;

}