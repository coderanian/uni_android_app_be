package de.patternframeworks.busash.offer

import de.patternframeworks.busash.user.persistance.User
import javax.persistence.*

@Entity
@Table(name = "offers")
data class Offer(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "offer_id") val offerId: Long,
    @Column(name = "title") val title: String,
    @Column(name = "description") val description: String? = "",
    @Enumerated(EnumType.STRING) @Column(name = "category") val category: OfferCategory,
    @Column(name = "quantity") val quantity: String? = "",
    @Enumerated(EnumType.STRING) @Column(name = "price_type") val priceType: PriceType,
    @Column(name = "price") val price: String,
    @Column(name ="product_pic") val productPic: String? = "",
    //Information is extracted from authroization header, so we have to allow it being absent from request header
    @ManyToOne @JoinColumn(name = "author_id") var author: User? = null
)
