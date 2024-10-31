package dev.vicynet.piimask;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.io.IOException;

@NoArgsConstructor
@AllArgsConstructor
public class MaskDataSerializer extends JsonSerializer<Object> implements ContextualSerializer {

    private MaskData maskData;

    @Override
    public void serialize(Object value, JsonGenerator jsonGenerator, SerializerProvider serializers) throws IOException {
        if (maskData != null && value != null) {
            String maskedValue = applyMasking(value.toString(), maskData.type(), maskData.length());
            jsonGenerator.writeString(maskedValue);
        } else {
            jsonGenerator.writeString(String.valueOf(value));
        }
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider serializerProvider, BeanProperty beanProperty) throws JsonMappingException {
        if (beanProperty != null) {
            MaskData maskData = beanProperty.getAnnotation(MaskData.class);
            if (maskData != null) {
                return new MaskDataSerializer(maskData); // Return serializer with masking settings
            }
            if(beanProperty.getType() != null){
                return serializerProvider.findValueSerializer(beanProperty.getType(), beanProperty);
            }
        }
        return serializerProvider.findValueSerializer(Object.class, beanProperty); // Default serializer if no MaskData annotation
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