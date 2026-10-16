package steve_gall.minecolonies_tweaks.mixin.client.minecolonies;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import com.minecolonies.core.client.render.ScarecrowRenderState;

import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.core.Direction;
import steve_gall.minecolonies_tweaks.core.client.render.ScarecrowSeedRenderState;

@Mixin(value = ScarecrowRenderState.class, remap = false)
public abstract class ScarecrowRenderStateMixin implements ScarecrowSeedRenderState
{
	@Unique
	private final ItemStackRenderState minecolonies_tweaks$seed = new ItemStackRenderState();
	@Unique
	private Direction minecolonies_tweaks$seedFacing = Direction.NORTH;

	@Override
	public ItemStackRenderState minecolonies_tweaks$getSeed()
	{
		return this.minecolonies_tweaks$seed;
	}

	@Override
	public Direction minecolonies_tweaks$getSeedFacing()
	{
		return this.minecolonies_tweaks$seedFacing;
	}

	@Override
	public void minecolonies_tweaks$setSeedFacing(Direction facing)
	{
		this.minecolonies_tweaks$seedFacing = facing;
	}

}
