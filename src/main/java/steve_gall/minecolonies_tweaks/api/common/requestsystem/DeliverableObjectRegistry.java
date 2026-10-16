package steve_gall.minecolonies_tweaks.api.common.requestsystem;

import net.minecraft.resources.Identifier;
import steve_gall.minecolonies_tweaks.api.common.SimpleObjectRegistry;

public class DeliverableObjectRegistry extends SimpleObjectRegistry<IDeliverableObject>
{
	public static final DeliverableObjectRegistry INSTANCE = new DeliverableObjectRegistry();

	@Override
	protected Identifier getId(IDeliverableObject object)
	{
		return object.getId();
	}

}
