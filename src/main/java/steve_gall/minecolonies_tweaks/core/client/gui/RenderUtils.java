package steve_gall.minecolonies_tweaks.core.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;

/**
 * GUI fluid rendering.
 * 26.1.2 port: the immediate-mode Tesselator/RenderSystem path is gone; the fluid sprite and tint come from the
 * fluid model set and are drawn as tiled sprites, the partial fill height is cut with a scissor.
 */
public class RenderUtils
{
	public static final int FLUID_TEXTURE_SIZE = 16;

	public static void renderFluid(GuiGraphicsExtractor graphics, int width, int height, FluidStack fluidStack)
	{
		renderFluid(graphics, width, height, fluidStack, fluidStack.getAmount());
	}

	public static void renderFluid(GuiGraphicsExtractor graphics, int width, int height, FluidStack fluidStack, int capacity)
	{
		var fluid = fluidStack.getFluid();

		if (fluid.isSame(Fluids.EMPTY))
		{
			return;
		}

		var fluidModel = Minecraft.getInstance().getModelManager().getFluidStateModelSet().get(fluid.defaultFluidState());

		if (fluidModel == null)
		{
			return;
		}

		var sprite = fluidModel.stillMaterial().sprite();
		var tintColor = -1;
		var tintSource = fluidModel.fluidTintSource();

		if (tintSource != null)
		{
			tintColor = tintSource.colorAsStack(fluidStack);
		}

		var scachedHeight = (int) (height * (fluidStack.getAmount() / Math.max(capacity, 1.0D)));

		if (scachedHeight <= 0)
		{
			return;
		}

		graphics.enableScissor(0, height - scachedHeight, width, height);

		for (var x = 0; x < width; x += FLUID_TEXTURE_SIZE)
		{
			for (var y = height - FLUID_TEXTURE_SIZE; y > -FLUID_TEXTURE_SIZE; y -= FLUID_TEXTURE_SIZE)
			{
				graphics.blitSprite(RenderPipelines.GUI_TEXTURED, sprite, x, y, FLUID_TEXTURE_SIZE, FLUID_TEXTURE_SIZE, tintColor | 0xFF000000);
			}

		}

		graphics.disableScissor();
	}

	private RenderUtils()
	{

	}

}
