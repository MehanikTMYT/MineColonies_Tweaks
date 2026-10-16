package steve_gall.minecolonies_tweaks.api.common.building.module;

import java.util.Collection;
import java.util.Collections;

import org.jetbrains.annotations.NotNull;

import com.minecolonies.api.colony.buildings.modules.IBuildingModuleView;

import net.minecraft.resources.Identifier;

public interface IIdListModuleView extends IBuildingModuleView
{
	default boolean addId(@NotNull Identifier id)
	{
		return this.addIds(Collections.singletonList(id));
	}

	boolean addIds(@NotNull Collection<Identifier> ids);

	boolean containsId(@NotNull Identifier id);

	default boolean removeId(@NotNull Identifier id)
	{
		return this.removeIds(Collections.singletonList(id));
	}

	boolean removeIds(@NotNull Collection<Identifier> ids);

	void clearIds();

	@NotNull
	String getListId();
}
