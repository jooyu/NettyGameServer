package cpgame.demo.core.utils;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;
import org.springframework.util.SystemPropertyUtils;

/**
 * 序列化对象转换类
 * 
 * @author 0x737263
 * 
 */
public class PackageScanner {
	private static final Logger LOGGER = LoggerFactory.getLogger(PackageScanner.class);

	private static final ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
	private static final MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(resourcePatternResolver);
	private static final String DEFAULT_RESOURCE_PATTERN = "**/**.class";

	@SuppressWarnings("unchecked")
	public static <T> Collection<Class<T>> scanEntityClazz(Class<T> clazz, String packageSplit) {
		if (clazz.isInterface()) {
			LOGGER.error("not support interface type scan. class:{}", clazz);
			return Collections.emptyList();
		}
		
		String[] packageList = packageSplit.split(",");	

		Collection<Class<T>> clazzCollection = new HashSet<Class<T>>();
		try {

			for (String path : packageList) {
				String packageSearchPath = "classpath*:" + resolveBasePackage(path) + "/" + DEFAULT_RESOURCE_PATTERN;

				Resource[] resources = resourcePatternResolver.getResources(packageSearchPath);
				for (Resource resource : resources) {
					String className = "";
					try {
						if (resource.isReadable()) {
							MetadataReader metaReader = metadataReaderFactory.getMetadataReader(resource);
							if (metaReader.getClassMetadata().isInterface()) {
								continue;
							}
							className = metaReader.getClassMetadata().getClassName();

							if (metaReader.getClassMetadata().isInterface()) {
								continue;
							}

							if (metaReader.getClassMetadata().isAbstract()) {
								continue;
							}

							Class<T> newClazz = (Class<T>) Class.forName(className);

							if (clazz.isAssignableFrom(newClazz)) {
								clazzCollection.add(newClazz);
							}
						}
					} catch (Exception e) {
						System.out.println(className);
						LOGGER.error("package scanner {} error!", e);
					}
				}
			}
		} catch (IOException e) {
			LOGGER.error("package scanner {} error!", clazz);
		}

		return clazzCollection;
	}

	/**
	 * 
	 * @param packages
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> Collection<Class<T>> scanPackages(String packages) {
		Collection<Class<T>> clazzCollection = new HashSet<Class<T>>();

		try {
			String packageSearchPath = "classpath*:" + DEFAULT_RESOURCE_PATTERN;

			if (StringUtils.isNotBlank(packages)) {
				packageSearchPath = "classpath*:" + resolveBasePackage(packages) + "/" + DEFAULT_RESOURCE_PATTERN;
			}

			Resource[] resources = resourcePatternResolver.getResources(packageSearchPath);
			for (Resource resource : resources) {
				String className = "";
				try {
					if (resource.isReadable()) {
						MetadataReader metaReader = metadataReaderFactory.getMetadataReader(resource);
						className = metaReader.getClassMetadata().getClassName();

						Class<T> clazz = (Class<T>) Class.forName(className);
						clazzCollection.add(clazz);
					}
				} catch (ClassNotFoundException e) {
					LOGGER.error("class {} not exists!", className);
				}
			}
		} catch (IOException e) {
			LOGGER.error("package scanner {} error!", packages);
		}

		return clazzCollection;
	}

	/**
	 * 
	 * @param basePackage
	 * @return
	 */
	private static String resolveBasePackage(String basePackage) {
		String placeHolderReplace = SystemPropertyUtils.resolvePlaceholders(basePackage);
		return ClassUtils.convertClassNameToResourcePath(placeHolderReplace);
	}
}