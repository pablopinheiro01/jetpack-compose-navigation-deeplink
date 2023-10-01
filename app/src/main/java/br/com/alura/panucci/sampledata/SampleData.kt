package br.com.alura.panucci.sampledata

import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import br.com.alura.panucci.model.Product
import java.math.BigDecimal
import java.util.UUID
import kotlin.random.Random

private val loremName = LoremIpsum(Random.nextInt(10)).values.first()
private val loremDesc = LoremIpsum(Random.nextInt(30)).values.first()

val sampleProductWithImage = Product(
    id = UUID.randomUUID().toString(),
    name = LoremIpsum(10).values.first(),
    price = BigDecimal("9.99"),
    description = LoremIpsum(30).values.first(),
    image = "https://picsum.photos/1920/1080"
)

val sampleProductWithoutImage = Product(
    id = UUID.randomUUID().toString(),
    name = LoremIpsum(10).values.first(),
    price = BigDecimal("9.99"),
    description = LoremIpsum(30).values.first(),
)

val sampleProducts = List(10) { index ->
    val id=if(index ==0){
        "9adccd9a-3918-4996-8c96-2f5b9143cef2"
    }else{
        UUID.randomUUID().toString()
    }
    val price = if(index == 0) BigDecimal("299.90") else BigDecimal("10.00")
    Product(
        id = id,
        name = loremName,
        price = price,
        description = loremDesc,
        image = if (index % 2 == 0) "https://picsum.photos/1920/1080" else null
    )
}

