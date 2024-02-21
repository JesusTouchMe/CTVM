package cum.jesus.ctvm.constant;

import cum.jesus.ctvm.value.NumberValue;
import cum.jesus.ctvm.value.StringValue;
import cum.jesus.ctvm.value.Value;

import java.util.Arrays;
import java.util.Iterator;

public final class ConstantPool implements Iterable<Value> {
     private Value[] pool;

     public ConstantPool(int initialSize) {
         this.pool = new Value[(initialSize == 0) ? 10 : initialSize];
     }

     public int parse(byte[] bytes, int index) {
         int poolIndex = 0;

         loop:
         while (index < bytes.length) {
             if (poolIndex == pool.length) {
                 int oldCapacity = pool.length;
                 int newCapacity = oldCapacity + (oldCapacity >> 1);
                 pool = Arrays.copyOf(pool, newCapacity);
             }

             byte type = bytes[index++];

             switch (type) {
                 case Value.TYPE_BYTE:
                     pool[poolIndex++] = new NumberValue(type, bytes[index++]);
                     break;

                 case Value.TYPE_SHORT:
                     pool[poolIndex++] = new NumberValue(type, (short) (((bytes[index++] & 0xFF) << 8) | (bytes[index++] & 0xFF)));
                     break;

                 case Value.TYPE_INT:
                     pool[poolIndex++] = new NumberValue(type, ((bytes[index++] & 0xFF) << 24) | ((bytes[index++] & 0xFF) << 16) | ((bytes[index++] & 0xFF) << 8) | ((bytes[index++] & 0xFF)));
                     break;

                 case Value.TYPE_LONG:
                     pool[poolIndex++] = new NumberValue(type,
                             ((long) (bytes[index++] & 0xFF) << 56)
                                     | ((long) (bytes[index++] & 0xFF) << 48)
                                     | ((long) (bytes[index++] & 0xFF) << 40)
                                     | ((long) (bytes[index++] & 0xFF) << 32)
                                     | ((long) (bytes[index++] & 0xFF) << 24)
                                     | ((long) (bytes[index++] & 0xFF) << 16)
                                     | ((long) (bytes[index++] & 0xFF) << 8)
                                     | ((long) (bytes[index++] & 0xFF)));
                     break;

                 case Value.TYPE_STRING: {
                     int length = ((bytes[index++] & 0xFF) << 8) | (bytes[index++] & 0xFF);
                     byte[] string = new byte[length];

                     for (int i = 0; i < length; i++) {
                         string[i] = bytes[index++];
                     }

                     pool[poolIndex++] = new StringValue(new String(string));
                 } break;

                 case 0:
                     if (bytes[index] == 0) {
                         index++;
                         break loop;
                     }
             }
         }

         return index;
     }

     public int size() {
         return pool.length;
     }

     public Value get(int index) {
         return pool[index];
     }

     public void set(int index, Value value) {
         pool[index] = value;
     }

    @Override
    public Iterator<Value> iterator() {
         return Arrays.stream(pool).iterator();
    }
}
