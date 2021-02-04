package myutils.com.common;


import com.alibaba.fastjson.JSON;

import java.util.*;

/**
 * collections relevance utils
 * @date 2021-02-04
 * @author john
 */
public class CollectionUtils {

    enum  CombineMode{
        LEFT,//保留所有左边list中的keys，右边的keys在合并后可能丢失
        RIGHT,
        ALL
    }

    public static void main(String[] args) {
        Map<String, Object> map1 = new HashMap<>();
        Map<String, Object> map2 = new HashMap<>();
        Map<String, Object> map3 = new HashMap<>();
        Map<String, Object> map4 = new HashMap<>();
        List<Map<String, Object>> list1 = new ArrayList<>();
        List<Map<String, Object>> list2 = new ArrayList<>();

        map1.put("name", "john");
        map1.put("age", 20);
        map1.put("cls", "aaa");
        map1.put("love", "xiyu");
        map1.put("game", "boll");

        map3.put("name", "xiyu");
        map3.put("age", 18);
        map3.put("cls", "aaa");
        map3.put("love", "game");

        map2.put("name", "john");
        map2.put("age", 20);
        map2.put("cls", "aaa");
        map2.put("sport", "run");

        map4.put("name", "xiyu");
        map4.put("age", 18);
        map4.put("cls", "bbbbb");
        map4.put("love", "john");
        map4.put("game", "death");

        list1.add(map1);
        list1.add(map3);

        list2.add(map2);
        list2.add(map4);

        String[] keys = {"name", "age", "cls"};

        System.out.println(JSON.toJSONString(combineMapList(list1, list2, keys, CombineMode.LEFT)));
        System.out.println(JSON.toJSONString(combineMapList(list1, list2, keys, CombineMode.RIGHT)));
        System.out.println(JSON.toJSONString(combineMapList(list1, list2, keys, CombineMode.ALL)));


    }

    /**
     * 根据keys字段，合并两个List<Map<String, Object>>中相同keys的map
     */
    public static List<Map<String, Object>> combineMapList(List<Map<String, Object>> leftList, List<Map<String, Object>> rightList, String[] keys, CombineMode combineMode){
        Map<String, Map<String, Object>> combineResult = new HashMap<>();
        TreeSet<String> keySet = new TreeSet<>(Arrays.asList(keys));
        switch (combineMode){
            case LEFT:
                firstFillMap(combineResult, keySet, leftList);
                secondFillMap(combineResult, keySet, rightList);
                break;
            case RIGHT:
                firstFillMap(combineResult, keySet, rightList);
                secondFillMap(combineResult, keySet, leftList);
                break;
            case ALL:
                firstFillMap(combineResult, keySet, leftList);
                firstFillMap(combineResult, keySet, rightList);
                break;
            default:
                System.out.println("该合并模式不存在！");
        }
        List<Map<String, Object>> result = new ArrayList<>();
        combineResult.forEach((k, v) -> result.add(v));
        return result;
    }

    /**
     * 合成虚拟key
     * @param map
     * @param keySet
     * @return
     */
    private static String getCombineKey(Map<String, Object> map, TreeSet<String> keySet){
        StringBuilder sb = new StringBuilder();//虚拟key
        keySet.forEach(k ->{
            Object o = map.getOrDefault(k, "");
            sb.append(o);
        });
        return sb.toString();
    }

    /**
     * 首次填充
     * @param combineResult
     * @param keySet
     * @param list
     * @return
     */
    private static Map<String, Map<String, Object>> firstFillMap(Map<String, Map<String, Object>> combineResult, TreeSet<String> keySet, List<Map<String, Object>> list){
        list.forEach(m ->{
            String combineKey = getCombineKey(m, keySet);
            if(combineResult.containsKey(combineKey)){
                m.forEach((k, v) ->{
                    if(!keySet.contains(k)){
                        combineResult.get(combineKey).put(k, v);
                    }
                });
            }else {
                combineResult.put(combineKey, m);
            }
        });
        return combineResult;
    }

    /**
     * 二次填充
     * @param combineResult
     * @param keySet
     * @param list
     * @return
     */
    private static Map<String, Map<String, Object>> secondFillMap(Map<String, Map<String, Object>> combineResult, TreeSet<String> keySet, List<Map<String, Object>> list){
        list.forEach(m ->{
            String combineKey = getCombineKey(m, keySet);
            if(combineResult.containsKey(combineKey)){
                m.forEach((k, v) ->{
                    if(!keySet.contains(k)){
                        combineResult.get(combineKey).put(k, v);
                    }
                });
            }
        });
        return combineResult;
    }


}
