package com.test.bus.common.util;

import com.test.bus.common.pojo.message.EventHeaders;
import com.test.share.common.json.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;

import java.util.*;

/**
 * 事件推送条件工具类
 *
 * @author 费世程
 * @date 2021/3/2 14:22
 */
public class ConditionMatcher {

  private static final Logger log = LoggerFactory.getLogger(ConditionMatcher.class);

  /**
   * 将条件表达式解析为条件组
   *
   * @param conditionExpression 条件表达式
   * @return 条件组
   * @author 费世程
   * @date 2021/3/2 14:27
   */
  @NonNull
  public static List<Set<String>> parseConditions(@NonNull String conditionExpression) {
    if (StringUtils.isBlank(conditionExpression)) {
      return Collections.emptyList();
    }
    List<Set<String>> conditionGroup = new ArrayList<>();
    for (String condition : StringUtils.split(conditionExpression, "|")) {
      String[] split = StringUtils.split(condition, "&");
      conditionGroup.add(new HashSet<>(Arrays.asList(split)));
    }
    return conditionGroup;
  }

  /**
   * 条件匹配 <br/>
   * 同组为且，异组为或
   *
   * @param conditionGroup 条件组
   * @param headers        事件头
   * @return 条件是否匹配
   * @author 费世程
   * @date 2021/3/2 14:47
   */
  public static boolean match(@NonNull List<Set<String>> conditionGroup, @NonNull EventHeaders headers) {
    if (conditionGroup.isEmpty()) {
      return true;
    }
    if (headers.isEmpty()) {
      return false;
    }
    for (Set<String> condition : conditionGroup) {
      //组内判断，只要有一组条件满足则整体满足
      if (matchConditions(condition, headers)) {
        return true;
      }
    }
    return false;
  }

  /**
   * 通过条件表达式判断条件是否匹配
   *
   * @param conditionExpression 条件表达式
   * @param headers             事件头
   * @return 条件是否匹配
   * @author 费世程
   * @date 2021/3/2 14:49
   */
  public static boolean match(@NonNull String conditionExpression, @NonNull EventHeaders headers) {
    List<Set<String>> conditionGroup = parseConditions(conditionExpression);
    return match(conditionGroup, headers);
  }

  /**
   * 组内判断
   *
   * @param conditions 条件
   * @param headers    事件头
   * @return boolean
   * @author 费世程
   * @date 2021/3/2 14:50
   */
  public static boolean matchConditions(Set<String> conditions, EventHeaders headers) {
    if (conditions.isEmpty()) {
      return true;
    }
    for (String condition : conditions) {
      char operator = '0';
      int index = -1;
      char[] charArray = condition.toCharArray();
      for (int i = 0; i < charArray.length; i++) {
        char ch = charArray[i];
        if (ch == '=' || ch == '^' || ch == '>' || ch == '<') {
          operator = ch;
          index = i;
          break;
        }
      }
      if (index < 0) {
        log.debug("条件判断不通过，缺少条件运算符 -> {}", conditions);
        return false;
      }
      String key = StringUtils.substring(condition, 0, index);
      Set<String> headValues = headers.get(key);
      if (headValues == null || headValues.isEmpty()) {
        return false;
      }
      String value = StringUtils.substring(condition, index+1);
      switch (operator) {
        case '=': {
          if (!headValues.contains(value)) {
            return false;
          }
          break;
        }
        case '>': {
          boolean flag = false;
          for (String headValue : headValues) {
            long target;
            long cond;
            try {
              target = Long.parseLong(headValue);
              cond = Long.parseLong(value);
            } catch (NumberFormatException e) {
              log.debug("条件判断不通过 -> NumberFormatException：headValue{},value{}", headValue, value);
              return false;
            }
            if (target > cond) {
              flag = true;
            }
          }
          return flag;
        }
        case '<': {
          boolean flag = false;
          for (String headValue : headValues) {
            long target;
            long cond;
            try {
              target = Long.parseLong(headValue);
              cond = Long.parseLong(value);
            } catch (NumberFormatException e) {
              log.debug("条件判断不通过 -> NumberFormatException：headValue{},value{}", headValue, value);
              return false;
            }
            if (target < cond) {
              flag = true;
            }
          }
          if (!flag) {
            return false;
          }
          break;
        }
        case '^': {
          String[] split = StringUtils.split(value, ",");
          boolean flag = false;
          for (String s : split) {
            if (headValues.contains(s)) {
              flag = true;
              break;
            }
          }
          if (!flag) {
            return false;
          }
          break;
        }
        default: {
          log.debug("非法的操作符：{}", operator);
          break;
        }
      }
    }
    //全部满足了才返回true
    return true;
  }

  public static void main(String[] args) {
    //订阅条件
    String conditionExpression = "tenantId=3&userId^1001,1002,1003|birthYear>1997";
    List<Set<String>> conditionGroup = ConditionMatcher.parseConditions(conditionExpression);
    System.err.println(JsonUtils.INSTANCE.toJsonString(conditionGroup));
    //推送事件的条件
    EventHeaders headers = EventHeaders.create();
    headers.addAll("userId", new HashSet<>(Arrays.asList("1001", "1002")));
    headers.add("tenantId", "4");
    headers.add("tenantId", "3");
    System.err.println(ConditionMatcher.match(conditionGroup, headers));
  }

}
