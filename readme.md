# redis

## 基础命令及数据结构

Redis:通用命令：

- 通用指令是部分数据类型的，都可以使用的指令，常见的有：
- KEYS:查看符合模板的所有key,**不建议在生产环境设备上使用**
- DEL:删除一个指定的key
- EXISTS:判断key是否存在
- EXPIRE：给一个key设置有效期，有效期到期时该key会被自动删除
- TTL:查看一个KEY的剩余有效期
- 通过help[commandl可以查看一个命令的具体用法

#### String类型

String类型，也就是字符串类型，是Redis中最简单的存储类型。
其value是字符串，不过根据字符串的格式不同，又可以分为3类：
●string:普通字符串
●int:整数类型，可以做自增、自减操作
●float:浮点类型，可以做自增、自减操作
不管是哪种格式，底层都是字节数组形式存储，只不过是编码方式不同。字符串类型的最大空间不能超过512m.
KEY   	VALUE
msg  	hello world
num		10
score             92.5

**String类型的常见命令**

- SET:添加或者修改已经存在的一个String类型的键值对
- GET:根据key获取String类型的value
- MSET:批量添加多个String类型的键值对
- MGET:根据多个key获取多个String类型的value
- INCR:让一个整型的key自增1
- INCRBY:让一个整型的key自增并指定步长，例如：incrby num2让num值自增2
- INCRBYFLOAT:让一个浮点类型的数字自增并指定步长
- **SETNX:添加一个String类型的键值对，前提是这个key不存在，否则不执行**(可用作分布式锁)
- **SETEX:添加一个String类型的键值对，并且指定有效期**

Redis没有类似MySQL中的Table的概念，我们该如何区分不同类型的key呢？
例如，
需要存储用户、商品信息到redis,有一个用户
id是1，有一个商品id恰好也是1

**redis的方案**

key的结构
Redis的key允许有多个单词形成层级结构，多个单词之间用'：'隔开，格式如下：
项目名：业务名：类型：id
这个格式并非固定，也可以根据自己的需求来删除或添加词条。
例如我们的项目名称叫heima,有user和producti两种不同类型的数据，我们可以这样定义key:
◆user相关的key:
heima:user:1
product相关的key:
heima:product:1

如果Value是一个ava对象，例如一个User对象，则可以将对象序列化为SON字符串后存储：
KEY						VALUE
heima:user:1		{"name":"Jack","id":1,"age":21}
heima:product:1	["id":1,"name":"小米11"，"price":4999}

#### Hash类型

Hash类型，也叫散列，其value是一个无序字典，类似于引ava中的HashMap结构。
String结构是将对象序列化为S0N字符串后存储，当需要修改对象某个字段时很不方便：
KEY
VALUE
heima:user:1
{name:"Jack",age:21]
heima:user:2
{"name":"Rose","age":18}
Hash结构可以将对象中的每个字段独立存储，可以针对单个字段做CRUD:
VALUE
KEY			  field 	value

heima:user:1	name	 Jack

Hash类型的常见命令
Hash的常见命令有：

- HSET key field value:添加或者修改hash类型key的field的值
- HGET key field:获取一个hash类型key的field的值
- HMSET:批量添加多个hash类型key的field的值
- HMGET:批量获取多个hash类型key的field的值
- HGETALL:获取一个hash类型的key中的所有的field和value
- HKEYS:获取一个hash类型的key中的所有的field
- HVALS:获取一个hash类型的key中的所有的value
- HINCRBY:让一个hash类型key的字段值自增并指定步长
- HSETNX:添加一个hash类型的key的field值，前提是这个field.不存在，否则不执行

#### List类型

Redis中的List类型与java中的LinkedList类似，可以看做是一个双向链表结构。既可以支持正向检索和也可以支持反向
检索。
特征也与LinkedList类似：

- 有序
- 元素可以重复
- 插入和删除快
- 查询速度一般

常用来存储一个有序数据，例如：朋友圈点赞列表，评论列表等。

- List类型的常见命令
  List的常见命令有：
- PUSH key element..:向列表左侧插入一个或多个元素
- LPOP key:移除并返回列表左侧的第一个元素，没有则返回nil
- RPUSH key element..:向列表右侧插入一个或多个元素
- RPOP key:移除并返回列表右侧的第一个元素
- LRANGE key star end:返回一段角标范围内的所有元素
- BLPOP和BRPOP:与LPOP和RPOP类似，只不过在没有元素时等待指定时间，而不是直接返回nil

如何利用List结构模拟一个栈？
·入口和出口在同一边：LPUSH进LPOP出
如何利用Lst结构模拟一个队列？
·入口和出口在不同边：LPUSH进RPOP出
如何利用Lst结构模拟一个阻塞队列？
·入口和出口在不同边
出队时采用BLPOP或BRPOP

#### Set类型

Redis的Set结构与Java中的HashSet类似，可以看做是一个value为nul1的HashMap。因为也是一个hash表，因此具备与
HashSet类似的特征：

- 无序
- 元素不可重复
- 查找快
- 支持交集、并集、差集等功能

Set类型的常见命令

SADD key member.,:向set中添加一个或多个元素

SREM key member..:移除set中的指定元素

SCARD key:返回set中元素的个数

SISMEMBER key member:判断一个元素是否存在于set中

SMEMBERS:获取set中的所有元素

SINTER key1key2...:求keyl与key2的交集

SDIFF key1key2..:求keyl与key2的差集

SUNION key1 key2..:求key1和key2的并集

#### SortedSet类型

Redis的SortedSet:是一个可排序的set集合，与Java中的TreeSet有些类似，但底层数据结构却差别很大。SortedSet中
的每一个元素都带有一个score属性，可以基于scorel属性对元素排序，底层的实现是一个跳表(SkipList)加hash表
SortedSet具备下列特性：
可排序
元素不重复
查询速度快
因为SortedSet的可排序特性，经常被用来实现排行榜这样的功能。

SortedSet类型的常见命令

- SortedSet的常见命令有：
- ZADD key score member:添加一个或多个元素到sorted set,如果已经存在则更新其score值
- ZREM key member:册别除sorted set中的一个指定元素
- ZSC0 RE key member:获取sorted set中的指定元素的score值
- ZRANK key member:获取sorted set中的指定元素的排名
- ZCARD key:获取sorted set中的元素个数
- ZCOUNT key min max:统计score值在给定范围内的所有元素的个数
- ZINCRBY key increment member:让sorted set中的指定元素自增，步长为指定的increment值
- ZRANGE key min max:按照score排序后，获取指定排名范围内的元素
- ZRANGEBYSCORE key min max:按照score?排序后，获取指定score范围内的元素
- ZDIFF、ZINTER、ZUNION:求差集、交集、并集
- 注意：所有的排名默认都是升序，如果要降序则在命令的Z后面添加EV即可

## Jedis与springdataredis

### springdataredis的使用

1.引入依赖

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
    <version>2.7.12</version>
</dependency>
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <optional>true</optional>
</dependency>
<dependency>
    <groupId>org.apache.commons</groupId>
    <artifactId>commons-pool2</artifactId>
    <version>2.11.1</version>
</dependency>
2.配置连接application.yaml

spring:
data:
redis:
host: 192.168.32.131
port: 6379
lettuce:
pool:
max-active: 8
max-idle: 8
min-idle: 0
max-wait: 100ms
3.测试连接及数据的出入

@Autowired
private RedisTemplate redisTemplate;
@Test
void testString() {
//1.存入数据
redisTemplate.opsForValue().set("name","guojunzhang");
//2.取出数据
Object name = redisTemplate.opsForValue().get("name");
System.out.println("name = " + name);

}
以上面这种方式来存的话，会有一定的问题，这种方式存入会把key与value序列化，我们存入的key，value并不是真正的。

解决方案：自定义springdataredis的序列化方式

@Configuration
public class RedisConfig {

@Bean

public RedisTemplate<String,Object> redisTemplate(
RedisConnectionFactory redisConnectionFactory){
//1.创建redisTemplate对象
RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
//2.设置连接工厂
template.setConnectionFactory(redisConnectionFactory);
//3.创建JSPN序列化工具
GenericJackson2JsonRedisSerializer jsonRedisSerializer =
new GenericJackson2JsonRedisSerializer();
//4.设置key的序列化
template.setKeySerializer(RedisSerializer.string());
template.setHashKeySerializer(RedisSerializer.string());
//5.设置value的序列化
template.setValueSerializer(jsonRedisSerializer);
template.setHashValueSerializer(jsonRedisSerializer);
//6.返回
return template;

}
}
但是为了节省内存空间，我们并不会使用SON序列化器来处理value,而是统一使用String序列化器，要求只能存储String类型的key和value.。当需要存储Java对象时，手动完成对象的序列化和反序列化。

private static final ObjectMapper mapper = new ObjectMapper();

@Test
void testSavaUser() throws JsonProcessingException {
//创建对象
User user = new User("骏哥",21);
//手动序列化
String json = mapper.writeValueAsString(user);
//1.写入数据
stringRedisTemplate.opsForValue().set("user:200",json);
//2.获取数据
String jsonUser = stringRedisTemplate.opsForValue().get("user:200");
//手动反序列化
User user1 = mapper.readValue(jsonUser,User.class);
System.out.println(user1);

}

RedisTemplate的两种序列化实践方案：
方案一：
I.自定义RedisTemplate
2.修改RedisTemplate的序列化器为GenericJackson2]sonRedisSerializer
方案二：
l.使用StringRedisTemplate
2.写入Redis时，手动把对象序列化为SON
3.读取Redis时，手动把读取到的SON反序列化为对象
