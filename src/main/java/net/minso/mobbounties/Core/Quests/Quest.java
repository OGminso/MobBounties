package net.minso.mobbounties.Core.Quests;

import org.bukkit.entity.EntityType;

public class Quest{

    private String name;
    private EntityType mobType;
    private int count;
    private int rewardAmount;

    public Quest(String name, EntityType mobType, int count, int rewardAmount) {
        this.name = name;
        this.mobType = mobType;
        this.count = count;
        this.rewardAmount = rewardAmount;
    }

    public String getName() {
        return name;
    }

    public EntityType getMobType() {
        return mobType;
    }

    public int getCount() {
        return count;
    }

    public int getRewardAmount() {
        return rewardAmount;
    }

}
