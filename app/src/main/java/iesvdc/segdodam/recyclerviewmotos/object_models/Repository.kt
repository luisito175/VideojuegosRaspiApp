package iesvdc.segdodam.recyclerviewmotos.object_models

import iesvdc.segdodam.recyclerviewmotos.Interfaces.InterfaceDao
import iesvdc.segdodam.recyclerviewmotos.models.Moto

object Repository : InterfaceDao{

    val motos = listOf(
        Moto(
            marca = "Gas Gas",
            modelo = "EC 300 2005",
            caracteristicas = "Motor 2T, 293cc, potencia 55cv, suspensión WP, frenos disco, peso 104kg",
            imagen = "https://crm-img.stcrm.it/images/44372411/1000x/gas-gas-ec-300-12007_2.jpg"
        ),
        Moto(
            marca = "Yamaha",
            modelo = "MT‑07",
            caracteristicas = "Motor CP2 2 cilindros, 689cc, 74cv, transmisión 6 velocidades, peso 182kg",
            imagen = "https://www.motofichas.com/images/cache/10-yamaha-mt-07-2025-estudio-azul-01-398-a-mobile.jpg"
        ),
        Moto(
            marca = "Kawasaki",
            modelo = "Ninja 400",
            caracteristicas = "Motor 2 cilindros, 399cc, 48cv, refrigeración líquida, transmisión 6 velocidades, peso 168kg",
            imagen = "https://www.motofichas.com/images/cache/01-kawasaki-ninja-400-2023-estudio-verde-398-a-mobile.jpg"
        ),
        Moto(
            marca = "Honda",
            modelo = "CBR600RR 2005",
            caracteristicas = "Motor 4 cilindros en línea, 599cc, 105cv, transmisión 6 velocidades, peso 177kg",
            imagen = "https://i.ebayimg.com/images/g/mH8AAOSwnLVnIRFh/s-l1200.jpg"
        ),
        Moto(
            marca = "KTM",
            modelo = "EXC 250",
            caracteristicas = "Motor 2T, 249cc, 45cv, suspensión WP, frenos disco, peso 103kg",
            imagen = "https://www.motofichas.com/images/phocagallery/KTM/250-exc-tpi-2020/01-ktm-250-exc-tpi-2020-perfil.jpg"
        ),
        Moto(
            marca = "Suzuki",
            modelo = "GSX‑R750",
            caracteristicas = "Motor 4 cilindros en línea, 749cc, 114cv, transmisión 6 velocidades, peso 187kg",
            imagen = "https://motos.espirituracer.com/archivos/2024/06/suzuki-gsx-r-750-y-1200x676.webp"
        ),
        Moto(
            marca = "BMW",
            modelo = "R 1250 GS",
            caracteristicas = "Motor bóxer 2 cilindros, 1254cc, 136cv, transmisión 6 velocidades, suspensión telelever, peso 249kg",
            imagen = "https://www.lonerider-motorcycle.es/cdn/shop/articles/2023-BMW-R-1250-GS-Trophy.jpg?v=1661521279"
        ),
        Moto(
            marca = "Ducati",
            modelo = "Panigale V4",
            caracteristicas = "Motor V4, 1103cc, 214cv, transmisión 6 velocidades, electrónica avanzada, peso 198kg",
            imagen = "https://www.ducaticanarias.com/wp-content/uploads/2022/07/DUCATI_PANIGALE_V4S_STATIC_001_UC355519_High.jpg"
        ),
        Moto(
            marca = "Honda",
            modelo = "CRF 450R",
            caracteristicas = "Motor 4T, 449cc, 55cv, suspensión Showa, frenos disco, peso 109kg",
            imagen = "https://www.motociclismo.es/uploads/s1/83/10/77/1/acc-9090_7_1200x690.jpeg"
        ),
        Moto(
            marca = "Yamaha",
            modelo = "Tenere 700",
            caracteristicas = "Motor 2 cilindros en línea, 689cc, 72cv, transmisión 6 velocidades, suspensión larga, peso 205kg",
            imagen = "https://www.motoplanete.com/yamaha/10132-700-Tnr-2024_380px.webp"
        )
    )

    override fun getAllMotos(): List<Moto> {
        return motos
    }
}