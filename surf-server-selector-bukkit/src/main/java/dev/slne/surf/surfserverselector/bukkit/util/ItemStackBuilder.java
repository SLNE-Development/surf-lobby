package dev.slne.surf.surfserverselector.bukkit.util;

import static com.google.common.base.Preconditions.checkNotNull;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import java.util.List;
import java.util.function.Consumer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.format.TextDecoration.State;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class ItemStackBuilder {

  private final Material material;
  private int amount = 1;

  private ObjectList<Consumer<PersistentDataContainer>> pdcModifiers;

  private Component displayName;
  private List<? extends Component> lore;

  private ItemStackBuilder(Material material) {
    this.material = material;

    this.pdcModifiers = new ObjectArrayList<>();
  }

  @Contract(value = "_ -> new", pure = true)
  public static @NotNull ItemStackBuilder create(Material material) {
    return new ItemStackBuilder(material);
  }

  public ItemStackBuilder withAmount(int amount) {
    this.amount = amount;
    return this;
  }

  public ItemStackBuilder withDisplayName(Component displayName) {
    this.displayName = checkNotNull(displayName, "displayName");
    return this;
  }

  public ItemStackBuilder withLore(List<? extends Component> lore) {
    this.lore = checkNotNull(lore, "lore");
    return this;
  }

  public ItemStackBuilder withLore(Component... lore) {
    return withLore(List.of(lore));
  }

  public ItemStackBuilder withPdcModifier(Consumer<PersistentDataContainer> pdcModifier) {
    this.pdcModifiers.add(checkNotNull(pdcModifier, "pdcModifier"));
    return this;
  }

  @Contract("_ -> this")
  @SafeVarargs
  public final ItemStackBuilder withPdcModifier(Consumer<PersistentDataContainer> @NotNull ... pdcModifiers) {
    for (final Consumer<PersistentDataContainer> pdcModifier : pdcModifiers) {
      withPdcModifier(pdcModifier);
    }

    return this;
  }

  public ItemStackBuilder withPdcModifier(@NotNull List<Consumer<PersistentDataContainer>> pdcModifiers) {
    for (final Consumer<PersistentDataContainer> pdcModifier : pdcModifiers) {
      withPdcModifier(pdcModifier);
    }

    return this;
  }


  public ItemStack build() { // TODO: 16.05.2024 09:17 - build
    final ItemStack itemStack = new ItemStack(material, amount);

    itemStack.editMeta(meta -> {
      if (displayName != null) {
        meta.displayName(removeItalic(displayName));
      }

      if (lore != null) {
        meta.lore(lore.stream()
            .map(this::removeItalic)
            .toList());
      }

      final PersistentDataContainer pdc = meta.getPersistentDataContainer();
      pdcModifiers.forEach(pdcModifier -> pdcModifier.accept(pdc));
    });

    return itemStack;
  }

  private <T extends Component> @NotNull T removeItalic(@NotNull T component) {
    return (T) component.decorationIfAbsent(TextDecoration.ITALIC, State.FALSE);
  }
}
