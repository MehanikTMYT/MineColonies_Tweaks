package steve_gall.minecolonies_tweaks.core.common.crafting;

import java.util.List;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.NormalCraftingRecipe;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.level.Level;
import steve_gall.minecolonies_tweaks.core.common.init.MCTweaksRecipes;

/**
 * Shapeless recipe whose result receives the data components of every input stack.
 * 26.1.2 port: ShapelessRecipe#getSerializer is typed to ShapelessRecipe, so this recipe extends
 * NormalCraftingRecipe and delegates matching/display to a plain ShapelessRecipe with the same data
 * (same JSON format as ShapelessRecipe).
 */
public class ComponentMergeShapelessRecipe extends NormalCraftingRecipe
{
	public static final MapCodec<ComponentMergeShapelessRecipe> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			Recipe.CommonInfo.MAP_CODEC.forGetter(o -> o.commonInfo),
			CraftingRecipe.CraftingBookInfo.MAP_CODEC.forGetter(o -> o.bookInfo),
			ItemStackTemplate.CODEC.fieldOf("result").forGetter(o -> o.result),
			Codec.lazyInitialized(() -> Ingredient.CODEC.listOf(1, 9)).fieldOf("ingredients").forGetter(o -> o.ingredients))
			.apply(i, ComponentMergeShapelessRecipe::new));
	public static final StreamCodec<RegistryFriendlyByteBuf, ComponentMergeShapelessRecipe> STREAM_CODEC = StreamCodec.composite(
			Recipe.CommonInfo.STREAM_CODEC, o -> o.commonInfo,
			CraftingRecipe.CraftingBookInfo.STREAM_CODEC, o -> o.bookInfo,
			ItemStackTemplate.STREAM_CODEC, o -> o.result,
			Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()), o -> o.ingredients,
			ComponentMergeShapelessRecipe::new);

	private final ItemStackTemplate result;
	private final List<Ingredient> ingredients;
	private final ShapelessRecipe delegate;

	public ComponentMergeShapelessRecipe(Recipe.CommonInfo commonInfo, CraftingRecipe.CraftingBookInfo bookInfo, ItemStackTemplate result, List<Ingredient> ingredients)
	{
		super(commonInfo, bookInfo);
		this.result = result;
		this.ingredients = ingredients;
		this.delegate = new ShapelessRecipe(commonInfo, bookInfo, result, ingredients);
	}

	@Override
	public boolean matches(CraftingInput input, Level level)
	{
		return this.delegate.matches(input, level);
	}

	@Override
	public ItemStack assemble(CraftingInput container)
	{
		var assemble = this.delegate.assemble(container);
		var size = container.size();

		for (var i = 0; i < size; i++)
		{
			var base = container.getItem(i);
			assemble.applyComponents(base.getComponentsPatch());
		}

		return assemble;
	}

	@Override
	protected PlacementInfo createPlacementInfo()
	{
		return PlacementInfo.create(this.ingredients);
	}

	@Override
	public List<RecipeDisplay> display()
	{
		return this.delegate.display();
	}

	@Override
	public RecipeSerializer<ComponentMergeShapelessRecipe> getSerializer()
	{
		return MCTweaksRecipes.COMPONENT_MERGE_SHAPELESS_SERIALIZER.get();
	}

	public static RecipeSerializer<ComponentMergeShapelessRecipe> createSerializer()
	{
		return new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);
	}

}
