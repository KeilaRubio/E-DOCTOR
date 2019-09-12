package com.example.e_doctor.retrofit.error;

import com.example.e_doctor.retrofit.modelos.DATA_ERROR;
import com.example.e_doctor.retrofit.modelos.NOTICIAS;
import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.Map;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "estado",
        "noticias",
        "data"

})

public class ServiceError {
    @JsonProperty("estado")
    private String estado;
    @JsonProperty("noticias")
    private NOTICIAS noticias;
    @JsonProperty("mensaje_desarrollador")
    private String mensaje_desarrollador;
    @JsonProperty("data")
    private DATA_ERROR data;


    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     */
    public ServiceError() {
    }

    /**
     * @param message
     * @param status
     * @param mensaje_desa
     */
    public ServiceError(String status, NOTICIAS message, String mensaje_desa) {
        this.estado = status;
        this.noticias = message;
        this.mensaje_desarrollador = mensaje_desa;
    }

    /**
     * @return The status
     */
    @JsonProperty("estado")
    public String getEstado() {
        return estado;
    }

    /**
     * @param status The status
     */
    @JsonProperty("estado")
    public void setEstado(String status) {
        this.estado = status;
    }

    /**
     * @return The message
     */
    @JsonProperty("noticias")
    public NOTICIAS getNoticias() {
        return noticias;
    }

    /**
     * @param message The message
     */
    @JsonProperty("noticias")
    public void setNoticias(NOTICIAS noti) {
        this.noticias = noti;
    }


    @JsonProperty("data")
    public DATA_ERROR getData() {
        return data;
    }

    /**
     * @param message The message
     */
    @JsonProperty("data")
    public void setData(DATA_ERROR data) {
        this.data = data;
    }

    /**
     * @return The message
     */
    @JsonProperty("mensaje_desarrollador")
    public String getMensajeDesarrollador() {
        return mensaje_desarrollador;
    }

    /**
     * @param message The message
     */
    @JsonProperty("mensaje_desarrollador")
    public void setMensajeDesarrollador(String message) {
        this.mensaje_desarrollador = message;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }
}