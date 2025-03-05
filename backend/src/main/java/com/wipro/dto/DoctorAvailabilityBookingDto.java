package  com.wipro.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorAvailabilityBookingDto {

    private Long doctorAvailabilityId;
    private Integer doctorShift;
    private List<Integer> availableSlots;
}
