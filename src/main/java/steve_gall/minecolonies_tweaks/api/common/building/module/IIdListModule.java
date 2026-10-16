package steve_gall.minecolonies_tweaks.api.common.building.module;

import java.util.Collection;

import org.jetbrains.annotations.NotNull;

import net.minecraft.resources.Identifier;

public interface IIdListModule
{
	boolean addId(@NotNull Identifier id);

	boolean addIds(@NotNull Collection<Identifier> ids);

	boolean containsId(@NotNull Identifier id);

	boolean removeId(@NotNull Identifier id);

	boolean removeIds(@NotNull Collection<Identifier> ids);

	void clearIds();

	@NotNull
	Collection<Identifier> getIds();

	@NotNull
	String getListId();
}
