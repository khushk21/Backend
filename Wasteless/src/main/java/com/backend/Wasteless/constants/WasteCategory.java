package com.backend.Wasteless.constants;

import com.fasterxml.jackson.annotation.JsonProperty;
public enum WasteCategory {
    @JsonProperty("NORMAL_WASTE")
    NORMAL_WASTE,
    @JsonProperty("E_WASTE")
    E_WASTE,
    @JsonProperty("LIGHTING_WASTE")
    LIGHTING_WASTE,
    @JsonProperty("WASTE_TREATMENT")
    WASTE_TREATMENT,
    @JsonProperty("CASH_FOR_TRASH")
    CASH_FOR_TRASH,
}
