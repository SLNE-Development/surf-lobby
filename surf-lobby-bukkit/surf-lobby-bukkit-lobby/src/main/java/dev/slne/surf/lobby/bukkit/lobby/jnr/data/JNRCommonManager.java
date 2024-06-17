package dev.slne.surf.lobby.bukkit.lobby.jnr.data;

import dev.slne.surf.lobby.bukkit.lobby.jnr.data.type.Data;
import java.util.function.UnaryOperator;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataHolder;
import org.jetbrains.annotations.NotNull;

public abstract class JNRCommonManager {

  protected JNRCommonManager() {
  }

  protected <P, C> void editData(@NotNull PersistentDataHolder pdcHolder, @NotNull Data<P, C> dataType, UnaryOperator<C> edit) {
    editData(pdcHolder.getPersistentDataContainer(), dataType, edit);
  }

  protected <P, C> void editData(@NotNull PersistentDataContainer pdc, @NotNull Data<P, C> dataType,
      UnaryOperator<C> edit) {
    C data = pdc.get(dataType.getKey(), dataType.getType());

    if (data == null) {
      data = dataType.createDefault();
    }

    final C editedData = edit.apply(data);

    if (editedData == null) {
      pdc.remove(dataType.getKey());
      return;
    }

    pdc.set(dataType.getKey(), dataType.getType(), editedData);
  }

  protected <P, C> @NotNull C getData(@NotNull PersistentDataHolder pdcHolder, @NotNull Data<P, C> dataType) {
    return getData(pdcHolder.getPersistentDataContainer(), dataType);
  }

  protected <P, C> C getData(PersistentDataContainer pdc, @NotNull Data<P, C> dataType) {
    C data = pdc.get(dataType.getKey(), dataType.getType());

    if (data == null) {
      data = dataType.createDefault();

      if (data != null) {
        pdc.set(dataType.getKey(), dataType.getType(), data);
      }
    }

    return data;
  }
}
