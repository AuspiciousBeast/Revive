package me.xiaox.revive.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import me.xiaox.revive.Revive;
import me.xiaox.revive.enums.ReviveType;

public class FileUtil {
	
	/**
	 * ��ȡĳ��������Ƿ�����
	 * @param name �������
	 * @return ���ڷ���true���򷵻�false
	 */
	public static boolean isEnableRevive(String name) {
		FileConfiguration fc = Revive.getReviveFile();
		return fc.get(name + ".enable") == null || fc.getBoolean(name + ".enable");
	}
	
	/**
	 * ����ĳ�������
	 * @param name �������
	 */
	public static void enableRevive(String name) {
		FileConfiguration fc = Revive.getReviveFile();
		fc.set(name + ".enable", true);
		Revive.saveRevive(fc);
	}
	
	/**
	 * ����ĳ�������
	 * @param name �������
	 */
	public static void disableRevive(String name) {
		FileConfiguration fc = Revive.getReviveFile();
		fc.set(name + ".enable", false);
		Revive.saveRevive(fc);
	}
	
	/**
	 * ���һ�������
	 * @param type
	 * @param loc
	 * @param key
	 * @param group
	 */
	public static void addRevive(ReviveType type, Location loc, String name, String gr) {
		FileConfiguration fc = Revive.getReviveFile();
		String world = loc.getWorld().getName();
		double x = loc.getX();
		double y = loc.getY();
		double z = loc.getZ();
		float pitch = loc.getPitch();
		float yaw = loc.getYaw();
		fc.set(name + ".enable", true);
		fc.set(name + ".world", world);
		fc.set(name + ".x", x);
		fc.set(name + ".y", y);
		fc.set(name + ".z", z);
		fc.set(name + ".pitch", pitch);
		fc.set(name + ".yaw", yaw);
		fc.set(name + ".type", type.toString());
		if(type == ReviveType.GROUP) {
			fc.set(name + ".group", gr);
		}else if(type == ReviveType.RADIUS) {
			fc.set(name + ".radius", gr);
		}
		Revive.saveRevive(fc);
	}
	
	/**
	 * ��ָ����������һ��Title
	 * @param name �������
	 * @param title Ҫ��ӵ�title����
	 * @param subtitle Ҫ��Ӹ�����
	 */
	public static void addTitle(String name, String title, String subtitle) {
		FileConfiguration fc = Revive.getReviveFile();
		fc.set(name + ".title.title", title);
		fc.set(name + ".title.subtitle", subtitle);
		Revive.saveRevive(fc);
	}
	public static String getTitleName(String name) {
		FileConfiguration fc = Revive.getReviveFile();
		return fc.getString(name + ".title.title");
	}
	public static String getSubTitle(String name) {
		FileConfiguration fc = Revive.getReviveFile();
		return fc.getString(name + ".title.subtitle");
	}
	
	/**
	 * �ж�һ��������Ƿ����title
	 * @param name �������
	 * @return �����򷵻�true���򷵻�false
	 */
	public static boolean hasTitle(String name) {
		FileConfiguration fc = Revive.getReviveFile();
		return fc.get(name + ".title") != null ? true:false;
	}
	
	/**
	 * �༭ָ��������title
	 * @param name �������
	 * @param title Ҫ�༭��title����
	 * @param subtitle Ҫ�༭�ĸ�����
	 */
	public static void editTitle(String name, String title, String subtitle) {
		FileConfiguration fc = Revive.getReviveFile();
		fc.set(name + ".title.title", title);
		fc.set(name + ".title.subtitle", subtitle);
		Revive.saveRevive(fc);
	}
	
	/**
	 * ɾ��ָ��������Title
	 * @param name �������
	 */
	public static void delTitle(String name) {
		FileConfiguration fc = Revive.getReviveFile();
		fc.set(name + ".title", null);
		Revive.saveRevive(fc);
	}
	
	/**
	 * ɾ�������
	 * @param name �������
	 */
	public static void delRevive(String name) {
		FileConfiguration fc = Revive.getReviveFile();
		fc.set(name, null);
		Revive.saveRevive(fc);
	}
	
	/**
	 * ��yml��ȡLocation
	 * @param key
	 * @return
	 */
	public static Location getLocation(String key) {
		FileConfiguration fc = Revive.getReviveFile();
		String world = fc.getString(key + ".world");
		double x = fc.getDouble(key + ".x");
		double y = fc.getDouble(key + ".y");
		double z = fc.getDouble(key + ".z");
		float pitch = (float) fc.getDouble(key + ".pitch");
		float yaw = (float) fc.getDouble(key + ".yaw");
		return new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
	}
	
	/**
	 * ��ȡ���������
	 * @param name �������
	 * @return ���������ö��
	 */
	public static ReviveType getType(String name) {
		FileConfiguration fc = Revive.getReviveFile();
		return ReviveType.valueOf(fc.getString(name + ".type"));
	}
	
	/**
	 * ��ȡ�����ĸ����
	 * @param player
	 * @param worldName
	 * @return
	 */
	public static List<String> getSortReviveName(Player player) {
		List<String> list = getReviveNameInWorld(player.getWorld().getName());
		List<Double> distances = new ArrayList<>();
		Map<Double, String> map = new HashMap<>();
		List<String> sortlist = new ArrayList<>();
		for(String name : list) {
			double distance = player.getLocation().distance(getLocation(name));
			distances.add(distance);
			map.put(distance, name);
		}
		
		Collections.sort(distances);
		
		for(double distance : distances) {
			sortlist.add(map.get(distance));
		}
		
		map.clear();
		return sortlist;
	}
	
	/**
	 * ��ȡĳ������ĸ����
	 * @param worldName ������
	 * @return �����list
	 */
	public static List<String> getReviveNameInWorld(String worldName) {
		List<String> list = new ArrayList<String>();
		FileConfiguration config = Revive.getReviveFile();
		//��������
		for(String name : config.getKeys(false)) {
			//�����ָ������
			if(config.getString(name + ".world") != null && config.getString(name + ".world").equalsIgnoreCase(worldName)) {
				list.add(name);
			}
		}
		return list;
	}
	
	/**
	 * ��ȡ����������
	 * @param worldName ������
	 * @return
	 */
	/*
	public static <T> Map<ReviveType, List<String>> getReviveNameList(String worldName) {
		Map<ReviveType, List<String>> map = new HashMap<ReviveType, List<String>>();
		List<String> de = new ArrayList<String>();
		List<String> gl = new ArrayList<String>();
		List<String> gr = new ArrayList<String>();
		List<String> ra = new ArrayList<String>();
		List<String> wo = new ArrayList<String>();
		FileConfiguration config =Revive.getReviveFile();
		if(worldName == null || worldName.equalsIgnoreCase("") || worldName.equalsIgnoreCase("all")) {
			for(String name : Revive.getReviveFile().getKeys(false)) {
				ReviveType type = getType(name);
				switch (type) {
				case DEFAULT:
					de.add(name);
					break;
				case GLOBAL:
					gl.add(name);
					break;
				case GROUP:
					gr.add(name);
					break;
				case RADIUS:
					ra.add(name);
					break;
				case WORLD:
					wo.add(name);
					break;
				default:
					break;
				}
			}
			map.put(ReviveType.DEFAULT, de);
			map.put(ReviveType.GLOBAL, gl);
			map.put(ReviveType.GROUP, gr);
			map.put(ReviveType.RADIUS, ra);
			map.put(ReviveType.WORLD, wo);
			return map;
		}
		for(String name : Revive.getConfigFile().getKeys(false)) {
			if(config.getString(name + ".world") != null && config.getString(name + ".world").equalsIgnoreCase(worldName)) {
				ReviveType type = getType(name);
				switch (type) {
				case DEFAULT:
					de.add(name);
					break;
				case GLOBAL:
					gl.add(name);
					break;
				case GROUP:
					gr.add(name);
					break;
				case RADIUS:
					ra.add(name);
					break;
				case WORLD:
					wo.add(name);
					break;
				default:
					break;
				}
			}
		}
		map.put(ReviveType.DEFAULT, de);
		map.put(ReviveType.GLOBAL, gl);
		map.put(ReviveType.GROUP, gr);
		map.put(ReviveType.RADIUS, ra);
		map.put(ReviveType.WORLD, wo);
		return map;
	}
	*/
	
	/**
	 * ��ȡ������б� worldnameΪnull��all�򷵻�ȫ�����򷵻�Ŀ������
	 * @param worldname
	 * @return ���ظ����List�б�
	 */
	public static List<String> getReviveList(String worldName) {
		List<String> list = new ArrayList<String>();
		FileConfiguration config = Revive.getReviveFile();
		if(worldName == null || worldName.equalsIgnoreCase("") || worldName.equalsIgnoreCase("all")) {
			for(String name : Revive.getReviveFile().getKeys(false)) {
				ReviveType type = getType(name);
				switch (type) {
				case DEFAULT:
					list.add("��2" + name + "(" + config.getString(name + ".world") 
					+ " " + config.getDouble(name + ".x") 
					+ " " + config.getDouble(name + ".y") 
					+ " " + config.getDouble(name + ".z") + ")");
					break;
				case GLOBAL:
					list.add("��c��l" + name + "(" + config.getString(name + ".world") 
					+ " " + config.getDouble(name + ".x") 
					+ " " + config.getDouble(name + ".y") 
					+ " " + config.getDouble(name + ".z") + ")");
					break;
				case GROUP:
					list.add("��b" + name + "(" + config.getString(name + ".world") 
					+ " " + config.getDouble(name + ".x") 
					+ " " + config.getDouble(name + ".y") 
					+ " " + config.getDouble(name + ".z") + ") - " 
					+ config.getDouble(name + ".group"));
					break;
				case RADIUS:
					list.add("��5" + name + "(" + config.getString(name + ".world") 
					+ " " + config.getDouble(name + ".x") 
					+ " " + config.getDouble(name + ".y") 
					+ " " + config.getDouble(name + ".z") + ") - �뾶:" 
					+ config.getDouble(name + ".radius"));
					break;
				case WORLD:
					list.add("��c" + name + "(" + config.getString(name + ".world") 
					+ " " + config.getDouble(name + ".x") 
					+ " " + config.getDouble(name + ".y") 
					+ " " + config.getDouble(name + ".z") + ")");
					break;
				default:
					break;
				}
			}
			return list;
		}
		for(String name : Revive.getReviveFile().getKeys(false)) {
			if(config.getString(name + ".world") != null && config.getString(name + ".world").equalsIgnoreCase(worldName)) {
				ReviveType type = getType(name);
				switch (type) {
				case DEFAULT:
					list.add("��2" + name + "(" + config.getString(name + ".world") 
					+ " " + config.getDouble(name + ".x") 
					+ " " + config.getDouble(name + ".y") 
					+ " " + config.getDouble(name + ".z") + ")");
					break;
				case GLOBAL:
					list.add("��c��l" + name + "(" + config.getString(name + ".world") 
					+ " " + config.getDouble(name + ".x") 
					+ " " + config.getDouble(name + ".y") 
					+ " " + config.getDouble(name + ".z") + ")");
					break;
				case GROUP:
					list.add("��b" + name + "(" + config.getString(name + ".world") 
					+ " " + config.getDouble(name + ".x") 
					+ " " + config.getDouble(name + ".y") 
					+ " " + config.getDouble(name + ".z") + ") - " 
					+ config.getDouble(name + ".group"));
					break;
				case RADIUS:
					list.add("��5" + name + "(" + config.getString(name + ".world") 
					+ " " + config.getDouble(name + ".x") 
					+ " " + config.getDouble(name + ".y") 
					+ " " + config.getDouble(name + ".z") + ") - �뾶:" 
					+ config.getDouble(name + ".radius"));
					break;
				case WORLD:
					list.add("��c" + name + "(" + config.getString(name + ".world") 
					+ " " + config.getDouble(name + ".x") 
					+ " " + config.getDouble(name + ".y") 
					+ " " + config.getDouble(name + ".z") + ")");
					break;
				default:
					break;
				}
			}
		}
		return list;
	}
	
	/**
	 * �ж��Ƿ����
	 * @param key
	 * @return ���ڷ���true���򷵻�false
	 */
	public static boolean isExists(String key) {
		FileConfiguration fc = Revive.getReviveFile();
		return fc.get(key) != null;
	}
	
	/**
	 * ����һ��File
	 * @param file
	 * @return ����һ��config
	 */
	public static FileConfiguration load(File file){
		if (!(file.exists())){
			try{
				file.createNewFile();
				}
			catch(IOException   e){
				e.printStackTrace();
				}
			}
		return YamlConfiguration.loadConfiguration(file);
	}
}
