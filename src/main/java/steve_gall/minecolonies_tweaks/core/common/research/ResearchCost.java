package steve_gall.minecolonies_tweaks.core.common.research;

import net.minecraft.core.UUIDUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.jetbrains.annotations.NotNull;

import com.google.common.collect.ImmutableList;
import com.minecolonies.api.colony.IColonyView;
import com.minecolonies.api.colony.buildings.IBuilding;
import com.minecolonies.api.colony.buildings.views.IBuildingView;
import com.minecolonies.api.colony.requestsystem.factory.IFactoryController;
import com.minecolonies.api.colony.requestsystem.request.IRequest;
import com.minecolonies.api.crafting.ItemStorage;
import com.minecolonies.api.research.IGlobalResearchTree;
import com.minecolonies.api.util.NBTUtils;

import net.minecraft.ChatFormatting;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import steve_gall.minecolonies_tweaks.api.common.requestsystem.CustomizableRequestable;
import steve_gall.minecolonies_tweaks.api.common.requestsystem.IRequestableObject;
import steve_gall.minecolonies_tweaks.core.common.MineColoniesTweaks;
import steve_gall.minecolonies_tweaks.core.common.util.SerializationHelper;

public class ResearchCost implements IRequestableObject
{
	public static final Identifier ID = MineColoniesTweaks.rl("research_cost");
	public static final Component DISPLAY_STRING = Component.translatable("minecolonies_tweaks.text.research_cost");
	public static final Identifier ICON = Identifier.parse("textures/item/book.png");

	public static ResearchCost deserialize(HolderLookup.Provider provider, IFactoryController controller, CompoundTag compound)
	{
		var branch = Identifier.parse(compound.getStringOr("branch", ""));
		var research = Identifier.parse(compound.getStringOr("research", ""));
		var items = NBTUtils.streamCompound(compound.getListOrEmpty("stacks")).map(SerializationHelper.<ItemStorage> deserializerTag(provider)).toList();
		var requester = compound.read("requester", UUIDUtil.CODEC).isPresent() ? compound.read("requester", UUIDUtil.CODEC).orElseThrow() : null;

		return new ResearchCost(branch, research, items, requester);
	}

	public static void serialize(HolderLookup.Provider provider, IFactoryController controller, CompoundTag compound, ResearchCost cost)
	{
		compound.putString("branch", cost.branchId.toString());
		compound.putString("research", cost.researchId.toString());
		compound.put("items", cost.items.stream().map(SerializationHelper.serializerTag(provider)).collect(NBTUtils.toListNBT()));

		if (cost.requester != null)
		{
			compound.store("requester", UUIDUtil.CODEC, cost.requester);
		}

	}

	public static boolean test(IRequest<?> request, Identifier branch, Identifier research)
	{
		if (request.getRequest() instanceof CustomizableRequestable cr && cr.getObject() instanceof ResearchCost other)
		{
			if (other.getBranchId().equals(branch) && other.getResearchId().equals(research))
			{
				return true;
			}

		}

		return false;
	}

	public static boolean isRequested(IBuilding building, Identifier branch, Identifier research)
	{
		for (var token : building.getOpenRequestsByRequestableType().getOrDefault(CustomizableRequestable.TYPE_TOKEN, Collections.emptyList()))
		{
			var request = building.getColony().getRequestManager().getRequestForToken(token);

			if (test(request, branch, research))
			{
				return true;
			}

		}

		return false;
	}

	public static boolean isRequested(IBuildingView buildingView, Identifier branch, Identifier research)
	{
		for (var request : buildingView.getOpenRequestsOfBuilding())
		{
			if (test(request, branch, research))
			{
				return true;
			}

		}

		return false;
	}

	private final Identifier branchId;
	private final Identifier researchId;
	private final List<ItemStorage> items;
	private final UUID requester;

	private final Component longText;
	private final List<MutableComponent> tooltip;

	public ResearchCost(Identifier branchId, Identifier researchId, List<ItemStorage> items, UUID requester)
	{
		this.branchId = branchId;
		this.researchId = researchId;
		this.items = ImmutableList.copyOf(items);
		this.requester = requester;

		var researchTree = IGlobalResearchTree.getInstance();
		var branch = researchTree.getBranchData(branchId);

		var branchText = Component.literal(branchId.toString());
		var researchText = Component.literal(researchId.toString());
		var tooltip = new ArrayList<MutableComponent>();
		tooltip.add(Component.empty().append(DISPLAY_STRING));

		if (branch == null)
		{
			tooltip.add(Component.translatable("minecolonies_tweaks.gui.branch_missing").withStyle(ChatFormatting.RED));
			tooltip.add(Component.translatable("minecolonies_tweaks.text.research_cost.branch", Component.literal(branchId.toString()).withStyle(ChatFormatting.RED)));
			tooltip.add(Component.translatable("minecolonies_tweaks.text.research_cost.research", Component.literal(researchId.toString()).withStyle(ChatFormatting.RED)));
		}
		else
		{
			var research = researchTree.getResearch(branchId, researchId);
			branchText = MutableComponent.create(branch.getName());
			tooltip.add(Component.translatable("minecolonies_tweaks.text.research_cost.branch", branchText));

			if (research == null)
			{
				tooltip.add(Component.translatable("minecolonies_tweaks.gui.research_missing").withStyle(ChatFormatting.RED));
				tooltip.add(Component.translatable("minecolonies_tweaks.text.research_cost.research", Component.literal(researchId.toString()).withStyle(ChatFormatting.RED)));
			}
			else
			{
				researchText = MutableComponent.create(research.getName());
				tooltip.add(Component.translatable("minecolonies_tweaks.text.research_cost.research", researchText));
			}

		}

		this.longText = Component.translatable("minecolonies_tweaks.text.research_cost.long", branchText, researchText);
		this.tooltip = tooltip;
	}

	@Override
	public @NotNull Identifier getId()
	{
		return ID;
	}

	@Override
	public @NotNull Component getShortDisplayString()
	{
		return DISPLAY_STRING;
	}

	@Override
	public @NotNull Component getLongDisplayString()
	{
		return this.longText;
	}

	@Override
	public @NotNull List<ItemStack> getDisplayStacks()
	{
		return Collections.emptyList();
	}

	@Override
	public Identifier getDisplayIcon()
	{
		return ICON;
	}

	@Override
	public @NotNull List<MutableComponent> getResolverToolTip(@NotNull IColonyView colony)
	{
		return this.tooltip;
	}

	@Override
	public boolean equals(Object obj)
	{
		return obj instanceof ResearchCost other && other.branchId.equals(this.branchId) && other.researchId.equals(this.researchId);
	}

	public Identifier getBranchId()
	{
		return this.branchId;
	}

	public Identifier getResearchId()
	{
		return this.researchId;
	}

	public List<ItemStorage> getItems()
	{
		return this.items;
	}

	public UUID getRequester()
	{
		return this.requester;
	}

}
