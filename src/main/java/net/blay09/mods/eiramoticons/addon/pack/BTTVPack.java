// Copyright (c) 2015, Christopher "blay09" Baker
// Some rights reserved.

package net.blay09.mods.eiramoticons.addon.pack;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.blay09.mods.eiramoticons.api.EiraMoticonsAPI;
import net.blay09.mods.eiramoticons.api.IEmoticon;
import net.blay09.mods.eiramoticons.api.IEmoticonLoader;
import net.minecraft.client.resources.I18n;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class BTTVPack implements IEmoticonLoader {

	public BTTVPack() {
		try {
			URL apiURL = new URL("https://cdn.betterttv.net/emotes/emotes.json");
			InputStreamReader reader = new InputStreamReader(apiURL.openStream());
			Gson gson = new Gson();
			JsonArray root = gson.fromJson(reader, JsonArray.class);
			for(int i = 0; i < root.size(); i++) {
				JsonObject entry = root.get(i).getAsJsonObject();
				String code = entry.get("regex").getAsString();
				// We don't do regex emotes, so we just strip the regex stuff and add a special case for the one emote that actually needs it
				code = code.replace("\\", "");
				if(code.equals("(:trollface:|:tf:)")) {
					IEmoticon emoticon = EiraMoticonsAPI.registerEmoticon(":trollface:", this);
					emoticon.setLoadData(entry.get("url").getAsString());
					emoticon.setTooltip(I18n.format("eiramoticons:group.twitch.bttv"));

					emoticon = EiraMoticonsAPI.registerEmoticon(":tf:", this);
					emoticon.setLoadData(entry.get("url").getAsString());
					emoticon.setTooltip(I18n.format("eiramoticons:group.twitch.bttv"));
				} else {
					IEmoticon emoticon = EiraMoticonsAPI.registerEmoticon(code, this);
					emoticon.setLoadData(entry.get("url").getAsString());
					emoticon.setTooltip(I18n.format("eiramoticons:group.twitch.bttv"));
				}
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void loadEmoticonImage(IEmoticon emoticon) {
		try {
			BufferedImage image = ImageIO.read(new URL("http:" + emoticon.getLoadData().toString()));
			if(image != null) {
				emoticon.setImage(image);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}