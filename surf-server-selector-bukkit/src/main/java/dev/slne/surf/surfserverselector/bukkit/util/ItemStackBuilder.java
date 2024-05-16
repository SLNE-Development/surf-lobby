package dev.slne.surf.surfserverselector.bukkit.util;

import static com.google.common.base.Preconditions.checkNotNull;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap.Entry;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.util.List;
import java.util.function.Consumer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.format.TextDecoration.State;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class ItemStackBuilder {

  private final Material material;
  private int amount = 1;

  private ObjectList<Consumer<PersistentDataContainer>> pdcModifiers;
  private Object2ObjectMap<Class<? extends ItemMeta>, ObjectList<Consumer<? super ItemMeta>>> metaModifiers;

  private Object2IntMap<Enchantment> enchantments;
  private ObjectSet<ItemFlag> itemFlags;

  private Component displayName;
  private List<? extends Component> lore;

  private ItemStackBuilder(Material material) {
    this.material = material;

    this.pdcModifiers = new ObjectArrayList<>();
    this.metaModifiers = new Object2ObjectOpenHashMap<>();

    this.enchantments = new Object2IntOpenHashMap<>();
    this.enchantments.defaultReturnValue(1);

    this.itemFlags = new ObjectArraySet<>(ItemFlag.values().length);
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
  public final ItemStackBuilder withPdcModifier(
      Consumer<PersistentDataContainer> @NotNull ... pdcModifiers) {
    for (final Consumer<PersistentDataContainer> pdcModifier : pdcModifiers) {
      withPdcModifier(pdcModifier);
    }

    return this;
  }

  public ItemStackBuilder withPdcModifier(
      @NotNull List<Consumer<PersistentDataContainer>> pdcModifiers) {
    for (final Consumer<PersistentDataContainer> pdcModifier : pdcModifiers) {
      withPdcModifier(pdcModifier);
    }

    return this;
  }

  public <M extends ItemMeta> ItemStackBuilder withMetaModifier(Class<M> metaType,
      Consumer<? super M> metaModifier) {
    final ObjectList<Consumer<? super ItemMeta>> metaModifiers = this.metaModifiers.computeIfAbsent(
        metaType, key -> new ObjectArrayList<>());
    metaModifiers.add((Consumer<? super ItemMeta>) checkNotNull(metaModifier, "metaModifier"));
    return this;
  }

  public <T extends ItemMeta> ItemStackBuilder withMetaModifier(Class<T> metaType,
      Consumer<T> @NotNull ... metaModifiers) {
    for (final Consumer<T> metaModifier : metaModifiers) {
      withMetaModifier(metaType, metaModifier);
    }

    return this;
  }

  public <T extends ItemMeta> ItemStackBuilder withMetaModifier(Class<T> metaType,
      @NotNull List<Consumer<T>> metaModifiers) {
    for (final Consumer<T> metaModifier : metaModifiers) {
      withMetaModifier(metaType, metaModifier);
    }

    return this;
  }

  public ItemStackBuilder withEnchantment(Enchantment enchantment, int level) {
    final int currentLevel = enchantments.getInt(enchantment);
    enchantments.put(enchantment, Math.max(currentLevel, level));

    return this;
  }

  public ItemStackBuilder withEnchantment(Enchantment enchantment) {
    return withEnchantment(enchantment, 1);
  }

  public ItemStackBuilder withEnchantments(Enchantment... enchantments) {
    for (final Enchantment enchantment : enchantments) {
      withEnchantment(enchantment);
    }

    return this;
  }

  public ItemStackBuilder withGlint() {
    return withEnchantment(Enchantment.LURE);
  }

  public ItemStackBuilder withItemFlag(@NotNull ItemFlag @NotNull ... itemFlag) {
    for (final ItemFlag flag : checkNotNull(itemFlag, "itemFlags")) {
      this.itemFlags.add(checkNotNull(flag, "itemFlag"));
    }

    return this;
  }

  public ItemStack build() {
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

      meta.addItemFlags(itemFlags.toArray(ItemFlag[]::new));

      for (final Entry<Enchantment> enchantmentEntry : enchantments.object2IntEntrySet()) {
        meta.addEnchant(enchantmentEntry.getKey(), enchantmentEntry.getIntValue(), true);
      }

      final PersistentDataContainer pdc = meta.getPersistentDataContainer();
      pdcModifiers.forEach(pdcModifier -> pdcModifier.accept(pdc));

      metaModifiers.forEach((metaType, metaModifiers) -> {
        if (metaType.isInstance(meta)) {
          metaModifiers.forEach(metaModifier -> metaModifier.accept(metaType.cast(meta)));
        }
      });
    });

    return itemStack;
  }

  private <T extends Component> @NotNull T removeItalic(@NotNull T component) {
    return (T) component.decorationIfAbsent(TextDecoration.ITALIC, State.FALSE);
  }
}
