package me.xiaox.revive.utils;

import java.lang.reflect.InvocationTargetException;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.WrappedChatComponent;

import me.xiaox.revive.Revive;

public class SendUtil {
	
	private static FileConfiguration config;
	private static String prefix;
	
	public static void initSend() {
		config = Revive.getConfigFile();
		prefix = config.getString("prefix");
	}
	
	/**
	 * ��sender����һ����Ϣ
	 * @param sender Ҫ���͵���
	 * @param key ���ü�
	 */
	public static void sendMessage(CommandSender sender, String key) {
		sender.sendMessage(config.getString(key).replace("&", "��").replace("%prefix%", prefix.replace("&", "��")));
		return;
	}
	
	/**
	 * ����һ��Title�����
	 * @param player Ŀ�����
	 * @param fadeIn ����ʱ�� tick
	 * @param stay ͣ��ʱ�� tick
	 * @param fadeOut ����ʱ�� tick
	 * @param title ��������
	 * @param subTitle ����������
	 */
	public static void sendTitle(Player player, Integer fadeIn, Integer stay, Integer fadeOut, String title, String subTitle) {
		ProtocolManager pm = Revive.getProtocolManager();
		PacketContainer packet = null;
		
		//���ñ���
		if (title != null) {
			title = title.replace("&", "��").replace("%player%", player.getName());
			packet = pm.createPacket(PacketType.Play.Server.TITLE);
			packet.getTitleActions().write(0, EnumWrappers.TitleAction.TITLE); // EnumTitleAction
			packet.getChatComponents().write(0, WrappedChatComponent.fromText(title)); // ��������
			try {
				pm.sendServerPacket(player, packet, false); // �������ݰ�
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		
		//���ø�����
		if (subTitle != null) {
			subTitle = subTitle.replace("&", "��").replace("%player%", player.getName());
			packet = pm.createPacket(PacketType.Play.Server.TITLE);
			packet.getTitleActions().write(0, EnumWrappers.TitleAction.SUBTITLE);
			packet.getChatComponents().write(0, WrappedChatComponent.fromText(subTitle));
			try {
				pm.sendServerPacket(player, packet, false); // �������ݰ�
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		packet = pm.createPacket(PacketType.Play.Server.TITLE);
		packet.getTitleActions().write(0, EnumWrappers.TitleAction.TIMES);
		packet.getIntegers().write(0, fadeIn); // ---> c
		packet.getIntegers().write(1, stay); // ---> d
		packet.getIntegers().write(2, fadeOut); // ---> e
		try {
			pm.sendServerPacket(player, packet, false); // �������ݰ�
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		
	}
}
