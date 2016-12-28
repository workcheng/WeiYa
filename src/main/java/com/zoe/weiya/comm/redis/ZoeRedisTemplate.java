/**
 * @Copyright 0gmi
 * @Author Bane.Shi 2015年7月3日-下午5:29:41
 */
package com.zoe.weiya.comm.redis;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author Chenghui
 *
 */
public class ZoeRedisTemplate {
  private RedisTemplate<String, Object> redisTemplate = null;
  private boolean                       usePool       = true;

  public void setUsePool(boolean usePool) {
    this.usePool = usePool;
  }

  public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
    this.redisTemplate = redisTemplate;
  }

  public void setValue(final String key, Object value) {
    redisTemplate.opsForValue().set(key, value);
  }

  public void setValue(String key, Object value, int timeout) {
    redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.MINUTES);
  }

  public Object getValue(Object key) {
    return redisTemplate.opsForValue().get(key);
  }

  public String getByteValue(final String key) {
    return redisTemplate.execute(new RedisCallback<String>() {
      @Override
      public String doInRedis(RedisConnection connection) throws DataAccessException {
        RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
        String result = "";
        byte[] b = connection.get(serializer.serialize(key));
        if (null != b && b.length > 0) {
          result = serializer.deserialize(b);
        }
        return result;
      }
    });
  }

  public Object getValueExpire(final String key, final int timeout) {
    return redisTemplate.execute(new RedisCallback<Object>() {

      @Override
      public Object doInRedis(RedisConnection connection) throws DataAccessException {
        BoundValueOperations<String, Object> boundValueOperations = redisTemplate.boundValueOps(key);
        boundValueOperations.expire(timeout, TimeUnit.MINUTES);
        return boundValueOperations.get();
      }
    });
  }

  public void setHash(final String key, final String hashKey, final String value) {
    redisTemplate.opsForHash().put(key, hashKey, value);
  }

  public void setHash(final String key, final Object hashKey, final Object value, final int timeout) {
    redisTemplate.execute(new RedisCallback<Object>() {

      @Override
      public Object doInRedis(RedisConnection connection) throws DataAccessException {
        BoundHashOperations<String, Object, Object> boundHashOperations = redisTemplate.boundHashOps(key);
        boundHashOperations.put(hashKey, value);
        boundHashOperations.expire(timeout, TimeUnit.MINUTES);
        return null;
      }
    });
  }

  /**
   * 
   * @param key
   * @param value
   * @param timeout
   *          (second)
   * @return
   */
  public boolean setNX(final String key, final String value, final int timeout) {
    return redisTemplate.execute(new RedisCallback<Boolean>() {
      @Override
      public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
        RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
        if (connection.setNX(serializer.serialize(key), serializer.serialize(value))) {
          connection.expire(serializer.serialize(key), timeout);
          return true;
        } else {
          return false;
        }
      }
    });
  }

  public Map<String, String> hGetAll(final String key) {
    Map<byte[], byte[]> map = redisTemplate.execute(new RedisCallback<Map<byte[], byte[]>>() {
      @Override
      public Map<byte[], byte[]> doInRedis(RedisConnection connection) throws DataAccessException {
        RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
        return connection.hGetAll(serializer.serialize(key));
      }
    });
    RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
    Map<String, String> result = new HashMap<String, String>();
    if (null != map && map.size() > 0) {
      Iterator<byte[]> it = map.keySet().iterator();
      while (it.hasNext()) {
        byte[] hashKey = it.next();
        byte[] value = map.get(hashKey);
        result.put(serializer.deserialize(hashKey), serializer.deserialize(Arrays.copyOfRange(value, 7, value.length)));
      }
    }
    return result;

  }

  public void setHash(final String key, final Object obj) {
    redisTemplate.execute(new RedisCallback<Object>() {

      @SuppressWarnings("unchecked")
      @Override
      public Object doInRedis(RedisConnection connection) throws DataAccessException {
        BoundHashOperations<String, Object, Object> boundHashOperations = redisTemplate.boundHashOps(key);
        if (obj instanceof Map) {
          for (Map.Entry<String, Object> mapitem : ((Map<String, Object>) obj).entrySet()) {
            boundHashOperations.put(mapitem.getKey(), mapitem.getValue());
          }
        } else {
          Field[] fiels = obj.getClass().getDeclaredFields();
          for (Field field : fiels) {
            Object fieldValue = null;
            try {
              fieldValue = obj.getClass().getMethod("get" + field.getName()).invoke(obj);
            } catch (IllegalAccessException e) {
              // TODO Auto-generated catch block
              e.printStackTrace();
            } catch (IllegalArgumentException e) {
              // TODO Auto-generated catch block
              e.printStackTrace();
            } catch (InvocationTargetException e) {
              // TODO Auto-generated catch block
              e.printStackTrace();
            } catch (NoSuchMethodException e) {
              // TODO Auto-generated catch block
              e.printStackTrace();
            } catch (SecurityException e) {
              // TODO Auto-generated catch block
              e.printStackTrace();
            }
            boundHashOperations.put(field.getName(), fieldValue);
          }
        }

        return null;
      }

    });
  }

  public void setHash(final String key, final Object obj, final int timeout) {
    redisTemplate.execute(new RedisCallback<Object>() {

      @SuppressWarnings("unchecked")
      @Override
      public Object doInRedis(RedisConnection connection) throws DataAccessException {
        BoundHashOperations<String, Object, Object> boundHashOperations = redisTemplate.boundHashOps(key);
        if (obj instanceof Map) {
          for (Map.Entry<String, Object> mapitem : ((Map<String, Object>) obj).entrySet()) {
            boundHashOperations.put(mapitem.getKey(), mapitem.getValue());
          }
        } else {
          Field[] fiels = obj.getClass().getDeclaredFields();
          for (Field field : fiels) {
            Object fieldValue = null;
            try {
              fieldValue = obj.getClass().getMethod("get" + field.getName()).invoke(obj);
            } catch (IllegalAccessException e) {
              // TODO Auto-generated catch block
              e.printStackTrace();
            } catch (IllegalArgumentException e) {
              // TODO Auto-generated catch block
              e.printStackTrace();
            } catch (InvocationTargetException e) {
              // TODO Auto-generated catch block
              e.printStackTrace();
            } catch (NoSuchMethodException e) {
              // TODO Auto-generated catch block
              e.printStackTrace();
            } catch (SecurityException e) {
              // TODO Auto-generated catch block
              e.printStackTrace();
            }
            boundHashOperations.put(field.getName(), fieldValue);
          }
        }
        boundHashOperations.expire(timeout, TimeUnit.MINUTES);
        return null;
      }

    });
  }

  public Object getHash(String key, Object hashKey) {
    return redisTemplate.opsForHash().get(key, hashKey);
  }

  public Object getHash(final String key, final Object hashKey, final int timeout) {
    return redisTemplate.execute(new RedisCallback<Object>() {

      @Override
      public Object doInRedis(RedisConnection connection) throws DataAccessException {
        BoundHashOperations<String, Object, Object> boundHashOperations = redisTemplate.boundHashOps(key);
        boundHashOperations.expire(timeout, TimeUnit.MINUTES);
        return boundHashOperations.get(hashKey);
      }
    });
  };

  public void incr(final String key) {
    redisTemplate.execute(new RedisCallback<Long>() {
      @Override
      public Long doInRedis(RedisConnection connection) throws DataAccessException {
        RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
        return connection.incr(serializer.serialize(key));
      }
    });
  }

  public void deleteHash(String key) {
    redisTemplate.opsForHash().delete(key);
  }

  public void deleteValue(String key) {
    redisTemplate.delete(key);
  }

  public Set<String> getAllSessionkey() {

    Set<String> sets = redisTemplate.keys("00:session:*");
    return sets;
  }

  public long getExpire(String key) {
    return redisTemplate.getExpire(key);
  }

  public long getExpire(String key, TimeUnit timeUnit) {
    return redisTemplate.getExpire(key, timeUnit);
  }

  public Long setSet(String key, Object obj){
    return redisTemplate.opsForSet().add(key,obj);
  }

  public boolean isMember(String key, Object obj){
    return redisTemplate.opsForSet().isMember(key,obj);
  }

  public Long getSetSize(String key){
    return redisTemplate.opsForSet().size(key);
  }

  public Object pop(String key){
    return redisTemplate.opsForSet().pop(key);
  }

  public Set<Object> getSet(String key){
    return redisTemplate.opsForSet().members(key);
  }

  public Object randomMember(String key){
    return redisTemplate.opsForSet().randomMember(key);
  }

  public List<Object> randomMember(String key, long l){
    return redisTemplate.opsForSet().randomMembers(key,l);
  }

  public Long remove(String key, List<Object> list){
    return redisTemplate.opsForSet().remove(key, list);
  }

  public boolean move(String key){
    return redisTemplate.move(key,1);
  }

  public ListOperations<String, Object> getListOperations(){
      return redisTemplate.opsForList();
  }

}
