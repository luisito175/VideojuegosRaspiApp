package iesvdc.segdodam.recyclerviewmotos.domain.models

/**
 * Entity del dominio que representa una motocicleta.
 * No tiene dependencias externas.
 */
data class MotoEntity(
    val marca: String,
    val modelo: String,
    val caracteristicas: String,
    val imagen: String
)
