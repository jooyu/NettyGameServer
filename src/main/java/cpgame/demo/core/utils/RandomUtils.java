package cpgame.demo.core.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

/**
 * 随机工具类
 * @author 0x737263
 *
 */
public class RandomUtils {
	private static Random random = new Random();

	public static final int THOUSAND = 1000;
	public static final int HUNDRED = 100;

	/**
	 * 随机随机范围的索引
	 * @param size	大小
	 * @return
	 */
	public static int nextIntIndex(int size) {
		return nextInt(0, size - 1);
	}

	/**
	 * 随机范围值 minValue到 maxValue的闭区间
	 * @param minValue
	 * @param maxValue
	 * @return
	 */
	public static int nextInt(int minValue, int maxValue) {
		if (minValue == maxValue) {
			return minValue;
		}
		// if (maxValue < minValue) {
		// maxValue = minValue;
		// }
		// return (int) Math.floor((Math.random() * (maxValue - minValue + 1) +
		// minValue));

		if (maxValue - minValue <= 0)
			return minValue;
		return minValue + random.nextInt(maxValue - minValue + 1);
	}

	/**
	 * 随机一个高斯值(正态值)，接近于平均值的范围概率越高
	 * @param sd 标准差
	 * @param mean 平均值
	 * @return
	 */
	public static int nextGaussianInt(int sd, int mean) {
		return (int) ((double) sd * random.nextGaussian() + (double) mean);
	}

	/**
	 * 是否命中
	 * @param rate		概率
	 * @param maxValue	最大值
	 * @return
	 */
	public static boolean isHit(int rate, int maxValue) {
		if (rate < 1) {
			return false;
		}

		if (rate == maxValue) {
			return true;
		}

		return nextInt(1, maxValue) <= rate;
	}

	/**
	 * 0 - 100数值范围内(包含100)是否命中
	 * @param rate
	 * @return
	 */
	public static boolean is100Hit(int rate) {
		if (rate <= 0) {
			return false;
		}
		if (rate >= 100) {
			return true;
		}
		return isHit(rate, 100);
	}

	/**
	 * 0 - 1000数值范围内(包含100)是否命中
	 * @param rate
	 * @return
	 */
	public static boolean is1000Hit(int rate) {
		if (rate <= 0) {
			return false;
		}
		if (rate >= 1000) {
			return true;
		}
		return isHit(rate, 1000);
	}

	/**
	 * 随机一个范围内[minValue,maxValue]不重复的数字
	 * @param num 数字个数
	 * @param minValue 最小值
	 * @param maxValue 最大值
	 * @return 随机数组
	 */
	public static int[] uniqueRandom(int num, int minValue, int maxValue) {
		int length = maxValue - minValue + 1;
		int[] seed = new int[length];
		for (int i = 0; i < length; i++) {
			seed[i] = minValue + i;
		}

		num = Math.min(num, length);

		int[] ranArr = new int[num];
		for (int i = 0; i < num; i++) {
			int j = nextIntIndex(length - i);
			ranArr[i] = seed[j];
			seed[j] = seed[length - i - 1];
		}
		return ranArr;
	}

	/**
	 * 根据概率配置,返回随机命中的ID
	 * 比如:
	 * ID1_500|ID2_500
	 * 每个id都有50%概率出现,通过randomHit返回随机命中的ID
	 * @param base  概率的最大值
	 * @param map	ID和对应的出现概率
	 * @return
	 */
	public static <ID> ID randomHit(int base, Map<ID, Integer> map) {
		int rate = nextInt(1, base);
		int total = 0;
		for (Entry<ID, Integer> entry : map.entrySet()) {
			total += entry.getValue();
			if (total >= rate) {
				return entry.getKey();
			}
		}
		return null;
	}

	public static <ID> ID randomHit(Collection<ID> collection) {
		if (collection == null || collection.size() < 1) {
			return null;
		}

		List<ID> list = new ArrayList<>(collection.size());
		list.addAll(collection);
		int index = nextIntIndex(list.size());
		return list.get(index);
	}
	
	/**
	 * 随机不重复的指定数量的元素
	 * @param srcList
	 * @param randomNum
	 * @return 当给定源列表中元素的数量小于需要随机的数量时，返回列表中会出现重复的元素
	 */
	public static <ID> List<ID> randomNotRepetitionList(List<ID> srcList, int randomNum) {
		if (randomNum > srcList.size()) {
			randomNum = srcList.size();
		}
		List<ID> tempList = new ArrayList<>(srcList);
		List<ID> list = new ArrayList<>();
		int startIndex = 0;
		for (int i = 0; i < randomNum; i++) {
			if (i > tempList.size() - 1) {
				startIndex = 0;
			}
			int randomIndex = nextInt(startIndex, tempList.size() - 1);
			ID value = tempList.get(randomIndex);
			list.add(value);
			// 将已随到的移动到列表前面，并移动随机开始位置标记
			tempList.set(randomIndex, tempList.get(startIndex));
			tempList.set(startIndex, value);
			startIndex++;
		}
		return list;
	}
	
	/**
	 * 根据权重随机
	 * @param attributeMap
	 * @return
	 */
	public static <ID> ID randomByWeight(Map<ID, Integer> attributeMap) {
		int totalWeight = 0;
		for (Integer weight : attributeMap.values()) {
			totalWeight += weight;
		}
		return randomHit(totalWeight, attributeMap);
	}
}