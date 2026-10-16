package steve_gall.minecolonies_tweaks.core.common.item.capability;

import java.util.Objects;

import com.minecolonies.api.items.ModItems;

import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.transfer.ItemAccessResourceHandler;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.item.ItemResource;

/**
 * Fluid handler of MineColonies' large bottles: empty <-> one bucket of water or milk.
 * 26.1.2 port: rewritten from IFluidHandlerItem onto the transfer API (same model as NeoForge's BucketResourceHandler).
 */
public class LargeBottleCapabilityProvider extends ItemAccessResourceHandler<FluidResource>
{
	public LargeBottleCapabilityProvider(ItemAccess itemAccess)
	{
		super(itemAccess, 1);
	}

	private static boolean isMilk(FluidResource resource)
	{
		return NeoForgeMod.MILK.isBound() && resource.is(NeoForgeMod.MILK.get());
	}

	@Override
	protected FluidResource getResourceFrom(ItemResource accessResource, int index)
	{
		if (accessResource.is(ModItems.large_water_bottle))
		{
			return FluidResource.of(Fluids.WATER);
		}
		else if (accessResource.is(ModItems.large_milk_bottle) && NeoForgeMod.MILK.isBound())
		{
			return FluidResource.of(NeoForgeMod.MILK.get());
		}

		return FluidResource.EMPTY;
	}

	@Override
	protected int getAmountFrom(ItemResource accessResource, int index)
	{
		return this.getResourceFrom(accessResource, index).isEmpty() ? 0 : FluidType.BUCKET_VOLUME;
	}

	@Override
	public boolean isValid(int index, FluidResource resource)
	{
		return resource.is(Fluids.WATER) || isMilk(resource);
	}

	@Override
	protected ItemResource update(ItemResource accessResource, int index, FluidResource newResource, int newAmount)
	{
		if (newAmount == 0)
		{
			return ItemResource.of(ModItems.large_empty_bottle);
		}
		else if (newAmount != FluidType.BUCKET_VOLUME)
		{
			return ItemResource.EMPTY;
		}
		else if (newResource.is(Fluids.WATER))
		{
			return ItemResource.of(ModItems.large_water_bottle);
		}
		else if (isMilk(newResource))
		{
			return ItemResource.of(ModItems.large_milk_bottle);
		}

		return ItemResource.EMPTY;
	}

	@Override
	protected int getCapacity(int index, FluidResource resource)
	{
		Objects.checkIndex(index, this.size());
		return FluidType.BUCKET_VOLUME;
	}

}
