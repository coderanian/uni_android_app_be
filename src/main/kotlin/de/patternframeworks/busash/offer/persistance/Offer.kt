package de.patternframeworks.busash.offer.persistance

import de.patternframeworks.busash.offer.OfferCategory
import de.patternframeworks.busash.offer.PriceType
import de.patternframeworks.busash.reservation.persistance.Reservation
import de.patternframeworks.busash.user.persistance.User
import org.hibernate.validator.constraints.Length
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
    @Column(name ="product_pic", length = 15000) @Length(max = 15000) val productPic: String? = "",
    @ManyToOne @JoinColumn(name = "author_id") var author: User,
    @OneToMany(mappedBy = "item") val reservations: List<Reservation>? = null
)
