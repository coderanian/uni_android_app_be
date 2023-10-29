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
    @Column(name = "quantity") val quantity: String? = "",
    @Enumerated(EnumType.STRING) @Column(name = "price_type") val priceType: PriceType,
    @Column(name = "price") val price: String,
    @Column(name ="product_pic") val productPic: String? = "",
    @ManyToOne @JoinColumn(name = "author_id") val author: User
)
