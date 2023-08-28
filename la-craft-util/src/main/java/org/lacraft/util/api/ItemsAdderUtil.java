package org.lacraft.util.api;

import dev.lone.itemsadder.api.CustomStack;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.security.CodeSource;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * 플러그인의 resources/contents 디렉토리 내의 모든 파일을 plugins/ItemsAdder 디렉토리로 복사합니다. 만약 복사 대상 파일이 이미 존재한다면, 해당 파일은 건너뜁니다.
 */
public class ItemsAdderUtil {

    static final String WARNING = "<RED>Please don't forget to regen your resourcepack using /iazip command.</RED>";

    // 이 메서드는 플러그인의 resources 폴더에서 모든 파일을 가져와서 ItemsAdder 폴더로 복사합니다.
    // 복사 후에는 새로운 아이템이 추가되었음을 알리는 이벤트를 발생시킵니다.
    public static boolean extractDefaultAssets(JavaPlugin plugin) {
        MessageUtil.sendConsoleMessage("<GREEN>" + plugin.getName() + " -> ItemsAdder" + "</GREEN>");

        boolean needsIaZip = false;
        File itemsadderRoot = new File(plugin.getDataFolder().getParent(), "/ItemsAdder");

        MessageUtil.sendConsoleMessage("<AQUA>Extracting assets...</AQUA>");
        // resources 폴더에서 모든 파일을 가져와서 ItemsAdder 폴더로 복사합니다.
        needsIaZip = needsIaZip || extractResourceFilesToTargetDir(plugin, itemsadderRoot, "contents");

        MessageUtil.sendConsoleMessage("<GREEN>DONE extracting assets!</GREEN>");
        if (needsIaZip) {
            MessageUtil.sendConsoleMessage(WARNING);
            // 새로운 아이템이 추가되었음을 알리는 이벤트를 발생시킵니다.

        }
        return needsIaZip;
    }

    // 이 메서드는 특정 파일을 플러그인의 resources 폴더에서 ItemsAdder 폴더로 복사합니다.
    // 파일이 이미 존재한다면 복사를 건너뜁니다.
    private static boolean doExtractFile(JavaPlugin plugin, File itemsadderRoot, String resourceName) throws IOException {
        File dest = new File(itemsadderRoot, resourceName);
        // 대상 파일이 존재하지 않는 경우만 복사를 진행합니다.
        if (!dest.exists()) {
            FileUtils.copyInputStreamToFile(plugin.getResource(resourceName), dest);
            MessageUtil.sendConsoleMessage("<AQUA>       - Extracted " + resourceName + "</AQUA>");
            return true;
        }
        return false;
    }

    // 이 메서드는 플러그인의 resources 폴더에서 모든 파일을 가져와서 ItemsAdder 폴더로 복사합니다.
    private static boolean extractResourceFilesToTargetDir(JavaPlugin plugin, File itemsadderRoot, String folder) {
        boolean needsIaZip = false;
        CodeSource src = plugin.getClass().getProtectionDomain().getCodeSource();

        if (src != null) {
            URL jar = src.getLocation();
            try (ZipInputStream zip = new ZipInputStream(jar.openStream())) {
                while (true) {
                    ZipEntry e = zip.getNextEntry();
                    if (e == null) {
                        break;
                    }
                    if (e.isDirectory()) {
                        continue;
                    }
                    String name = e.getName();
                    if (name.startsWith(folder)) {
                        boolean result = doExtractFile(plugin, itemsadderRoot, name);
                        if (result) {
                            needsIaZip = true;
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return needsIaZip;
    }

    public static ItemStack getCustomItemStack(String namespacedID, ItemStack defaultItemStack) {
        if (Bukkit.getPluginManager().isPluginEnabled("ItemsAdder")) {
            CustomStack customStack = CustomStack.getInstance(namespacedID);
            if (customStack != null) {
                return customStack.getItemStack();
            } else {
                return new ItemStack(defaultItemStack);
            }
        } else {
            return new ItemStack(defaultItemStack);
        }
    }
}

