package api.test;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PhoneTags {
    private String priceRange;
    private String brand;
    private String color;
    private String os;
    private String internalMemory;
    private String ram;
    private String displaySize;
    private String displayResolution;
    private String camera;
    private String cpu;
}
