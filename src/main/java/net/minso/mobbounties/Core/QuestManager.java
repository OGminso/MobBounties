package net.minso.mobbounties.Core;

import net.minso.mobbounties.Main;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class QuestManager {
    Main main = Main.getInstance();

    private List<Quest> quests;

    public QuestManager() {
        this.quests = new ArrayList<>();
        loadQuestsFromFile();
    }

    private void loadQuestsFromFile() {
        Yaml yaml = new Yaml();
        File file  = new File("plugins/" + main.getPluginName() +"/quests.yml");

        try (FileInputStream fis = new FileInputStream(file)) {
            // Parse the YAML file
            Map<String, List<Map<String, Object>>> data = yaml.load(fis);

            // Process the parsed data
            if (data != null && data.containsKey("quests")) {
                List<Map<String, Object>> questList = data.get("quests");

                for (Map<String, Object> questData : questList) {
                    try {
                        String name = (String) questData.get("name");
                        String mobType = (String) questData.get("mobType");
                        int count = (int) questData.get("count");
                        int rewardAmount = (int) questData.get("rewardAmount");

                        // Convert mobType string to EntityType
                        EntityType entityType = EntityType.valueOf(mobType.toUpperCase());

                        // Create and add a new Quest object to the list
                        Quest quest = new Quest(name, entityType, count, rewardAmount);
                        quests.add(quest);

                    } catch (IllegalArgumentException e) {
                        throw new IllegalArgumentException("Error loading quest. Check the 'mobType' in your YAML file.", e);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



}
