package com.obiwanwheeler.interfaces;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface SerializableObject {
    @JsonIgnore String getFolderPath();
    @JsonIgnore String getFileName();
}
