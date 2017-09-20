package cpgame.demo.core.db.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * db字段标注,被加的字段必需为public
 * @author 0x737263
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface Column {

	/**
	 * 是否为主键。默认为false
	 * @return
	 */
	public boolean pk() default false;

	/**
	 * 字段别名(注意字段顺序),  暂不支持映射多个字段-_-|  .eg..{"column1","column2"}
	 * @return
	 */
	public String alias() default "";

}
