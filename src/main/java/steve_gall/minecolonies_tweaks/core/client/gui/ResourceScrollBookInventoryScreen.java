package steve_gall.minecolonies_tweaks.core.client.gui;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import steve_gall.minecolonies_tweaks.core.common.MineColoniesTweaks;
import steve_gall.minecolonies_tweaks.core.common.inventory.ResourceScrollBookInventoryMenu;

public class ResourceScrollBookInventoryScreen extends AbstractContainerScreen<ResourceScrollBookInventoryMenu>
{
	public static final Identifier TEXTURE = MineColoniesTweaks.rl("textures/gui/resourcescroll_book_inventory.png");

	public ResourceScrollBookInventoryScreen(ResourceScrollBookInventoryMenu menu, Inventory inventory, Component title)
	{
		super(menu, inventory, title);
	}

	@Override
	public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTicks)
	{
		super.extractBackground(graphics, mouseX, mouseY, partialTicks);
		graphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, 256, 256);
	}

}
