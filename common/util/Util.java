package util;

import java.io.DataInputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Util {
	
	@SuppressWarnings("unchecked")
	public static <K> K[] toArray(Class<K> cls, Collection<K>list){
		K [] here = (K[]) Array.newInstance(cls , list.size());
		return (K[])list.toArray( here );
	}
	
	/**
	 * 해당 타입을 반환하는 함수형 인터페이스를 인자로 받아 실행하며,
	 * 도중 오류가 발생할 경우 즉시 RuntimeException을 던져 실행을 중단시킵니다.
	 * @param <T> 실행하려는 함수형 인터페이스의 구현체
	 * @throws RuntimeException
	 */
	public static <T> T tryDoing(ReturnableAction<T> action) throws RuntimeException{
		try {
			return action.run();
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	
	/**
	 * void의 함수형 인터페이스를 인자로 받아 실행하며,
	 * 도중 오류가 발생할 경우 즉시 RuntimeException을 던져 실행을 중단시킵니다.
	 * @param action 실행하려는 함수형 인터페이스의 구현체
	 * @throws RuntimeException
	 */
	public static void tryDoing(VoidAction runnable) throws RuntimeException{
		try {
			runnable.run();
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
	
	public static <K> K createInstance ( Class<K> cls) {
		try {
			return cls.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new RuntimeException("fail to create instance", e);
		}
	}
	/**
	 * 
	 * @param keys 쉼표로
	 * @param ins
	 * @return
	 */
	public static Map<String, Object> map(String ptn, Object...ins){
		String [] keys = ptn.split(",");
		if(ins.length!=keys.length) return null;

		Map<String, Object> map = new HashMap<>();
		
		for(int i = 0; i< keys.length; i++){
			map.put(keys[i].trim(), ins[i]);
		}
		return map;
	}
//	public static Map<String, Object> map(Object[]values, String...keys){
//		if(values.length!=keys.length) return null;
////		Map<String, Object> map = new HashMap<>();
//		
//		for(int i=0; i<keys.length; i++){
//			map.put(keys[i], values[i]);
//		}
//		return map;
//	}
	
}
