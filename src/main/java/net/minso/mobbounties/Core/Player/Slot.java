package net.minso.mobbounties.Core.Player;

import net.minso.mobbounties.Core.Quests.Quest;

public class Slot {

    private Quest quest;
    private int progress;
    private long cooldown;

    public Slot(Quest quest, int progress, long cooldown) {
        this.quest = quest;
        this.progress = progress;
        this.cooldown = cooldown;
    }

    public Quest getQuest() {
        return quest;
    }

    public int getProgress() {
        return progress;
    }

    public long getCooldown() {
        return cooldown;
    }

}
