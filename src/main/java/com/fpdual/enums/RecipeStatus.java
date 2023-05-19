package com.fpdual.enums;
public enum RecipeStatus {
    PENDING("PENDING"),
    ACCEPTED("ACCEPTED"),
    DECLINED("DECLINED");

    private String status;

    RecipeStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
    // Devuelve la instancia del enum RecipeStatus correspondiente al estado proporcionado
    public static RecipeStatus fromString(String status) {
        for (RecipeStatus recipeStatus : RecipeStatus.values()) {
            if (recipeStatus.getStatus().equalsIgnoreCase(status)) {
                return recipeStatus;
            }
        }
        // Lanza una excepción si se proporciona un estado no válido
        throw new IllegalArgumentException("Invalid RecipeStatus: " + status);
    }
}
