package de.patternframeworks.busash.model

import de.patternframeworks.busash.offer.OfferCategory
import de.patternframeworks.busash.offer.PriceType

class OfferDto(
    val offerId: Long,
    val title: String,
    val description: String,
    val category: OfferCategory,
    val quantity: String,
    val priceType: PriceType,
    val price: String,
    val productPic: String?,
    val author: AuthorDto
)