package steve_gall.minecolonies_tweaks.mixin.common.minecolonies;

import org.spongepowered.asm.mixin.injection.Redirect;
import java.util.function.Consumer;
import java.util.List;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.minecolonies.api.util.constant.TranslationConstants;
import com.minecolonies.core.blocks.MinecoloniesCropBlock;
import com.minecolonies.core.items.ItemCrop;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import steve_gall.minecolonies_tweaks.core.common.config.MCTweaksConfigServer;
import steve_gall.minecolonies_tweaks.core.common.item.ItemCropExtension;

@Mixin(value = ItemCrop.class, remap = false)
public abstract class ItemCropMixin extends BlockItem implements ItemCropExtension
{
	@Final
	@Mutable
	@Shadow(remap = false)
	private TagKey<Biome> preferredBiome;
	private TagKey<Biome> minecolonies_tweaks$preferredBiome;

	public ItemCropMixin(Block p_40565_, Properties p_40566_)
	{
		super(p_40565_, p_40566_);
	}

	@Inject(method = "<init>", remap = false, at = @At(value = "TAIL"))
	public void init(MinecoloniesCropBlock cropBlock, Properties builder, TagKey<Biome> preferredBiome, CallbackInfo ci)
	{
		this.minecolonies_tweaks$preferredBiome = preferredBiome;
	}

	@Override
	public void minecolonies_tweaks$onServerConfigReloaded()
	{
		this.preferredBiome = MCTweaksConfigServer.INSTANCE.blocks.cropIgnoreBiome.get().booleanValue() ? null : this.minecolonies_tweaks$preferredBiome;
	}

	@WrapOperation(method = "canPlace", remap = true, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;isCreative()Z", remap = true))
	private boolean canPlace_isCreative(Player player, Operation<Boolean> operation)
	{
		if (MCTweaksConfigServer.INSTANCE.blocks.cropVanillaFarmland.get().booleanValue())
		{
			return true;
		}
		else
		{
			return operation.call(player);
		}

	}

	// 26.1.2 port: the tooltip is a Consumer<Component>, so unwanted lines are filtered as they are emitted
	// instead of being removed from the finished list.
	@Redirect(method = "appendHoverText", remap = true, at = @At(value = "INVOKE", target = "Ljava/util/function/Consumer;accept(Ljava/lang/Object;)V"))
	private void appendHoverText_accept(Consumer<Object> tooltip, Object line)
	{
		if (!(line instanceof Component component && this.testForRemove(component)))
		{
			tooltip.accept(line);
		}

	}

	private boolean testForRemove(Component line)
	{
		var config = MCTweaksConfigServer.INSTANCE.blocks;

		if (config.cropVanillaFarmland.get().booleanValue())
		{
			return line.getContents() instanceof TranslatableContents contents && contents.getKey().equals(TranslationConstants.CROP_TOOLTIP);
		}

		return false;
	}

}
