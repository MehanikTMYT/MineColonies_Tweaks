package steve_gall.minecolonies_tweaks.api.common.tool;

import java.util.HashMap;
import java.util.Map;

import org.jetbrains.annotations.NotNull;

import com.minecolonies.api.util.constant.Constants;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import steve_gall.minecolonies_tweaks.core.common.MineColoniesTweaks;

public class ToolTypeTags
{
	private static final Map<Identifier, TagKey<Item>> CUSTOM_ITEMS = new HashMap<>();
	private static final Map<Identifier, Int2ObjectOpenHashMap<TagKey<Item>>> CUSTOM_LEVELS = new HashMap<>();
	private static final Map<Identifier, TagKey<Item>> BLACKLISTS = new HashMap<>();

	public static final String CUSTOM_PREFIX = "custom_tools";
	public static final String BLACKLIST_PREFIX = "tool_blacklists";
	public static final TagKey<Item> BLACKLIST_ALL = getBlacklist(MineColoniesTweaks.rl("all"));

	@NotNull
	public static String getTagPath(@NotNull Identifier toolTypeId)
	{
		var namespace = toolTypeId.getNamespace();

		if (namespace.equals(Constants.MOD_ID) || namespace.equals(MineColoniesTweaks.MOD_ID))
		{
			return toolTypeId.getPath();
		}
		else
		{
			return namespace + "/" + toolTypeId.getPath();
		}

	}

	@NotNull
	public static TagKey<Item> getCustomItem(@NotNull Identifier toolTypeId)
	{
		return CUSTOM_ITEMS.computeIfAbsent(toolTypeId, n ->
		{
			var path = CUSTOM_PREFIX + "/" + getTagPath(n);
			return ItemTags.create(MineColoniesTweaks.rl(path));
		});
	}

	@NotNull
	public static TagKey<Item> getCustomLevel(@NotNull Identifier toolTypeId, int level)
	{
		return CUSTOM_LEVELS.computeIfAbsent(toolTypeId, p -> new Int2ObjectOpenHashMap<>()).computeIfAbsent(level, l ->
		{
			var path = CUSTOM_PREFIX + "/" + getTagPath(toolTypeId) + "/" + l;
			return ItemTags.create(MineColoniesTweaks.rl(path));
		});
	}

	public static boolean isInBlacklist(@NotNull ItemStack stack, @NotNull Identifier toolTypeId)
	{
		return stack.is(BLACKLIST_ALL) || stack.is(getBlacklist(toolTypeId));
	}

	@NotNull
	public static TagKey<Item> getBlacklist(@NotNull Identifier toolTypeId)
	{
		return BLACKLISTS.computeIfAbsent(toolTypeId, n ->
		{
			var path = BLACKLIST_PREFIX + "/" + getTagPath(n);
			return ItemTags.create(MineColoniesTweaks.rl(path));
		});
	}

	private ToolTypeTags()
	{

	}

}
