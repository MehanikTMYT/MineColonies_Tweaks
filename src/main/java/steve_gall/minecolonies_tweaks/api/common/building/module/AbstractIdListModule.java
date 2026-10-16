package steve_gall.minecolonies_tweaks.api.common.building.module;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.jetbrains.annotations.NotNull;

import com.minecolonies.api.colony.buildings.modules.AbstractBuildingModule;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.Identifier;
import steve_gall.minecolonies_tweaks.core.common.MineColoniesTweaks;

public abstract class AbstractIdListModule extends AbstractBuildingModule implements ICopyableModule, IIdListModule
{
	public static final String TAG_IDS = MineColoniesTweaks.rl("ids").toString();

	@NotNull
	private final String listId;
	@NotNull
	private final Set<Identifier> ids;

	public AbstractIdListModule(@NotNull String listId)
	{
		this.listId = listId;
		this.ids = new HashSet<>();
	}

	@Override
	public void deserializeNBT(@NotNull HolderLookup.Provider provider, @NotNull CompoundTag compound)
	{
		this.ids.clear();
		var idsTag = compound.getListOrEmpty(TAG_IDS);

		for (var i = 0; i < idsTag.size(); i++)
		{
			this.ids.add(Identifier.parse(idsTag.getStringOr(i, "")));
		}

	}

	@Override
	public void serializeNBT(@NotNull HolderLookup.Provider provider, @NotNull CompoundTag compound)
	{
		var idsTag = new ListTag();
		compound.put(TAG_IDS, idsTag);

		for (var id : this.ids)
		{
			idsTag.add(StringTag.valueOf(id.toString()));
		}

	}

	@Override
	public void serializeToView(@NotNull RegistryFriendlyByteBuf buf)
	{
		super.serializeToView(buf);

		buf.writeUtf(this.listId);
		buf.writeCollection(this.ids, FriendlyByteBuf::writeIdentifier);
	}

	protected void onIdAdded(@NotNull Identifier id)
	{

	}

	protected void onIdRemoved(@NotNull Identifier id)
	{

	}

	protected void onIdsCleared()
	{

	}

	protected void onIdsChanged()
	{

	}

	@Override
	public boolean addId(@NotNull Identifier id)
	{
		var added = this.ids.add(id);

		if (added)
		{
			this.onIdAdded(id);
			this.onIdsChanged();
			this.markDirty();
		}

		return added;
	}

	@Override
	public boolean addIds(@NotNull Collection<Identifier> ids)
	{
		var anyAdded = false;

		for (var id : ids)
		{
			if (this.ids.add(id))
			{
				anyAdded = true;
				this.onIdAdded(id);
			}

		}

		if (anyAdded)
		{
			this.onIdsChanged();
			this.markDirty();
		}

		return anyAdded;
	}

	@Override
	public boolean containsId(@NotNull Identifier id)
	{
		return this.ids.contains(id);
	}

	@Override
	public boolean removeId(@NotNull Identifier id)
	{
		var removed = this.ids.remove(id);

		if (removed)
		{
			this.onIdRemoved(id);
			this.onIdsChanged();
			this.markDirty();
		}

		return removed;
	}

	@Override
	public boolean removeIds(@NotNull Collection<Identifier> ids)
	{
		var anyRemoved = false;

		for (var id : ids)
		{
			if (this.ids.remove(id))
			{
				anyRemoved = true;
				this.onIdRemoved(id);
			}

		}

		if (anyRemoved)
		{
			this.onIdsChanged();
			this.markDirty();
		}

		return false;
	}

	@Override
	public void clearIds()
	{
		this.ids.clear();

		this.onIdsCleared();
		this.onIdsChanged();
		this.markDirty();
	}

	@Override
	public @NotNull Set<Identifier> getIds()
	{
		return new HashSet<>(this.ids);
	}

	@Override
	public @NotNull String getListId()
	{
		return this.listId;
	}

}
