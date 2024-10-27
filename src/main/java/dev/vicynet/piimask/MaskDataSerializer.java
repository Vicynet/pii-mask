package dev.vicynet.piimask;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MaskDataSerializer extends JsonSerializer<Object> {
    private static final Logger logger = Logger.getLogger(String.valueOf(MaskDataSerializer.class));

    @Override
    public void serialize(Object value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if (value == null) {
            jsonGenerator.writeNull();
            return;
        }

        jsonGenerator.writeStartObject();
        Class<?> valueClass = value.getClass();

        for (Field field : valueClass.getDeclaredFields()) {
            field.setAccessible(true);
            try {
                Object fieldValue = field.get(value);
                String fieldName = field.getName();

                if (fieldValue == null) {
                    continue;
                }

                MaskData maskData = field.getAnnotation(MaskData.class);
                if (maskData != null && fieldValue instanceof String stringValue) {
                    String maskedValue = applyMasking(stringValue, maskData.type(), maskData.length());
                    jsonGenerator.writeStringField(fieldName, maskedValue);
                } else {
                    jsonGenerator.writeObjectField(fieldName, fieldValue);
                }
            } catch (IllegalAccessException e) {
                logger.log(Level.SEVERE, e.getMessage());
            }
        }
        jsonGenerator.writeEndObject();
    }

    private String applyMasking(String value, MaskDataE type, int length) {
        return switch (type) {
            case MASK_START -> maskStart(value, length);
            case MASK_END -> maskEnd(value, length);
            case MASK_MIDDLE -> maskMiddle(value, length);
            case MASK_START_END -> maskStartAndEnd(value, length);
        };
    }

    private String maskStart(String value, int length) {
        return "*".repeat(Math.min(length, value.length())) + value.substring(Math.min(length, value.length()));
    }

    private String maskEnd(String value, int length) {
        return value.substring(0, Math.max(0, value.length() - length)) + "*".repeat(Math.min(length, value.length()));
    }

    private String maskMiddle(String value, int length) {
        if (value.length() <= 2 + length) {
            return "*".repeat(value.length());
        }
        int start = (value.length() - length) / 2;
        int end = start + length;
        return value.substring(0, start) + "*".repeat(length) + value.substring(end);
    }

    private String maskStartAndEnd(String value, int length) {
        return maskStart(value, length) + maskEnd(value, length);
    }
}
