package com.stratecide.disable.villagers;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.fabricmc.api.ModInitializer;
import net.minecraft.loot.LootGsons;
import net.minecraft.loot.LootTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class DisableVillagersMod implements ModInitializer {

	public static final Logger LOGGER = LoggerFactory.getLogger("disable.villagers");
	private static final String CONFIG_FOLDER = "config/";
	private static final String CONFIG_FILE = CONFIG_FOLDER + "disable-villagers.json";
	private static final String DEFAULT_CONFIG = """
{
  "killVillagers": true,
  "disableWanderingTrader": true,
  "blockTrading": true,
  "spareExperiencedVillagers": true,
  "breeding": false,
  "disableVillages": true,
  "disableZombies": false,
  "curableZombies": true,
  "curedZombieLoot": {
	"pools": [
	  {
		"rolls": 1,
		"entries": [
		  {
			"type": "minecraft:item",
			"name": "minecraft:emerald_block"
		  }
		]
	  }
	]
  }
}""";
	private static final Gson GSON = LootGsons.getTableGsonBuilder().create();
	private static boolean isInitialized = false;

	public static boolean killVillagers = false;
	public static boolean disableWanderingTrader = false;
	public static boolean blockTrading = false;
	public static boolean spareExperiencedVillagers = false;
	public static boolean breeding = false;
	private static boolean disableZombies = true;
	public static boolean getDisabledZombies() {
		loadConfig();
		return disableZombies;
	}
	public static boolean curableZombies = true;
	private static JsonElement curedZombieLootJson = null;
	public static LootTable curedZombieLoot = null;
	private static boolean disableVillages = false;
	public static boolean getDisabledVillages() {
		loadConfig();
		return disableVillages;
	}

	@Override
	public void onInitialize() {
		loadConfig();
		if (curedZombieLootJson != null)
			curedZombieLoot = GSON.fromJson(curedZombieLootJson, LootTable.class);
	}

	private static void loadConfig() {
		if (isInitialized)
			return;
		isInitialized = true;
		File file = new File(CONFIG_FILE);
		String data;
		if (!file.exists()) {
			file.getParentFile().mkdirs();
			data = DEFAULT_CONFIG;
			try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
				writer.write(data);
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		else {
			try (Scanner scanner = new Scanner(file)) {
				StringBuilder builder = new StringBuilder();
				while (scanner.hasNextLine())
					builder.append(scanner.nextLine());
				data = builder.toString();
			}
			catch (FileNotFoundException e) {
				e.printStackTrace();
				data = DEFAULT_CONFIG;
			}
		}
		JsonObject config = JsonParser.parseString(data).getAsJsonObject();
		killVillagers = config.get("killVillagers").getAsBoolean();
		disableWanderingTrader = config.has("disableWanderingTrader") && config.get("disableWanderingTrader").getAsBoolean();
		blockTrading = config.get("blockTrading").getAsBoolean();
		spareExperiencedVillagers = config.get("spareExperiencedVillagers").getAsBoolean();
		breeding = config.get("breeding").getAsBoolean();
		disableZombies = config.has("disableZombies") && config.get("disableZombies").getAsBoolean();
		curableZombies = config.get("curableZombies").getAsBoolean();
		disableVillages = config.get("disableVillages").getAsBoolean();
		curedZombieLootJson = config.get("curedZombieLoot");
	}
}
