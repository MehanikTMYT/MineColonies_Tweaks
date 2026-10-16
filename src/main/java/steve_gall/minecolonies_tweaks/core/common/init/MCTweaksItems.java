package steve_gall.minecolonies_tweaks.core.common.init;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.minecolonies.core.items.ItemResourceScroll;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import steve_gall.minecolonies_tweaks.core.common.MineColoniesTweaks;
import steve_gall.minecolonies_tweaks.core.common.item.ItemCopyScroll;
import steve_gall.minecolonies_tweaks.core.common.item.ItemInventoryScroll;
import steve_gall.minecolonies_tweaks.core.common.item.ItemResourceScrollBook;
import steve_gall.minecolonies_tweaks.core.common.item.ItemUniversityScroll;

public class MCTweaksItems
{
	public static final DeferredRegister.Items REGISTER = DeferredRegister.createItems(MineColoniesTweaks.MOD_ID);
	public static final Map<DyeColor, DeferredHolder<Item, ItemResourceScroll>> COLOR_RESOURCE_SCROLLS;
	public static final DeferredHolder<Item, ItemResourceScrollBook> RESOURCESCROLL_BOOK = REGISTER.registerItem("resourcescroll_book", properties -> new ItemResourceScrollBook(properties, 9 * 3));
	public static final DeferredHolder<Item, ItemInventoryScroll> INVENTORYSCROLL = REGISTER.registerItem("inventoryscroll", properties -> new ItemInventoryScroll(properties));
	public static final Map<DyeColor, DeferredHolder<Item, ItemInventoryScroll>> COLOR_INVENTORY_SCROLLS;
	public static final DeferredHolder<Item, ItemUniversityScroll> UNIVERSITYSCROLL = REGISTER.registerItem("universityscroll", properties -> new ItemUniversityScroll(properties));
	public static final DeferredHolder<Item, ItemCopyScroll> COPYSCROLL = REGISTER.registerItem("copyscroll", properties -> new ItemCopyScroll(properties));

	static
	{
		var colorResourceScrolls = new HashMap<DyeColor, DeferredHolder<Item, ItemResourceScroll>>();
		COLOR_RESOURCE_SCROLLS = Collections.unmodifiableMap(colorResourceScrolls);
		var colorWarehouseScrolls = new HashMap<DyeColor, DeferredHolder<Item, ItemInventoryScroll>>();
		COLOR_INVENTORY_SCROLLS = Collections.unmodifiableMap(colorWarehouseScrolls);

		for (var color : DyeColor.values())
		{
			var resourceScroll = REGISTER.registerItem("resourcescroll_" + color.getName().toLowerCase(), properties -> new ItemResourceScroll(properties));
			colorResourceScrolls.put(color, resourceScroll);

			var warehouseScroll = REGISTER.registerItem("inventoryscroll_" + color.getName().toLowerCase(), properties -> new ItemInventoryScroll(properties));
			colorWarehouseScrolls.put(color, warehouseScroll);
		}

	}

	private MCTweaksItems()
	{

	}

}
