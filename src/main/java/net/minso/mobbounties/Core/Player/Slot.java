package net.minso.mobbounties.Core.Player;

import net.minso.mobbounties.Core.Quests.Quest;

public class Slot {

    private static final long COOLDOWN_DURATION = 24 * 60 * 60 * 1000; // 24 hours in milliseconds

    private long cooldown;
    private Quest quest;
    private int progress;

    public Slot(long cooldown, Quest quest, int progress) {
        this.cooldown = cooldown;
        this.quest = quest;
        this.progress = progress;
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

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public void setCooldown() {
        this.cooldown = System.currentTimeMillis() + COOLDOWN_DURATION;
    }

    public boolean onCooldown(){
        return System.currentTimeMillis() < cooldown;
    }

    public String getFormattedCooldown() {
        long remainingTime = cooldown - System.currentTimeMillis();

        if (remainingTime <= 0) {
            return "00:00:00";
        }

        long hours = remainingTime / (60 * 60 * 1000);
        long minutes = (remainingTime % (60 * 60 * 1000)) / (60 * 1000);
        long seconds = (remainingTime % (60 * 1000)) / 1000;

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    public void removeQuest() {
        this.quest = null;
    }

}
