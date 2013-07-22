package org.mingy.kernel.facade;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.mingy.kernel.bean.IEntity;
import org.mingy.kernel.bean.ILogicDeletable;

/**
 * 所有单表/对象操作的门面接口，供所有的外部操作单表/对象的客户端调用。 <br>
 * 提供相关的保存、删除和获取数据的操作。
 * 
 * @author Mingy
 * 
 */
public interface IEntityDaoFacade {

	/**
	 * 删除指定数据表中的所有数据
	 * 
	 * @param cls
	 *            指定数据表映射的实体类
	 */
	public <T extends IEntity> void deleteAll(Class<T> cls);

	/**
	 * 逻辑删除指定数据表中的所有数据，仅仅是将记录标记为无效。
	 * 
	 * @param cls
	 *            指定数据表映射的实体类
	 * @see ILogicDeletable
	 */
	public <T extends ILogicDeletable> void logicDeleteAll(Class<T> cls);

	/**
	 * 删除指定的对象记录
	 * 
	 * @param cls
	 *            指定数据表映射的实体类
	 * @param key
	 *            指定的关键字
	 */
	public <T extends IEntity> void delete(Class<T> cls, Serializable key);

	/**
	 * 逻辑删除指定的对象记录，仅仅是将记录标记为无效。<br>
	 * 该对象必须是实现了ILogicDeletable接口的对象。
	 * 
	 * @param cls
	 *            指定数据表映射的实体类
	 * @param key
	 *            指定的关键字
	 */
	public <T extends ILogicDeletable> void logicDelete(Class<T> cls,
			Serializable key);

	/**
	 * 删除指定的对象记录
	 * 
	 * @param object
	 *            将要被删除的对象
	 */
	public <T extends IEntity> void delete(T object);

	/**
	 * 逻辑删除指定的对象记录，仅仅是将记录标记为无效。<br>
	 * 该对象必须是实现了ILogicDeletable接口的对象。
	 * 
	 * @param object
	 *            将要被删除的对象
	 * @see ILogicDeletable
	 */
	public <T extends ILogicDeletable> void logicDelete(T object);

	/**
	 * 根据指定的关键字列表删除相关的对象记录
	 * 
	 * @param cls
	 *            映射表对应的类
	 * @param Serializable
	 *            [] 对象记录的关键字数组
	 */
	public <T extends IEntity> void delete(Class<T> cls, Serializable[] keys);

	/**
	 * 根据指定的关键字列表逻辑删除相关的对象记录
	 * 
	 * @param cls
	 *            映射表对应的类
	 * @param Serializable
	 *            [] 对象记录的关键字数组
	 */
	public <T extends ILogicDeletable> void logicDelete(Class<T> cls,
			Serializable[] keys);

	/**
	 * 通过一个原生的查询语句来删除数据
	 * 
	 * @param ql
	 *            原生的查询语句
	 * @param params
	 *            严格按照先后顺序排列的查询参数
	 * @param isNamedQuery
	 *            设置为true表示本次查询为命名查询，否则为一般查询。
	 * @return 删除的数据行数
	 */
	public long deleteByQuery(String ql, List<Object> params,
			boolean isNamedQuery);

	/**
	 * 通过一个原生的查询语句来删除数据
	 * 
	 * @param ql
	 *            原生的查询语句
	 * @param params
	 *            按照参数名称保存的查询参数
	 * @param isNamedQuery
	 *            设置为true表示本次查询为命名查询，否则为一般查询。
	 * @return 删除的数据行数
	 */
	public long deleteByQuery(String ql, Map<String, Object> params,
			boolean isNamedQuery);

	/**
	 * 通过一个原生的查询语句来删除数据
	 * 
	 * @param ql
	 *            原生的查询语句
	 * @param params
	 *            严格按照先后顺序排列的查询参数
	 * @param isNamedQuery
	 *            设置为true表示本次查询为命名查询，否则为一般查询。
	 * @return 删除的数据行数
	 */
	public long deleteByQuery(String ql, Object[] params, boolean isNamedQuery);

	/**
	 * 获取表中的所有对象。
	 * 
	 * @param cls
	 *            表达该对象的类定义，此类必须是SingleObject类的子类
	 * @return List 返回的对象列表，如果该表为空，则返回列表记录数为0，该List不为null。
	 */
	public <T extends IEntity> List<T> loadAll(Class<T> cls);

	/**
	 * 获取表中的所有对象。
	 * 
	 * @param cls
	 *            表达该对象的类定义，此类必须是SingleObject类的子类
	 * @param lazyFields
	 *            懒加载字段名的数组，即java的field名，支持以"."分隔的多级方式
	 * @return List 返回的对象列表，如果该表为空，则返回列表记录数为0，该List不为null。
	 */
	public <T extends IEntity> List<T> loadAll(Class<T> cls, String[] lazyFields);

	/**
	 * 获取表中的所有符合有效标记的对象。
	 * 
	 * @param cls
	 *            表达该对象的类定义，此类必须是SingleObject类的子类
	 * @param valid
	 *            有效标记，true表示获取有效的记录；否则获取无效的记录
	 * @return List 返回的对象列表，如果该表为空，则返回列表记录数为0，该List不为null。
	 */
	public <T extends ILogicDeletable> List<T> loadAll(Class<T> cls,
			boolean valid);

	/**
	 * 获取表中的所有符合有效标记的对象。
	 * 
	 * @param cls
	 *            表达该对象的类定义，此类必须是SingleObject类的子类
	 * @param valid
	 *            有效标记，true表示获取有效的记录；否则获取无效的记录
	 * @param lazyFields
	 *            懒加载字段名的数组，即java的field名，支持以"."分隔的多级方式
	 * @return List 返回的对象列表，如果该表为空，则返回列表记录数为0，该List不为null。
	 */
	public <T extends ILogicDeletable> List<T> loadAll(Class<T> cls,
			boolean valid, String[] lazyFields);

	/**
	 * 获取表中符合条件的所有对象。
	 * 
	 * @param cls
	 *            表达该对象的类定义，此类必须是SingleObject类的子类
	 * @param key
	 *            字段的值
	 * @param field
	 *            字段名，即java的field名
	 * @return List 返回的对象列表，如果该表为空，则返回列表记录数为0，该List不为null。
	 */
	public <T extends IEntity> List<T> loadAll(Class<T> cls, Object key,
			String field);

	/**
	 * 获取表中符合条件的所有对象。
	 * 
	 * @param cls
	 *            表达该对象的类定义，此类必须是SingleObject类的子类
	 * @param key
	 *            字段的值
	 * @param field
	 *            字段名，即java的field名
	 * @param lazyFields
	 *            懒加载字段名的数组，即java的field名，支持以"."分隔的多级方式
	 * @return List 返回的对象列表，如果该表为空，则返回列表记录数为0，该List不为null。
	 */
	public <T extends IEntity> List<T> loadAll(Class<T> cls, Object key,
			String field, String[] lazyFields);

	/**
	 * 获取表中符合条件的所有对象。
	 * 
	 * @param cls
	 *            表达该对象的类定义，此类必须是SingleObject类的子类
	 * @param keys
	 *            字段的值的数组
	 * @param fields
	 *            字段名的数组，即java的field名
	 * @return List 返回的对象列表，如果该表为空，则返回列表记录数为0，该List不为null。
	 */
	public <T extends IEntity> List<T> loadAll(Class<T> cls, Object[] keys,
			String[] fields);

	/**
	 * 获取表中符合条件的所有对象。
	 * 
	 * @param cls
	 *            表达该对象的类定义，此类必须是SingleObject类的子类
	 * @param keys
	 *            字段的值的数组
	 * @param fields
	 *            字段名的数组，即java的field名
	 * @param lazyFields
	 *            懒加载字段名的数组，即java的field名，支持以"."分隔的多级方式
	 * @return List 返回的对象列表，如果该表为空，则返回列表记录数为0，该List不为null。
	 */
	public <T extends IEntity> List<T> loadAll(Class<T> cls, Object[] keys,
			String[] fields, String[] lazyFields);

	/**
	 * 根据指定的关键字获取指定的对象
	 * 
	 * @param cls
	 *            表达该对象的类定义，此类必须是SingleObject类的子类
	 * @param key
	 *            指定的关键字
	 * @return SingleObject 返回的对象，如果不存在指定的关键字，则返回null。
	 */
	public <T extends IEntity> T load(Class<T> cls, Serializable key);

	/**
	 * 根据指定的关键字获取指定的对象
	 * 
	 * @param cls
	 *            表达该对象的类定义，此类必须是SingleObject类的子类
	 * @param key
	 *            指定的关键字
	 * @param lazyFields
	 *            需要使用到的lazy的属性
	 * @return SingleObject 返回的对象，如果不存在指定的关键字，则返回null。
	 */
	public <T extends IEntity> T load(Class<T> cls, Serializable key,
			String[] lazyFields);

	/**
	 * 根据给定的字段来加载实体。
	 * 
	 * @param cls
	 *            实体类
	 * @param key
	 *            字段的值
	 * @param field
	 *            字段名，即java的field名
	 * @return 持久化的实体，如果它含有懒加载属性的话， 只能在事务边界内正常使用<br>
	 *         如果字段不是唯一的，只能得到第一个符合条件的实体
	 */
	public <T extends IEntity> T load(Class<T> cls, Object key, String field);

	/**
	 * 根据给定的字段来加载实体。
	 * 
	 * @param cls
	 *            实体类
	 * @param key
	 *            字段的值
	 * @param field
	 *            字段名，即java的field名
	 * @param lazyFields
	 *            懒加载字段名的数组，即java的field名，支持以"."分隔的多级方式
	 * @return 持久化的实体，如果它含有懒加载属性的话， 只能在事务边界内正常使用<br>
	 *         如果字段不是唯一的，只能得到第一个符合条件的实体
	 */
	public <T extends IEntity> T load(Class<T> cls, Object key, String field,
			String[] lazyFields);

	/**
	 * 根据给定的字段来加载实体。
	 * 
	 * @param cls
	 *            实体类
	 * @param keys
	 *            字段的值的数组
	 * @param fields
	 *            字段名的数组，即java的field名
	 * @return 持久化的实体，如果它含有懒加载属性的话， 只能在事务边界内正常使用<br>
	 *         如果字段不是唯一的，只能得到第一个符合条件的实体
	 */
	public <T extends IEntity> T load(Class<T> cls, Object[] keys,
			String[] fields);

	/**
	 * 根据给定的字段来加载实体。
	 * 
	 * @param cls
	 *            实体类
	 * @param keys
	 *            字段的值的数组
	 * @param fields
	 *            字段名的数组，即java的field名
	 * @param lazyFields
	 *            懒加载字段名的数组，即java的field名，支持以"."分隔的多级方式
	 * @return 持久化的实体，如果它含有懒加载属性的话， 只能在事务边界内正常使用<br>
	 *         如果字段不是唯一的，只能得到第一个符合条件的实体
	 */
	public <T extends IEntity> T load(Class<T> cls, Object[] keys,
			String[] fields, String[] lazyFields);

	/**
	 * 通过一个原生的查询语句来获取数据
	 * 
	 * @param ql
	 *            原生的查询语句
	 * @param params
	 *            严格按照先后顺序排列的查询参数
	 * @param isNamedQuery
	 *            设置为true表示本次查询为命名查询，否则为一般查询。
	 * @return 符合条件的数据对象列表，数据对象内容取决于查询语句中的选择部分（SELECT）的内容，可能SingleObjectBean对象，
	 *         也可以是各种类型的对象数组。
	 */
	@SuppressWarnings("rawtypes")
	public List query(String ql, List<Object> params, boolean isNamedQuery);

	/**
	 * 通过一个原生的查询语句来获取数据
	 * 
	 * @param ql
	 *            原生的查询语句
	 * @param params
	 *            严格按照先后顺序排列的查询参数
	 * @param isNamedQuery
	 *            设置为true表示本次查询为命名查询，否则为一般查询。
	 * @param firstResult
	 *            分页查询的开始位置
	 * @param maxResults
	 *            分页查询的记录数
	 * @return 符合条件的数据对象列表，数据对象内容取决于查询语句中的选择部分（SELECT）的内容，可能SingleObjectBean对象，
	 *         也可以是各种类型的对象数组。
	 */
	@SuppressWarnings("rawtypes")
	public List query(String ql, List<Object> params, boolean isNamedQuery,
			int firstResult, int maxResults);

	/**
	 * 通过一个原生的查询语句来获取数据
	 * 
	 * @param ql
	 *            原生的查询语句
	 * @param params
	 *            按照参数名称保存的查询参数
	 * @param isNamedQuery
	 *            设置为true表示本次查询为命名查询，否则为一般查询。
	 * @return 符合条件的数据对象列表，数据对象内容取决于查询语句中的选择部分（SELECT）的内容，可能SingleObjectBean对象，
	 *         也可以是各种类型的对象数组。
	 */
	@SuppressWarnings("rawtypes")
	public List query(String ql, Map<String, Object> params,
			boolean isNamedQuery);

	/**
	 * 通过一个原生的查询语句来获取数据
	 * 
	 * @param ql
	 *            原生的查询语句
	 * @param params
	 *            按照参数名称保存的查询参数
	 * @param isNamedQuery
	 *            设置为true表示本次查询为命名查询，否则为一般查询。
	 * @param firstResult
	 *            分页查询的开始位置
	 * @param maxResults
	 *            分页查询的记录数
	 * @return 符合条件的数据对象列表，数据对象内容取决于查询语句中的选择部分（SELECT）的内容，可能SingleObjectBean对象，
	 *         也可以是各种类型的对象数组。
	 */
	@SuppressWarnings("rawtypes")
	public List query(String ql, Map<String, Object> params,
			boolean isNamedQuery, int firstResult, int maxResults);

	/**
	 * 通过一个原生的查询语句来获取数据
	 * 
	 * @param ql
	 *            原生的查询语句
	 * @param params
	 *            严格按照先后顺序排列的查询参数
	 * @param isNamedQuery
	 *            设置为true表示本次查询为命名查询，否则为一般查询。
	 * @return 符合条件的数据对象列表，数据对象内容取决于查询语句中的选择部分（SELECT）的内容，可能SingleObjectBean对象，
	 *         也可以是各种类型的对象数组。
	 */
	@SuppressWarnings("rawtypes")
	public List query(String ql, Object[] params, boolean isNamedQuery);

	/**
	 * 通过一个原生的查询语句来获取数据
	 * 
	 * @param ql
	 *            原生的查询语句
	 * @param params
	 *            严格按照先后顺序排列的查询参数
	 * @param isNamedQuery
	 *            设置为true表示本次查询为命名查询，否则为一般查询。
	 * @param firstResult
	 *            分页查询的开始位置
	 * @param maxResults
	 *            分页查询的记录数
	 * @return 符合条件的数据对象列表
	 */
	@SuppressWarnings("rawtypes")
	public List query(String ql, Object[] params, boolean isNamedQuery,
			int firstResult, int maxResults);

	/**
	 * 通过指定的select语句进行数据查询，，该语句可以是命名查询，也可以是非命名查询；可是JPQL， 也可以是Native SQL。
	 * 
	 * @param ql
	 *            指定的语句，如果是命名查询，则为命名查询的ID。
	 * @param params
	 *            指定语句的附加参数，可以是：Object[], List<String>, Map<String, Object>。
	 * @param isNamedQuery
	 *            如果设置为true，表示为命名查询，否则为非命名查询。
	 * @param isNativeQuery
	 *            如果设置为true，表示为Native SQL语句，否则为JPQL语句。
	 * @param firstResult
	 *            数据查询的起始位置，如果设置为小于0的数值，则被忽略。
	 * @param maxResults
	 *            数据查询的最大记录行数，如果设置为小于0的数值，则被忽略。
	 * @return 返回查询的记录结果列表
	 */
	@SuppressWarnings("rawtypes")
	public List query(final String ql, final Object params,
			final boolean isNamedQuery, final boolean isNativeQuery,
			final int firstResult, final int maxResults);

	/**
	 * 保存指定的对象
	 * 
	 * @param object
	 *            SingleObject 将要保存的对象
	 * @throws Throwable
	 */
	public <T extends IEntity> void save(T object);

	/**
	 * 根据一个原生的查询语句来执行一个保存操作，比如update操作等。
	 * 
	 * @param ql
	 *            原生的查询语句。
	 * @param params
	 *            严格按照先后顺序排列的查询参数。
	 * @param isNamedQuery
	 *            设置为true表示本次查询为命名查询，否则为一般查询。
	 */
	public void updateByQuery(String ql, List<Object> params,
			boolean isNamedQuery);

	/**
	 * 根据一个原生的查询语句来执行一个保存操作，比如update操作等。
	 * 
	 * @param ql
	 *            原生的查询语句。
	 * @param params
	 *            按照参数名称保存的查询参数。
	 * @param isNamedQuery
	 *            设置为true表示本次查询为命名查询，否则为一般查询。
	 */
	public void updateByQuery(String ql, Map<String, Object> params,
			boolean isNamedQuery);

	/**
	 * 根据一个原生的查询语句来执行一个保存操作，比如update操作等。
	 * 
	 * @param ql
	 *            原生的查询语句。
	 * @param params
	 *            严格按照先后顺序排列的查询参数。
	 * @param isNamedQuery
	 *            设置为true表示本次查询为命名查询，否则为一般查询。
	 */
	public void updateByQuery(String ql, Object[] params, boolean isNamedQuery);

	/**
	 * 通过指定的update或者delete语句进行数据操作（数据更新或者数据删除），该语句可以是命名查询，也可以是非命名查询；可是JPQL，
	 * 也可以是Native SQL。
	 * 
	 * @param ql
	 *            指定的语句，如果是命名查询，则为命名查询的ID。
	 * @param params
	 *            指定语句的附加参数，可以是：Object[], List<String>, Map<String, Object>。
	 * @param isNamedQuery
	 *            如果设置为true，表示为命名查询，否则为非命名查询。
	 * @param isNativeQuery
	 *            如果设置为true，表示为Native SQL语句，否则为JPQL语句。
	 * @return 成功执行后影响的数据行数
	 */
	public long updateByQuery(final String ql, final Object params,
			final boolean isNamedQuery, final boolean isNativeQuery);
}
