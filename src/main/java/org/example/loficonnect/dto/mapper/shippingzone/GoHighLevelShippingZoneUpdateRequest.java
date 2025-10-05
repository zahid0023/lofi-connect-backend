package org.example.loficonnect.dto.mapper.shippingzone;

import lombok.Data;
import org.example.loficonnect.dto.request.shippingzone.ShippingZoneUpdateRequest;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class GoHighLevelShippingZoneUpdateRequest {
    private String altId;
    private String altType;
    private String name;
    private List<Country> countries;

    @Data
    public static class Country {
        private String code;
        private List<State> states;
    }

    @Data
    public static class State {
        private String code;
    }

    public static GoHighLevelShippingZoneUpdateRequest fromRequest(ShippingZoneUpdateRequest request) {
        GoHighLevelShippingZoneUpdateRequest ghlRequest = new GoHighLevelShippingZoneUpdateRequest();
        ghlRequest.setAltId(request.getAltId());
        ghlRequest.setAltType(request.getAltType());
        ghlRequest.setName(request.getName());

        if (request.getCountries() != null) {
            List<Country> countryList = request.getCountries().stream().map(c -> {
                Country country = new Country();
                country.setCode(c.getCode());
                if (c.getStates() != null) {
                    List<State> stateList = c.getStates().stream().map(s -> {
                        State state = new State();
                        state.setCode(s.getCode());
                        return state;
                    }).collect(Collectors.toList());
                    country.setStates(stateList);
                }
                return country;
            }).collect(Collectors.toList());
            ghlRequest.setCountries(countryList);
        }
        return ghlRequest;
    }
}
