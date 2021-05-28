package com.stratecide.disable.villagers;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.fabricmc.api.ModInitializer;
import net.minecraft.loot.LootGsons;
import net.minecraft.loot.LootTable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class DisableVillagersMod implements ModInitializer {

	private static final String CONFIG_FOLDER = "config/";
	private static final String CONFIG_FILE = CONFIG_FOLDER + "disable-villagers.json";
	private static final String DEFAULT_CONFIG = "{\n" +
			"  killVillagers: true,\n" +
			"  spareExperiencedVillagers: true,\n" +
			"  breeding: false,\n" +
			"  disableVillages: true,\n" +
			"  curableZombies: true,\n" +
			"  curedZombieLoot: {\n" +
			"    \"pools\": [\n" +
			"      {\n" +
			"        \"rolls\": 1,\n" +
			"        \"entries\": [\n" +
			"          {\n" +
			"            \"type\": \"minecraft:item\",\n" +
			"            \"name\": \"minecraft:emerald_block\"\n" +
			"          }\n" +
			"        ]\n" +
			"      }\n" +
			"    ]\n" +
			"  }\n" +
			"}";
	private static final Gson GSON = LootGsons.getTableGsonBuilder().create();
	private static boolean isInitialized = false;

	public static boolean killVillagers = false;
	public static boolean spareExperiencedVillagers = false;
	public static boolean breeding = false;
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
		JsonObject config = new JsonParser().parse(data).getAsJsonObject();
		killVillagers = config.get("killVillagers").getAsBoolean();
		spareExperiencedVillagers = config.get("spareExperiencedVillagers").getAsBoolean();
		breeding = config.get("breeding").getAsBoolean();
		curableZombies = config.get("curableZombies").getAsBoolean();
		disableVillages = config.get("disableVillages").getAsBoolean();
		curedZombieLootJson = config.get("curedZombieLoot");
	}
}
