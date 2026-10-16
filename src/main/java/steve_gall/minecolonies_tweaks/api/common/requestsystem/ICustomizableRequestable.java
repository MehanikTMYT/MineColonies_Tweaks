package steve_gall.minecolonies_tweaks.api.common.requestsystem;

import org.jetbrains.annotations.NotNull;

import com.minecolonies.api.colony.requestsystem.requestable.IRequestable;

import net.minecraft.resources.Identifier;

public interface ICustomizableRequestable extends IRequestable
{
	@NotNull
	Identifier getId();

	@NotNull
	IRequestableObject getObject();
}
