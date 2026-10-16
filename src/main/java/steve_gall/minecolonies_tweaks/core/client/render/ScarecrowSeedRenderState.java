package steve_gall.minecolonies_tweaks.core.client.render;

import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.core.Direction;

/** Extra render-state data added to MineColonies' ScarecrowRenderState (26.1.2 port). */
public interface ScarecrowSeedRenderState
{
	ItemStackRenderState minecolonies_tweaks$getSeed();

	Direction minecolonies_tweaks$getSeedFacing();

	void minecolonies_tweaks$setSeedFacing(Direction facing);
}
