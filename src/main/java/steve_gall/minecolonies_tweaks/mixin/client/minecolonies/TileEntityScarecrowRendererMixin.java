package steve_gall.minecolonies_tweaks.mixin.client.minecolonies;

import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.minecolonies.api.blocks.huts.AbstractBlockMinecoloniesDefault;
import com.minecolonies.api.colony.IColonyView;
import com.minecolonies.api.tileentities.AbstractTileEntityScarecrow;
import com.minecolonies.core.client.render.ScarecrowRenderState;
import com.minecolonies.core.client.render.TileEntityScarecrowRenderer;
import com.minecolonies.core.colony.buildingextensions.FarmField;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.phys.Vec3;
import steve_gall.minecolonies_tweaks.core.client.render.ScarecrowSeedRenderState;
import steve_gall.minecolonies_tweaks.core.common.colony.IRegisteredStructureManagerViewExtension;
import steve_gall.minecolonies_tweaks.core.common.config.MCTweaksConfigClient;

/**
 * Renders the field's seed above the scarecrow.
 * 26.1.2 port: block entity rendering is split into extractRenderState (game data -> render state) and submit
 * (render state -> draw calls), so the seed is resolved into an ItemStackRenderState carried by the scarecrow's
 * render state (see ScarecrowRenderStateMixin) and submitted after the scarecrow itself.
 */
@Mixin(value = TileEntityScarecrowRenderer.class, remap = false)
public abstract class TileEntityScarecrowRendererMixin
{
	@Inject(method = "extractRenderState(Lcom/minecolonies/api/tileentities/AbstractTileEntityScarecrow;Lcom/minecolonies/core/client/render/ScarecrowRenderState;FLnet/minecraft/world/phys/Vec3;Lnet/minecraft/client/renderer/feature/ModelFeatureRenderer$CrumblingOverlay;)V", remap = false, at = @At(value = "TAIL"))
	private void extractRenderState(AbstractTileEntityScarecrow te, ScarecrowRenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress, CallbackInfo ci)
	{
		var seedState = (ScarecrowSeedRenderState) state;
		var seed = seedState.minecolonies_tweaks$getSeed();
		seed.clear();

		if (!state.render || !MCTweaksConfigClient.INSTANCE.renderFieldSeed.get())
		{
			return;
		}

		if (te.getCurrentColony() instanceof IColonyView colonyView && colonyView.getClientBuildingManager() instanceof IRegisteredStructureManagerViewExtension buildingManager && buildingManager.minecolonies_tweaks$getBuildingExtension(te.getBlockPos()) instanceof FarmField field)
		{
			var facing = te.getLevel().getBlockState(te.getBlockPos()).getOptionalValue(AbstractBlockMinecoloniesDefault.FACING).orElse(null);

			if (facing == null)
			{
				return;
			}

			seedState.minecolonies_tweaks$setSeedFacing(facing);
			Minecraft.getInstance().getItemModelResolver().updateForTopItem(seed, field.getSeed(), ItemDisplayContext.FIXED, te.getLevel(), null, 0);
		}

	}

	@Inject(method = "submit(Lcom/minecolonies/core/client/render/ScarecrowRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V", remap = false, at = @At(value = "TAIL"))
	private void submit(ScarecrowRenderState state, PoseStack matrixStack, SubmitNodeCollector collector, CameraRenderState camera, CallbackInfo ci)
	{
		var seedState = (ScarecrowSeedRenderState) state;
		var seed = seedState.minecolonies_tweaks$getSeed();

		if (seed.isEmpty())
		{
			return;
		}

		matrixStack.pushPose();
		matrixStack.translate(0.5D, 2.5D, 0.5D);
		matrixStack.mulPose(seedState.minecolonies_tweaks$getSeedFacing().getRotation());
		matrixStack.mulPose(Axis.ZP.rotationDegrees(180.0F));
		matrixStack.mulPose(Axis.XP.rotationDegrees(-90.0F));
		matrixStack.scale(0.5F, 0.5F, 0.5F);
		seed.submit(matrixStack, collector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
		matrixStack.popPose();
	}

}
