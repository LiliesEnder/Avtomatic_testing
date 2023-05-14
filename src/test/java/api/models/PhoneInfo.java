package api.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PhoneInfo {
    private String name;
    private String dimensions;
    private String weight;
    private String displayType;
    private String displaySize;
    private String displayResolution;
    private String os;
    private String cpu;
    private String internalMemory;
    private String ram;
    private String camera;
    private String batery;
    private String color;
    private float price;
    private String photo;

}
