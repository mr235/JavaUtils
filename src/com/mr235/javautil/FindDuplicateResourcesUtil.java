package com.mr235.javautil;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/7/6.
 */
public class FindDuplicateResourcesUtil {
	public static void find(String inputResDir, String outputDir) {
		File inputRes = new File(inputResDir);
		if (!(inputRes.exists() && inputRes.isDirectory())) {
			return;
		}
		File[] drawableDirs = inputRes.listFiles(new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				return pathname.getName().toLowerCase().startsWith("drawable");
			}
		});
		for (File drawableDir : drawableDirs) {
			Map<String, List<String>> duplicateMap = find(drawableDir);
			if (duplicateMap.size()>0) {
				handleDuplicateFile(drawableDir, duplicateMap);
			}
		}
	}

	private static void handleDuplicateFile(File drawableDir, Map<String, List<String>> duplicateMap) {
		System.out.println();
		System.out.println(drawableDir.getName());
		System.out.println();
		for (String md5 : duplicateMap.keySet()) {
			for (String fileName : duplicateMap.get(md5)) {
				System.out.println(fileName);
			}
		}

	}

	private static Map<String, List<String>> find(File drawableDir) {
		Map<String, List<String>> temp = new HashMap<>();
		File[] files = drawableDir.listFiles(new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				return pathname.isFile();
			}
		});
		for (File file : files) {
			try {
				String md5 =  Utils.getMD5(file);
				if (md5!=null) {
					List<String> list = temp.get(md5);
					if (list==null) {
						list = new ArrayList<>();
					}
					list.add(file.getName());
					temp.put(md5, list);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		Map<String, List<String>> result = new HashMap<>();
		for (String md5 : temp.keySet()) {
			List<String> list = temp.get(md5);
			if (list.size()>1) {
				result.put(md5, list);
			}
		}

		return result;
	}

}
