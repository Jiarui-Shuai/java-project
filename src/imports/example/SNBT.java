package imports.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SNBT {
    private Object root; // 移除final修饰符以支持根对象替换

    private SNBT(Object root) {
        this.root = root;
    }

    public static SNBT ParseSNBT(String snbt) {
        Object parsed = parseSNBTInternal(snbt);
        return new SNBT(parsed);
    }

    public Object GetValue(String path) {
        return getValueByPath(root, path);
    }

    // 新增的SetValue方法
    public void SetValue(String path, Object value) {
        if (path == null || path.isEmpty()) {
            // 路径为空时替换整个根对象
            this.root = value;
        } else {
            setValueByPath(root, path, value);
        }
    }

    private static void setValueByPath(Object root, String path, Object value) {
        if (root == null || path == null || path.isEmpty()) {
            throw new IllegalArgumentException("根对象或路径不能为空");
        }

        List<String> parts = splitPath(path);
        Object current = root;

        // 遍历到最后一个部分的前一个对象（父容器）
        for (int i = 0; i < parts.size() - 1; i++) {
            String part = parts.get(i);
            if (current == null) {
                throw new IllegalArgumentException("路径访问中断，遇到null值: " + path);
            }
            current = getValueFromObject(current, part);
        }

        String lastPart = parts.get(parts.size() - 1);
        if (isIndexAccess(lastPart)) {
            int index = parseIndex(lastPart);
            setIndexedValue(current, index, value);
        } else {
            if (current instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> map = (Map<String, Object>) current;
                map.put(lastPart, value);
            } else {
                throw new IllegalArgumentException("当前对象不是Map，无法通过键设置: " + lastPart);
            }
        }
    }

    private static void setIndexedValue(Object container, int index, Object value) {
        if (container instanceof List) {
            @SuppressWarnings("unchecked")
            List<Object> list = (List<Object>) container;
            if (index < 0 || index >= list.size()) {
                throw new IndexOutOfBoundsException("索引越界: " + index);
            }
            list.set(index, value);
        } else if (container instanceof Object[]) {
            Object[] array = (Object[]) container;
            if (index < 0 || index >= array.length) {
                throw new IndexOutOfBoundsException("对象数组索引越界: " + index);
            }
            array[index] = value;
        } else if (container instanceof byte[]) {
            byte[] array = (byte[]) container;
            if (index < 0 || index >= array.length) {
                throw new IndexOutOfBoundsException("字节数组索引越界: " + index);
            }
            if (value instanceof Byte) {
                array[index] = (Byte) value;
            } else if (value instanceof Number) {
                array[index] = ((Number) value).byteValue();
            } else {
                throw new IllegalArgumentException("无法将值转换为字节类型: " + value.getClass().getSimpleName());
            }
        } else if (container instanceof int[]) {
            int[] array = (int[]) container;
            if (index < 0 || index >= array.length) {
                throw new IndexOutOfBoundsException("整型数组索引越界: " + index);
            }
            if (value instanceof Integer) {
                array[index] = (Integer) value;
            } else if (value instanceof Number) {
                array[index] = ((Number) value).intValue();
            } else {
                throw new IllegalArgumentException("无法将值转换为整型: " + value.getClass().getSimpleName());
            }
        } else if (container instanceof long[]) {
            long[] array = (long[]) container;
            if (index < 0 || index >= array.length) {
                throw new IndexOutOfBoundsException("长整型数组索引越界: " + index);
            }
            if (value instanceof Long) {
                array[index] = (Long) value;
            } else if (value instanceof Number) {
                array[index] = ((Number) value).longValue();
            } else {
                throw new IllegalArgumentException("无法将值转换为长整型: " + value.getClass().getSimpleName());
            }
        } else {
            throw new IllegalArgumentException("无法索引访问非数组/列表类型: " + container.getClass().getSimpleName());
        }
    }

    // 重构后的getValueByPath（使用辅助方法）
    private static Object getValueByPath(Object root, String path) {
        if (root == null || path == null || path.isEmpty()) {
            return root;
        }

        List<String> parts = splitPath(path);
        Object current = root;

        for (String part : parts) {
            if (current == null) {
                throw new IllegalArgumentException("路径访问中断，遇到null值: " + path);
            }
            current = getValueFromObject(current, part);
        }

        return current;
    }

    // 辅助方法：根据部分路径（键或索引）从对象获取值
    private static Object getValueFromObject(Object obj, String part) {
        if (isIndexAccess(part)) {
            int index = parseIndex(part);
            return getIndexedValue(obj, index);
        } else {
            if (obj instanceof Map) {
                Map<?, ?> map = (Map<?, ?>) obj;
                Object value = map.get(part);
                if (value == null && !map.containsKey(part)) {
                    throw new IllegalArgumentException("键不存在: " + part);
                }
                return value;
            } else {
                throw new IllegalArgumentException("无法通过键访问非Map类型: " + obj.getClass().getSimpleName());
            }
        }
    }

    // 辅助方法：根据索引从容器获取值
    private static Object getIndexedValue(Object obj, int index) {
        if (obj instanceof List) {
            List<?> list = (List<?>) obj;
            if (index < 0 || index >= list.size()) {
                throw new IndexOutOfBoundsException("列表索引越界: " + index);
            }
            return list.get(index);
        } else if (obj instanceof Object[]) {
            Object[] array = (Object[]) obj;
            if (index < 0 || index >= array.length) {
                throw new IndexOutOfBoundsException("对象数组索引越界: " + index);
            }
            return array[index];
        } else if (obj instanceof byte[]) {
            byte[] array = (byte[]) obj;
            if (index < 0 || index >= array.length) {
                throw new IndexOutOfBoundsException("字节数组索引越界: " + index);
            }
            return array[index];
        } else if (obj instanceof int[]) {
            int[] array = (int[]) obj;
            if (index < 0 || index >= array.length) {
                throw new IndexOutOfBoundsException("整型数组索引越界: " + index);
            }
            return array[index];
        } else if (obj instanceof long[]) {
            long[] array = (long[]) obj;
            if (index < 0 || index >= array.length) {
                throw new IndexOutOfBoundsException("长整型数组索引越界: " + index);
            }
            return array[index];
        } else {
            throw new IllegalArgumentException("无法索引访问非数组/列表类型: " + obj.getClass().getSimpleName());
        }
    }

    private static Object parseSNBTInternal(String snbt) {
        snbt = snbt.trim();
        if (snbt.startsWith("{") && snbt.endsWith("}")) {
            snbt = snbt.substring(1, snbt.length() - 1);
        } else {
            throw new IllegalArgumentException("SNBT字符串格式不正确");
        }

        String[] pairs = splitPairs(snbt);
        Map<String, Object> result = new HashMap<>();

        for (String pair : pairs) {
            if (pair.trim().isEmpty()) continue;
            
            String[] keyValue = pair.split(":", 2);
            if (keyValue.length != 2) {
                throw new IllegalArgumentException("SNBT键值对格式不正确: " + pair);
            }

            String key = keyValue[0].trim();
            String valueStr = keyValue[1].trim();

            // 处理键可能是字符串的情况
            if ((key.startsWith("\"") && key.endsWith("\"")) || 
                (key.startsWith("'") && key.endsWith("'"))) {
                key = unescapeString(key);
            }
            
            Object value = parseValue(valueStr);
            result.put(key, value);
        }

        return result;
    }

    private static Object parseValue(String valueStr) {
        if (valueStr.startsWith("{") && valueStr.endsWith("}")) {
            return parseSNBTInternal(valueStr);
        } else if (valueStr.startsWith("[") && valueStr.endsWith("]")) {
            // 检查是否为特殊类型数组
            if (valueStr.startsWith("[B;") || valueStr.startsWith("[b;")) {
                return parseByteArray(valueStr);
            } else if (valueStr.startsWith("[I;") || valueStr.startsWith("[i;")) {
                return parseIntArray(valueStr);
            } else if (valueStr.startsWith("[L;") || valueStr.startsWith("[l;")) {
                return parseLongArray(valueStr);
            } else {
                return parseList(valueStr.substring(1, valueStr.length() - 1));
            }
        } else if ((valueStr.startsWith("\"") && valueStr.endsWith("\"")) || 
                   (valueStr.startsWith("'") && valueStr.endsWith("'"))) {
            return unescapeString(valueStr);
        } else if ("true".equalsIgnoreCase(valueStr)) {
            return true;
        } else if ("false".equalsIgnoreCase(valueStr)) {
            return false;
        } else {
            // 处理带后缀的数值类型
            if (valueStr.length() > 1) {
                char lastChar = valueStr.charAt(valueStr.length() - 1);
                String numberPart = valueStr.substring(0, valueStr.length() - 1);
                
                try {
                    switch (lastChar) {
                        case 's': return Short.parseShort(numberPart);
                        case 'b': return Byte.parseByte(numberPart);
                        case 'l': return Long.parseLong(numberPart);
                        case 'f': return Float.parseFloat(numberPart);
                        case 'd': return Double.parseDouble(numberPart);
                    }
                } catch (NumberFormatException ignored) {}
            }
            
            // 处理无后缀数值
            try {
                if (valueStr.contains(".") || valueStr.contains("e") || valueStr.contains("E")) {
                    return Double.parseDouble(valueStr);
                } else {
                    return Integer.parseInt(valueStr);
                }
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("无法解析的值: " + valueStr);
            }
        }
    }

    private static List<Object> parseList(String listStr) {
        String[] items = splitListItems(listStr);
        List<Object> result = new ArrayList<>();

        for (String item : items) {
            if (item.trim().isEmpty()) continue;
            result.add(parseValue(item.trim()));
        }

        return result;
    }

    // 解析字节数组 [B;1b,2b,3b]
    private static byte[] parseByteArray(String valueStr) {
        // 移除 [B; 和末尾的 ]
        String content = valueStr.substring(3, valueStr.length() - 1).trim();
        if (content.isEmpty()) {
            return new byte[0];
        }
        
        String[] items = splitListItems(content);
        byte[] result = new byte[items.length];
        
        for (int i = 0; i < items.length; i++) {
            String item = items[i].trim();
            // 移除可选的 'b' 后缀
            if (item.endsWith("b") || item.endsWith("B")) {
                item = item.substring(0, item.length() - 1).trim();
            }
            result[i] = Byte.parseByte(item);
        }
        
        return result;
    }

    // 解析整型数组 [I;1,2,3]
    private static int[] parseIntArray(String valueStr) {
        // 移除 [I; 和末尾的 ]
        String content = valueStr.substring(3, valueStr.length() - 1).trim();
        if (content.isEmpty()) {
            return new int[0];
        }
        
        String[] items = splitListItems(content);
        int[] result = new int[items.length];
        
        for (int i = 0; i < items.length; i++) {
            String item = items[i].trim();
            result[i] = Integer.parseInt(item);
        }
        
        return result;
    }

    // 解析长整型数组 [L;1l,2l,3l]
    private static long[] parseLongArray(String valueStr) {
        // 移除 [L; 和末尾的 ]
        String content = valueStr.substring(3, valueStr.length() - 1).trim();
        if (content.isEmpty()) {
            return new long[0];
        }
        
        String[] items = splitListItems(content);
        long[] result = new long[items.length];
        
        for (int i = 0; i < items.length; i++) {
            String item = items[i].trim();
            // 移除可选的 'l' 后缀
            if (item.endsWith("l") || item.endsWith("L")) {
                item = item.substring(0, item.length() - 1).trim();
            }
            result[i] = Long.parseLong(item);
        }
        
        return result;
    }

    private static String[] splitPairs(String snbt) {
        List<String> pairs = new ArrayList<>();
        int depth = 0;
        char quoteChar = 0;  // 当前引号字符，0表示不在字符串中
        boolean escape = false;
        StringBuilder current = new StringBuilder();

        for (char c : snbt.toCharArray()) {
            if (quoteChar != 0) {  // 在字符串中
                if (escape) {
                    escape = false;
                } else if (c == '\\') {
                    escape = true;
                } else if (c == quoteChar) {
                    quoteChar = 0;  // 结束字符串
                }
            } else {  // 不在字符串中
                if (c == '"' || c == '\'') {
                    quoteChar = c;  // 开始字符串
                } else if (c == '{' || c == '[') {
                    depth++;
                } else if (c == '}' || c == ']') {
                    depth--;
                } else if (c == ',' && depth == 0) {
                    if (current.length() > 0) {
                        pairs.add(current.toString());
                        current.setLength(0);
                    }
                    continue;
                }
            }
            
            current.append(c);
        }

        if (current.length() > 0) {
            pairs.add(current.toString());
        }

        return pairs.toArray(new String[0]);
    }

    private static String[] splitListItems(String listStr) {
        List<String> items = new ArrayList<>();
        int depth = 0;
        char quoteChar = 0;  // 当前引号字符，0表示不在字符串中
        boolean escape = false;
        StringBuilder current = new StringBuilder();

        for (char c : listStr.toCharArray()) {
            if (quoteChar != 0) {  // 在字符串中
                if (escape) {
                    escape = false;
                } else if (c == '\\') {
                    escape = true;
                } else if (c == quoteChar) {
                    quoteChar = 0;  // 结束字符串
                }
            } else {  // 不在字符串中
                if (c == '"' || c == '\'') {
                    quoteChar = c;  // 开始字符串
                } else if (c == '{' || c == '[') {
                    depth++;
                } else if (c == '}' || c == ']') {
                    depth--;
                } else if (c == ',' && depth == 0) {
                    if (current.length() > 0) {
                        items.add(current.toString());
                        current.setLength(0);
                    }
                    continue;
                }
            }
            
            current.append(c);
        }

        if (current.length() > 0) {
            items.add(current.toString());
        }

        return items.toArray(new String[0]);
    }

    private static String unescapeString(String str) {
        // 确定引号类型并提取内容
        if ((!str.startsWith("\"") || !str.endsWith("\"")) && 
            (!str.startsWith("'") || !str.endsWith("'"))) {
            return str;
        }
        
        // char quoteChar = str.charAt(0);
        String content = str.substring(1, str.length() - 1);
        StringBuilder sb = new StringBuilder();
        boolean escape = false;
        
        for (char c : content.toCharArray()) {
            if (escape) {
                switch (c) {
                    case '\\': sb.append('\\'); break;
                    case '"': sb.append('"'); break;
                    case '\'': sb.append('\''); break;
                    case 'n': sb.append('\n'); break;
                    case 'r': sb.append('\r'); break;
                    case 't': sb.append('\t'); break;
                    default: sb.append(c);
                }
                escape = false;
            } else {
                if (c == '\\') {
                    escape = true;
                } else {
                    sb.append(c);
                }
            }
        }
        
        return sb.toString();
    }

    private static List<String> splitPath(String path) {
        List<String> parts = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inBrackets = false;
        boolean escape = false;
        
        for (char c : path.toCharArray()) {
            if (escape) {
                current.append(c);
                escape = false;
            } else if (c == '\\') {
                escape = true;
            } else if (c == '[') {
                if (inBrackets) {
                    throw new IllegalArgumentException("无效路径: 嵌套括号");
                }
                if (current.length() > 0) {
                    parts.add(current.toString());
                    current.setLength(0);
                }
                inBrackets = true;
                current.append(c);
            } else if (c == ']') {
                if (!inBrackets) {
                    throw new IllegalArgumentException("无效路径: 未匹配的']'");
                }
                current.append(c);
                parts.add(current.toString());
                current.setLength(0);
                inBrackets = false;
            } else if (c == '.' && !inBrackets) {
                if (current.length() > 0) {
                    parts.add(current.toString());
                    current.setLength(0);
                }
            } else {
                current.append(c);
            }
        }
        
        if (inBrackets) {
            throw new IllegalArgumentException("无效路径: 未关闭的括号");
        }
        
        if (current.length() > 0) {
            parts.add(current.toString());
        }
        
        return parts;
    }
    
    private static boolean isIndexAccess(String part) {
        return part.startsWith("[") && part.endsWith("]");
    }
    
    private static int parseIndex(String indexStr) {
        try {
            return Integer.parseInt(indexStr.substring(1, indexStr.length() - 1));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("无效索引格式: " + indexStr);
        }
    }
    
    // 测试方法
    public static void main(String[] args) {
        String snbt = "{"
            + "person: {name: 'John', age: 30, scores: [85, 92, 78]}, "
            + "ids: [I;101,102,103], "
            + "data: {"
            + "  bytes: [B;1b,2b,3b], "
            + "  longs: [L;1000l,2000l]"
            + "}"
            + "}";
        
        SNBT parsed = SNBT.ParseSNBT(snbt);
        
        // 测试原始值
        System.out.println("原始值:");
        System.out.println(parsed.GetValue("person.name")); // "John"
        System.out.println(parsed.GetValue("person.age"));  // 30
        System.out.println(parsed.GetValue("person.scores[1]")); // 92
        System.out.println(parsed.GetValue("ids[2]")); // 103
        System.out.println(parsed.GetValue("data.bytes[0]")); // 1 (byte)
        System.out.println(parsed.GetValue("data.longs[1]")); // 2000 (long)
        
        // 测试修改值
        System.out.println("\n修改值后:");
        parsed.SetValue("person.name", "Alice");
        parsed.SetValue("person.age", 35);
        parsed.SetValue("person.scores[1]", 95);
        parsed.SetValue("ids[0]", 201);
        parsed.SetValue("data.bytes[2]", (byte) 10);
        parsed.SetValue("data.longs[1]", 3000L);
        
        // 验证修改结果
        System.out.println(parsed.GetValue("person.name")); // "Alice"
        System.out.println(parsed.GetValue("person.age"));  // 35
        System.out.println(parsed.GetValue("person.scores[1]")); // 95
        System.out.println(parsed.GetValue("ids[0]")); // 201
        System.out.println(parsed.GetValue("data.bytes[2]")); // 10 (byte)
        System.out.println(parsed.GetValue("data.longs[1]")); // 3000 (long)
        
        // 测试数组整体访问
        System.out.println("\n数组整体访问:");
        System.out.println(Arrays.toString((int[]) parsed.GetValue("ids"))); // [201, 102, 103]
        System.out.println(Arrays.toString((byte[]) parsed.GetValue("data.bytes"))); // [1, 2, 10]
    }
}