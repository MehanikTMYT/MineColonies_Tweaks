package steve_gall.minecolonies_tweaks.core.common;

import java.util.function.Predicate;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;

@SuppressWarnings({"removal", "deprecation"})
public class CuriosCompat
{
	public static final String MOD_ID = "curios";

	public static ItemStack findFirstCurio(Player player, Predicate<ItemStack> predicate)
	{
		return CuriosApi.getCuriosInventory(player).flatMap(inventory -> inventory.findFirstCurio(predicate)).map(result -> result.stack()).orElse(ItemStack.EMPTY);
	}

	private CuriosCompat()
	{

	}

}
