package com.amdcruz.alertarotulo.history.data.mapper

import com.amdcruz.alertarotulo.history.data.local.HistoryItemEntity
import com.amdcruz.alertarotulo.history.data.model.HistoryItem
import com.amdcruz.alertarotulo.product.data.model.Product

object HistoryMapper {

    fun toEntity(product: Product): HistoryItemEntity {
        return HistoryItemEntity(
            code = product.code,
            name = product.name,
            brand = product.brand,
            allergens = product.allergens,
            ingredients = product.ingredients,
            nutriScore = product.nutriScore?.toString(),
            consultedAtEpochMillis = product.consultedAtEpochMillis,
        )
    }

    fun toModel(entity: HistoryItemEntity): HistoryItem {
        return HistoryItem(
            code = entity.code,
            name = entity.name,
            brand = entity.brand,
            allergens = entity.allergens,
            ingredients = entity.ingredients,
            nutriScore = entity.nutriScore?.firstOrNull(),
            consultedAtEpochMillis = entity.consultedAtEpochMillis,
        )
    }

    fun toProduct(item: HistoryItem): Product {
        return Product(
            code = item.code,
            name = item.name,
            brand = item.brand,
            allergens = item.allergens,
            ingredients = item.ingredients,
            nutriScore = item.nutriScore,
            consultedAtEpochMillis = item.consultedAtEpochMillis,
        )
    }
}
