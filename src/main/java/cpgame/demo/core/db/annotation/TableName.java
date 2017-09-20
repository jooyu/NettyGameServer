package cpgame.demo.core.db.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * db表名标注
 * @author 0x737263
 *
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface TableName {

	/**
	 * 表名
	 * @return
	 */
	String name();

	/**
	 * 主键是否由idbuilder表来管理
	 * @return
	 */
	boolean isIdBuilder() default true;
	
	/**
	 * id自增初始值
	 * @return
	 */
	long startId() default 1;
}
