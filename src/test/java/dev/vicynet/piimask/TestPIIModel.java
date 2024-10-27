package dev.vicynet.piimask;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TestPIIModel {

    @MaskData(type = MaskDataE.MASK_START, length = 5)
    private String phoneNumber = "08012345678";

    @MaskData(type = MaskDataE.MASK_END, length = 4)
    private String email = "ihedioha@gmail.com";

    @MaskData(type = MaskDataE.MASK_MIDDLE, length = 6)
    private String meterNumber = "8074567893453";

    @MaskData(type = MaskDataE.MASK_START_END, length = 2)
    private String creditCard = "1234-5678-9876-5432";

}

