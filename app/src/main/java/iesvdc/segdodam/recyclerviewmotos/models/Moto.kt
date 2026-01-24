package iesvdc.segdodam.recyclerviewmotos.models

/**
 * `Moto.kt`
 * Modelo de datos que representa una motocicleta.
 *
 * Usamos una `data class` porque nos proporciona automáticamente
 * métodos útiles como `equals()`, `hashCode()`, `toString()`, `copy()`, etc.
 *
 * @property marca La marca de la motocicleta (ej. Yamaha, Honda).
 * @property modelo El modelo específico de la moto.
 * @property caracteristicas Una breve descripción de sus características técnicas.
 * @property imagen La URL o URI de la imagen de la moto.
 */
data class Moto (
    val marca: String,
    val modelo: String,
    val caracteristicas: String,
    val imagen : String
)
